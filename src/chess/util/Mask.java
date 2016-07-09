package chess.util;

public class Mask {

    public static final long border = 0xff818181818181ffL;
    public static final long blackSquares = 0x5555555555555555L;
    public static final long whiteSquares = 0x9999999999999999L;
    public static final long fileA = 0x0101010101010101L;
    public static final long fileB = 0x0202020202020202L;
    public static final long fileC = 0x0404040404040404L;
    public static final long fileD = 0x0808080808080808L;
    public static final long fileE = 0x1010101010101010L;
    public static final long fileF = 0x2020202020202020L;
    public static final long fileG = 0x4040404040404040L;
    public static final long fileH = 0x8080808080808080L;
    public static final long rank1 = 0xffL;
    public static final long rank2 = 0xff00L;
    public static final long rank3 = 0xff0000L;
    public static final long rank4 = 0xff000000L;
    public static final long rank5 = 0xff00000000L;
    public static final long rank6 = 0xff0000000000L;
    public static final long rank7 = 0xff000000000000L;
    public static final long rank8 = 0xff00000000000000L;
    public static long[] mRankMask;//
    public static long[] mFileMask;//
    public static long[] mDiA1Mask;//
    public static long[] mDiA8Mask;//
    private static long[] fullDiA1Mask;//
    private static long[] fullDiA8Mask;//
    public static long[] mRankAttacks;//
    public static long[] mFileAttacks;//
    public static long[] mDiA1Attacks;//
    public static long[] mDiA8Attacks;//
    public static long[] mKnightAttacks;//
    public static long[] mKingAttacks;//
    private static final long[] pawnAttacks;//
    public static final long[] sideNeighbors;//
    private static final long[] diA1Attacks = new long[64 * 64];
    private static final long[] diA8Attacks = new long[64 * 64];
    private static final long[] rankAttacks = new long[64 * 64];
    private static final long[] fileAttacks = new long[64 * 64];

