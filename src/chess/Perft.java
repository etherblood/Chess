package chess;

import chess.moves.handlers.MoveExecutor;
import chess.moves.Move;
import chess.moves.generators.MoveGenerator;
import chess.transpositions.PerftTranspositionEntry;
import chess.transpositions.PerftTranspositionTable;

/**
 *
 * @author Philipp
 */
public class Perft {
    private final PerftTranspositionTable table;
    private final PerftTranspositionEntry[] entries = new PerftTranspositionEntry[50];
    private final MoveExecutor exec = new MoveExecutor();
    private final MoveGenerator gen = new MoveGenerator();

    public Perft() {
        this(null);
    }
    public Perft(PerftTranspositionTable table) {
        this.table = table;
        if(table != null) {
            for (int i = 0; i < entries.length; i++) {
                entries[i] = table.createEntry();
            }
        }
    }

    public long perft(ChessState state, Move[] buffer, int offset, int depth) {
        if (depth == 0) {
            return 1;
        }
        
        if(table != null) {
            long hash = state.currentHistory().hash;
            PerftTranspositionEntry entry = entries[depth];
            table.attach(hash, entry);
            if(entry.depth(hash) == depth) {
                return entry.score();
            }
        }
        
        long sum = 0;
        for (int length = gen.generateMoves(state, buffer, offset); offset < length; length--) {
            Move move = buffer[length - 1];
            exec.makeMove(state, move);
            if (!gen.isThreateningKing(state)) {
                sum += depth == 1 ? 1 : perft(state, buffer, length, depth - 1);
            }
            exec.unmakeMove(state, move);
        }
        
        if(table != null) {
            long hash = state.currentHistory().hash;
            PerftTranspositionEntry entry = entries[depth];
            entry.store(hash, depth, sum);
        }
        
        return sum;
    }

    public void divide(ChessState state, Move[] buffer, long[] result, int depth) {
        for (int i = 0; i < result.length; i++) {
            Move move = buffer[i];
            exec.makeMove(state, move);
            if (!gen.isThreateningKing(state)) {
                result[i] = perft(state, buffer, result.length, depth - 1);
            }
            exec.unmakeMove(state, move);
        }
    }
}
