package chess.bots;

import chess.ChessState;

/**
 *
 * @author Philipp
 */
public interface Evaluation {
    int evaluate(ChessState state);
}