    static {
        CalculateFileMask();
        CalculateRankMask();
        CalculateDiMasks();

        calcSlides();

        CalculateKingAttacks();
        CalculateKnightAttacks();
        sideNeighbors = new long[64];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int square = Board.square(x, y);
                if (x > 0) {
                    sideNeighbors[square] |= Mask.single(Board.square(x - 1, y));
                }
                if (x < 7) {
                    sideNeighbors[square] |= Mask.single(Board.square(x + 1, y));
                }
            }
        }
        pawnAttacks = new long[128];
        for (int square = 0; square < 64; square++) {
            for (int player = 0; player < 2; player++) {
                int y = Board.y(square) + Player.sign(player);
                if ((y & 7) == y) {
                    int x = Board.x(square) - 1;
                    if ((x & 7) == x) {
                        pawnAttacks[(square << 1) | player] |= single(Board.square(x, y));
                    }
                    x += 2;
                    if ((x & 7) == x) {
                        pawnAttacks[(square << 1) | player] |= single(Board.square(x, y));
                    }
                }
            }
        }
    }

    public static void CalculateKingAttacks() {
        mKingAttacks = new long[64];
        for (int from = 0; from < 64; from++) {
            for (int to = 0; to < 64; to++) {
                int x = Board.x(from) - Board.x(to);
                int y = Board.y(from) - Board.y(to);
                if (x * x + y * y <= 2) {
                    mKingAttacks[from] |= Mask.single(to);
                }
            }
            mKingAttacks[from] ^= Mask.single(from);
        }
    }

    public static void CalculateKnightAttacks() {
        mKnightAttacks = new long[64];
        for (int from = 0; from < 64; from++) {
            for (int to = 0; to < 64; to++) {
                int x = Board.x(from) - Board.x(to);
                int y = Board.y(from) - Board.y(to);
                if (x * x + y * y == 5) {
                    mKnightAttacks[from] |= Mask.single(to);
                }
            }
        }
    }

    private static void calcSlides() {
        long[] vertical = new long[8 * 64];
        long[] horizontal = new long[8 * 64];
        for (int j = 0; j < 8; j++) {
            for (int state6Bit = 0; state6Bit < 64; state6Bit++) {
                int state8Bit = state6Bit << 1;
                long vertMask = 0;
                long horiMask = 0;
                for (int i = j - 1; i >= 0; i--) {
                    vertMask |= fileA << i;
                    horiMask |= rank8 >>> (8 * i);
                    if (((1 << i) & state8Bit) != 0) {
                        break;
                    }
                }
                for (int i = j + 1; i < 8; i++) {
                    vertMask |= fileA << i;
                    horiMask |= rank8 >>> (8 * i);
                    if (((1 << i) & state8Bit) != 0) {
                        break;
                    }
                }
                vertical[j + state6Bit * 8] = vertMask;
                horizontal[7 - j + state6Bit * 8] = horiMask;
            }
        }

        for (int square = 0; square < 64; square++) {
            int x = Board.x(square);
            int y = Board.y(square);
            for (int state6Bit = 0; state6Bit < 64; state6Bit++) {
                diA1Attacks[square + state6Bit * 64] = fullDiA1Mask[square] & vertical[x + state6Bit * 8];
                diA8Attacks[square + state6Bit * 64] = fullDiA8Mask[square] & vertical[x + state6Bit * 8];
                long fullRankMask = rank1 << (square & ~7);
                rankAttacks[square + state6Bit * 64] = fullRankMask & vertical[x + state6Bit * 8];
                long fullFileMask = fileA << x;
                fileAttacks[square + state6Bit * 64] = fullFileMask & horizontal[y + state6Bit * 8];
            }
        }
    }

    static void CalculateFileMask() {
        mFileMask = new long[64];
        for (int square = 0; square < 64; square++) {
            mFileMask[square] = 0;
            for (int y = 1; y < 7; y++) {
                mFileMask[square] |= single(Board.square(Board.x(square), y));
            }
        }
    }

    static void CalculateRankMask() {
        mRankMask = new long[64];
        for (int square = 0; square < 64; square++) {
            mRankMask[square] = 0;
            int y = Board.y(square);
            for (int x = 1; x < 7; x++) {
                mRankMask[square] |= single(Board.square(x, y));
            }
        }
    }

    static void CalculateDiMasks() {
        fullDiA1Mask = new long[64];
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                int diA1 = file - rank;
                if (diA1 > -1) {
                    for (int square = 0; square < 8 - diA1; square++) {
                        fullDiA1Mask[Board.square(file, rank)] |= single(Board.square(diA1 + square, square));
                    }
                } else {
                    for (int square = 0; square < 8 + diA1; square++) {
                        fullDiA1Mask[Board.square(file, rank)] |= single(Board.square(square, square - diA1));
                    }
                }
            }
        }
        mDiA1Mask = new long[64];
        for (int i = 0; i < 64; i++) {
            mDiA1Mask[i] = fullDiA1Mask[i] & ~border;
        }

        fullDiA8Mask = new long[64];
        MirrorHelper2(fullDiA1Mask, fullDiA8Mask);
        mDiA8Mask = new long[64];
        MirrorHelper2(mDiA1Mask, mDiA8Mask);
    }

    static void MirrorHelper(long[] masks) {
        for (int i = 0; i < 64; i++) {
            int mirror = Board.mirror(i);
            masks[1 + 2 * i] = MirrorHelperHelper(masks[2 * mirror]);
        }
    }

    static void MirrorHelper2(long[] from, long[] to) {
        for (int i = 0; i < 64; i++) {
            int mirror = Board.mirror(i);
            to[i] = MirrorHelperHelper(from[mirror]);
        }
    }

    static long MirrorHelperHelper(long mask) {
        long result = 0L;
        for (int i = 0; i < 64; i++) {
            if ((mask & single(i)) != 0) {
                result |= single(Board.mirror(i));
            }
        }
        return result;
    }

    public static long single(int pos) {
        assert (pos & 0x3f) == pos : pos;
        return 1L << pos;
    }

    public static int first(long mask) {
        return 63 - Long.numberOfLeadingZeros(mask);
    }

    public static int last(long mask) {
        return Long.numberOfTrailingZeros(mask);
    }

    public static int count(long mask) {
        return Long.bitCount(mask);
    }

    public static int fileState(long mask, int square) {
        return (int) ((((mask >>> Board.x(square)) & fileA) * 0x80402010080400L) >>> 58);
    }

    public static long FileMovement(long mask, int square) {
        return fileAttacks[square + 64 * fileState(mask, square)];
    }

    public static int rankState(long mask, int square) {
        return (int) (((mask & mRankMask[square]) * fileB) >>> 58);
    }

    public static long RankMovement(long mask, int square) {
        return rankAttacks[square + 64 * rankState(mask, square)];
    }

    public static int diA1State(long mask, int square) {
        return (int) (((mask & mDiA1Mask[square]) * fileB) >>> 58);
    }

    public static long DiA1Movement(long mask, int square) {
        return diA1Attacks[square + 64 * diA1State(mask, square)];
    }

    public static int diA8State(long mask, int square) {
        return (int) (((mask & mDiA8Mask[square]) * fileB) >>> 58);
    }

    public static long DiA8Movement(long mask, int square) {
        return diA8Attacks[square + 64 * diA8State(mask, square)];
    }

    public static long BishopMovement(long mask, int square) {
        return DiA1Movement(mask, square) | DiA8Movement(mask, square);
    }

    public static long RookMovement(long mask, int square) {
        return FileMovement(mask, square) | RankMovement(mask, square);
    }

    public static long QueenMovement(long mask, int square) {
        return RookMovement(mask, square) | BishopMovement(mask, square);
    }
    
    public static long pawnThreat(int square, int player) {
        return pawnAttacks[(square << 1) | player];
    }

    public static long whitePawn7Attacks(long pawns) {
        return (pawns & ~fileA) << 7;
    }

    public static long whitePawn9Attacks(long pawns) {
        return (pawns & ~fileH) << 9;
    }

    public static long blackPawn7Attacks(long pawns) {
        return (pawns & ~fileH) >>> 7;
    }

    public static long blackPawn9Attacks(long pawns) {
        return (pawns & ~fileA) >>> 9;
    }
}
