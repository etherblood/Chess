package com.etherblood.chess.engine.transpositions;

import com.etherblood.chess.engine.transpositions.RawTranspositionEntry;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class RawTranspositionEntryTest {
    

    @Test
    public void test() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(RawTranspositionEntry.BYTES);
        buffer.order(ByteOrder.BIG_ENDIAN);
        RawTranspositionEntry entry = new RawTranspositionEntry(buffer);
        
        long hash = 0xdeadbeef;
        short move = (short) 0xaaaa;
        int type = 3;
        int depth = 33;
        int score = -Short.MAX_VALUE;
        
        entry.attach(0);
        entry.store(hash, move, depth, type, score);
        entry.refresh();
        assertTrue(entry.hashEquals(hash));
        assertEquals(depth, entry.depth());
        assertEquals(move, entry.move());
        assertEquals(score, entry.score());
        assertEquals(type, entry.type());
    }
    
}
