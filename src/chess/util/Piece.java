package chess.util;

/**
 *
 * @author Philipp
 */
public class Piece {
    public static final int EMPTY = 0;
    public static final int RESERVED_1 = 1;
    public static final int RESERVED_2 = 2;
    public static final int RESERVED_3 = 3;
    public static final int W_PAWN = 4;
    public static final int B_PAWN = 5;
    public static final int W_KING = 6;
    public static final int B_KING = 7;
    public static final int W_BISHOP = 8;
    public static final int B_BISHOP = 9;
    public static final int W_KNIGHT = 10;
    public static final int B_KNIGHT = 11;
    public static final int W_ROOK = 12;
    public static final int B_ROOK = 13;
    public static final int W_QUEEN = 14;
    public static final int B_QUEEN = 15;
    
    
    public static int asWhitePiece(int piece) {
        return piece & ~1;
    }
    public static int asBlackPiece(int piece) {
        return piece | 1;
    }
    public static int asOpponentPiece(int piece) {
        return piece ^ 1;
    }
    
    public static boolean isWhite(int piece) {
        return owner(piece) == Player.WHITE;
    }
    
    public static boolean isBlack(int piece) {
        return owner(piece) == Player.BLACK;
    }
    
    public static boolean isPawn(int piece) {
        return asWhitePiece(piece) == W_PAWN;
    }
    
    public static boolean isKing(int piece) {
        return asWhitePiece(piece) == W_KING;
    }
    
    public static boolean isRook(int piece) {
        return asWhitePiece(piece) == W_ROOK;
    }
    
    public static boolean isValidPromotion(int piece) {
        return piece >= W_BISHOP;
    }
    
    public static int owner(int piece) {
        return piece & 1;
    }
    
    public static int pawn(int owner) {
        return withOwner(W_PAWN, owner);
    }
    
    public static int king(int owner) {
        return withOwner(W_KING, owner);
    }
    
    public static int bishop(int owner) {
        return withOwner(W_BISHOP, owner);
    }
    
    public static int knight(int owner) {
        return withOwner(W_KNIGHT, owner);
    }
    
    public static int rook(int owner) {
        return withOwner(W_ROOK, owner);
    }
    
    public static int queen(int owner) {
        return withOwner(W_QUEEN, owner);
    }
    
    private static int withOwner(int whitePiece, int owner) {
        assert isWhite(whitePiece);
        return whitePiece | owner;
    }
}
