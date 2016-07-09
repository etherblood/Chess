package chess.util;

import java.util.Arrays;

/**
 *
 * @author Philipp
 */
public class Castling {

    public static final int CASTLE_A1 = 1;
    public static final int CASTLE_H1 = 2;
    public static final int CASTLE_A8 = 4;
    public static final int CASTLE_H8 = 8;

    private static final long[] CASTLE_MAPS;

    static {
        CASTLE_MAPS = new long[16];
        Arrays.fill(CASTLE_MAPS, 0);
        for (int i = 0; i < CASTLE_MAPS.length; i++) {
            if ((i & CASTLE_A1) != 0) {
                CASTLE_MAPS[i] |= Mask.single(Board.C1);
            }
            if ((i & CASTLE_A8) != 0) {
                CASTLE_MAPS[i] |= Mask.single(Board.C8);
            }
            if ((i & CASTLE_H1) != 0) {
                CASTLE_MAPS[i] |= Mask.single(Board.G1);
            }
            if ((i & CASTLE_H8) != 0) {
                CASTLE_MAPS[i] |= Mask.single(Board.G8);
            }
        }
    }

    public static long availableCastlings(int castleIndex) {
        return CASTLE_MAPS[castleIndex];
    }
    
    public static int flipCastlingSides(int castleIndex) {
        return ((castleIndex & 3) << 2) | ((castleIndex & 12) >>> 2);
    }

    public static long castlingFreeArea(int castleSquare) {
        switch (castleSquare) {
            case Board.C1:
                return Mask.single(Board.B1) | Mask.single(Board.C1) | Mask.single(Board.D1);
            case Board.G1:
                return Mask.single(Board.F1) | Mask.single(Board.G1);
            case Board.C8:
                return Mask.single(Board.B8) | Mask.single(Board.C8) | Mask.single(Board.D8);
            case Board.G8:
                return Mask.single(Board.F8) | Mask.single(Board.G8);
            default:
                throw new IllegalArgumentException("" + castleSquare);
        }
    }

    public static long castlingSafetyArea(int castleSquare) {
        switch (castleSquare) {
            case Board.C1:
                return Mask.single(Board.C1) | Mask.single(Board.D1) | Mask.single(Board.E1);
            case Board.G1:
                return Mask.single(Board.E1) | Mask.single(Board.F1) | Mask.single(Board.G1);
            case Board.C8:
                return Mask.single(Board.C8) | Mask.single(Board.D8) | Mask.single(Board.E8);
            case Board.G8:
                return Mask.single(Board.E8) | Mask.single(Board.F8) | Mask.single(Board.G8);
            default:
                throw new IllegalArgumentException("" + castleSquare);
        }
    }

}
