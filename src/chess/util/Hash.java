package chess.util;

import java.util.Random;

/**
 *
 * @author Philipp
 */
public class Hash {

    private static final Random RNG = new Random(0xdeadbeef);//TODO: make sure quality hashes are produced by measuring
    private static final long[] PIECE_HASHES = new long[16 * 64];
    private static final long[] BONUS_HASHES = new long[48];
    private static final long BLACKS_TURN_HASH;

    static {
        populate(PIECE_HASHES);
        populate(BONUS_HASHES);
        BONUS_HASHES[0] = 0;
        BLACKS_TURN_HASH = RNG.nextLong();
    }

    public static long pieceHash(int piece, int square) {
        return PIECE_HASHES[(piece << 6) | square];
    }

    public static long pieceHash(int piece, int from, int to) {
        piece <<= 6;
        return PIECE_HASHES[piece | from] ^ PIECE_HASHES[piece | to];
    }

    public static long enPassantHash(int position) {
        return BONUS_HASHES[position];
    }

    public static long castleHash(int castleIndex) {
        return BONUS_HASHES[castleIndex];
    }
    
    public static long blackToMoveHash() {
        return BLACKS_TURN_HASH;
    }

    private static void populate(long[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = RNG.nextLong();
        }
    }
}
