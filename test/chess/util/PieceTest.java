package chess.util;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;

/**
 *
 * @author Philipp
 */
public class PieceTest {

    /**
     * Test of asWhitePiece method, of class Piece.
     */
    @Test
    public void asWhitePiece() {
        assertEquals(Piece.W_PAWN, Piece.asWhitePiece(Piece.W_PAWN));
        assertEquals(Piece.W_KING, Piece.asWhitePiece(Piece.W_KING));
        assertEquals(Piece.W_BISHOP, Piece.asWhitePiece(Piece.W_BISHOP));
        assertEquals(Piece.W_KNIGHT, Piece.asWhitePiece(Piece.W_KNIGHT));
        assertEquals(Piece.W_ROOK, Piece.asWhitePiece(Piece.W_ROOK));
        assertEquals(Piece.W_QUEEN, Piece.asWhitePiece(Piece.W_QUEEN));
        
        assertEquals(Piece.W_PAWN, Piece.asWhitePiece(Piece.B_PAWN));
        assertEquals(Piece.W_KING, Piece.asWhitePiece(Piece.B_KING));
        assertEquals(Piece.W_BISHOP, Piece.asWhitePiece(Piece.B_BISHOP));
        assertEquals(Piece.W_KNIGHT, Piece.asWhitePiece(Piece.B_KNIGHT));
        assertEquals(Piece.W_ROOK, Piece.asWhitePiece(Piece.B_ROOK));
        assertEquals(Piece.W_QUEEN, Piece.asWhitePiece(Piece.B_QUEEN));
    }
    
    /**
     * Test of asBlackPiece method, of class Piece.
     */
    @Test
    public void asBlackPiece() {
        assertEquals(Piece.B_PAWN, Piece.asBlackPiece(Piece.W_PAWN));
        assertEquals(Piece.B_KING, Piece.asBlackPiece(Piece.W_KING));
        assertEquals(Piece.B_BISHOP, Piece.asBlackPiece(Piece.W_BISHOP));
        assertEquals(Piece.B_KNIGHT, Piece.asBlackPiece(Piece.W_KNIGHT));
        assertEquals(Piece.B_ROOK, Piece.asBlackPiece(Piece.W_ROOK));
        assertEquals(Piece.B_QUEEN, Piece.asBlackPiece(Piece.W_QUEEN));
        
        assertEquals(Piece.B_PAWN, Piece.asBlackPiece(Piece.B_PAWN));
        assertEquals(Piece.B_KING, Piece.asBlackPiece(Piece.B_KING));
        assertEquals(Piece.B_BISHOP, Piece.asBlackPiece(Piece.B_BISHOP));
        assertEquals(Piece.B_KNIGHT, Piece.asBlackPiece(Piece.B_KNIGHT));
        assertEquals(Piece.B_ROOK, Piece.asBlackPiece(Piece.B_ROOK));
        assertEquals(Piece.B_QUEEN, Piece.asBlackPiece(Piece.B_QUEEN));
    }

    /**
     * Test of isPawn method, of class Piece.
     */
    @Test
    public void isPawn() {
        assertTrue(Piece.isPawn(Piece.W_PAWN));
        assertFalse(Piece.isPawn(Piece.W_KING));
        assertFalse(Piece.isPawn(Piece.W_BISHOP));
        assertFalse(Piece.isPawn(Piece.W_KNIGHT));
        assertFalse(Piece.isPawn(Piece.W_ROOK));
        assertFalse(Piece.isPawn(Piece.W_QUEEN));
        
        assertTrue(Piece.isPawn(Piece.B_PAWN));
        assertFalse(Piece.isPawn(Piece.B_KING));
        assertFalse(Piece.isPawn(Piece.B_BISHOP));
        assertFalse(Piece.isPawn(Piece.B_KNIGHT));
        assertFalse(Piece.isPawn(Piece.B_ROOK));
        assertFalse(Piece.isPawn(Piece.B_QUEEN));
    }
    
    @Test
    public void isRook() {
        assertFalse(Piece.isRook(Piece.W_PAWN));
        assertFalse(Piece.isRook(Piece.W_KING));
        assertFalse(Piece.isRook(Piece.W_BISHOP));
        assertFalse(Piece.isRook(Piece.W_KNIGHT));
        assertTrue(Piece.isRook(Piece.W_ROOK));
        assertFalse(Piece.isRook(Piece.W_QUEEN));
        
        assertFalse(Piece.isRook(Piece.B_PAWN));
        assertFalse(Piece.isRook(Piece.B_KING));
        assertFalse(Piece.isRook(Piece.B_BISHOP));
        assertFalse(Piece.isRook(Piece.B_KNIGHT));
        assertTrue(Piece.isRook(Piece.B_ROOK));
        assertFalse(Piece.isRook(Piece.B_QUEEN));
    }

