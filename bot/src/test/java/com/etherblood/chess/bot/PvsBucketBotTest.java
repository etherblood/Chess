package com.etherblood.chess.bot;

import com.etherblood.chess.bot.PvsBucketBot;
import com.etherblood.chess.engine.ChessSetup;
import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.bot.PvsBucketBot;
import com.etherblood.chess.bot.evaluations.PstEvaluation;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.util.Board;
import com.etherblood.chess.engine.util.Piece;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class PvsBucketBotTest {
    
    @Test
    public void test1MateIn1() {
        assertFindsMove("2R5/3k4/5P2/4P2K/8/8/8/1B6 w - -", Board.B1, Board.F5);
    }
    
    @Test
    public void test2MateIn1() {
        assertFindsMove("8/k7/P1Q5/2P5/1P6/8/8/K7 w - -", Board.C6, Board.B7);
    }
    
    @Test
    public void test3MateIn1() {
        assertFindsMove("8/k7/P1Q5/2P5/1P6/8/8/1K6 w - -", Board.C6, Board.B7);
    }
    
    @Test
    public void testDrawIn3() {
        assertFindsMove("8/8/2K5/8/8/8/4pkN1/8 w - -", Board.G2, Board.F4);
    }
    
    @Test
    public void underpromoteBishopToEvadeThreat() {
        assertFindsMove("4N3/1P6/4P3/4bP2/8/PNk5/P1p5/K7 w - -", Board.B7, Board.B8, Piece.W_BISHOP);
    }
    
    private void assertFindsMove(String fen, int from, int to) {
        assertFindsMove(fen, from, to, 0);
    }
    private void assertFindsMove(String fen, int from, int to, int info) {
        Move move = new Move();
        move.from = from;
        move.to = to;
        move.info = info;
        assertFindsMove(fen, move);
    }
    private void assertFindsMove(String fen, Move move) {
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.fromFen(state, fen);
        PvsBucketBot instance = new PvsBucketBot(new PstEvaluation());
        instance.setState(state);
        Move result = instance.compute(8);
        assertTrue(move.identityEquals(result));
    }
    
}
