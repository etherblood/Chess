package com.etherblood.chess.bot.client;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.etherblood.chess.api.PollEvent;
import com.etherblood.chess.api.match.ChessMatchTo;
import com.etherblood.chess.api.match.MatchRequestTo;
import com.etherblood.chess.api.match.moves.ChessMove;
import com.etherblood.chess.api.player.PlayerTo;
import com.etherblood.chess.bot.client.model.MatchRequests;
import com.etherblood.chess.bot.client.model.PollEvents;
import com.etherblood.chess.bot.client.model.Uuids;

public class ApiService {

	private final HttpService httpService;

	public ApiService(HttpService httpService) {
		super();
		this.httpService = httpService;
	}

	public int subscribePolling() throws IOException {
		return httpService.post("/api/poll/subscribe", null, Integer.class);
	}

	public List<PollEvent<?>> poll(int pollingId) throws IOException {
		return httpService.get("/api/poll/" + pollingId, PollEvents.class);
	}

	public PlayerTo getSelf() throws IOException {
		return httpService.get("/api/account/self", PlayerTo.class);
	}

	public ChessMatchTo getMatch(UUID matchId) throws IOException {
		return httpService.get("/api/match/" + matchId, ChessMatchTo.class);
	}

	public List<UUID> getActiveMatches() throws IOException {
		return httpService.get("/api/match/list", Uuids.class);
	}

	public List<MatchRequestTo> getRequestedMatches() throws IOException {
		return httpService.get("/api/match/requests", MatchRequests.class);
	}

	public void acceptMatchRequest(UUID matchId) throws IOException {
		httpService.post("/api/match/" + matchId + "/accept", null, Void.class);
	}

	public void move(UUID matchId, ChessMove move) throws IOException {
		httpService.post("/api/match/" + matchId + "/move", move, Void.class);
	}
}
