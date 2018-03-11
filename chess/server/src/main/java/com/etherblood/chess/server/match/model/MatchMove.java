package com.etherblood.chess.server.match.model;

import com.etherblood.chess.engine.moves.Move;
import java.util.Date;

/**
 *
 * @author Philipp
 */
public class MatchMove {

    private Move move;
    private Date date;

    public MatchMove() {
    }

    public MatchMove(Move move, Date date) {
        this.move = move;
        this.date = date;
    }

    public Move getMove() {
        return move;
    }

    public Date getDate() {
        return date;
    }
}
