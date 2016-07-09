package chess;

/**
 *
 * @author Philipp
 */
public final class HistoryState {
    public byte enPassant, fiftyRule, castling;
    public int lastMove;
    public long hash;
}
