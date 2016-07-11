package chess.moves.generators.implementations.defaults.moveMasks;

import chess.util.Mask;

/**
 *
 * @author Philipp
 */
public class QueenMoveMask implements SinglePieceMoveMask {

    @Override
    public long moves(int square, long allPieces, long targetSquares) {
        return Mask.QueenMovement(allPieces, square) & targetSquares;
    }

}
