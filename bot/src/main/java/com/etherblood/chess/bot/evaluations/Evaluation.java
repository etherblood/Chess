package com.etherblood.chess.bot.evaluations;

import com.etherblood.chess.engine.ChessState;

/**
 *
 * @author Philipp
 */
public interface Evaluation {
    int evaluate(ChessState state, int alpha, int beta);
}
