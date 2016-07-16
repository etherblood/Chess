package chess.transpositions;

import java.nio.ByteBuffer;

/**
 *
 * @author Philipp
 */
public class BucketTranspositionTable {
    private final static boolean DISABLED = false;
    private final static int ENTRY_BYTES = 10;
    private final static int BUCKET_ENTRIES = 3;
    private final ByteBuffer source;
    private final int mask;

    public BucketTranspositionTable(int addressBits) {
        int size = 1 << addressBits;
        mask = size - 1;
        source = ByteBuffer.allocateDirect(ENTRY_BYTES * BUCKET_ENTRIES * size);
    }
    
    public boolean load(long hash, BucketTranspositionEntry entry) {
        if(DISABLED) {
            return false;
        }
        int bucket = bucketIndex(hash);
        for (int i = 0; i < BUCKET_ENTRIES; i++) {
            long readFirst = source.getLong(bucket + i * ENTRY_BYTES) ^ hash;
            if((readFirst & ~0xffffffL) == 0) {
                entry.hash = hash;
                entry.move = (short) readFirst;
                entry.type = (int) ((readFirst >>> 16) & 3);
                entry.depth = (int) ((readFirst >>> 18) & 0x3f);
                entry.score = source.getShort(bucket + i * ENTRY_BYTES + Long.BYTES);
                return true;
            }
        }
        return false;
    }
    
    public void store(BucketTranspositionEntry entry) {
        if(DISABLED) {
            return;
        }
        long hash = entry.hash;
        int bucket = bucketIndex(hash);
        int best = Integer.MAX_VALUE;
        int bestIndex = -1;
        for (int i = 0; i < BUCKET_ENTRIES; i++) {
            long readFirst = source.getLong(bucket + i * ENTRY_BYTES) ^ hash;
            if((readFirst & ~0xffffffL) == 0) {
                store(bucket + i * ENTRY_BYTES, entry);
                return;
            } else {
                int replacementScore = (int) ((readFirst >>> 16) & 0xff);
                if(replacementScore < best) {
                    best = replacementScore;
                    bestIndex = i;
                }
            }
        }
        store(bucket + bestIndex * ENTRY_BYTES, entry);
    }
    
    private void store(int index, BucketTranspositionEntry entry) {
        assert (entry.type & 3) == entry.type;
        assert (entry.depth & 0x3f) == entry.depth;
        long hash = entry.hash;
        hash ^= entry.move & 0xffff;
        hash ^= entry.type << 16;
        hash ^= entry.depth << 18;
        source.putLong(index, hash);
        source.putShort(index + Long.BYTES, entry.score);
    }
    
    private int bucketIndex(long hash) {
        return (int) ((hash & mask) * ENTRY_BYTES * BUCKET_ENTRIES);
    }
    
    public void attach(long hash, RawTranspositionEntry entry) {
        entry.attach((int) ((hash & mask) * RawTranspositionEntry.BYTES));
    }
    
    public RawTranspositionEntry createEntry() {
        return new RawTranspositionEntry(source);
    }
}
