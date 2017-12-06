package com.etherblood.chess.engine;

import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.ChessSetup;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.moves.generators.MoveGenerator;
import com.etherblood.chess.engine.util.Board;
import com.etherblood.chess.engine.util.Piece;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;

/**
 *
 * @author Philipp
 */
public class Testpositions {
    
    @Test
    public void pawnBorders() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.A5, Piece.W_PAWN);
        setup.flipPiece(state, Board.H6, Piece.B_PAWN);
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = new MoveGenerator().generateMoves(state, buffer, 0);
        assertEquals(1, offset);
        state.moveCounter++;
        offset = new MoveGenerator().generateMoves(state, buffer, 0);
        assertEquals(1, offset);
    }
    
    @Test
    public void pawnBorders2() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.H3, Piece.W_PAWN);
        setup.flipPiece(state, Board.A5, Piece.B_PAWN);
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = new MoveGenerator().generateMoves(state, buffer, 0);
        assertEquals(1, offset);
        state.moveCounter++;
        offset = new MoveGenerator().generateMoves(state, buffer, 0);
        assertEquals(1, offset);
    }
    
    @Test
    public void queenSkip() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.reset(state);
        setup.flipPiece(state, Board.G1, Piece.W_KNIGHT);
        setup.flipPiece(state, Board.H3, Piece.W_KNIGHT);
        setup.flipPiece(state, Board.G8, Piece.B_KNIGHT);
        setup.flipPiece(state, Board.H6, Piece.B_KNIGHT);
        Move[] buffer = MoveGenerator.createBuffer(100);
        int offset = new MoveGenerator().generateMoves(state, buffer, 0);
        assertEquals(20, offset);
        state.moveCounter++;
        offset = new MoveGenerator().generateMoves(state, buffer, 0);
        assertEquals(20, offset);
    }
    
}
