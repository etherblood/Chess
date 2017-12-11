package com.etherblood.chess.engine.moves;

import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.HistoryState;
import com.etherblood.chess.engine.util.Hash;

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
