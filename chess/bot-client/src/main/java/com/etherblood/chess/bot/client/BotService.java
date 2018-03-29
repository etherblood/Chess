package com.etherblood.chess.bot.client;

import com.etherblood.chess.api.match.ChessMatchTo;
import com.etherblood.chess.api.match.moves.ChessMove;
import com.etherblood.chess.bot.Bot;
import com.etherblood.chess.engine.ChessSetup;
import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.moves.generators.MoveGenerator;
import com.etherblood.chess.engine.moves.handlers.MoveExecutor;
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
		MoveGenerator gen = new MoveGenerator();
		Move[] moveBuffer = new Move[256];
		for (int i = 0; i < moveBuffer.length; i++) {
			moveBuffer[i] = new Move();
		}
		MoveExecutor moveExecutor = new MoveExecutor();
		setup.fromFen(state, match.startFen);
		for (ChessMove move : match.moves) {
			Move selectedMove = ChessModelConverter.convertMove(move, state.currentPlayer() == Player.WHITE);
			int moveCount = gen.generateMoves(state, moveBuffer, 0);
			selectedMove = moveBuffer[indexOf(moveBuffer, moveCount, selectedMove)];
			moveExecutor.makeMove(state, selectedMove);
		}
		if(gen.generateMoves(state, moveBuffer, 0) == 0) {
			return null;
		}
		bot.setState(state);
		Move move = bot.compute(8);
		return ChessModelConverter.convertMove(move);
	}
	
	private int indexOf(Move[] moves, int moveCount, Move move) {
		for (int i = 0; i < moveCount; i++) {
			Move m = moves[i];
			if(movesEqual(move, m)) {
				return i;
			}
		}
		throw new AssertionError();
	}
	
	private boolean movesEqual(Move a, Move b) {
		return a.from == b.from && a.to == b.to && a.info == b.info;
	}
}
