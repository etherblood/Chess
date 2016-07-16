package chess;

import chess.moves.Move;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philipp
 */
public class MoveTest {

    @Test
    public void intConversion() {
        Move move = new Move();
        for (int from = 0; from < 64; from++) {
            move.from = from;
            for (int to = 0; to < 64; to++) {
                move.to = to;
                for (int capture = 0; capture < 16; capture++) {
                    move.capture = capture;
                    for (int info = 0; info < 4; info++) {
                        move.info = info;
                        int asInt = move.toInt();
                        move.fromInt(asInt);
                        assertEquals(asInt, move.toInt());
                    }
                }
            }
        }
    }

}
