package com.etherblood.chess.engine.util;

public class Mask {

    public static final long BORDER_SQUARES = 0xff818181818181ffL;
    public static final long BLACK_SQUARES = 0xAA55AA55AA55AA55L;
    public static final long WHITE_SQUARES = 0x55AA55AA55AA55AAL;
    public static final long FILE_A = 0x0101010101010101L;
    public static final long FILE_B = 0x0202020202020202L;
    public static final long FILE_C = 0x0404040404040404L;
    public static final long FILE_D = 0x0808080808080808L;
    public static final long FILE_E = 0x1010101010101010L;
    public static final long FILE_F = 0x2020202020202020L;
    public static final long FILE_G = 0x4040404040404040L;
    public static final long FILE_H = 0x8080808080808080L;
    public static final long RANK_1 = 0xffL;
    public static final long RANK_2 = 0xff00L;
    public static final long RANK_3 = 0xff0000L;
    public static final long RANK_4 = 0xff000000L;
    public static final long RANK_5 = 0xff00000000L;
    public static final long RANK_6 = 0xff0000000000L;
    public static final long RANK_7 = 0xff000000000000L;
    public static final long RANK_8 = 0xff00000000000000L;
    private static final long[] RANK_MASKS = new long[64];
    private static final long[] BORDERLESS_DIAGONAL_A1_MASKS = new long[64];
    private static final long[] BORDERLESS_DIAGONAL_A8_MASKS = new long[64];
    private static final long[] KNIGHT_ATTACKS = new long[64];
    private static final long[] KING_ATTACKS = new long[64];
    private static final long[] PAWN_ATTACKS = new long[2 * 64];
    private static final long[] HORIZONTAL_NEIGHBORS = new long[64];
    private static final long[] DIAGONAL_A1_ATTACKS = new long[64 * 64];
    private static final long[] DIAGONAL_A8_ATTACKS = new long[64 * 64];
    private static final long[] RANK_ATTACKS = new long[64 * 64];
    private static final long[] FILE_ATTACKS = new long[64 * 64];

    static {
        calculateRankMask();
        calculateFullDiagonalMasks(BORDERLESS_DIAGONAL_A1_MASKS, BORDERLESS_DIAGONAL_A8_MASKS);
        calcSlides(BORDERLESS_DIAGONAL_A1_MASKS, BORDERLESS_DIAGONAL_A8_MASKS);
        for (int square = 0; square < 64; square++) {
            BORDERLESS_DIAGONAL_A1_MASKS[square] &= ~BORDER_SQUARES;
            BORDERLESS_DIAGONAL_A8_MASKS[square] &= ~BORDER_SQUARES;
        }

        calculateKingAttacks();
        calculateKnightAttacks();
        calculateHorizontalNeighbors();
        calculatePawnAttacks();
    }

