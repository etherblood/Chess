package com.etherblood.chess.bot.client;

import com.etherblood.chess.api.match.ChessMatchTo;
import com.etherblood.chess.api.match.ChessSquare;
import com.etherblood.chess.api.match.moves.ChessMove;
import com.etherblood.chess.bot.Bot;
import com.etherblood.chess.engine.ChessSetup;
import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.util.ChessWrapper;
import com.etherblood.chess.engine.util.Player;

public class BotService {
	private final Bot bot;
	
	public BotService(Bot bot) {
		super();
		this.bot = bot;
	}

	public ChessMove calcMove(ChessMatchTo match) {
		ChessState state = new ChessState();
		ChessSetup setup = new ChessSetup();
		ChessWrapper chess = new ChessWrapper();
		setup.fromFen(state, match.startFen);
		for (ChessMove move : match.moves) {
			Move selectedMove = chess.find(state, ChessSquare.toInt(move.from), ChessSquare.toInt(move.to), ChessModelConverter.convertMoveType(move.type, state.currentPlayer() == Player.WHITE));
			chess.makeMove(state, selectedMove);
		}
		if(chess.isGameOver(state)) {
			return null;
		}
		bot.setState(state);
		Move move = bot.compute(8);
		return ChessModelConverter.convertMove(move);
	}
	
}
