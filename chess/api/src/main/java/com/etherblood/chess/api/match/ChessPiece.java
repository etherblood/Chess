package com.etherblood.chess.api.match;

/**
 *
 * @author Philipp
 */
public enum ChessPiece {
    WHITE_PAWN, WHITE_KING, WHITE_KNIGHT, WHITE_BISHOP, WHITE_ROOK, WHITE_QUEEN,
    BLACK_PAWN, BLACK_KING, BLACK_KNIGHT, BLACK_BISHOP, BLACK_ROOK, BLACK_QUEEN;

    public ChessColor getColor() {
        switch(this) {
            case WHITE_PAWN:
            case WHITE_KING:
            case WHITE_KNIGHT:
            case WHITE_BISHOP:
            case WHITE_ROOK:
            case WHITE_QUEEN:
                return ChessColor.WHITE;
            case BLACK_PAWN:
            case BLACK_KING:
            case BLACK_KNIGHT:
            case BLACK_BISHOP:
            case BLACK_ROOK:
            case BLACK_QUEEN:
                return ChessColor.BLACK;
            default:
                throw new AssertionError();
        }
    }
    
    public ChessPieceType getType() {
        switch(this) {
            case WHITE_PAWN:
            case BLACK_PAWN:
                return ChessPieceType.PAWN;
            case WHITE_KING:
            case BLACK_KING:
                return ChessPieceType.KING;
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                return ChessPieceType.KNIGHT;
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                return ChessPieceType.BISHOP;
            case WHITE_ROOK:
            case BLACK_ROOK:
                return ChessPieceType.ROOK;
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                return ChessPieceType.QUEEN;
            default:
                throw new AssertionError();
        }
    }
}
