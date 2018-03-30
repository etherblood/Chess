package com.etherblood.chess.engine.util;

import java.util.ArrayList;
import java.util.List;

import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.HistoryState;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.moves.generators.MoveGenerator;
import com.etherblood.chess.engine.moves.handlers.MoveExecutor;

public class ChessWrapper {
	private final MoveExecutor exe;
	private final MoveGenerator gen;
	private final Move[] buffer;

	public ChessWrapper() {
		this(new MoveExecutor(), new MoveGenerator());
	}

	public ChessWrapper(MoveExecutor exe, MoveGenerator gen) {
		this(exe, gen, MoveGenerator.createBuffer(256));
	}

	public ChessWrapper(MoveExecutor exe, MoveGenerator gen, Move[] buffer) {
		super();
		this.exe = exe;
		this.gen = gen;
		this.buffer = buffer;
	}
	
	public List<Move> legalMoves(ChessState state) {
		int moves = gen.generateMoves(state, buffer, 0);
		List<Move> result = new ArrayList<>(moves);
		for (int i = 0; i < moves; i++) {
			exe.makeMove(state, buffer[i]);
			if(!gen.isThreateningKing(state)) {
				Move move = new Move();
				move.fromMove(buffer[i]);
				result.add(move);
			}
			exe.unmakeMove(state, buffer[i]);
		}
		return result;
	}
	
	public Move move(ChessState state, int from, int to) {
		return find(state, from, to, Piece.EMPTY);
	}
	
	public Move pawnJump(ChessState state, int from, int to) {
		return find(state, from, to, Piece.RESERVED_1);
	}
	
	public Move enPassant(ChessState state, int from, int to) {
		return find(state, from, to, Piece.RESERVED_2);
	}
	
	public Move castling(ChessState state, int from, int to) {
		return find(state, from, to, Piece.RESERVED_3);
	}
	
	public Move promotion(ChessState state, int from, int to, int promotion) {
		return find(state, from, to, Piece.asPieceWithOwner(promotion, state.currentPlayer()));
	}
	
	public Move find(ChessState state, int from, int to, int info) {
		for (Move move : legalMoves(state)) {
			if(move.from == from && move.to == to && move.info == info) {
				return move;
			}
		}
		return null;
	}
	
	public boolean isKingChecked(ChessState state) {
		return gen.isKingThreatened(state);
	}
	
	public boolean isKingMated(ChessState state) {
		return gen.isKingThreatened(state) && legalMoves(state).isEmpty();
	}
	
	public boolean isStalemate(ChessState state) {
		return !gen.isKingThreatened(state) && legalMoves(state).isEmpty();
	}
	
	public boolean isFiftyRule(ChessState state) {
		return state.currentHistory().fiftyRule >= 100;
	}
	
	public boolean is3FoldRepetition(ChessState state) {
        HistoryState history = state.currentHistory();
        long hash = history.hash;
        int limit = state.moveCounter - history.fiftyRule;
        int repetitions = 0;
        for (int i = state.moveCounter - 4; i >= limit; i -= 2) {
            if (state.history[i].hash == hash) {
                repetitions++;
            }
        }
        return repetitions >= 2;
	}
	
	public boolean insufficientMatingMaterial(ChessState state) {
		if ((state.pieceMasks[Piece.W_PAWN] | state.pieceMasks[Piece.B_PAWN]) == 0) {
            long whitePieces = state.playerMasks[Player.WHITE] ^ state.pieceMasks[Piece.W_KING];
            long blackPieces = state.playerMasks[Player.BLACK] ^ state.pieceMasks[Piece.B_KING];
            long allPieces = whitePieces | blackPieces;
            
            long allKnights = state.pieceMasks[Piece.W_KNIGHT] | state.pieceMasks[Piece.B_KNIGHT];
            if((allKnights & (allKnights - 1)) == 0) {
                if (allKnights == allPieces) {
                    return true;
                }
                long allBishops = state.pieceMasks[Piece.W_BISHOP] | state.pieceMasks[Piece.B_BISHOP];
                return (allBishops == allPieces && ((allBishops & Mask.WHITE_SQUARES) == 0 || (allBishops & Mask.BLACK_SQUARES) == 0));
            }
        }
		return false;
	}

	public boolean isGameOver(ChessState state) {
		return legalMoves(state).isEmpty() || isFiftyRule(state) || is3FoldRepetition(state) || insufficientMatingMaterial(state);
	}

	public void makeMove(ChessState state, Move move) {
		exe.makeMove(state, move);
	}

}
