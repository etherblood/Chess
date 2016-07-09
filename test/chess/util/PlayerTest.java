package chess.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class PlayerTest {
    
    /**
     * Test of sign method, of class Player.
     */
    @Test
    public void sign() {
        assertEquals(1, Player.sign(Player.WHITE));
        assertEquals(-1, Player.sign(Player.BLACK));
    }

    /**
     * Test of opponent method, of class Player.
     */
    @Test
    public void opponent() {
        assertEquals(Player.BLACK, Player.opponent(Player.WHITE));
        assertEquals(Player.WHITE, Player.opponent(Player.BLACK));
    }

    /**
     * Test of isWhite method, of class Player.
     */
    @Test
    public void isWhite() {
        assertTrue(Player.isWhite(Player.WHITE));
        assertFalse(Player.isWhite(Player.BLACK));
    }
    
}
