package com.etherblood.chess.engine.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class Hash {

    private static final Random RNG = new SecureRandom(new byte[]{(byte)0xde, (byte)0xad, (byte)0xbe, (byte)0xef});
    private static final long[] PIECE_HASHES = new long[16 * 64];
    private static final long[] BONUS_HASHES = new long[48];
    private static final long BLACKS_TURN_HASH;

    static {
        populate(PIECE_HASHES);
        populate(BONUS_HASHES);
        BONUS_HASHES[0] = 0;
        BLACKS_TURN_HASH = RNG.nextLong();
        for (int i = 0; i < PIECE_HASHES.length - 1; i++) {
            assert PIECE_HASHES[i] != 0;
            for (int j = i + 1; j < PIECE_HASHES.length; j++) {
                assert PIECE_HASHES[i] != PIECE_HASHES[j];
            }
        }
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
