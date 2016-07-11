package chess.moves.generators.implementations.defaults.moveMasks;

import chess.util.Mask;

/**
 *
 * @author Philipp
 */
public class BishopMoveMask implements SinglePieceMoveMask {

    @Override
    public long moves(int square, long allPieces, long targetSquares) {
        return Mask.BishopMovement(allPieces, square) & targetSquares;
    }

}
