package com.etherblood.chess.api.match.moves;

import com.etherblood.chess.api.match.ChessPiece;
import com.etherblood.chess.api.match.ChessSquare;

/**
 *
 * @author Philipp
 */
public class ChessMove {

    public MoveType type;
    public ChessSquare from, to;
    public ChessPiece promotion;

    public ChessMove(MoveType type, ChessSquare from, ChessSquare to) {
        this(type, from, to, null);
    }

    public ChessMove(MoveType type, ChessSquare from, ChessSquare to, ChessPiece promotion) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }
}
