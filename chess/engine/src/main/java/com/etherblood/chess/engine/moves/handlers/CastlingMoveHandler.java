package com.etherblood.chess.engine.moves.handlers;

import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.util.Board;
import com.etherblood.chess.engine.util.Mask;
import com.etherblood.chess.engine.util.Piece;


public class CastlingMoveHandler extends MoveAdapter {

    @Override
    public void makeMove(ChessState state, Move move) {
        int rookFrom, rookTo;
        switch (move.to) {
            case Board.C1:
                rookFrom = Board.A1;
                rookTo = Board.D1;
                break;
            case Board.G1:
                rookFrom = Board.H1;
                rookTo = Board.F1;
                break;
            case Board.C8:
                rookFrom = Board.A8;
                rookTo = Board.D8;
                break;
            case Board.G8:
                rookFrom = Board.H8;
                rookTo = Board.F8;
                break;
            default:
                throw new IllegalStateException("invalid castling");
        }
        
        int from = move.from;
        int to = move.to;
        int king = state.pieces[from];
        int rook = state.pieces[rookFrom];
        assert Piece.isKing(king);
        assert Piece.isRook(rook);
        assert Piece.owner(king) == Piece.owner(rook);
        
        //move king
        flipMasks(state, Mask.toFlag(from) | Mask.toFlag(to), king);
        flipHash(state, king, from, to);
        state.pieces[from] = Piece.EMPTY;
        state.pieces[to] = king;
        
        //move rook
        flipMasks(state, Mask.toFlag(rookFrom) | Mask.toFlag(rookTo), rook);
        flipHash(state, rook, rookFrom, rookTo);
        state.pieces[rookFrom] = Piece.EMPTY;
        state.pieces[rookTo] = rook;
        
        //states
        updateCastling(state, from);
        resetFifty(state);
    }

    @Override
    public void unmakeMove(ChessState state, Move move) {
        int rookFrom, rookTo;
        switch (move.to) {
            case Board.C1:
                rookFrom = Board.A1;
                rookTo = Board.D1;
                break;
            case Board.G1:
                rookFrom = Board.H1;
                rookTo = Board.F1;
                break;
            case Board.C8:
                rookFrom = Board.A8;
                rookTo = Board.D8;
                break;
            case Board.G8:
                rookFrom = Board.H8;
                rookTo = Board.F8;
                break;
            default:
                throw new IllegalStateException("invalid castling");
        }
        
        int from = move.from;
        int to = move.to;
        int king = state.pieces[to];
        int rook = state.pieces[rookTo];
        assert Piece.isKing(king);
        assert Piece.isRook(rook);
        
        //move king
        flipMasks(state, Mask.toFlag(from) | Mask.toFlag(to), king);
        state.pieces[from] = king;
        state.pieces[to] = Piece.EMPTY;
        
        //move rook
        flipMasks(state, Mask.toFlag(rookFrom) | Mask.toFlag(rookTo), rook);
        state.pieces[rookFrom] = rook;
        state.pieces[rookTo] = Piece.EMPTY;
    }

}
