package chess.transpositions;

/**
 *
 * @author Philipp
 */
public class PerftTranspositionEntry {

    public static final int LONGS = 2;
    private final long[] source;
    private int index;

    PerftTranspositionEntry(long[] source) {
        this.source = source;
    }

    public void store(long hash, long score) {
        source[index] = hash;
        source[index + 1] = score;
    }

    public long hash() {
        return source[index];
    }

    public long score() {
        return source[index + 1];
    }

    protected void attach(int index) {
        this.index = index;
    }
}
