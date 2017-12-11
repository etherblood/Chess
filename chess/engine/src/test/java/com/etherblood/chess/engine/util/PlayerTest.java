package com.etherblood.chess.engine.util;

import com.etherblood.chess.engine.util.Player;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class PlayerTest {
    
    @Test
    public void sign() {
        assertEquals(1, Player.sign(Player.WHITE));
        assertEquals(-1, Player.sign(Player.BLACK));
    }

    @Test
    public void opponent() {
        assertEquals(Player.BLACK, Player.opponent(Player.WHITE));
        assertEquals(Player.WHITE, Player.opponent(Player.BLACK));
    }

    @Test
    public void isWhite() {
        assertTrue(Player.isWhite(Player.WHITE));
        assertFalse(Player.isWhite(Player.BLACK));
    }
    
}
