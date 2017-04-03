package chess.transpositions;

import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class BucketTranspositionEntryTest {

    @Test
    public void test() {
        BucketTranspositionTable table = new BucketTranspositionTable(10);
        TranspositionEntry entry = new TranspositionEntry();

        long hash = 0xdeadbeef;
        short move = (short) 0xaaaa;
        byte type = 3;
        int depth = 33;
        short score = -Short.MAX_VALUE;

        assertFalse(table.load(hash, entry));
        assertEquals(0, entry.hash);

        entry.hash = hash;
        entry.depth = depth;
        entry.score = score;
        entry.type = type;
        entry.move = move;

        table.store(entry);

        TranspositionEntry entry2 = new TranspositionEntry();

        assertTrue(table.load(hash, entry2));
        assertEquals(hash, entry2.hash);
        assertEquals(depth, entry2.depth);
        assertEquals(move, entry2.move);
        assertEquals(score, entry2.score);
        assertEquals(type, entry2.type);
    }

    @Test
    public void stresstest() {
        Random rng = new Random();
        BucketTranspositionTable table = new BucketTranspositionTable(10);

        for (int i = 0; i < 1000; i++) {
            TranspositionEntry entry = new TranspositionEntry();
            long hash = rng.nextLong();
            short move = (short) rng.nextInt();
            byte type = (byte) rng.nextInt(4);
            int depth = rng.nextInt(64);
            short score = (short) rng.nextInt();

            assertFalse(table.load(hash, entry));
            assertEquals(0, entry.hash);

            entry.hash = hash;
            entry.depth = depth;
            entry.score = score;
            entry.type = type;
            entry.move = move;

            table.store(entry);

            TranspositionEntry entry2 = new TranspositionEntry();

            assertTrue(table.load(hash, entry2));
            assertEquals(hash, entry2.hash);
            assertEquals(depth, entry2.depth);
            assertEquals(move, entry2.move);
            assertEquals(score, entry2.score);
            assertEquals(type, entry2.type);
        }
    }

}
