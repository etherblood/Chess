package chess.moves.generators;

import chess.ChessState;
import chess.moves.Move;
import chess.moves.generators.moveMasks.SinglePieceMoveMask;
import chess.moves.generators.moveMasks.BishopMoveMask;
import chess.moves.generators.moveMasks.KingMoveMask;
import chess.moves.generators.moveMasks.KnightMoveMask;
import chess.moves.generators.moveMasks.QueenMoveMask;
import chess.moves.generators.moveMasks.RookMoveMask;
import chess.util.Mask;
import chess.util.Piece;


public final class DefaultMoveGenerator extends AbstractMoveGenerator {
    private final KingMoveMask kingMoves = new KingMoveMask();
    private final BishopMoveMask bishopMoves = new BishopMoveMask();
    private final KnightMoveMask knightMoves = new KnightMoveMask();
    private final RookMoveMask rookMoves = new RookMoveMask();
    private final QueenMoveMask queenMoves = new QueenMoveMask();
    
    @Override
    public int generateMoves(ChessState state, Move[] buffer, int offset) {
        offset = generate(state, buffer, offset, kingMoves, Piece.W_KING);
        offset = generate(state, buffer, offset, bishopMoves, Piece.W_BISHOP);
        offset = generate(state, buffer, offset, knightMoves, Piece.W_KNIGHT);
        offset = generate(state, buffer, offset, rookMoves, Piece.W_ROOK);
        offset = generate(state, buffer, offset, queenMoves, Piece.W_QUEEN);
        return offset;
    }
    
    private int generate(ChessState state, Move[] buffer, int offset, SinglePieceMoveMask moves, int whitePiece) {
        long allPieces = state.allPieces();
        int currentPlayer = state.currentPlayer();
        long pieceMask = state.pieceMasks[Piece.withOwner(whitePiece, currentPlayer)];
        long ownPieces = state.playerMasks[currentPlayer];
        while (pieceMask != 0) {
            int from = Mask.last(pieceMask);
            long targets = moves.moves(from, allPieces, ownPieces);
            offset = defaultGenerate(state, buffer, offset, from, targets);
            pieceMask ^= Mask.single(from);
        }
        return offset;
    }

    private int defaultGenerate(ChessState state, Move[] buffer, int offset, int from, long targets) {
        while (targets != 0) {
            int to = Mask.last(targets);
            Move move = buffer[offset++];
            move.to = to;
            move.from = from;
            move.capture = state.pieces[to];
            move.info = Piece.EMPTY;
            targets ^= Mask.single(to);
        }
        return offset;
    }
}