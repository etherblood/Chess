package com.etherblood.chess.bot.client;

import java.io.IOException;
import java.util.UUID;

import com.etherblood.chess.api.match.ChessMatchTo;
import com.etherblood.chess.api.match.MatchRequestTo;

public class MatchService {
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
		System.out.println("processing request " + request);
		if (request.receiverId.equals(selfId)) {
			apiService.acceptMatchRequest(request.matchId);
		}
	}

	public void handleMatch(ChessMatchTo match) throws IOException {
		System.out.println("processing match " + match);
		boolean whiteToMove = (match.moves.size() & 1) == 0;
		if (whiteToMove) {
			if (match.whiteId.equals(selfId)) {
				apiService.move(match.id, botService.calcMove(match));
			}
		} else {
			if (match.blackId.equals(selfId)) {
				apiService.move(match.id, botService.calcMove(match));
			}
		}
	}
}
