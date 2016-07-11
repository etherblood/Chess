package chess.moves.generators;

import chess.moves.generators.implementations.AbstractMoveGenerator;
import chess.moves.generators.implementations.AttackPawnMoveGenerator;
import chess.moves.generators.implementations.defaults.DefaultCaptureGenerator;
import chess.ChessState;
import chess.moves.Move;

/**
 *
 * @author Philipp
 */
public final class CaptureGenerator extends AbstractMoveGenerator {

    private final DefaultCaptureGenerator defaults = new DefaultCaptureGenerator();
    private final AttackPawnMoveGenerator attacks = new AttackPawnMoveGenerator();

    @Override
    public int generateMoves(ChessState state, Move[] buffer, int offset) {
        offset = attacks.generateMoves(state, buffer, offset);
        offset = defaults.generateMoves(state, buffer, offset);
        return offset;
    }

}
