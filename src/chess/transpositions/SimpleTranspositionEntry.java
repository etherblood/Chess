package chess.transpositions;

/**
 *
 * @author Philipp
 */
public class SimpleTranspositionEntry {//4-8 bytes reference
    public long key;//8 bytes
    public short move, score;//4 bytes
    public byte depth, type;//2 bytes
    //total 18-22 bytes
}
