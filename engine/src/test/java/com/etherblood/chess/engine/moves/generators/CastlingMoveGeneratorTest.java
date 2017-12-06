package com.etherblood.chess.engine.moves.generators;

import com.etherblood.chess.engine.moves.generators.MoveGenerator;
import com.etherblood.chess.engine.moves.generators.implementations.CastlingMoveGenerator;
import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.ChessSetup;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.moves.handlers.MoveExecutor;
import com.etherblood.chess.engine.util.Board;
import com.etherblood.chess.engine.util.Castling;
import com.etherblood.chess.engine.util.Piece;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class CastlingMoveGeneratorTest {
    
    @Test
    public void whiteCastling() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.E1, Piece.W_KING);
        setup.flipPiece(state, Board.A1, Piece.W_ROOK);
        setup.flipPiece(state, Board.H1, Piece.W_ROOK);
        setup.setCastling(state, Castling.CASTLE_A1 | Castling.CASTLE_H1);
        CastlingMoveGenerator castleGen = new CastlingMoveGenerator();
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = castleGen.generateMoves(state, buffer, 0);
        assertEquals(2, offset);
        assertEquals(Board.C1, buffer[0].to);
        assertEquals(Board.G1, buffer[1].to);
    }
    
    @Test
    public void attackingPawn1() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.E1, Piece.W_KING);
        setup.flipPiece(state, Board.A1, Piece.W_ROOK);
        setup.flipPiece(state, Board.H1, Piece.W_ROOK);
        setup.flipPiece(state, Board.E2, Piece.B_PAWN);
        setup.setCastling(state, Castling.CASTLE_A1 | Castling.CASTLE_H1);
        CastlingMoveGenerator castleGen = new CastlingMoveGenerator();
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = castleGen.generateMoves(state, buffer, 0);
        assertEquals(0, offset);
    }
    
    @Test
    public void attackingPawn2() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.E1, Piece.W_KING);
        setup.flipPiece(state, Board.A1, Piece.W_ROOK);
        setup.flipPiece(state, Board.H1, Piece.W_ROOK);
        setup.flipPiece(state, Board.F2, Piece.B_PAWN);
        setup.setCastling(state, Castling.CASTLE_A1 | Castling.CASTLE_H1);
        CastlingMoveGenerator castleGen = new CastlingMoveGenerator();
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = castleGen.generateMoves(state, buffer, 0);
        assertEquals(0, offset);
    }
    
    @Test
    public void attackingPawn3() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.E1, Piece.W_KING);
        setup.flipPiece(state, Board.A1, Piece.W_ROOK);
        setup.flipPiece(state, Board.H1, Piece.W_ROOK);
        setup.flipPiece(state, Board.G2, Piece.B_PAWN);
        setup.setCastling(state, Castling.CASTLE_A1 | Castling.CASTLE_H1);
        CastlingMoveGenerator castleGen = new CastlingMoveGenerator();
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = castleGen.generateMoves(state, buffer, 0);
        assertEquals(1, offset);
        assertEquals(Board.C1, buffer[0].to);
    }
    
    @Test
    public void attackingBishop() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.E1, Piece.W_KING);
        setup.flipPiece(state, Board.A1, Piece.W_ROOK);
        setup.flipPiece(state, Board.H1, Piece.W_ROOK);
        setup.flipPiece(state, Board.E2, Piece.B_BISHOP);
        setup.setCastling(state, Castling.CASTLE_A1 | Castling.CASTLE_H1);
        CastlingMoveGenerator castleGen = new CastlingMoveGenerator();
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = castleGen.generateMoves(state, buffer, 0);
        assertEquals(0, offset);
    }
    
    @Test
    public void attackingQueen() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.E1, Piece.W_KING);
        setup.flipPiece(state, Board.A1, Piece.W_ROOK);
        setup.flipPiece(state, Board.H1, Piece.W_ROOK);
        setup.flipPiece(state, Board.E2, Piece.B_QUEEN);
        setup.setCastling(state, Castling.CASTLE_A1 | Castling.CASTLE_H1);
        CastlingMoveGenerator castleGen = new CastlingMoveGenerator();
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = castleGen.generateMoves(state, buffer, 0);
        assertEquals(0, offset);
    }
    
    @Test
    public void attackingKnight1() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.E1, Piece.W_KING);
        setup.flipPiece(state, Board.A1, Piece.W_ROOK);
        setup.flipPiece(state, Board.H1, Piece.W_ROOK);
        setup.flipPiece(state, Board.E2, Piece.B_KNIGHT);
        setup.setCastling(state, Castling.CASTLE_A1 | Castling.CASTLE_H1);
        CastlingMoveGenerator castleGen = new CastlingMoveGenerator();
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = castleGen.generateMoves(state, buffer, 0);
        assertEquals(0, offset);
    }
    
    @Test
    public void attackingKnight2() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.E1, Piece.W_KING);
        setup.flipPiece(state, Board.A1, Piece.W_ROOK);
        setup.flipPiece(state, Board.H1, Piece.W_ROOK);
        setup.flipPiece(state, Board.E3, Piece.B_KNIGHT);
        setup.setCastling(state, Castling.CASTLE_A1 | Castling.CASTLE_H1);
        CastlingMoveGenerator castleGen = new CastlingMoveGenerator();
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = castleGen.generateMoves(state, buffer, 0);
        assertEquals(0, offset);
    }
    
    @Test
    public void attackingKnight3() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.E1, Piece.W_KING);
        setup.flipPiece(state, Board.A1, Piece.W_ROOK);
        setup.flipPiece(state, Board.H1, Piece.W_ROOK);
        setup.flipPiece(state, Board.F3, Piece.B_KNIGHT);
        setup.setCastling(state, Castling.CASTLE_A1 | Castling.CASTLE_H1);
        CastlingMoveGenerator castleGen = new CastlingMoveGenerator();
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = castleGen.generateMoves(state, buffer, 0);
        assertEquals(0, offset);
    }
    
    @Test
    public void moveRook() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.E1, Piece.W_KING);
        setup.flipPiece(state, Board.A1, Piece.W_ROOK);
        setup.setCastling(state, Castling.CASTLE_A1);
        CastlingMoveGenerator castleGen = new CastlingMoveGenerator();
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = castleGen.generateMoves(state, buffer, 0);
        assertEquals(1, offset);
        Move move = new Move();
        move.from = Board.A1;
        move.to = Board.A2;
        new MoveExecutor().makeMove(state, move);
        offset = castleGen.generateMoves(state, buffer, 0);
        assertEquals(0, offset);
    }
    
    @Test
    public void moveKing() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.E1, Piece.W_KING);
        setup.flipPiece(state, Board.A1, Piece.W_ROOK);
        setup.flipPiece(state, Board.H1, Piece.W_ROOK);
        setup.setCastling(state, Castling.CASTLE_A1 | Castling.CASTLE_H1);
        CastlingMoveGenerator castleGen = new CastlingMoveGenerator();
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = castleGen.generateMoves(state, buffer, 0);
        assertEquals(2, offset);
        Move move = new Move();
        move.from = Board.E1;
        move.to = Board.E2;
        new MoveExecutor().makeMove(state, move);
        offset = castleGen.generateMoves(state, buffer, 0);
        assertEquals(0, offset);
    }
    
//    public void pawnThreatBorder() {
//        CastlingMoveGenerator castleGen = new CastlingMoveGenerator();
//        long blackPawns = Mask.single(Board.H3);
//        assertEquals(Mask.single(Board.G2), castleGen.pawnThreat(blackPawns, false));
//    }

}
