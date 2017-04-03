package chess.moves;

import chess.ChessState;
import chess.HistoryState;
import chess.util.Hash;

/**
 *
 * @author Philipp
 */
public class NullMoveExecutor {

    public void makeNullMove(ChessState state) {
        HistoryState history = state.currentHistory();
        HistoryState nextHistory = state.nextHistory();
        nextHistory.fiftyRule = 0;
        nextHistory.hash = history.hash ^ Hash.enPassantHash(history.enPassant) ^ Hash.blackToMoveHash();
        nextHistory.lastMove = Move.empty().toInt();
        nextHistory.castling = history.castling;
        nextHistory.enPassant = 0;
        state.moveCounter++;
    }

    public void unmakeNullMove(ChessState state) {
        state.moveCounter--;
    }
}
