package chess.moves.handlers;

import chess.ChessState;
import chess.HistoryState;
import chess.moves.Move;
import chess.util.Board;
import chess.util.Castling;
import chess.util.Hash;
import chess.util.Piece;

/**
 *
 * @author Philipp
 */
public abstract class MoveAdapter {

    public abstract void makeMove(ChessState state, Move move);
    public abstract void unmakeMove(ChessState state, Move move);
    
    protected void flipMasks(ChessState state, long squares, int piece) {
        state.pieceMasks[piece] ^= squares;
        state.playerMasks[Piece.owner(piece)] ^= squares;
    }

    protected void flipHash(ChessState state, int piece, int from, int to) {
        state.nextHistory().hash ^= Hash.pieceHash(piece, from, to);
    }

    protected void flipHash(ChessState state, int piece, int square) {
        state.nextHistory().hash ^= Hash.pieceHash(piece, square);
    }
    
    protected void resetFifty(ChessState state) {
        state.nextHistory().fiftyRule = 0;
    }
    
    protected void updateCastling(ChessState state, int square) {
        switch (square) {
            case Board.A1:
                removeCastling(state, Castling.CASTLE_A1);
                break;
            case Board.A8:
                removeCastling(state, Castling.CASTLE_A8);
                break;
            case Board.H1:
                removeCastling(state, Castling.CASTLE_H1);
                break;
            case Board.H8:
                removeCastling(state, Castling.CASTLE_H8);
                break;
            case Board.E1:
                removeCastling(state, Castling.CASTLE_A1 | Castling.CASTLE_H1);
                break;
            case Board.E8:
                removeCastling(state, Castling.CASTLE_A8 | Castling.CASTLE_H8);
                break;
        }
    }

    private void removeCastling(ChessState state, int castleFlags) {
        HistoryState nextHistory = state.nextHistory();
        nextHistory.hash ^= Hash.castleHash(nextHistory.castling);
        nextHistory.castling &= ~castleFlags;
        nextHistory.hash ^= Hash.castleHash(nextHistory.castling);
        nextHistory.fiftyRule = 0;
    }
    
}
