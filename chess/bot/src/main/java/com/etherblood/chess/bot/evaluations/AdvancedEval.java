package com.etherblood.chess.bot.evaluations;

import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.moves.generators.implementations.QuietPawnMoveGenerator;
import com.etherblood.chess.engine.util.Board;
import com.etherblood.chess.engine.util.Castling;
import com.etherblood.chess.engine.util.Mask;
import com.etherblood.chess.engine.util.Piece;
import com.etherblood.chess.engine.util.Player;

/**
 *
 * @author Philipp
 */
public class AdvancedEval implements Evaluation {
    private static final int SIDE_TO_MOVE = 5;
    private static final int BISHOP_PAIR = 10;
    private static final int ROOK_BEHIND_PASSED = 20;
    private static final int STRONG_SHIELD = 9;
    private static final int WEAK_SHIELD = 4;
    private static final int PASSED_PAWN = 20;
    private static final int DOUBLED_PAWN = -10;
    private static final int ISOLATED_PAWN = -20;
    private static final int BACKWARD_PAWN = -8;
    private static final int[] W_CASTLING_SCORES;
    private static final int[] PAWN_OPP_DISTANCE = new int[] { 0, 2, 1, 0, 0, 0, 0, 0 };
    private static final int[] PAWN_OWN_DISTANCE = new int[] { 0, 8, 4, 2, 0, 0, 0, 0 };
    private static final int[] KNIGHT_DISTANCE = new int[] { 0, 4, 4, 0, 0, 0, 0, 0 };
    private static final int[] BISHOP_DISTANCE = new int[] { 0, 5, 4, 3, 2, 1, 0, 0 };
    private static final int[] ROOK_DISTANCE = new int[] { 0, 7, 5, 4, 3, 0, 0, 0 };
    private static final int[] QUEEN_DISTANCE = new int[] { 0, 10, 8, 5, 4, 0, 0, 0 };
    
    static {
        int scoreNone = 0;
        int scoreA = 4;
        int scoreH = 5;
        int scoreBoth = 6;
        W_CASTLING_SCORES = new int[16];
        for (int i = 0; i < 16; i++) {
            int score = 0;
            switch(i & (Castling.CASTLE_A1 | Castling.CASTLE_H1)) {
                case 0:
                    score += scoreNone;
                    break;
                case Castling.CASTLE_A1:
                    score += scoreA;
                    break;
                case Castling.CASTLE_H1:
                    score += scoreH;
                    break;
                case Castling.CASTLE_A1 | Castling.CASTLE_H1:
                    score += scoreBoth;
                    break;
            }
            
            switch(i & (Castling.CASTLE_A8 | Castling.CASTLE_H8)) {
                case 0:
                    score -= scoreNone;
                    break;
                case Castling.CASTLE_A8:
                    score -= scoreA;
                    break;
                case Castling.CASTLE_H8:
                    score -= scoreH;
                    break;
                case Castling.CASTLE_A8 | Castling.CASTLE_H8:
                    score -= scoreBoth;
                    break;
            }
            
            W_CASTLING_SCORES[i] = score;
        }
    }
    
    @Override
    public int evaluate(ChessState state, int alpha, int beta) {
        if ((state.pieceMasks[Piece.W_PAWN] | state.pieceMasks[Piece.B_PAWN]) == 0) {
            long whitePieces = state.playerMasks[Player.WHITE] ^ state.pieceMasks[Piece.W_KING];
            long blackPieces = state.playerMasks[Player.BLACK] ^ state.pieceMasks[Piece.B_KING];
            long allPieces = whitePieces | blackPieces;
            
            long allKnights = state.pieceMasks[Piece.W_KNIGHT] | state.pieceMasks[Piece.B_KNIGHT];
            if((allKnights & (allKnights - 1)) == 0) {
                if (allKnights == allPieces) {
                    return 0;
                }
                long allBishops = state.pieceMasks[Piece.W_BISHOP] | state.pieceMasks[Piece.B_BISHOP];
                if (allBishops == allPieces && ((allBishops & Mask.WHITE_SQUARES) == 0 || (allBishops & Mask.BLACK_SQUARES) == 0)) {
                    return 0;
                }
            }
        }
        
        int w_score = 0;
        w_score += W_CASTLING_SCORES[state.currentHistory().castling];
        w_score += playerEval(state, Player.WHITE);
        w_score -= playerEval(state, Player.BLACK);
        
        int score = Player.sign(state.currentPlayer()) * w_score;
        return score + SIDE_TO_MOVE;
    }
    
