package chess.util;

/**
 *
 * @author Philipp
 */
public class Board {
    
    private static final int[] DISTANCE;

    public static final int A1 = 0;
    public static final int A2 = 8;
    public static final int A3 = 16;
    public static final int A4 = 24;
    public static final int A5 = 32;
    public static final int A6 = 40;
    public static final int A7 = 48;
    public static final int A8 = 56;

    public static final int B1 = 1;
    public static final int B2 = 9;
    public static final int B3 = 17;
    public static final int B4 = 25;
    public static final int B5 = 33;
    public static final int B6 = 41;
    public static final int B7 = 49;
    public static final int B8 = 57;

    public static final int C1 = 2;
    public static final int C2 = 10;
    public static final int C3 = 18;
    public static final int C4 = 26;
    public static final int C5 = 34;
    public static final int C6 = 42;
    public static final int C7 = 50;
    public static final int C8 = 58;

    public static final int D1 = 3;
    public static final int D2 = 11;
    public static final int D3 = 19;
    public static final int D4 = 27;
    public static final int D5 = 35;
    public static final int D6 = 43;
    public static final int D7 = 51;
    public static final int D8 = 59;

    public static final int E1 = 4;
    public static final int E2 = 12;
    public static final int E3 = 20;
    public static final int E4 = 28;
    public static final int E5 = 36;
    public static final int E6 = 44;
    public static final int E7 = 52;
    public static final int E8 = 60;

    public static final int F1 = 5;
    public static final int F2 = 13;
    public static final int F3 = 21;
    public static final int F4 = 29;
    public static final int F5 = 37;
    public static final int F6 = 45;
    public static final int F7 = 53;
    public static final int F8 = 61;

    public static final int G1 = 6;
    public static final int G2 = 14;
    public static final int G3 = 22;
    public static final int G4 = 30;
    public static final int G5 = 38;
    public static final int G6 = 46;
    public static final int G7 = 54;
    public static final int G8 = 62;

    public static final int H1 = 7;
    public static final int H2 = 15;
    public static final int H3 = 23;
    public static final int H4 = 31;
    public static final int H5 = 39;
    public static final int H6 = 47;
    public static final int H7 = 55;
    public static final int H8 = 63;
    
    static {
        DISTANCE = new int[64 * 64];
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                int dX = Math.abs(x(i) - x(j));
                int dY = Math.abs(y(i) - y(j));
                DISTANCE[i * 64 + j] = Math.max(dX, dY);
            }
        }
    }
    
    public static int square(int x, int y) {
        return x | (y << 3);
    }
    
    public static int x(int square) {
        return square & 7;
    }
    
    public static int y(int square) {
        return square >>> 3;
    }
    
    public static int mirror(int square) {
        return square(x(square), 7 ^ y(square));
    }
    
    public static int distance(int squareA, int squareB) {
        return DISTANCE[squareA | (squareB << 6)];
    }
}
