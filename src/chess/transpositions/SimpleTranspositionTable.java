package chess.transpositions;


public class SimpleTranspositionTable  {

    private final SimpleTranspositionEntry[] map;
    private final int mask;

    public SimpleTranspositionTable(int sizeBase) {
        int size = 1 << sizeBase;
        mask = size - 1;
        map = new SimpleTranspositionEntry[size];
        for (int i = 0; i < size; i++) {
            map[i] = new SimpleTranspositionEntry();
        }
    }
    
    public SimpleTranspositionEntry get(int hash) {
        return map[hash & mask];
    }
}
