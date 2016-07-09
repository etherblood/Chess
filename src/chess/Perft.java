package chess;

import chess.moves.handlers.MoveExecutor;
import chess.moves.Move;
import chess.moves.generators.MoveGenerator;

/**
 *
 * @author Philipp
 */
public class Perft {

    private final MoveExecutor exec = new MoveExecutor();
    private final MoveGenerator gen = new MoveGenerator();

    public long perft(ChessState state, Move[] buffer, int offset, int depth) {
        if (depth == 0) {
            return 1;
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
