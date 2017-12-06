package com.etherblood.chess.engine.moves.handlers;

import com.etherblood.chess.engine.moves.handlers.MoveExecutor;
import com.etherblood.chess.engine.ChessSetup;
import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.util.Board;
import com.etherblood.chess.engine.util.Castling;
import com.etherblood.chess.engine.util.Piece;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class MoveExecutorTest {
    
    @Test
    public void kingMoveWithoutCastleRightsFiftyRuleNotReset() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.E1, Piece.W_KING);
        setup.setCastling(state, 0);
        setup.setFiftyRule(state, (byte)20);
        Move move = new Move();
        move.from = Board.E1;
        move.to = Board.E2;
        new MoveExecutor().makeMove(state, move);
        assertEquals(21, state.currentHistory().fiftyRule);
    }
    
    @Test
    public void kingMoveWithCastleRightsFiftyRuleReset() {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.flipPiece(state, Board.E1, Piece.W_KING);
        setup.setCastling(state, Castling.CASTLE_A1);
        setup.setFiftyRule(state, (byte)20);
        Move move = new Move();
        move.from = Board.E1;
        move.to = Board.E2;
        new MoveExecutor().makeMove(state, move);
        assertEquals(0, state.currentHistory().fiftyRule);
    }
    
}
