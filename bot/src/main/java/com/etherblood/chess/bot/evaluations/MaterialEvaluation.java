package com.etherblood.chess.bot.evaluations;

import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.util.Mask;
import com.etherblood.chess.engine.util.Piece;
import com.etherblood.chess.engine.util.Player;


public class MaterialEvaluation implements Evaluation {
    
    @Override
    public int evaluate(ChessState state, int alpha, int beta) {
        int score = 0;
        score += 100 * Mask.count(state.pieceMasks[Piece.W_PAWN]);
        score += 300 * Mask.count(state.pieceMasks[Piece.W_BISHOP]);
        score += 300 * Mask.count(state.pieceMasks[Piece.W_KNIGHT]);
        score += 500 * Mask.count(state.pieceMasks[Piece.W_ROOK]);
        score += 900 * Mask.count(state.pieceMasks[Piece.W_QUEEN]);
        
        score -= 100 * Mask.count(state.pieceMasks[Piece.B_PAWN]);
        score -= 300 * Mask.count(state.pieceMasks[Piece.B_BISHOP]);
        score -= 300 * Mask.count(state.pieceMasks[Piece.B_KNIGHT]);
        score -= 500 * Mask.count(state.pieceMasks[Piece.B_ROOK]);
        score -= 900 * Mask.count(state.pieceMasks[Piece.B_QUEEN]);
        
        return Player.sign(state.currentPlayer()) * score;
    }

}
