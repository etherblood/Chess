package chess.moves.handlers;

import chess.ChessState;
import chess.HistoryState;
import chess.ChessStateValidator;
import chess.moves.Move;
import chess.util.Piece;
import chess.util.Hash;

/**
 *
 * @author Philipp
 */
public final class MoveExecutor {
    private final ChessStateValidator validator = new ChessStateValidator();
    private final DefaultMoveHandler defaultHandler = new DefaultMoveHandler();
    private final DoubleMoveHandler doubleHandler = new DoubleMoveHandler();
    private final EnPassantMoveHandler enPassantHandler = new EnPassantMoveHandler();
    private final CastlingMoveHandler castlingHandler = new CastlingMoveHandler();
    private final PromotionMoveHandler promotionHandler = new PromotionMoveHandler();

    public void makeMove(ChessState state, Move move) {
        assert state.pieces[move.from] != Piece.EMPTY;
        assert move.assertRanges();
        HistoryState history = state.currentHistory();
        HistoryState nextHistory = state.nextHistory();
        nextHistory.fiftyRule = (byte) (history.fiftyRule + 1);
        nextHistory.hash = history.hash ^ Hash.enPassantHash(history.enPassant) ^ Hash.blackToMoveHash();
        nextHistory.lastMove = move.toInt();
        nextHistory.castling = history.castling;
        nextHistory.enPassant = 0;

        switch (move.info) {
            case Piece.EMPTY:
                defaultHandler.makeMove(state, move);
                break;
            case Piece.RESERVED_1:
                doubleHandler.makeMove(state, move);
                break;
            case Piece.RESERVED_2:
                enPassantHandler.makeMove(state, move);
                break;
            case Piece.RESERVED_3:
                castlingHandler.makeMove(state, move);
                break;
            default:
                promotionHandler.makeMove(state, move);
                break;
        }
        state.moveCounter++;
//        assert validator.validate(state);
    }
 
    public void unmakeMove(ChessState state, Move move) {
        assert move.toInt() == state.currentHistory().lastMove;
        assert move.assertRanges();
        state.moveCounter--;
        switch (move.info) {
            case Piece.EMPTY:
                defaultHandler.unmakeMove(state, move);
                break;
            case Piece.RESERVED_1:
                doubleHandler.unmakeMove(state, move);
                break;
            case Piece.RESERVED_2:
                enPassantHandler.unmakeMove(state, move);
                break;
            case Piece.RESERVED_3:
                castlingHandler.unmakeMove(state, move);
                break;
            default:
                promotionHandler.unmakeMove(state, move);
                break;
        }
//        assert validator.validate(state);
    }
    
//    public boolean reconstruct(ChessState state, Move move) {
//        switch (move.info) {
//            case Piece.EMPTY:
//                return defaultHandler.reconstruct(state, move);
//            case Piece.RESERVED_1:
//                return doubleHandler.reconstruct(state, move);
//            case Piece.RESERVED_2:
//                return enPassantHandler.reconstruct(state, move);
//            case Piece.RESERVED_3:
//                return castlingHandler.reconstruct(state, move);
//            default:
//                return promotionHandler.reconstruct(state, move);
//        }
//    }

}