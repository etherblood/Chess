package com.etherblood.chess.bot.client;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etherblood.chess.api.match.ChessMatchTo;
import com.etherblood.chess.api.match.MatchRequestTo;
import com.etherblood.chess.api.match.moves.ChessMove;

public class MatchService {
	private final static Logger LOG = LoggerFactory.getLogger(MatchService.class);

	private final ApiService apiService;
	private final UUID selfId;
	private final BotService botService;

	public MatchService(ApiService apiService, UUID selfId, BotService botService) {
		super();
		this.apiService = apiService;
		this.selfId = selfId;
		this.botService = botService;
	}

	public void handleMatchRequest(MatchRequestTo request) throws IOException {
		LOG.info("processing request " + request);
		if (request.receiverId.equals(selfId)) {
			apiService.acceptMatchRequest(request.matchId);
		}
	}

	public void handleMatch(ChessMatchTo match) throws IOException {
		LOG.info("processing match " + match);
		boolean whiteToMove = (match.moves.size() & 1) == 0;
		ChessMove selectedMove = null;
		if (whiteToMove) {
			if (match.whiteId.equals(selfId)) {
				selectedMove = botService.calcMove(match);
			}
		} else {
			if (match.blackId.equals(selfId)) {
				selectedMove = botService.calcMove(match);
			}
		}
		if (selectedMove != null) {
			apiService.move(match.id, selectedMove);
		}
	}
}
