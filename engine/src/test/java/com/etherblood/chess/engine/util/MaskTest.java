package com.etherblood.chess.engine.util;

import com.etherblood.chess.engine.util.Mask;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class MaskTest {
    
    @Test
    public void squareColors() {
        assertEquals(Mask.BLACK_SQUARES, ~Mask.WHITE_SQUARES);
        long oddFiles = Mask.FILE_A | Mask.FILE_C | Mask.FILE_E | Mask.FILE_G;
        long evenRanks = Mask.RANK_2 | Mask.RANK_4 | Mask.RANK_6 | Mask.RANK_8;
        System.out.println(Long.toHexString(oddFiles ^ evenRanks));
        assertEquals(Mask.BLACK_SQUARES, oddFiles ^ evenRanks);
    }
    
}
