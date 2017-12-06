package com.etherblood.chess.engine;

import com.etherblood.chess.engine.util.Board;
import com.etherblood.chess.engine.util.Castling;
import com.etherblood.chess.engine.util.Hash;
import com.etherblood.chess.engine.util.Mask;
import com.etherblood.chess.engine.util.Piece;
import com.etherblood.chess.engine.util.Player;

/**
 *
 * @author Philipp
 */
public class ChessStateValidator {
    public boolean validate(ChessState state) {
        assert (state.playerMasks[Player.WHITE] & state.playerMasks[Player.BLACK]) == 0;
        for (int i = 0; i < state.pieceMasks.length - 1; i++) {
            for (int j = i + 1; j < state.pieceMasks.length; j++) {
                assert (state.pieceMasks[i] & state.pieceMasks[j]) == 0;
            }
        }
        long whitePiecesMask = 0;
        long blackPiecesMask = 0;
        for (int i = 0; i < state.pieceMasks.length; i++) {
            if(Piece.isWhite(i)) {
                whitePiecesMask |= state.pieceMasks[i];
            } else if(Piece.isBlack(i)) {
                blackPiecesMask |= state.pieceMasks[i];
            }
        }
        assert state.playerMasks[Player.WHITE] == whitePiecesMask;
        assert state.playerMasks[Player.BLACK] == blackPiecesMask;
        assert (state.pieceMasks[Piece.W_PAWN] & (Mask.RANK_1 | Mask.RANK_8)) == 0;
        assert (state.pieceMasks[Piece.B_PAWN] & (Mask.RANK_1 | Mask.RANK_8)) == 0;
        
        int castling = state.currentHistory().castling;
        if((castling & Castling.CASTLE_A1) != 0) {
            assert state.pieces[Board.E1] == Piece.W_KING;
            assert state.pieces[Board.A1] == Piece.W_ROOK;
        }
        if((castling & Castling.CASTLE_H1) != 0) {
            assert state.pieces[Board.E1] == Piece.W_KING;
            assert state.pieces[Board.H1] == Piece.W_ROOK;
        }
        if((castling & Castling.CASTLE_A8) != 0) {
            assert state.pieces[Board.E8] == Piece.B_KING;
            assert state.pieces[Board.A8] == Piece.B_ROOK;
        }
        if((castling & Castling.CASTLE_H8) != 0) {
            assert state.pieces[Board.E8] == Piece.B_KING;
            assert state.pieces[Board.H8] == Piece.B_ROOK;
        }
        
        long expectedHash = 0;
        for (int square = 0; square < 64; square++) {
            int piece = state.pieces[square];
            if(piece != Piece.EMPTY) {
                expectedHash ^= Hash.pieceHash(piece, square);
                assert (state.pieceMasks[piece] & Mask.toFlag(square)) != 0;
            } else {
                assert (state.allPieces() & Mask.toFlag(square)) == 0;
            }
        }
        expectedHash ^= Hash.castleHash(state.currentHistory().castling);
        expectedHash ^= Hash.enPassantHash(state.currentHistory().enPassant);
        expectedHash ^= state.currentPlayer() * Hash.blackToMoveHash();
        assert expectedHash == state.currentHistory().hash;
        assert state.moveCounter >= state.currentHistory().fiftyRule;
        if(state.currentHistory().enPassant != 0) {
            assert state.pieces[state.currentHistory().enPassant] == Piece.EMPTY;
        }
        return true;
    }
}
