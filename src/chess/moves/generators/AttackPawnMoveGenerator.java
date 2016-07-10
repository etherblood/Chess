package chess.moves.generators;

import chess.ChessState;
import chess.moves.Move;
import chess.util.Mask;
import chess.util.Piece;
import chess.util.Player;


public final class AttackPawnMoveGenerator extends AbstractPawnMoveGenerator {

    @Override
    public int generateMoves(ChessState state, Move[] buffer, int offset) {
        int currentPlayer = state.currentPlayer();
        boolean isWhite = Player.isWhite(currentPlayer);
        long ownPawns = state.pieceMasks[Piece.withOwner(Piece.W_PAWN, currentPlayer)];
        int enPassant = state.currentHistory().enPassant;
        long enPassantMask = Mask.single(enPassant) & ~1;
        long attackableMask = state.playerMasks[state.opponentPlayer()] | enPassantMask;
        if (isWhite) {
            long targetSquares = Mask.whitePawn7Attacks(ownPawns) & attackableMask;
            offset = pawnAttackBuffer(targetSquares, currentPlayer, -7, state, buffer, offset);
            targetSquares = Mask.whitePawn9Attacks(ownPawns) & attackableMask;
            offset = pawnAttackBuffer(targetSquares, currentPlayer, -9, state, buffer, offset);
        } else {
            long targetSquares = Mask.blackPawn9Attacks(ownPawns) & attackableMask;
            offset = pawnAttackBuffer(targetSquares, currentPlayer, 9, state, buffer, offset);
            targetSquares = Mask.blackPawn7Attacks(ownPawns) & attackableMask;
            offset = pawnAttackBuffer(targetSquares, currentPlayer, 7, state, buffer, offset);
        }
        return offset;
    }

    private int pawnAttackBuffer(long targetSquares, int currentPlayer, int direction, ChessState state, Move[] buffer, int offset) {
        while (targetSquares != 0) {
            int to = Mask.last(targetSquares);
            int from = to + direction;
            long single = Mask.single(to);
            int capture = state.pieces[to];
            if (((Mask.rank1 | Mask.rank8) & single) == 0) {
                Move move = buffer[offset++];
                move.to = to;
                move.from = from;
                if(capture != Piece.EMPTY) {
                    move.capture = capture;
                    move.info = Piece.EMPTY;
                } else {
                    move.capture = Piece.pawn(Player.opponent(currentPlayer));
                    move.info = Piece.RESERVED_2;
                }
            } else {
                offset = generatePromotions(from, to, capture, currentPlayer, buffer, offset);
            }
            targetSquares ^= single;
        }
        return offset;
    }

}
