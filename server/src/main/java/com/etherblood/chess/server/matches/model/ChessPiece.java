package com.etherblood.chess.server.matches.model;

/**
 *
 * @author Philipp
 */
public class ChessPiece {

    private ChessPieceType type;
    private ChessPieceColor color;
    private int square;

    public ChessPiece() {
    }

    public ChessPiece(ChessPieceType type, ChessPieceColor color, int square) {
        this.type = type;
        this.color = color;
        this.square = square;
    }

    public ChessPieceType getType() {
        return type;
    }

    public ChessPieceColor getColor() {
        return color;
    }

    public int getSquare() {
        return square;
    }
}
