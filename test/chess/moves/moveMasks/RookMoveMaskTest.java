/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.moves.moveMasks;

import chess.moves.generators.moveMasks.RookMoveMask;
import chess.util.Board;
import chess.util.Mask;
import static chess.util.Mask.mRankMask;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class RookMoveMaskTest {
    
    /**
     * Test of moves method, of class RookMoveMask.
     */
    @Test
    public void test1() {
        long ownPieces = Mask.rank1 | Mask.rank2;
        
        ownPieces ^= Mask.single(Board.G1) | Mask.single(Board.H3);
        
        int square = Board.D1;
        
        assertEquals(0, new RookMoveMask().moves(square, ownPieces, ownPieces));
    }
    
}
