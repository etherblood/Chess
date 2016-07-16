package chess.moves.generators.implementations;

import chess.ChessState;
import chess.moves.Move;
import chess.util.Castling;
import chess.util.Mask;
import chess.util.Piece;
import chess.util.Player;


public class CastlingMoveGenerator {

    public int generateMoves(ChessState state, Move[] buffer, int offset) {
        long allPieces = state.allPieces();
        int currentPlayer = state.currentPlayer();
        long kings = state.pieceMasks[Piece.king(currentPlayer)];
        int from = Mask.last(kings);
        long availableCastlings = Castling.availableCastlings(state.currentHistory().castling);
        availableCastlings &= Mask.RANK_1 << (currentPlayer * 56);
        while (availableCastlings != 0) {            
            int castling = Mask.last(availableCastlings);
            long free = Castling.castlingFreeArea(castling);
            if((allPieces & free) == 0) {
                long safety = Castling.castlingSafetyArea(castling);
                if(!isThreateningZone(state, state.opponentPlayer(), safety)) {
                    Move move = buffer[offset++];
                    move.to = castling;
                    move.from = from;
                    move.capture = Piece.EMPTY;
                    move.info = Piece.RESERVED_3;
                }
            }
            availableCastlings ^= Mask.single(castling);
        }
        return offset;
    }
    
    
    public boolean isThreateningKing(ChessState state) {
        return isThreateningPosition(state, state.currentPlayer(), Mask.last(state.pieceMasks[Piece.king(state.opponentPlayer())]));
    }

    public boolean isKingThreatened(ChessState state) {
        return isThreateningPosition(state, state.opponentPlayer(), Mask.last(state.pieceMasks[Piece.king(state.currentPlayer())]));
    }

    public boolean isThreateningZone(ChessState state, int attacker, long zone) {
        while (zone != 0) {
            int pos = Mask.first(zone);
            zone ^= Mask.single(pos);

            if (isThreateningPosition(state, attacker, pos)) {
                return true;
            }
        }
        return false;
    }

    private boolean isThreateningPosition(ChessState state, int attacker, int square) {
        int defender = Player.opponent(attacker);

        long threat = Mask.pawnThreat(square, defender) & state.pieceMasks[Piece.pawn(attacker)];
        threat |= Mask.knightAttacks(square) & state.pieceMasks[Piece.knight(attacker)];
        threat |= Mask.kingAttacks(square) & state.pieceMasks[Piece.king(attacker)];
        if (threat != 0) {
            return true;
        }
        threat = Mask.rookMovement(state.allPieces(), square) & (state.pieceMasks[Piece.rook(attacker)] | state.pieceMasks[Piece.queen(attacker)]);
        if (threat != 0) {
            return true;
        }
        threat = Mask.bishopMovement(state.allPieces(), square) & (state.pieceMasks[Piece.bishop(attacker)] | state.pieceMasks[Piece.queen(attacker)]);
        return threat != 0;
    }

    public long pawnThreat(long pawns, boolean white) {
        if (white) {
            pawns = Mask.whitePawn7Attacks(pawns) | Mask.whitePawn9Attacks(pawns);
        } else {
            pawns = Mask.blackPawn7Attacks(pawns) | Mask.blackPawn9Attacks(pawns);
        }
        return pawns;
    }

}
