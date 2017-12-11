package com.etherblood.chess.bot;

import com.etherblood.chess.engine.moves.Move;

/**
 *
 * @author Philipp
 */
public interface MoveOrder {
    void sort(Move[] buffer, int from, int to);
}
