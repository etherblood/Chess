package chess.transpositions;

/**
 *
 * @author Philipp
 */
public class BucketTranspositionEntry {
    public long hash;
    public short move, score;
    public int depth, type;
}
