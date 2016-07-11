package chess.transpositions;

import java.util.Arrays;

/**
 *
 * @author Philipp
 */
public class PerftTranspositionTable {

    private final long[] source;
    private final int mask;

    public PerftTranspositionTable(int addressBits) {
        int size = 1 << addressBits;
        mask = size - 1;
        source = new long[PerftTranspositionEntry.LONGS * size];
    }

    public void attach(long hash, PerftTranspositionEntry entry) {
        entry.attach((int) ((hash & mask) * PerftTranspositionEntry.LONGS));
    }

    public PerftTranspositionEntry createEntry() {
        return new PerftTranspositionEntry(source);
    }
    
    public void clear() {
        Arrays.fill(source, 0);
    }
    
    public int size() {
        return source.length / PerftTranspositionEntry.LONGS;
    }
    
    public int used() {
        int sum = 0;
        for (int i = 0; i < source.length; i += PerftTranspositionEntry.LONGS) {
            if(source[i] != 0) {
                sum++;
            }
        }
        return sum;
    }
}
