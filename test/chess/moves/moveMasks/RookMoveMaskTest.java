package chess.moves.moveMasks;

import chess.moves.generators.implementations.defaults.moveMasks.RookMoveMask;
import chess.util.Board;
import chess.util.Mask;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class RookMoveMaskTest {
    
    @Test
    public void test1() {
        long ownPieces = Mask.rank1 | Mask.rank2;
        
        ownPieces ^= Mask.single(Board.G1) | Mask.single(Board.H3);
        
        int square = Board.D1;
        
        assertEquals(0, new RookMoveMask().moves(square, ownPieces, ~ownPieces));
    }
    
}
