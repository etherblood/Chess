package chess.moves;

import chess.ChessPrinter;

/**
 *
 * @author Philipp
 */
public final class Move {
    public static final Move EMPTY = new Move();
    public int from, to, capture, info, score;

    public void fromInt(int value) {
        from = value & 0xff;
        value >>>= 8;
        to = value & 0xff;
        value >>>= 8;
        capture = value & 0xff;
        value >>>= 8;
        info = value;
        assertRanges();
    }
    
    public int toInt() {
        assertRanges();
        return toInt(from, to, capture, info);
    }
    
    private static int toInt(int from, int to, int capture, int info) {
        assert (from & 0x3f) == from;
        assert (to & 0x3f) == to;
        assert (capture & 0xff) == capture;
        assert (info & 0xf) == info;
        int result = info;
        result <<= 8;
        result |= capture;
        result <<= 8;
        result |= to;
        result <<= 8;
        result |= from;
        return result;
    }

    public void fromShort(short value) {
        from = value & 0x3f;
        value >>>= 6;
        to = value & 0x3f;
        value >>>= 6;
        info = value;
        assertRanges();
    }
    
    public short toShort() {
        assertRanges();
        return toShort(from, to, info);
    }
    
    private static short toShort(int from, int to, int info) {
        assert (from & 0x3f) == from;
        assert (to & 0x3f) == to;
        assert (info & 0xf) == info;
        int result = info;
        result <<= 6;
        result |= to;
        result <<= 6;
        result |= from;
        return (short) result;
    }

    public boolean assertRanges() {
        assert (from & 0x3f) == from;
        assert (to & 0x3f) == to;
        assert (capture & 0xff) == capture;
        assert (info & 0xf) == info;
        return true;
    }

    @Override
    public String toString() {
        return ChessPrinter.fromSquare(from) + "->" + ChessPrinter.fromSquare(to);
    }
    
    public String toRawString() {
        return from + "->" + to;
    }
}
