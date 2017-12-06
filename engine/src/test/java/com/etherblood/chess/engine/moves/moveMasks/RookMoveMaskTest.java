package com.etherblood.chess.engine.moves.moveMasks;

import com.etherblood.chess.engine.moves.generators.implementations.defaults.moveMasks.RookMoveMask;
import com.etherblood.chess.engine.util.Board;
import com.etherblood.chess.engine.util.Mask;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class RookMoveMaskTest {
    
    @Test
    public void test1() {
        long ownPieces = Mask.RANK_1 | Mask.RANK_2;
        
        ownPieces ^= Mask.toFlag(Board.G1) | Mask.toFlag(Board.H3);
        
        int square = Board.D1;
        
        assertEquals(0, new RookMoveMask().moves(square, ownPieces, ~ownPieces));
    }
    
}
