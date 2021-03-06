package com.etherblood.chess.engine.moves.generators.implementations;

import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.util.Mask;
import com.etherblood.chess.engine.util.Piece;
import com.etherblood.chess.engine.util.Player;


public final class AttackPawnMoveGenerator extends AbstractPawnMoveGenerator {

    public int generateMoves(ChessState state, Move[] buffer, int offset) {
        int currentPlayer = state.currentPlayer();
        boolean isWhite = Player.isWhite(currentPlayer);
        long ownPawns = state.pieceMasks[Piece.pawn(currentPlayer)];
        int enPassant = state.currentHistory().enPassant;
        long enPassantMask = Mask.toFlag(enPassant) & ~1;
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
            int to = Mask.lowest(targetSquares);
            int from = to + direction;
            long single = Mask.toFlag(to);
            int capture = state.pieces[to];
            if (((Mask.RANK_1 | Mask.RANK_8) & single) == 0) {
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
