package chess.bots;

import chess.ChessState;
import chess.moves.Move;

/**
 *
 * @author Philipp
 */
public interface Bot {
    void setState(ChessState state);
    Move compute();
}
