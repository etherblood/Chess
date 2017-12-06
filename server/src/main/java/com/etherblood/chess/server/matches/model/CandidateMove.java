package com.etherblood.chess.server.matches.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author Philipp
 */
public class CandidateMove {

    private int from, to;
    @JsonInclude(Include.NON_NULL)
    private ChessPieceType promotion;

    public CandidateMove() {
    }

    public CandidateMove(int from, int to, ChessPieceType promotion) {
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public ChessPieceType getPromotion() {
        return promotion;
    }
}
