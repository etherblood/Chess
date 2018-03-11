package com.etherblood.chess.server.match;

import com.etherblood.chess.api.match.ChessPiece;
import com.etherblood.chess.api.match.ChessSquare;
import com.etherblood.chess.api.match.moves.ChessMove;
import com.etherblood.chess.api.match.moves.MoveType;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.util.Piece;

/**
 *
 * @author Philipp
 */
public class ChessModelConverter {

    public static ChessMove convertMove(Move move) {
        switch (move.info) {
            case Piece.EMPTY:
                return new ChessMove(MoveType.SIMPLE, ChessSquare.fromInt(move.from), ChessSquare.fromInt(move.to));
            case Piece.RESERVED_1:
                return new ChessMove(MoveType.DOUBLE, ChessSquare.fromInt(move.from), ChessSquare.fromInt(move.to));
            case Piece.RESERVED_2:
                return new ChessMove(MoveType.EN_PASSANT, ChessSquare.fromInt(move.from), ChessSquare.fromInt(move.to));
            case Piece.RESERVED_3:
                return new ChessMove(MoveType.CASTLING, ChessSquare.fromInt(move.from), ChessSquare.fromInt(move.to));
            default:
                return new ChessMove(MoveType.PROMOTION, ChessSquare.fromInt(move.from), ChessSquare.fromInt(move.to), convertPiece(move.info));
        }
    }

    public static Move convertMove(ChessMove move) {
        switch (move.type) {
            case SIMPLE:
                return new Move(ChessSquare.toInt(move.from), ChessSquare.toInt(move.to), 0, Piece.EMPTY);
            case DOUBLE:
                return new Move(ChessSquare.toInt(move.from), ChessSquare.toInt(move.to), 0, Piece.RESERVED_1);
            case EN_PASSANT:
                return new Move(ChessSquare.toInt(move.from), ChessSquare.toInt(move.to), 0, Piece.RESERVED_2);
            case CASTLING:
                return new Move(ChessSquare.toInt(move.from), ChessSquare.toInt(move.to), 0, Piece.RESERVED_3);
            case PROMOTION:
                return new Move(ChessSquare.toInt(move.from), ChessSquare.toInt(move.to), 0, convertPiece(move.promotion));
            default:
                throw new AssertionError();
        }
    }

    public static ChessPiece convertPiece(int chessPiece) {
        switch (chessPiece) {
            case Piece.W_PAWN:
                return ChessPiece.WHITE_PAWN;
            case Piece.W_KING:
                return ChessPiece.WHITE_KING;
            case Piece.W_KNIGHT:
                return ChessPiece.WHITE_KNIGHT;
            case Piece.W_BISHOP:
                return ChessPiece.WHITE_BISHOP;
            case Piece.W_ROOK:
                return ChessPiece.WHITE_ROOK;
            case Piece.W_QUEEN:
                return ChessPiece.WHITE_QUEEN;

            case Piece.B_PAWN:
                return ChessPiece.BLACK_PAWN;
            case Piece.B_KING:
                return ChessPiece.BLACK_KING;
            case Piece.B_KNIGHT:
                return ChessPiece.BLACK_KNIGHT;
            case Piece.B_BISHOP:
                return ChessPiece.BLACK_BISHOP;
            case Piece.B_ROOK:
                return ChessPiece.BLACK_ROOK;
            case Piece.B_QUEEN:
                return ChessPiece.BLACK_QUEEN;
            default:
                throw new AssertionError();
        }
    }

    public static int convertPiece(ChessPiece chessPiece) {
        switch (chessPiece) {
            case WHITE_PAWN:
                return Piece.W_PAWN;
            case WHITE_KING:
                return Piece.W_KING;
            case WHITE_KNIGHT:
                return Piece.W_KNIGHT;
            case WHITE_BISHOP:
                return Piece.W_BISHOP;
            case WHITE_ROOK:
                return Piece.W_ROOK;
            case WHITE_QUEEN:
                return Piece.W_QUEEN;

            case BLACK_PAWN:
                return Piece.B_PAWN;
            case BLACK_KING:
                return Piece.B_KING;
            case BLACK_KNIGHT:
                return Piece.B_KNIGHT;
            case BLACK_BISHOP:
                return Piece.B_BISHOP;
            case BLACK_ROOK:
                return Piece.B_ROOK;
            case BLACK_QUEEN:
                return Piece.B_QUEEN;
            default:
                throw new AssertionError();
        }
    }
}
