package com.etherblood.chess.engine.moves.generators.implementations.defaults.moveMasks;

/**
 *
 * @author Philipp
 */
public interface SinglePieceMoveMask {
    long moves(int square, long allPieces, long targetSquares);
}
