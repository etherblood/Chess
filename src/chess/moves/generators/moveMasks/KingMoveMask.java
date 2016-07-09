package chess.moves.generators.moveMasks;

import chess.util.Mask;

/**
 *
 * @author Philipp
 */
public class KingMoveMask implements SinglePieceMoveMask {

    @Override
    public long moves(int square, long allPieces, long ownPieces) {
        return Mask.mKingAttacks[square] & ~ownPieces;
    }

}
