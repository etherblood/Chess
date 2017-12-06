package com.etherblood.chess.engine;

/**
 *
 * @author Philipp
 */
public final class HistoryState {
    public byte enPassant, fiftyRule, castling;
    public int lastMove;
    public long hash;
}
