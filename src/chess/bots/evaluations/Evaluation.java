package chess.bots.evaluations;

import chess.ChessState;

/**
 *
 * @author Philipp
 */
public interface Evaluation {
    int evaluate(ChessState state, int alpha, int beta);
}
