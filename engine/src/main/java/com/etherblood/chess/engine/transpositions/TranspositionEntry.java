package com.etherblood.chess.engine.transpositions;

/**
 *
 * @author Philipp
 */
public class TranspositionEntry {
    public long hash;
    public short move, score;
    public int depth;
    public byte type;
}
