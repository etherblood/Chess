package chess.transpositions;

/**
 *
 * @author Philipp
 */
public interface TranspositionTable {
    boolean load(long hash, TranspositionEntry entry);
    void store(TranspositionEntry entry);
}
