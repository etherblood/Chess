package chess.util;

/**
 *
 * @author Philipp
 */
public class Board {

    public static final int A1 = 0;
    public static final int A2 = 8;
    public static final int A3 = 16;
    public static final int A4 = 24;
    public static final int A5 = 32;
    public static final int A6 = 40;
    public static final int A7 = 48;
    public static final int A8 = 56;

    public static final int B1 = 0 + 1;
    public static final int B2 = 8 + 1;
    public static final int B3 = 16 + 1;
    public static final int B4 = 24 + 1;
    public static final int B5 = 32 + 1;
    public static final int B6 = 40 + 1;
    public static final int B7 = 48 + 1;
    public static final int B8 = 56 + 1;

    public static final int C1 = 0 + 2;
    public static final int C2 = 8 + 2;
    public static final int C3 = 16 + 2;
    public static final int C4 = 24 + 2;
    public static final int C5 = 32 + 2;
    public static final int C6 = 40 + 2;
    public static final int C7 = 48 + 2;
    public static final int C8 = 56 + 2;

    public static final int D1 = 0 + 3;
    public static final int D2 = 8 + 3;
    public static final int D3 = 16 + 3;
    public static final int D4 = 24 + 3;
    public static final int D5 = 32 + 3;
    public static final int D6 = 40 + 3;
    public static final int D7 = 48 + 3;
    public static final int D8 = 56 + 3;

    public static final int E1 = 0 + 4;
    public static final int E2 = 8 + 4;
    public static final int E3 = 16 + 4;
    public static final int E4 = 24 + 4;
    public static final int E5 = 32 + 4;
    public static final int E6 = 40 + 4;
    public static final int E7 = 48 + 4;
    public static final int E8 = 56 + 4;

    public static final int F1 = 0 + 5;
    public static final int F2 = 8 + 5;
    public static final int F3 = 16 + 5;
    public static final int F4 = 24 + 5;
    public static final int F5 = 32 + 5;
    public static final int F6 = 40 + 5;
    public static final int F7 = 48 + 5;
    public static final int F8 = 56 + 5;

    public static final int G1 = 0 + 6;
    public static final int G2 = 8 + 6;
    public static final int G3 = 16 + 6;
    public static final int G4 = 24 + 6;
    public static final int G5 = 32 + 6;
    public static final int G6 = 40 + 6;
    public static final int G7 = 48 + 6;
    public static final int G8 = 56 + 6;

    public static final int H1 = 0 + 7;
    public static final int H2 = 8 + 7;
    public static final int H3 = 16 + 7;
    public static final int H4 = 24 + 7;
    public static final int H5 = 32 + 7;
    public static final int H6 = 40 + 7;
    public static final int H7 = 48 + 7;
    public static final int H8 = 56 + 7;
    
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
        return square(x(square), 7 - y(square));
    }
}
