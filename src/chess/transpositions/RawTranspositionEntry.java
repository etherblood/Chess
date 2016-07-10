package chess.transpositions;

/**
 *
 * @author Philipp
 */
public class RawTranspositionEntry {
    //total 12 bytes
    private final int[] source;
    public int index;

    RawTranspositionEntry(int[] source) {
        this.source = source;
    }
    
    public boolean hashEquals(long hash) {//only compares topmost 48 bits because lowest 16 bits were discarded
        if((hash >>> 32) == source[index]) {
            return ((hash ^ source[index + 1]) & 0xffff0000) == 0;
        }
        return false;
    }
    
    public void hash(long hash) {//discards lowest 16 bits
        source[index] = (int) (hash >>> 32);
        source[index + 1] ^= (int) (((hash >>> 16) ^ source[index + 1]) & 0xffff0000);
    }
    
    public void move(short move) {
        source[index + 1] ^= (move ^ source[index + 1]) & 0xffff;
    }
    
    public void depth(int depth) {
        source[index + 2] ^= (depth ^ source[index + 2]) & 0xff;
    }
    
    public void type(int type) {
        source[index + 2] ^= ((type << 8) ^ source[index + 2]) & 0xff00;
    }
    
    public void score(int score) {
        source[index + 2] ^= ((score << 16) ^ source[index + 2]) & 0xffff0000;
    }
    
    public short move() {
        return (short) source[index + 1];
    }
    
    public int depth() {
        return source[index + 2] & 0xff;
    }
    
    public int type() {
        return (source[index + 2] >>> 8) & 0xff;
    }
    
    public short score() {
        return (short) (source[index + 2] >>> 16);
    }
}
