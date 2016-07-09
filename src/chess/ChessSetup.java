package chess;

import chess.util.Board;
import chess.util.Castling;
import chess.util.Hash;
import chess.util.Mask;
import chess.util.Piece;
import chess.util.Player;
import java.util.Arrays;

/**
 *
 * @author Philipp
 */
public class ChessSetup {

    public void reset(ChessState state) {
        clear(state);
        fromFen(state, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
//        setCastling(state, Castling.CASTLE_A1 | Castling.CASTLE_H1 | Castling.CASTLE_A8 | Castling.CASTLE_H8);
//
//        for (int x = 0; x < 8; x++) {
//            flipPieceMirrored(state, Board.A2 + x, Piece.W_PAWN);
//        }
//        flipPieceMirrored(state, Board.A1, Piece.W_ROOK);
//        flipPieceMirrored(state, Board.B1, Piece.W_KNIGHT);
//        flipPieceMirrored(state, Board.C1, Piece.W_BISHOP);
//        flipPieceMirrored(state, Board.D1, Piece.W_QUEEN);
//        flipPieceMirrored(state, Board.E1, Piece.W_KING);
//        flipPieceMirrored(state, Board.F1, Piece.W_BISHOP);
//        flipPieceMirrored(state, Board.G1, Piece.W_KNIGHT);
//        flipPieceMirrored(state, Board.H1, Piece.W_ROOK);
    }

    public void clear(ChessState state) {
        state.moveCounter = 0;
        Arrays.fill(state.pieceMasks, 0);
        Arrays.fill(state.playerMasks, 0);
        Arrays.fill(state.pieces, Piece.EMPTY);
        
        HistoryState history = state.currentHistory();
        history.castling = 0;
        history.enPassant = 0;
        history.fiftyRule = 0;
        history.lastMove = 0;
        history.hash = 0;
    }
    
    public void mirrorState(ChessState state) {
        int[] pieces = new int[64];
        System.arraycopy(state.pieces, 0, pieces, 0, 64);
        for (int square = 0; square < 64; square++) {
            int piece = pieces[square];
            if(piece != Piece.EMPTY) {
                flipPiece(state, square, piece);
                flipPiece(state, Board.mirror(square), Piece.asOpponentPiece(piece));
            }
        }
        setPlayer(state, state.opponentPlayer());
        HistoryState history = state.currentHistory();
        if(history.enPassant != 0) {
            setEnPassant(state, Board.mirror(history.enPassant));
        }
        setCastling(state, Castling.flipCastlingSides(history.castling));
        assert new ChessStateValidator().validate(state);
    }

    public void flipPiece(ChessState state, int square, int piece) {
        state.pieces[square] ^= piece;
        state.pieceMasks[piece] ^= Mask.single(square);
        state.playerMasks[Piece.owner(piece)] ^= Mask.single(square);
        state.currentHistory().hash ^= Hash.pieceHash(piece, square);
    }

    public void setCastling(ChessState state, int castleIndex) {
        HistoryState history = state.currentHistory();
        history.hash ^= Hash.castleHash(history.castling);
        history.castling = (byte) castleIndex;
        history.hash ^= Hash.castleHash(history.castling);
    }

    public void setEnPassant(ChessState state, int enPassantSquare) {
        HistoryState history = state.currentHistory();
        history.hash ^= Hash.enPassantHash(history.enPassant);
        history.enPassant = (byte) enPassantSquare;
        history.hash ^= Hash.enPassantHash(history.enPassant);
    }
    
    public void setPlayer(ChessState state, int player) {
        int currentPlayer = state.moveCounter;
        HistoryState history = state.history[currentPlayer];
        state.history[currentPlayer] = state.history[player];
        state.history[player] = history;
        history.hash ^= Hash.blackToMoveHash() * ((player ^ currentPlayer) & 1);
        state.moveCounter = player;
    }

    public void fromFen(ChessState state, String fen) {
        clear(state);
        
        String[] parts = fen.split(" ");
        fenPieces(state, parts[0].split("/"));
        fenPlayer(state, parts[1]);
        fenCastling(state, parts[2]);
        fenPassant(state, parts[3]);
        if(parts.length >= 5) {
            state.currentHistory().fiftyRule = Byte.parseByte(parts[4]);
        }
        assert new ChessStateValidator().validate(state);
    }
    
    private void fenPassant(ChessState state, String castleString) {
        if(castleString.length() == 2) {
            setEnPassant(state, Board.square(castleString.charAt(0) - 'a', castleString.charAt(1) - '1'));
        }
    }

    private void fenCastling(ChessState state, String castleString) {
        int castleIndex = 0;
        if(castleString.contains("k")) {
            castleIndex |= Castling.CASTLE_H8;
        }
        if(castleString.contains("K")) {
            castleIndex |= Castling.CASTLE_H1;
        }
        if(castleString.contains("q")) {
            castleIndex |= Castling.CASTLE_A8;
        }
        if(castleString.contains("Q")) {
            castleIndex |= Castling.CASTLE_A1;
        }
        setCastling(state, castleIndex);
    }

    private void fenPlayer(ChessState state, String part) {
        if(part.equals("b")) {
            setPlayer(state, Player.BLACK);
        } else {
            setPlayer(state, Player.WHITE);
        }
    }

    private void fenPieces(ChessState state, String[] rows) {
        for (int y = 0; y < 8; y++) {
            String row = rows[7 - y];
            int x = 0;
            for (int i = 0; i < row.length(); i++) {
                char c = row.charAt(i);
                int square = Board.square(x, y);
                switch (c) {
                    case 'p':
                        flipPiece(state, square, Piece.B_PAWN);
                        break;
                    case 'k':
                        flipPiece(state, square, Piece.B_KING);
                        break;
                    case 'b':
                        flipPiece(state, square, Piece.B_BISHOP);
                        break;
                    case 'n':
                        flipPiece(state, square, Piece.B_KNIGHT);
                        break;
                    case 'r':
                        flipPiece(state, square, Piece.B_ROOK);
                        break;
                    case 'q':
                        flipPiece(state, square, Piece.B_QUEEN);
                        break;
                    case 'P':
                        flipPiece(state, square, Piece.W_PAWN);
                        break;
                    case 'K':
                        flipPiece(state, square, Piece.W_KING);
                        break;
                    case 'B':
                        flipPiece(state, square, Piece.W_BISHOP);
                        break;
                    case 'N':
                        flipPiece(state, square, Piece.W_KNIGHT);
                        break;
                    case 'R':
                        flipPiece(state, square, Piece.W_ROOK);
                        break;
                    case 'Q':
                        flipPiece(state, square, Piece.W_QUEEN);
                        break;
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                        x += c - '1';
                        break;
                }
                x++;
            }
        }
    }

}
