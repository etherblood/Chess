package chess;

import chess.moves.Move;
import chess.moves.generators.MoveGenerator;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class PerftTest {
    
    @Test
    public void defaultPosition() {
        assertPerft("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 6, 119060324);
//        ChessState state = new ChessState();
//        new ChessSetup().reset(state);
//        Move[] buffer = MoveGenerator.createBuffer(1000);
//        Perft instance = new Perft();
//        assertEquals(20, instance.perft(state, buffer, 0, 1));
//        assertEquals(400, instance.perft(state, buffer, 0, 2));
//        assertEquals(8902, instance.perft(state, buffer, 0, 3));
//        assertEquals(197281, instance.perft(state, buffer, 0, 4));
//        assertEquals(4865609, instance.perft(state, buffer, 0, 5));
//        assertEquals(119060324, instance.perft(state, buffer, 0, 6));
    }
    
    @Test
    public void kiwipetePosition() {
        assertPerft("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -", 5, 193690690);
//        ChessState state = new ChessState();
//        new ChessSetup().fromFen(state, "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
//        Move[] buffer = MoveGenerator.createBuffer(1000);
//        Perft instance = new Perft();
//        assertEquals(48, instance.perft(state, buffer, 0, 1));
//        assertEquals(2039, instance.perft(state, buffer, 0, 2));
//        assertEquals(97862, instance.perft(state, buffer, 0, 3));
//        assertEquals(4085603, instance.perft(state, buffer, 0, 4));
//        assertEquals(193690690, instance.perft(state, buffer, 0, 5));
    }
    
    @Test
    public void position3() {
        assertPerft("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -", 7, 178633661);
//        ChessState state = new ChessState();
//        new ChessSetup().fromFen(state, "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");
//        Move[] buffer = MoveGenerator.createBuffer(1000);
//        Perft instance = new Perft();
//        assertEquals(14, instance.perft(state, buffer, 0, 1));
//        assertEquals(191, instance.perft(state, buffer, 0, 2));
//        assertEquals(2812, instance.perft(state, buffer, 0, 3));
//        assertEquals(43238, instance.perft(state, buffer, 0, 4));
//        assertEquals(674624, instance.perft(state, buffer, 0, 5));
//        assertEquals(11030083, instance.perft(state, buffer, 0, 6));
//        assertEquals(178633661, instance.perft(state, buffer, 0, 7));
    }
    
    @Test
    public void position4() {
        assertPerft("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1", 5, 15833292);
//        ChessState state = new ChessState();
//        new ChessSetup().fromFen(state, "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
//        Move[] buffer = MoveGenerator.createBuffer(1000);
//        Perft instance = new Perft();
//        assertEquals(6, instance.perft(state, buffer, 0, 1));
//        assertEquals(264, instance.perft(state, buffer, 0, 2));
//        assertEquals(9467, instance.perft(state, buffer, 0, 3));
//        assertEquals(422333, instance.perft(state, buffer, 0, 4));
//        assertEquals(15833292, instance.perft(state, buffer, 0, 5));
////        assertEquals(706045033, instance.perft(state, buffer, 0, 6));
    }
    
    @Test
    public void position5() {
        assertPerft("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8", 5, 89941194);
//        ChessState state = new ChessState();
//        new ChessSetup().fromFen(state, "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
//        Move[] buffer = MoveGenerator.createBuffer(1000);
//        Perft instance = new Perft();
//        assertEquals(44, instance.perft(state, buffer, 0, 1));
//        assertEquals(1486, instance.perft(state, buffer, 0, 2));
//        assertEquals(62379, instance.perft(state, buffer, 0, 3));
//        assertEquals(2103487, instance.perft(state, buffer, 0, 4));
//        assertEquals(89941194, instance.perft(state, buffer, 0, 5));
    }
    
    @Test
    public void position6() {
        assertPerft("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10", 5, 164075551);
//        ChessState state = new ChessState();
//        new ChessSetup().fromFen(state, "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
//        Move[] buffer = MoveGenerator.createBuffer(1000);
//        Perft instance = new Perft();
//        assertEquals(46, instance.perft(state, buffer, 0, 1));
//        assertEquals(2079, instance.perft(state, buffer, 0, 2));
//        assertEquals(89890, instance.perft(state, buffer, 0, 3));
//        assertEquals(3894594, instance.perft(state, buffer, 0, 4));
//        assertEquals(164075551, instance.perft(state, buffer, 0, 5));
    }
    
    @Test
    public void illegalEp1() {
        assertPerft("3k4/3p4/8/K1P4r/8/8/8/8 b - - 0 1", 6, 1134888);
    }
    
    @Test
    public void illegalEp2() {
        assertPerft("8/8/4k3/8/2p5/8/B2P2K1/8 w - - 0 1", 6, 1015133);
    }
    
    @Test
    public void epCheck() {
        assertPerft("8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1", 6, 1440467);
    }
    
    @Test
    public void shortCastleCheck() {
        assertPerft("5k2/8/8/8/8/8/8/4K2R w K - 0 1", 6, 661072);
    }
    
    @Test
    public void longCastleCheck() {
        assertPerft("3k4/8/8/8/8/8/8/R3K3 w Q - 0 1", 6, 803711);
    }
    
    @Test
    public void castleRights() {
        assertPerft("r3k2r/1b4bq/8/8/8/8/7B/R3K2R w KQkq - 0 1", 4, 1274206);
    }
    
    @Test
    public void castlePrevented() {
        assertPerft("r3k2r/8/3Q4/8/8/5q2/8/R3K2R b KQkq - 0 1", 4, 1720476);
    }
    
    @Test
    public void promoteOutOfCheck() {
        assertPerft("2K2r2/4P3/8/8/8/8/8/3k4 w - - 0 1", 6, 3821001);
    }
    
    @Test
    public void discoveredCheck() {
        assertPerft("8/8/1P2K3/8/2n5/1q6/8/5k2 b - - 0 1", 5, 1004658);
    }
    
    @Test
    public void promoteGiveCheck() {
        assertPerft("4k3/1P6/8/8/8/8/K7/8 w - - 0 1", 6, 217342);
    }
    
    @Test
    public void underpromoteGiveCheck() {
        assertPerft("8/P1k5/K7/8/8/8/8/8 w - - 0 1", 6, 92683);
    }
    
    @Test
    public void selfStalemate() {
        assertPerft("K1k5/8/P7/8/8/8/8/8 w - - 0 1", 6, 2217);
    }
    
    @Test
    public void stalemateAndCheckmate1() {
        assertPerft("8/k1P5/8/1K6/8/8/8/8 w - - 0 1", 7, 567584);
    }
    
    @Test
    public void stalemateAndCheckmate2() {
        assertPerft("8/8/2k5/5q2/5n2/8/5K2/8 b - - 0 1", 4, 23527);
    }
    
    @Test
    public void pawns1() {
        assertPerft("8/p7/8/1P6/K1k3p1/6P1/7P/8 w - -", 8, 8103790);
    }
    
    @Test
    public void pawns2() {
        assertPerft("8/5p2/8/2k3P1/p3K3/8/1P6/8 b - -", 8, 64451405);
    }
    
    @Test
    public void pawns3() {//sub perft of pawns 1
        assertPerft("8/p7/8/1P6/K1k5/6Pp/8/8 w - -", 1, 4);
    }
    
    @Test
    public void position7() {
        assertPerft("r3k2r/pb3p2/5npp/n2p4/1p1PPB2/6P1/P2N1PBP/R3K2R b KQkq -", 5, 26957954);
    }
    
    @Test
    public void castlings() {
        assertPerft("r3k2r/p6p/8/B7/1pp1p3/3b4/P6P/R3K2R w KQkq -", 6, 77054993 );
    }
    
    private void assertPerft(String fen, int depth, long value) {
        assert depth <= 10;
        ChessState state = new ChessState();
        ChessState other = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.fromFen(state, fen);
        setup.fromFen(other, fen);
        Move[] buffer = MoveGenerator.createBuffer(500);
        Perft instance = new Perft();
        
        long perftResult = instance.perft(state, buffer, 0, depth);
        assertStatesEqual(state, other);
        assertEquals(value, perftResult);
        
        setup.mirrorState(state);
        setup.mirrorState(other);
        
        perftResult = instance.perft(state, buffer, 0, depth);
        assertStatesEqual(state, other);
        assertEquals(value, perftResult);
    }
    
    public boolean assertStatesEqual(ChessState a, ChessState b) {
        assert Arrays.equals(a.pieces, b.pieces);
        assert Arrays.equals(a.pieceMasks, b.pieceMasks);
        assert Arrays.equals(a.playerMasks, b.playerMasks);
        assert a.moveCounter == b.moveCounter;
        HistoryState aHistory = a.currentHistory();
        HistoryState bHistory = b.currentHistory();
        assert aHistory.castling == bHistory.castling;
        assert aHistory.enPassant == bHistory.enPassant;
        assert aHistory.fiftyRule == bHistory.fiftyRule;
        assert aHistory.hash == bHistory.hash;
        assert aHistory.lastMove == bHistory.lastMove;
        return true;
    }
    
}
