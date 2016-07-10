package chess.moves.handlers;

import chess.ChessState;
import chess.moves.Move;
import chess.util.Board;
import chess.util.Mask;
import chess.util.Piece;


public class PromotionMoveHandler extends MoveAdapter {

    @Override
    public void makeMove(ChessState state, Move move) {
        int from = move.from;
        int to = move.to;
        int pawn = state.pieces[from];
        int capture = move.capture;
        int promotion = move.info;
        
        //move from
        flipMasks(state, Mask.single(from), pawn);
        flipHash(state, pawn, from);
        state.pieces[from] = Piece.EMPTY;
        
        //move to
        flipMasks(state, Mask.single(to), promotion);
        flipHash(state, promotion, to);
        state.pieces[to] = promotion;
        
        //capture
        if(capture != Piece.EMPTY) {
            flipMasks(state, Mask.single(to), capture);
            flipHash(state, capture, to);
        }
        
        //states
        updateCastling(state, to);
        resetFifty(state);
    }

    @Override
    public void unmakeMove(ChessState state, Move move) {
        int from = move.from;
        int to = move.to;
        int pawn = Piece.pawn(state.currentPlayer());
        int capture = move.capture;
        int promotion = move.info;
        
        //move from
        flipMasks(state, Mask.single(from), pawn);
        state.pieces[from] = pawn;
        
        //move to
        flipMasks(state, Mask.single(to), promotion);
        state.pieces[to] = capture;
        
        //capture
        if(capture != Piece.EMPTY) {
            flipMasks(state, Mask.single(to), capture);
        }
    }

//    @Override
//    public boolean reconstruct(ChessState state, Move move) {
//        int from = move.from;
//        int to = move.to;
//        int pawn = state.pieces[from];
//        if(!Piece.isPawn(pawn)) {
//            return false;
//        }
//        int capture = state.pieces[to];
//        if((capture == Piece.EMPTY) != (Board.x(from) == Board.x(to))) {
//            return false;
//        }
////        int promotion = move.info;
////        if(!Piece.isValidPromotion(promotion)) {
////            return false;
////        }
////        if(Piece.owner(pawn) != state.currentPlayer()) {
////            return false;
////        }
////        if(Piece.owner(promotion) != state.currentPlayer()) {
////            return false;
////        }
//        move.capture = capture;
//        return true;
//    }

}
