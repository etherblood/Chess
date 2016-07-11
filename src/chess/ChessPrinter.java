package chess;

import chess.moves.Move;
import chess.util.Board;
import chess.util.Castling;
import chess.util.Mask;
import chess.util.Piece;
import chess.util.Player;
import java.util.Arrays;

/**
 *
 * @author Philipp
 */
public class ChessPrinter {

    private static final char[] CHARS = new char[16];

    static {
        CHARS[Piece.EMPTY] = ' ';

        CHARS[Piece.W_PAWN] = 'P';
        CHARS[Piece.W_KING] = 'K';
        CHARS[Piece.W_BISHOP] = 'B';
        CHARS[Piece.W_KNIGHT] = 'N';
        CHARS[Piece.W_ROOK] = 'R';
        CHARS[Piece.W_QUEEN] = 'Q';

        CHARS[Piece.B_PAWN] = 'p';
        CHARS[Piece.B_KING] = 'k';
        CHARS[Piece.B_BISHOP] = 'b';
        CHARS[Piece.B_KNIGHT] = 'n';
        CHARS[Piece.B_ROOK] = 'r';
        CHARS[Piece.B_QUEEN] = 'q';
    }

    public void print(int[] pieces) {
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                System.out.print("[" + CHARS[pieces[Board.square(x, y)]] + ']');
            }
            System.out.println();
        }
    }

    public void print(long mask) {
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                long single = Mask.single(Board.square(x, y));
                System.out.print("[" + ((mask & single) != 0 ? 'X' : ' ') + ']');
            }
            System.out.println();
        }
    }

    public void printMove(int[] pieces, Move move) {
        System.out.println(fromMove(move, pieces));
    }

    private String fromMove(Move move, int[] pieces) {
        return fromSquare(move.from) + " (" + CHARS[pieces[move.from]] + ") => " + fromSquare(move.to) + " (" + CHARS[pieces[move.to]] + ")";
    }

    public void printMoves(int[] pieces, Move[] buffer, int from, int to) {
        System.out.println(to - from + " moves:");
        System.out.println(Arrays.toString(Arrays.asList(buffer).subList(from, to).toArray()));
    }

    public static String fromSquare(int square) {
        return (char) ('a' + Board.x(square)) + "" + (Board.y(square) + 1);
    }

    public static String toFen(ChessState state) {
        StringBuilder builder = new StringBuilder();
        for (int y = 7; y >= 0; y--) {
            int empty = 0;
            for (int x = 0; x < 8; x++) {
                int piece = state.pieces[Board.square(x, y)];
                if (piece == Piece.EMPTY) {
                    empty++;
                } else {
                    if (empty != 0) {
                        builder.append(empty);
                        empty = 0;
                    }
                    builder.append(CHARS[piece]);
                }
            }
            if (empty != 0) {
                builder.append(empty);
            }
            if(y != 0) {
                builder.append('/');
            }
        }
        builder.append(' ');
        if(Player.isWhite(state.currentPlayer())) {
            builder.append('w');
        } else {
            builder.append('b');
        }
        builder.append(' ');
        byte castling = state.currentHistory().castling;
        if(castling == 0) {
            builder.append('-');
        } else {
            if((castling & Castling.CASTLE_H1) != 0) {
                builder.append('K');
            }
            if((castling & Castling.CASTLE_A1) != 0) {
                builder.append('Q');
            }
            if((castling & Castling.CASTLE_H8) != 0) {
                builder.append('K');
            }
            if((castling & Castling.CASTLE_A8) != 0) {
                builder.append('Q');
            }
        }
        builder.append(' ');
        byte enPassant = state.currentHistory().enPassant;
        if(enPassant != 0) {
            builder.append(fromSquare(enPassant));
        } else {
            builder.append('-');
        }
        builder.append(' ');
        builder.append(state.currentHistory().fiftyRule);
        builder.append(' ');
        builder.append(state.moveCounter / 2 + 1);
        return builder.toString();
    }
}
