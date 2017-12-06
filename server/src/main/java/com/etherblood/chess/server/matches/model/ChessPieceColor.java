package com.etherblood.chess.server.matches.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Philipp
 */
public enum ChessPieceColor {
    @JsonProperty("white")
    WHITE,
    @JsonProperty("black")
    BLACK
}
