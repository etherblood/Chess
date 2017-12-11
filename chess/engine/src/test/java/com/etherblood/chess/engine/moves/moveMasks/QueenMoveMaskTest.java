/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.chess.engine.moves.moveMasks;

import com.etherblood.chess.engine.moves.generators.implementations.defaults.moveMasks.QueenMoveMask;
import com.etherblood.chess.engine.util.Board;
import com.etherblood.chess.engine.util.Mask;
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
public class QueenMoveMaskTest {
    
    /**
     * Test of moves method, of class QueenMoveMask.
     */
    @Test
    public void test1() {
        long ownPieces = Mask.RANK_1 | Mask.RANK_2;
        
        ownPieces ^= Mask.toFlag(Board.G1) | Mask.toFlag(Board.H3);
        
        int square = Board.D1;
        
        assertEquals(0, new QueenMoveMask().moves(square, ownPieces, ~ownPieces));
    }
    
}
