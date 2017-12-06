package com.etherblood.chess.engine.util;

/**
 *
 * @author Philipp
 */
public class Player {
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    
    public static int sign(int player) {
        return 1 - (player << 1);
    }
    
    public static int opponent(int player) {
        return player ^ 1;
    }
    
    public static boolean isWhite(int player) {
        return player == 0;
    }
}
