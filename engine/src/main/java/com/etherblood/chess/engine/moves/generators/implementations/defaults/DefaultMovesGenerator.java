package com.etherblood.chess.engine.moves.generators.implementations.defaults;

import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.moves.generators.implementations.defaults.moveMasks.BishopMoveMask;
import com.etherblood.chess.engine.moves.generators.implementations.defaults.moveMasks.KingMoveMask;
import com.etherblood.chess.engine.moves.generators.implementations.defaults.moveMasks.KnightMoveMask;
import com.etherblood.chess.engine.moves.generators.implementations.defaults.moveMasks.QueenMoveMask;
import com.etherblood.chess.engine.moves.generators.implementations.defaults.moveMasks.RookMoveMask;
import com.etherblood.chess.engine.moves.generators.implementations.defaults.moveMasks.SinglePieceMoveMask;
import com.etherblood.chess.engine.util.Mask;
import com.etherblood.chess.engine.util.Piece;


public final class DefaultMovesGenerator {
    private final KingMoveMask kingMoves = new KingMoveMask();
    private final BishopMoveMask bishopMoves = new BishopMoveMask();
    private final KnightMoveMask knightMoves = new KnightMoveMask();
    private final RookMoveMask rookMoves = new RookMoveMask();
    private final QueenMoveMask queenMoves = new QueenMoveMask();
    
    public int generateMoves(ChessState state, Move[] buffer, int offset, long targetable) {
        int currentPlayer = state.currentPlayer();
        offset = generate(state, buffer, offset, kingMoves, Piece.king(currentPlayer), targetable);
        offset = generate(state, buffer, offset, bishopMoves, Piece.bishop(currentPlayer), targetable);
        offset = generate(state, buffer, offset, knightMoves, Piece.knight(currentPlayer), targetable);
        offset = generate(state, buffer, offset, rookMoves, Piece.rook(currentPlayer), targetable);
        offset = generate(state, buffer, offset, queenMoves, Piece.queen(currentPlayer), targetable);
        return offset;
    }
    
    private int generate(ChessState state, Move[] buffer, int offset, SinglePieceMoveMask moves, int piece, long targetable) {
        long allPieces = state.allPieces();
        long pieceMask = state.pieceMasks[piece];
        while (pieceMask != 0) {
            int from = Mask.lowest(pieceMask);
            long targets = moves.moves(from, allPieces, targetable);
            offset = defaultGenerate(state.pieces, buffer, offset, from, targets);
            pieceMask &= pieceMask - 1;
        }
        return offset;
    }

    private int defaultGenerate(int[] pieces, Move[] buffer, int offset, int from, long targets) {
        while (targets != 0) {
            int to = Mask.lowest(targets);
            Move move = buffer[offset++];
            move.to = to;
            move.from = from;
            move.capture = pieces[to];
            move.info = Piece.EMPTY;
            targets &= targets - 1;
        }
        return offset;
    }
}