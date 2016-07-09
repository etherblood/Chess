package chess.moves.generators;

import chess.ChessState;
import chess.moves.Move;

/**
 *
 * @author Philipp
 */
public abstract class AbstractMoveGenerator {
    public abstract int generateMoves(ChessState state, Move[] buffer, int offset);
}
