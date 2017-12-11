package com.etherblood.chess.bot;

import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.moves.Move;

/**
 *
 * @author Philipp
 */
public interface Bot {
    void setState(ChessState state);
    Move compute(int strength);
}
