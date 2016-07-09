package chess.moves.generators;

import chess.ChessState;
import chess.moves.Move;
import chess.util.Mask;
import chess.util.Piece;
import chess.util.Player;


public final class QuietPawnMoveGenerator extends AbstractPawnMoveGenerator {

    @Override
    public int generateMoves(ChessState state, Move[] buffer, int offset) {
        long allPieces = state.allPieces();
        int currentPlayer = state.currentPlayer();
        boolean isWhite = Player.isWhite(currentPlayer);
        long ownPawns = state.pieceMasks[Piece.withOwner(Piece.W_PAWN, currentPlayer)];
        long pawnSimpleMoves = generatePawnSimpleMoves(ownPawns, allPieces, isWhite);
        long targetSquares = pawnSimpleMoves;
        while (targetSquares != 0) {
            int to = Mask.last(targetSquares);
            int from = to - Player.sign(currentPlayer) * 8;
            long single = Mask.single(to);
            if (((Mask.rank1 | Mask.rank8) & single) == 0) {
                Move move = buffer[offset++];
                move.to = to;
                move.from = from;
                move.capture = Piece.EMPTY;
                move.info = Piece.EMPTY;
            } else {
                offset = generatePromotions(from, to, Piece.EMPTY, currentPlayer, buffer, offset);
            }
            targetSquares ^= single;
        }

        targetSquares = generatePawnDoubleMoves(pawnSimpleMoves, allPieces, isWhite);
        while (targetSquares != 0) {
            Move move = buffer[offset++];
            int single = Mask.last(targetSquares);
            move.to = single;
            move.from = single - Player.sign(currentPlayer) * 16;
            move.capture = Piece.EMPTY;
            move.info = Piece.RESERVED_1;
            targetSquares ^= Mask.single(single);
        }
        return offset;
    }

    private long generatePawnSimpleMoves(long pawns, long allPieces, boolean white) {
        if (white) {
            pawns <<= 8;
        } else {
            pawns >>>= 8;
        }
        return pawns & ~allPieces;
    }

    private long generatePawnDoubleMoves(long simplePawnMoves, long allPieces, boolean white) {
        if (white) {
            simplePawnMoves &= Mask.rank3;
            simplePawnMoves <<= 8;
        } else {
            simplePawnMoves &= Mask.rank6;
            simplePawnMoves >>>= 8;
        }
        return simplePawnMoves & ~allPieces;
    }

}
