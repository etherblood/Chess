package com.etherblood.chess.engine.moves.handlers;

import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.HistoryState;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.util.Hash;
import com.etherblood.chess.engine.util.Mask;
import com.etherblood.chess.engine.util.Piece;


public class DoubleMoveHandler extends MoveAdapter {

    @Override
    public void makeMove(ChessState state, Move move) {
        int from = move.from;
        int to = move.to;
        int piece = state.pieces[from];
        
        //move
        flipMasks(state, Mask.toFlag(from) | Mask.toFlag(to), piece);
        flipHash(state, piece, from, to);
        state.pieces[from] = Piece.EMPTY;
        state.pieces[to] = piece;
        
        //states
        if((state.pieceMasks[Piece.pawn(state.opponentPlayer())] & Mask.horizontalNeighbors(to)) != 0) {
            HistoryState history = state.nextHistory();
            int ep = (from + to) >>> 1;
            history.enPassant = (byte)ep;
            history.hash ^= Hash.enPassantHash(ep);
        }
        resetFifty(state);
    }

    @Override
    public void unmakeMove(ChessState state, Move move) {
        int from = move.from;
        int to = move.to;
        int piece = state.pieces[to];
        
        //move
        flipMasks(state, Mask.toFlag(from) | Mask.toFlag(to), piece);
        state.pieces[from] = piece;
        state.pieces[to] = Piece.EMPTY;
    }

//    @Override
//    public boolean reconstruct(ChessState state, Move move) {
//        int from = move.from;
//        int to = move.to;
//        int pawn = state.pieces[from];
//        if(!Piece.isPawn(pawn)) {
//            return false;
//        }
//        if(state.pieces[to] != Piece.EMPTY) {
//            return false;
//        }
//        if(state.pieces[(from + to) / 2] != Piece.EMPTY) {
//            return false;
//        }
//        return Piece.owner(pawn) == state.currentPlayer();
//    }

}
