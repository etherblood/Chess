package com.etherblood.chess.server.matches.model;

import com.etherblood.chess.engine.util.Piece;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Philipp
 */
public enum ChessPieceType {
    @JsonProperty("pawn")
    PAWN(Piece.W_PAWN),
    @JsonProperty("king")
    KING(Piece.W_KING),
    @JsonProperty("knight")
    KNIGHT(Piece.W_KNIGHT),
    @JsonProperty("bishop")
    BISHOP(Piece.W_BISHOP),
    @JsonProperty("rook")
    ROOK(Piece.W_ROOK),
    @JsonProperty("queen")
    QUEEN(Piece.W_QUEEN);

    private final int pieceCode;

    private ChessPieceType(int pieceCode) {
        this.pieceCode = pieceCode;
    }

    public int getPieceCode() {
        return pieceCode;
    }
}
