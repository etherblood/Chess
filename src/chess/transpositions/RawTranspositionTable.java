package chess.transpositions;

/**
 *
 * @author Philipp
 */
public class RawTranspositionTable {
    private final int[] map;
    private final int mask;

    public RawTranspositionTable(int sizeBase) {
        int size = 1 << sizeBase;
        mask = size - 1;
        map = new int[3 * size];
    }
    
    public void get(long hash, RawTranspositionEntry entry) {
        entry.index = (int) ((hash & mask) * 3);
    }
    
    public RawTranspositionEntry createEntry() {
        return new RawTranspositionEntry(map);
    }
}
