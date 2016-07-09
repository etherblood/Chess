package chess.moves.generators.moveMasks;

/**
 *
 * @author Philipp
 */
public interface SinglePieceMoveMask {
    long moves(int square, long allPieces, long ownPieces);
}
