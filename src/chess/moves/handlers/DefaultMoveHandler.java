package chess.moves.handlers;

import chess.ChessState;
import chess.moves.Move;
import chess.util.Mask;
import chess.util.Piece;


public final class DefaultMoveHandler extends MoveAdapter {

    @Override
    public void makeMove(ChessState state, Move move) {
        int from = move.from;
        int to = move.to;
        int piece = state.pieces[from];
        int capture = move.capture;
        
        //move
        flipMasks(state, Mask.single(from) | Mask.single(to), piece);
        flipHash(state, piece, from, to);
        state.pieces[from] = Piece.EMPTY;
        state.pieces[to] = piece;
        
        if(capture != Piece.EMPTY) {
            //capture
            flipMasks(state, Mask.single(to), capture);
            flipHash(state, capture, to);
            
            //states
            updateCastling(state, to);
            resetFifty(state);
        } else if(Piece.isPawn(piece)) {
            resetFifty(state);
        }
        updateCastling(state, from);
    }

    @Override
    public void unmakeMove(ChessState state, Move move) {
        int from = move.from;
        int to = move.to;
        int piece = state.pieces[to];
        int capture = move.capture;
        
        //move
        flipMasks(state, Mask.single(from) | Mask.single(to), piece);
        state.pieces[from] = piece;
        state.pieces[to] = capture;
        
        //capture
        if(capture != Piece.EMPTY) {
            flipMasks(state, Mask.single(to), capture);
        }
    }

}
