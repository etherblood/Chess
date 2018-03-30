package com.etherblood.chess.bot.client;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etherblood.chess.api.PollEvent;
import com.etherblood.chess.api.match.ChessMatchTo;
import com.etherblood.chess.api.match.MatchRequestTo;
import com.etherblood.chess.api.match.events.MatchMoveEvent;
import com.etherblood.chess.api.match.events.MatchRequestedEvent;
import com.etherblood.chess.api.match.events.MatchStartedEvent;
import com.etherblood.chess.api.match.moves.ChessMove;
import com.etherblood.chess.bot.Bot;
import com.etherblood.chess.bot.PvsBucketBot;
import com.etherblood.chess.bot.evaluations.AdvancedEval;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
	
	private final static Logger LOG = LoggerFactory.getLogger(Main.class);
	
	public static void main(String... args) throws IOException, InterruptedException {
		if(args.length != 3) {
			LOG.error("args are: serverUrl loginHandle password");
			return;
		}
		Config config = new Config();
		config.serverUrl = args[0];
		config.loginHandle = args[1];
		config.password = args[2];

		LOG.info("initializing services...");
		Gson gson = new GsonBuilder().registerTypeAdapter(PollEvent.class, new PollEventDeserializer()).create();
		HttpService httpService = new HttpService(config, gson);
		ApiService apiService = new ApiService(httpService);
		Bot bot = new PvsBucketBot(new AdvancedEval());
		BotService botService = new BotService(bot);
		MatchService matchService = new MatchService(apiService, apiService.getSelf().id, botService);

		int pollingId = apiService.subscribePolling();
		LOG.info("accepting match requests...");
		for (MatchRequestTo request : apiService.getRequestedMatches()) {
			matchService.handleMatchRequest(request);
		}

		LOG.info("processing running matches...");
		for (UUID matchId : apiService.getActiveMatches()) {
			ChessMatchTo match = apiService.getMatch(matchId);
			matchService.handleMatch(match);
		}

		LOG.info("started polling");
		while (true) {
			LOG.info("polling...");
			for (PollEvent<?> event : apiService.poll(pollingId)) {
				LOG.info("handling " + gson.toJson(event));
				if (event instanceof MatchRequestedEvent) {
					matchService.handleMatchRequest(((MatchRequestedEvent) event).getData());
				} else if (event instanceof MatchStartedEvent) {
					UUID matchId = ((MatchStartedEvent) event).getData();
					ChessMatchTo match = apiService.getMatch(matchId);
					matchService.handleMatch(match);
				} else if (event instanceof MatchMoveEvent) {
					Map<UUID, ChessMove> map = ((MatchMoveEvent) event).getData();
					for (UUID matchId : map.keySet()) {
						ChessMatchTo match = apiService.getMatch(matchId);
						matchService.handleMatch(match);
					}
				}
			}
			LOG.info("finished iteration");
			Thread.sleep(100);
		}
	}
}
