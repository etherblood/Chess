package chess.transpositions;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Philipp
 */
public class RawTranspositionEntry {
    public static final int BYTES = 10;
    private final ByteBuffer source;
    private long hash;
    private short score;
    private int index;

    RawTranspositionEntry(ByteBuffer source) {
        this.source = source;
        assert source.order() == ByteOrder.BIG_ENDIAN;
    }
    
    public boolean hashEquals(long hash) {
        return ((this.hash ^ hash) & ~0xffffffL) == 0;//lowest 3 bytes were overwritten, ignore them
    }
    
    public void store(long hash, short move, int depth, int type, int score) {
        assert (depth & 0x3f) == depth;
        assert (type & 0x3) == type;
        assert score == (short)score;
        source.putLong(index, hash);
        source.putShort(index + 6, move);
        source.put(index + 5, (byte) ((depth << 2) | type));
        source.putShort(index + 8, (short) score);//TODO: can we overwrite another 2 bytes of hash? this will result in more false positives but allows us to store more entries
    }
    
    public void refresh() {
        hash = source.getLong(index);
        score = source.getShort(index + 8);
    }
    
    protected void attach(int index) {
        this.index = index;
    }
    
    public int depth() {
        return ((int) hash >>> 18) & 0x3f;
    }
    
    public int type() {
        return ((int) hash >>> 16) & 3;
    }
    
    public int score() {
        return score;
    }
    
    public short move() {
        return (short) hash;
    }
    
}
