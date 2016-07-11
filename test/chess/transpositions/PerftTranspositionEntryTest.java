package chess.transpositions;

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
        int depth = 33;
        long score = 345678690685L;
        
        entry.attach(0);
        entry.store(hash, depth, score);
        assertEquals(entry.depth(hash), depth);
        assertEquals(score, entry.score());
    }
    
}
