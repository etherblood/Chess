package chess.moves.generators.implementations;

import chess.ChessState;
import chess.moves.Move;
import chess.util.Mask;
import chess.util.Piece;
import chess.util.Player;


public final class QuietPawnMoveGenerator extends AbstractPawnMoveGenerator {

    public int generateMoves(ChessState state, Move[] buffer, int offset) {
        long allPieces = state.allPieces();
        int currentPlayer = state.currentPlayer();
        boolean isWhite = Player.isWhite(currentPlayer);
        long ownPawns = state.pieceMasks[Piece.pawn(currentPlayer)];
        long pawnSimpleMoves = generatePawnSimpleMoves(ownPawns, allPieces, isWhite);
        long targetSquares = pawnSimpleMoves;
        while (targetSquares != 0) {
            int to = Mask.lowest(targetSquares);
            int from = to - Player.sign(currentPlayer) * 8;
            long single = Mask.single(to);
            if (((Mask.RANK_1 | Mask.RANK_8) & single) == 0) {
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
            int to = Mask.lowest(targetSquares);
            move.to = to;
            move.from = to - 16 * Player.sign(currentPlayer);
            move.capture = Piece.EMPTY;
            move.info = Piece.RESERVED_1;
            targetSquares &= targetSquares - 1;
        }
        return offset;
    }

    public static long generatePawnSimpleMoves(long pawns, long allPieces, boolean white) {
        if (white) {
            pawns <<= 8;
        } else {
            pawns >>>= 8;
        }
        return pawns & ~allPieces;
    }

    public static long generatePawnDoubleMoves(long simplePawnMoves, long allPieces, boolean white) {
        if (white) {
            simplePawnMoves &= Mask.RANK_3;
            simplePawnMoves <<= 8;
        } else {
            simplePawnMoves &= Mask.RANK_6;
            simplePawnMoves >>>= 8;
        }
        return simplePawnMoves & ~allPieces;
    }

}
