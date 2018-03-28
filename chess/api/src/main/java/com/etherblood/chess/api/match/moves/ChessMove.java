package com.etherblood.chess.api.match.moves;

import com.etherblood.chess.api.match.ChessSquare;

/**
 *
 * @author Philipp
 */
public class ChessMove {

    public MoveType type;
    public ChessSquare from, to;

    public ChessMove(MoveType type, ChessSquare from, ChessSquare to) {
        this.type = type;
        this.from = from;
        this.to = to;
    }

	@Override
	public String toString() {
		return "ChessMove [type=" + type + ", from=" + from + ", to=" + to + "]";
	}
}