    private int playerEval(ChessState state, int player) {
        int opponent = player;
        long ownPieces = state.playerMasks[player];
        long oppPieces = state.playerMasks[opponent];
        long allPieces = ownPieces | oppPieces;
        int king = Piece.king(player);
        int ownKingPos = Mask.lowest(state.pieceMasks[king]);
        int oppKingPos = Mask.lowest(state.pieceMasks[Piece.king(opponent)]);
        long passedPawns = 0;
        int score = 0;
        boolean endgame = false;
        int mobility = 0;
        
        int pawn = Piece.pawn(player);
        long pawns = state.pieceMasks[pawn];
        if(player == Player.WHITE) {
            long singleMoves = QuietPawnMoveGenerator.generatePawnSimpleMoves(pawns, allPieces, true);
            long doubleMoves = QuietPawnMoveGenerator.generatePawnDoubleMoves(singleMoves, allPieces, true);
            mobility += Mask.count((Mask.whitePawn9Attacks(pawns) & oppPieces) | singleMoves | doubleMoves);
            mobility += Mask.count(Mask.whitePawn7Attacks(pawns) & oppPieces);
        } else {
            long singleMoves = QuietPawnMoveGenerator.generatePawnSimpleMoves(pawns, allPieces, false);
            long doubleMoves = QuietPawnMoveGenerator.generatePawnDoubleMoves(singleMoves, allPieces, false);
            mobility += Mask.count((Mask.blackPawn9Attacks(pawns) & oppPieces) | singleMoves | doubleMoves);
            mobility += Mask.count(Mask.blackPawn7Attacks(pawns) & oppPieces);
        }
        while (pawns != 0) {
            int square = Mask.lowest(pawns);
            pawns &= pawns - 1;

            score += PstEvaluation.pieceSquareScore(Piece.asWhitePiece(pawn), square);
            score += PAWN_OPP_DISTANCE[Board.distance(square, oppKingPos)];
            if (endgame) {
                score += PAWN_OWN_DISTANCE[Board.distance(square, ownKingPos)];
            }
//                if ((Mask.mPassedPawns[player, position] & mPieceMasks[Piece.sPawn[opponent]]) == 0)
//                {
//                    score += PassedPawn;
//                    passedPawns ^= Mask.mSingleBit[position];
//                }
//                if ((mask & Mask.mFileMask[position]) != 0)
//                {
//                    score += Score.DoubledPawn;
//                }
//                if ((Mask.mIsolatedPawns[player, position] & mPieceMasks[Piece.sPawn[player]]) == 0)
//                {
//                    score += Score.IsolatedPawn;
//                }
//                else
//                {
//                    if ((Piece.mPawnAttackMask[player][position + 8 * Player.sSign[player]] & mPieceMasks[Piece.sPawn[opponent]]) != 0)
//                    {
//                        if ((Mask.mBackwardPawns[player, position] & mPieceMasks[Piece.sPawn[player]]) == 0)
//                        {
//                            score += Score.BackwardPawn;
//                        }
//                    }
//                }
            
        }
        
        //king
        score += PstEvaluation.pieceSquareScore(Piece.asWhitePiece(king), ownKingPos);
        mobility += Mask.count(Mask.kingAttacks(ownKingPos) & ~ownPieces);
        if (!endgame) {
//                score += StrongShield * Mask.count(Mask.mStrongShield[player, ownKingPos] & mPieceMasks[Piece.sPawn[player]]);
//                score += WeakShield * Mask.count(Mask.mWeakShield[player, ownKingPos] & mPieceMasks[Piece.sPawn[player]]);
        }
        
        int rook = Piece.rook(player);
        long rooks = state.pieceMasks[rook];
        while (rooks != 0) {
            int square = Mask.lowest(rooks);
            rooks &= rooks - 1;

            score += PstEvaluation.pieceSquareScore(Piece.asWhitePiece(rook), square);
            score += ROOK_DISTANCE[Board.distance(square, oppKingPos)];
            mobility += Mask.count(Mask.rookMovement(allPieces, square) & ~ownPieces);

            if ((Mask.fileMask(square) & passedPawns) != 0) {
                if (square < Mask.lowest(Mask.fileMask(square) & passedPawns)) {
                    score += ROOK_BEHIND_PASSED;
                }
            }
        }
        
        int knight = Piece.knight(player);
        long knights = state.pieceMasks[knight];
        while (knights != 0) {
            int square = Mask.lowest(knights);
            knights &= knights - 1;

            score += PstEvaluation.pieceSquareScore(Piece.asWhitePiece(knight), square);
            score += KNIGHT_DISTANCE[Board.distance(square, oppKingPos)];
            mobility += Mask.count(Mask.knightAttacks(square) & ~ownPieces);
        }
        
        int bishop = Piece.bishop(player);
        long bishops = state.pieceMasks[bishop];
        if ((bishops & (bishops - 1)) != 0) {
            score += BISHOP_PAIR;
        }
        while (bishops != 0) {
            int square = Mask.lowest(bishops);
            bishops &= bishops - 1;

            score += PstEvaluation.pieceSquareScore(Piece.asWhitePiece(bishop), square);
            score += BISHOP_DISTANCE[Board.distance(square, oppKingPos)];
            mobility += Mask.count(Mask.bishopMovement(allPieces, square) & ~ownPieces);
        }
        
        int queen = Piece.queen(player);
        long queens = state.pieceMasks[queen];
        while (queens != 0) {
            int square = Mask.lowest(queens);
            queens &= queens - 1;

            score += PstEvaluation.pieceSquareScore(Piece.asWhitePiece(queen), square);
            score += QUEEN_DISTANCE[Board.distance(square, oppKingPos)];
            mobility += Mask.count(Mask.queenMovement(allPieces, square) & ~ownPieces);
        }

        score += mobility;
        return score;
    }

}
