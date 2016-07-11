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

    public void store(long hash, int depth, long score) {
        source[index] = hash ^ depth;
        source[index + 1] = score;
    }

    public long depth(long hash) {
        return source[index] ^ hash;
    }

    public long score() {
        return source[index + 1];
    }

    protected void attach(int index) {
        this.index = index;
    }
}
