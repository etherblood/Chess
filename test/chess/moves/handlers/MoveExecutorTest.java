package chess.moves.handlers;

import chess.ChessSetup;
import chess.ChessState;
import chess.moves.Move;
import chess.util.Board;
import chess.util.Castling;
import chess.util.Piece;
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
