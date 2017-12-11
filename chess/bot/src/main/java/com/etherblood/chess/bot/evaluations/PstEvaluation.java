package com.etherblood.chess.bot.evaluations;

import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.util.Board;
import com.etherblood.chess.engine.util.Mask;
import com.etherblood.chess.engine.util.Piece;
import com.etherblood.chess.engine.util.Player;

/**
 *
 * @author Philipp
 */
public class PstEvaluation implements Evaluation {

    private static final int[] PST;

    static {
        int[][] calc = new int[6][];
        calc[0] = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0,
            100, 100, 100, 60, 60, 100, 100, 100,
            101, 102, 103, 90, 90, 103, 102, 101,
            102, 104, 106, 108, 108, 106, 104, 102,
            103, 106, 109, 112, 112, 109, 106, 103,
            104, 108, 112, 116, 116, 112, 108, 104,
            105, 110, 115, 120, 120, 115, 110, 105,
            0, 0, 0, 0, 0, 0, 0, 0
        };
        calc[1] = new int[]{
            0, 20, 40, -20, 0, -20, 40, 20,
            -20, -20, -20, -20, -20, -20, -20, -20,
            -40, -40, -40, -40, -40, -40, -40, -40,
            -40, -40, -40, -40, -40, -40, -40, -40,
            -40, -40, -40, -40, -40, -40, -40, -40,
            -40, -40, -40, -40, -40, -40, -40, -40,
            -40, -40, -40, -40, -40, -40, -40, -40,
            -40, -40, -40, -40, -40, -40, -40, -40
        };
        calc[2] = new int[]{
            290, 270, 290, 290, 290, 290, 270, 290,
            290, 300, 300, 300, 300, 300, 300, 290,
            290, 300, 305, 305, 305, 305, 300, 290,
            290, 300, 305, 310, 310, 305, 300, 290,
            290, 300, 305, 310, 310, 305, 300, 290,
            290, 300, 305, 305, 305, 305, 300, 290,
            290, 300, 300, 300, 300, 300, 300, 290,
            290, 290, 290, 290, 290, 290, 290, 290
        };
        calc[3] = new int[]{
            290, 290, 280, 290, 290, 280, 290, 290,
            290, 300, 300, 300, 300, 300, 300, 290,
            290, 300, 305, 305, 305, 305, 300, 290,
            290, 300, 305, 310, 310, 305, 300, 290,
            290, 300, 305, 310, 310, 305, 300, 290,
            290, 300, 305, 305, 305, 305, 300, 290,
            290, 300, 300, 300, 300, 300, 300, 290,
            290, 290, 290, 290, 290, 290, 290, 290
        };
        calc[4] = new int[]{
            490, 500, 500, 510, 510, 500, 500, 490,
            500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500,
            515, 515, 515, 515, 515, 515, 515, 515,
            500, 500, 500, 500, 500, 500, 500, 500,};
        calc[5] = new int[]{
            890, 890, 890, 890, 890, 890, 890, 890,
            890, 900, 900, 900, 900, 900, 900, 890,
            890, 900, 905, 905, 905, 905, 900, 890,
            890, 900, 905, 910, 910, 905, 900, 890,
            890, 900, 905, 910, 910, 905, 900, 890,
            890, 900, 905, 905, 905, 905, 900, 890,
            890, 900, 900, 900, 900, 900, 900, 890,
            890, 890, 890, 890, 890, 890, 890, 890
        };

        PST = new int[16 * 64];
        for (int square = 0; square < 64; square++) {
            PST[(Piece.W_PAWN << 6) | square] = calc[0][square];
            PST[(Piece.W_KING << 6) | square] = calc[1][square];
            PST[(Piece.W_BISHOP << 6) | square] = calc[2][square];
            PST[(Piece.W_KNIGHT << 6) | square] = calc[3][square];
            PST[(Piece.W_ROOK << 6) | square] = calc[4][square];
            PST[(Piece.W_QUEEN << 6) | square] = calc[5][square];

            int m = Board.mirror(square);
            PST[(Piece.B_PAWN << 6) | square] = -calc[0][m];
            PST[(Piece.B_KING << 6) | square] = -calc[1][m];
            PST[(Piece.B_BISHOP << 6) | square] = -calc[2][m];
            PST[(Piece.B_KNIGHT << 6) | square] = -calc[3][m];
            PST[(Piece.B_ROOK << 6) | square] = -calc[4][m];
            PST[(Piece.B_QUEEN << 6) | square] = -calc[5][m];
        }
    }

    @Override
    public int evaluate(ChessState state, int alpha, int beta) {
        int wPawns = Mask.count(state.pieceMasks[Piece.W_PAWN]);
        int bPawns = Mask.count(state.pieceMasks[Piece.B_PAWN]);

        if ((wPawns | bPawns) == 0) {
            int wKnights = Mask.count(state.pieceMasks[Piece.W_KNIGHT]);
            int wBishops = Mask.count(state.pieceMasks[Piece.W_BISHOP]);
            int wRooks = Mask.count(state.pieceMasks[Piece.W_ROOK]);
            int wQueens = Mask.count(state.pieceMasks[Piece.W_QUEEN]);
            int wTotal = wKnights + wBishops + wRooks + wQueens;

            int bKnights = Mask.count(state.pieceMasks[Piece.B_KNIGHT]);
            int bBishops = Mask.count(state.pieceMasks[Piece.B_BISHOP]);
            int bRooks = Mask.count(state.pieceMasks[Piece.B_ROOK]);
            int bQueens = Mask.count(state.pieceMasks[Piece.B_QUEEN]);
            int bTotal = bKnights + bBishops + bRooks + bQueens;
            
            if (wTotal + bTotal == 0) {
                return 0;
            }

            if (bTotal == 0 && wTotal == 1 && wKnights == 1) {
                return 0;
            }
            if (wTotal == 0 && bTotal == 1 && bKnights == 1) {
                return 0;
            }

            if (wBishops == wTotal && bBishops == bTotal) {
                if (((state.pieceMasks[Piece.W_BISHOP] | state.pieceMasks[Piece.B_BISHOP]) & Mask.WHITE_SQUARES) == 0
                        || ((state.pieceMasks[Piece.W_BISHOP] | state.pieceMasks[Piece.B_BISHOP]) & Mask.BLACK_SQUARES) == 0) {
                    return 0;
                }
            }
        }

        int score = 0;
        for (int square = 0; square < 64; square++) {
            int piece = state.pieces[square];
            score += PST[(piece << 6) | square];
        }

        return Player.sign(state.currentPlayer()) * score + 5;
    }
    
    public static int pieceSquareScore(int piece, int square) {
        assert (piece & 15) == piece;
        assert (square & 63) == square;
        return PST[(piece << 6) | square];
    }

}
