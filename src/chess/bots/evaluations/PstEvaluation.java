package chess.bots.evaluations;

import chess.ChessState;
import chess.util.Board;
import chess.util.Mask;
import chess.util.Piece;
import chess.util.Player;
import chess.util.Score;

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
        int score = 0;
        for (int square = 0; square < 64; square++) {
            int piece = state.pieces[square];
            score += PST[(piece << 6) | square];
        }
//        score += 100 * Mask.count(state.pieceMasks[Piece.W_PAWN]);
//        score += 300 * Mask.count(state.pieceMasks[Piece.W_BISHOP]);
//        score += 300 * Mask.count(state.pieceMasks[Piece.W_KNIGHT]);
//        score += 500 * Mask.count(state.pieceMasks[Piece.W_ROOK]);
//        score += 900 * Mask.count(state.pieceMasks[Piece.W_QUEEN]);
//        
//        score -= 100 * Mask.count(state.pieceMasks[Piece.B_PAWN]);
//        score -= 300 * Mask.count(state.pieceMasks[Piece.B_BISHOP]);
//        score -= 300 * Mask.count(state.pieceMasks[Piece.B_KNIGHT]);
//        score -= 500 * Mask.count(state.pieceMasks[Piece.B_ROOK]);
//        score -= 900 * Mask.count(state.pieceMasks[Piece.B_QUEEN]);
        
        return Score.boundScore(alpha, Player.sign(state.currentPlayer()) * score + 5, beta);
    }

}
