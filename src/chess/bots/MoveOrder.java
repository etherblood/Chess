package chess.bots;

import chess.moves.Move;

/**
 *
 * @author Philipp
 */
public interface MoveOrder {
    void sort(Move[] buffer, int from, int to);
}
