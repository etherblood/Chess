package chess.moves.generators;

import chess.moves.generators.implementations.CastlingMoveGenerator;
import chess.moves.generators.implementations.AttackPawnMoveGenerator;
import chess.moves.generators.implementations.QuietPawnMoveGenerator;
import chess.ChessState;
import chess.moves.Move;
import chess.moves.generators.implementations.defaults.DefaultMovesGenerator;

/**
 *
 * @author Philipp
 */
public final class MoveGenerator {

    private final DefaultMovesGenerator defaults = new DefaultMovesGenerator();
    private final AttackPawnMoveGenerator attacks = new AttackPawnMoveGenerator();
    private final CastlingMoveGenerator castlings = new CastlingMoveGenerator();
    private final QuietPawnMoveGenerator quiets = new QuietPawnMoveGenerator();

    public int generateMoves(ChessState state, Move[] buffer, int offset) {
        offset = attacks.generateMoves(state, buffer, offset);
        offset = quiets.generateMoves(state, buffer, offset);
        offset = castlings.generateMoves(state, buffer, offset);
        offset = defaults.generateMoves(state, buffer, offset, ~state.playerMasks[state.currentPlayer()]);
        return offset;
    }
    
    public int generateAttacks(ChessState state, Move[] buffer, int offset) {
        offset = attacks.generateMoves(state, buffer, offset);
        offset = defaults.generateMoves(state, buffer, offset, state.playerMasks[state.opponentPlayer()]);
        return offset;
    }

    public static Move[] createBuffer(int size) {
        Move[] buffer = new Move[size];
        for (int i = 0; i < size; i++) {
            buffer[i] = new Move();
        }
        return buffer;
    }
    
    public boolean isThreateningKing(ChessState state) {
        return castlings.isThreateningKing(state);
    }
    
    public boolean isKingThreatened(ChessState state) {
        return castlings.isKingThreatened(state);
    }

}
