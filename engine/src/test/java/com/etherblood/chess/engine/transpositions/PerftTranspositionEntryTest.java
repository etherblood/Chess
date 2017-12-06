package com.etherblood.chess.engine.transpositions;

import com.etherblood.chess.engine.transpositions.PerftTranspositionEntry;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class PerftTranspositionEntryTest {
    
    @Test
    public void test() {
        long[] buffer = new long[PerftTranspositionEntry.LONGS];
        PerftTranspositionEntry entry = new PerftTranspositionEntry(buffer);
        
        long hash = 0xdeadbeef;
        long score = 345678690685L;
        
        entry.attach(0);
        entry.store(hash, score);
        assertEquals(hash, entry.hash());
        assertEquals(score, entry.score());
    }
    
}
