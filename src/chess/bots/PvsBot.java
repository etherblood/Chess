package chess.bots;

import chess.bots.evaluations.Evaluation;
import chess.ChessState;
import chess.HistoryState;
import chess.moves.Move;
import chess.moves.generators.MoveGenerator;
import chess.moves.handlers.MoveExecutor;

public class PvsBot implements Bot {

    private final MoveGenerator gen = new MoveGenerator();
    private final MoveExecutor exe = new MoveExecutor();
    private final Move[] buffer = MoveGenerator.createBuffer(1000);
    private final Evaluation eval;
    private ChessState state;
    private long nodes;

    public PvsBot(Evaluation eval) {
        this.eval = eval;
    }

    @Override
    public void setState(ChessState state) {
        this.state = state;
    }

    @Override
    public Move compute() {
        long millis = -System.currentTimeMillis();
        int depth = 7;
        Move move = pvsRoot(depth, -Short.MAX_VALUE, Short.MAX_VALUE);
        millis += System.currentTimeMillis();
        System.out.println(nodes + " nodes / " + millis + " ms (" + (nodes / millis) + " kn/s)");
        return move;
    }
    
    private Move pvsRoot(final int depth, int alpha, final int beta) {
        nodes = 1;
        int best = 0;
        boolean foundPv = false;
        for (int moveIndex = gen.generateMoves(state, buffer, 0); moveIndex > 0; moveIndex--) {
            Move move = buffer[moveIndex - 1];
            exe.makeMove(state, move);
            if (gen.isThreateningKing(state)) {
                exe.unmakeMove(state, move);
            } else {
                int value;
                if (foundPv) {
                    value = -pvs(depth - 1, -alpha - 1, -alpha, moveIndex);
                    if (alpha < value) {
                        value = -pvs(depth - 1, -beta, -alpha, moveIndex);
                    }
                } else {
                    value = -pvs(depth - 1, -beta, -alpha, moveIndex);
                }
                exe.unmakeMove(state, move);
                if (value > alpha) {
                    best = move.toInt();
                    alpha = value;
                    foundPv = true;
                }
            }
        }
        Move bestMove = new Move();
        bestMove.fromInt(best);
        return bestMove;
    }

    private int pvs(final int depth, int alpha, final int beta, final int moveOffset) {
        nodes++;
        if(isRepetition()) {
            return 0;
        }
        if(state.currentHistory().fiftyRule >= 100) {
            if(gen.isKingThreatened(state) && noLegalMoves(moveOffset)) {
                return -Short.MAX_VALUE + state.moveCounter;
            }
            return 0;
        }
        if (depth == 0) {
            return eval.evaluate(state, alpha, beta);//TODO:qss
        }
        
        //TODO: tt_load
        //TODO: iid
        
        boolean foundPv = false, noMovesFound = true;
        for (int moveIndex = gen.generateMoves(state, buffer, moveOffset); moveIndex > moveOffset; moveIndex--) {
            Move move = buffer[moveIndex - 1];
            exe.makeMove(state, move);
            if (gen.isThreateningKing(state)) {
                exe.unmakeMove(state, move);
            } else {
                noMovesFound = false;
                int value;
                if (foundPv) {
                    value = -pvs(depth - 1, -alpha - 1, -alpha, moveIndex);
                    if (alpha < value) {
                        value = -pvs(depth - 1, -beta, -alpha, moveIndex);
                    }
                } else {
                    value = -pvs(depth - 1, -beta, -alpha, moveIndex);
                }
                exe.unmakeMove(state, move);
                if (value > alpha) {
                    if (value >= beta) {
                        alpha = beta;
                        break;
                    }
                    alpha = value;
                    foundPv = true;
                }
            }
        }
        if (noMovesFound && !gen.isKingThreatened(state)) {
            alpha = 0;
        }
        
        //TODO: tt_store
        
        return alpha;
    }

    private boolean noLegalMoves(int moveOffset) {
        boolean noMovesLeft = true;
        int moveLimit = gen.generateMoves(state, buffer, moveOffset);
        for (int i = moveOffset; i < moveLimit && noMovesLeft; i++) {
            Move move = buffer[i];
            exe.makeMove(state, move);
            noMovesLeft &= gen.isThreateningKing(state);
            exe.unmakeMove(state, move);
        }
        return noMovesLeft;
    }

    private boolean isRepetition() {
        HistoryState history = state.currentHistory();
        long hash = history.hash;
        int limit = state.moveCounter - history.fiftyRule;
        for (int i = state.moveCounter - 4; i >= limit; i -= 2) {
            if(state.history[i].hash == hash) {
                return true;
            }
        }
        return false;
    }
    
}
