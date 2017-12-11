package com.etherblood.chess.engine.transpositions;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Philipp
 */
public class RawTranspositionTable {
    private final ByteBuffer source;
    private final int mask;

    public RawTranspositionTable(int addressBits) {
        int size = 1 << addressBits;
        mask = size - 1;
        source = ByteBuffer.allocateDirect(RawTranspositionEntry.BYTES * size);
        source.order(ByteOrder.BIG_ENDIAN);
    }
    
    public void attach(long hash, RawTranspositionEntry entry) {
        entry.attach((int) ((hash & mask) * RawTranspositionEntry.BYTES));
    }
    
    public RawTranspositionEntry createEntry() {
        return new RawTranspositionEntry(source);
    }
}