    /**
     * Test of owner method, of class Piece.
     */
    @Test
    public void owner() {
        assertEquals(Player.WHITE, Piece.owner(Piece.W_PAWN));
        assertEquals(Player.WHITE, Piece.owner(Piece.W_KING));
        assertEquals(Player.WHITE, Piece.owner(Piece.W_BISHOP));
        assertEquals(Player.WHITE, Piece.owner(Piece.W_KNIGHT));
        assertEquals(Player.WHITE, Piece.owner(Piece.W_ROOK));
        assertEquals(Player.WHITE, Piece.owner(Piece.W_QUEEN));
        
        assertEquals(Player.BLACK, Piece.owner(Piece.B_PAWN));
        assertEquals(Player.BLACK, Piece.owner(Piece.B_KING));
        assertEquals(Player.BLACK, Piece.owner(Piece.B_BISHOP));
        assertEquals(Player.BLACK, Piece.owner(Piece.B_KNIGHT));
        assertEquals(Player.BLACK, Piece.owner(Piece.B_ROOK));
        assertEquals(Player.BLACK, Piece.owner(Piece.B_QUEEN));
    }

    /**
     * Test of withOwner method, of class Piece.
     */
    @Test
    public void withOwner() {
        assertEquals(Piece.W_PAWN, Piece.withOwner(Piece.W_PAWN, Player.WHITE));
        assertEquals(Piece.W_KING, Piece.withOwner(Piece.W_KING, Player.WHITE));
        assertEquals(Piece.W_BISHOP, Piece.withOwner(Piece.W_BISHOP, Player.WHITE));
        assertEquals(Piece.W_KNIGHT, Piece.withOwner(Piece.W_KNIGHT, Player.WHITE));
        assertEquals(Piece.W_ROOK, Piece.withOwner(Piece.W_ROOK, Player.WHITE));
        assertEquals(Piece.W_QUEEN, Piece.withOwner(Piece.W_QUEEN, Player.WHITE));
        
        assertEquals(Piece.B_PAWN, Piece.withOwner(Piece.W_PAWN, Player.BLACK));
        assertEquals(Piece.B_KING, Piece.withOwner(Piece.W_KING, Player.BLACK));
        assertEquals(Piece.B_BISHOP, Piece.withOwner(Piece.W_BISHOP, Player.BLACK));
        assertEquals(Piece.B_KNIGHT, Piece.withOwner(Piece.W_KNIGHT, Player.BLACK));
        assertEquals(Piece.B_ROOK, Piece.withOwner(Piece.W_ROOK, Player.BLACK));
        assertEquals(Piece.B_QUEEN, Piece.withOwner(Piece.W_QUEEN, Player.BLACK));
    }

    /**
     * Test of pawn method, of class Piece.
     */
    @Test
    public void pawn() {
        assertEquals(Piece.W_PAWN, Piece.pawn(Player.WHITE));
        assertEquals(Piece.B_PAWN, Piece.pawn(Player.BLACK));
    }

    /**
     * Test of king method, of class Piece.
     */
    @Test
    public void king() {
        assertEquals(Piece.W_KING, Piece.king(Player.WHITE));
        assertEquals(Piece.B_KING, Piece.king(Player.BLACK));
    }

    /**
     * Test of bishop method, of class Piece.
     */
    @Test
    public void bishop() {
        assertEquals(Piece.W_BISHOP, Piece.bishop(Player.WHITE));
        assertEquals(Piece.B_BISHOP, Piece.bishop(Player.BLACK));
    }

    /**
     * Test of knight method, of class Piece.
     */
    @Test
    public void knight() {
        assertEquals(Piece.W_KNIGHT, Piece.knight(Player.WHITE));
        assertEquals(Piece.B_KNIGHT, Piece.knight(Player.BLACK));
    }

    /**
     * Test of rook method, of class Piece.
     */
    @Test
    public void rook() {
        assertEquals(Piece.W_ROOK, Piece.rook(Player.WHITE));
        assertEquals(Piece.B_ROOK, Piece.rook(Player.BLACK));
    }

    /**
     * Test of queen method, of class Piece.
     */
    @Test
    public void queen() {
        assertEquals(Piece.W_QUEEN, Piece.queen(Player.WHITE));
        assertEquals(Piece.B_QUEEN, Piece.queen(Player.BLACK));
    }
    
}
