package chess.moves.handlers;

import chess.ChessState;
import chess.moves.Move;
import chess.util.Mask;
import chess.util.Piece;
import chess.util.Player;


public class EnPassantMoveHandler extends MoveAdapter {

    @Override
    public void makeMove(ChessState state, Move move) {
        int from = move.from;
        int to = move.to;
        int piece = state.pieces[from];
        int capture = move.capture;
        int epSquare = to - 8 * Player.sign(state.currentPlayer());
        
        //move
        flipMasks(state, Mask.single(from) | Mask.single(to), piece);
        flipHash(state, piece, from, to);
        state.pieces[from] = Piece.EMPTY;
        state.pieces[to] = piece;
        
        //capture
        flipMasks(state, Mask.single(epSquare), capture);
        flipHash(state, capture, epSquare);
        state.pieces[epSquare] = Piece.EMPTY;
        
        //states
        resetFifty(state);
    }

    @Override
    public void unmakeMove(ChessState state, Move move) {
        int from = move.from;
        int to = move.to;
        int piece = state.pieces[to];
        int capture = move.capture;
        int epSquare = to - 8 * Player.sign(state.currentPlayer());
        
        //move
        flipMasks(state, Mask.single(from) | Mask.single(to), piece);
        state.pieces[from] = piece;
        state.pieces[to] = Piece.EMPTY;
        
        //capture
        flipMasks(state, Mask.single(epSquare), capture);
        state.pieces[epSquare] = capture;
    }

//    @Override
//    public boolean reconstruct(ChessState state, Move move) {
//        int from = move.from;
//        int to = move.to;
//        int pawn = state.pieces[from];
//        if(!Piece.isPawn(pawn)) {
//            return false;
//        }
//        if(state.currentHistory().enPassant != to) {
//            return false;
//        }
//        return Piece.owner(pawn) == state.currentPlayer();
//    }

}