    private static void calculateHorizontalNeighbors() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int square = Board.square(x, y);
                if (x > 0) {
                    HORIZONTAL_NEIGHBORS[square] |= Mask.toFlag(Board.square(x - 1, y));
                }
                if (x < 7) {
                    HORIZONTAL_NEIGHBORS[square] |= Mask.toFlag(Board.square(x + 1, y));
                }
            }
        }
    }

    private static void calculatePawnAttacks() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int square = Board.square(x, y);
                for (int player = 0; player < 2; player++) {
                    int yForward = y + Player.sign(player);
                    if((yForward & 7) == yForward) {
                        PAWN_ATTACKS[(square << 1) | player] = HORIZONTAL_NEIGHBORS[Board.square(x, yForward)];
                    }
                }
            }
        }
    }

    private static void calculateKingAttacks() {
        for (int from = 0; from < 64; from++) {
            for (int to = 0; to < 64; to++) {
                int x = Board.x(from) - Board.x(to);
                int y = Board.y(from) - Board.y(to);
                if (x * x + y * y <= 2) {
                    KING_ATTACKS[from] |= Mask.toFlag(to);
                }
            }
            KING_ATTACKS[from] ^= Mask.toFlag(from);
        }
    }

    private static void calculateKnightAttacks() {
        for (int from = 0; from < 64; from++) {
            for (int to = 0; to < 64; to++) {
                int x = Board.x(from) - Board.x(to);
                int y = Board.y(from) - Board.y(to);
                if (x * x + y * y == 5) {
                    KNIGHT_ATTACKS[from] |= Mask.toFlag(to);
                }
            }
        }
    }

    private static void calcSlides(long[] fullDiagonalA1, long[] fullDiagonalA8) {
        long[] vertical = new long[8 * 64];
        long[] horizontal = new long[8 * 64];
        for (int j = 0; j < 8; j++) {
            for (int state6Bit = 0; state6Bit < 64; state6Bit++) {
                int state8Bit = state6Bit << 1;
                long verticalMask = 0;
                long horizontalMask = 0;
                for (int i = j - 1; i >= 0; i--) {
                    verticalMask |= FILE_A << i;
                    horizontalMask |= RANK_8 >>> (8 * i);
                    if (((1 << i) & state8Bit) != 0) {
                        break;
                    }
                }
                for (int i = j + 1; i < 8; i++) {
                    verticalMask |= FILE_A << i;
                    horizontalMask |= RANK_8 >>> (8 * i);
                    if (((1 << i) & state8Bit) != 0) {
                        break;
                    }
                }
                vertical[j + state6Bit * 8] = verticalMask;
                horizontal[7 - j + state6Bit * 8] = horizontalMask;
            }
        }

        for (int square = 0; square < 64; square++) {
            int x = Board.x(square);
            int y = Board.y(square);
            for (int state6Bit = 0; state6Bit < 64; state6Bit++) {
                DIAGONAL_A1_ATTACKS[square + state6Bit * 64] = fullDiagonalA1[square] & vertical[x + state6Bit * 8];
                DIAGONAL_A8_ATTACKS[square + state6Bit * 64] = fullDiagonalA8[square] & vertical[x + state6Bit * 8];
                long fullRankMask = RANK_1 << (square & ~7);
                RANK_ATTACKS[square + state6Bit * 64] = fullRankMask & vertical[x + state6Bit * 8];
                long fullFileMask = FILE_A << x;
                FILE_ATTACKS[square + state6Bit * 64] = fullFileMask & horizontal[y + state6Bit * 8];
            }
        }
    }

    private static void calculateRankMask() {
        for (int square = 0; square < 64; square++) {
            RANK_MASKS[square] = RANK_1 << (square & ~7);
        }
    }

    private static void calculateFullDiagonalMasks(long[] fullDiagonalA1, long[] fullDiagonalA8) {
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                int diA1 = file - rank;
                if (diA1 > -1) {
                    for (int square = 0; square < 8 - diA1; square++) {
                        fullDiagonalA1[Board.square(file, rank)] |= toFlag(Board.square(diA1 + square, square));
                    }
                } else {
                    for (int square = 0; square < 8 + diA1; square++) {
                        fullDiagonalA1[Board.square(file, rank)] |= toFlag(Board.square(square, square - diA1));
                    }
                }
            }
        }
        mirrorHelper(fullDiagonalA1, fullDiagonalA8);
    }

    private static void mirrorHelper(long[] from, long[] to) {
        for (int i = 0; i < 64; i++) {
            int mirror = Board.mirror(i);
            to[i] = mirrorHelperHelper(from[mirror]);
        }
    }

    private static long mirrorHelperHelper(long mask) {
        long result = 0L;
        for (int i = 0; i < 64; i++) {
            if ((mask & toFlag(i)) != 0) {
                result |= toFlag(Board.mirror(i));
            }
        }
        return result;
    }

    public static long toFlag(int square) {
        assert (square & 0x3f) == square : square;
        return 1L << square;
    }

    public static int lowest(long mask) {
        return Long.numberOfTrailingZeros(mask);
    }

    public static int count(long mask) {
        return Long.bitCount(mask);
    }

    public static int fileState(long mask, int square) {
        return (int) ((((mask >>> Board.x(square)) & FILE_A) * 0x80402010080400L) >>> 58);
    }

    public static long fileMovement(long mask, int square) {
        return FILE_ATTACKS[square | (fileState(mask, square) << 6)];
    }

    public static int rankState(long mask, int square) {
        return (int) (((mask & RANK_MASKS[square]) * FILE_B) >>> 58);
    }

    public static long rankMovement(long mask, int square) {
        return RANK_ATTACKS[square | (rankState(mask, square) << 6)];
    }

    public static int diA1State(long mask, int square) {
        return (int) (((mask & BORDERLESS_DIAGONAL_A1_MASKS[square]) * FILE_B) >>> 58);
    }

    public static long diA1Movement(long mask, int square) {
        return DIAGONAL_A1_ATTACKS[square | (diA1State(mask, square) << 6)];
    }

    public static int diA8State(long mask, int square) {
        return (int) (((mask & BORDERLESS_DIAGONAL_A8_MASKS[square]) * FILE_B) >>> 58);
    }

    public static long diA8Movement(long mask, int square) {
        return DIAGONAL_A8_ATTACKS[square | (diA8State(mask, square) << 6)];
    }

    public static long bishopMovement(long allPieces, int square) {
        return diA1Movement(allPieces, square) | diA8Movement(allPieces, square);
    }

    public static long rookMovement(long allPieces, int square) {
        return fileMovement(allPieces, square) | rankMovement(allPieces, square);
    }

    public static long queenMovement(long allPieces, int square) {
        return rookMovement(allPieces, square) | bishopMovement(allPieces, square);
    }

    public static long kingAttacks(int square) {
        return KING_ATTACKS[square];
    }

    public static long knightAttacks(int square) {
        return KNIGHT_ATTACKS[square];
    }

    public static long horizontalNeighbors(int square) {
        return HORIZONTAL_NEIGHBORS[square];
    }
    
    public static long pawnThreat(int square, int player) {
        assert (square & 63) == square;
        return PAWN_ATTACKS[(square << 1) | player];
    }

    public static long whitePawn7Attacks(long pawns) {
        return (pawns & ~FILE_A) << 7;
    }

    public static long whitePawn9Attacks(long pawns) {
        return (pawns & ~FILE_H) << 9;
    }

    public static long blackPawn7Attacks(long pawns) {
        return (pawns & ~FILE_H) >>> 7;
    }

    public static long blackPawn9Attacks(long pawns) {
        return (pawns & ~FILE_A) >>> 9;
    }
    
    public static long fileMask(int square) {
        return FILE_A << Board.x(square);
    }
}
