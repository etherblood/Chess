package chess.bots;

import chess.bots.evaluations.Evaluation;
import chess.ChessState;
import chess.HistoryState;
import chess.transpositions.RawTranspositionEntry;
import chess.transpositions.RawTranspositionTable;
import chess.transpositions.ScoreType;
import chess.moves.Move;
import chess.moves.generators.CaptureGenerator;
import chess.moves.generators.MoveGenerator;
import chess.moves.handlers.MoveExecutor;
import chess.util.Score;

public class PvsTableBot implements Bot {

    private final static int MAX_DEPTH = 20;
    private final MoveGenerator moveGen = new MoveGenerator();
    private final CaptureGenerator captGen = new CaptureGenerator();
    private final MoveExecutor exe = new MoveExecutor();
    private final Move[] buffer = MoveGenerator.createBuffer(1000);
    private final Evaluation eval;
    private ChessState state;
    private long nodes;
    private final RawTranspositionTable table = new RawTranspositionTable(25);// 2^25 * 10B = 335 544 320B
    private final RawTranspositionEntry[] depthEntries;
    private MoveOrder moveOrder;

    public PvsTableBot(Evaluation eval) {
        this.eval = eval;
        depthEntries = new RawTranspositionEntry[MAX_DEPTH];
        for (int i = 0; i < MAX_DEPTH; i++) {
            depthEntries[i] = table.createEntry();
        }
    }

    @Override
    public void setState(ChessState state) {
        this.state = state;
        moveOrder = new MvvLvaOrder(state);
    }

    @Override
    public Move compute() {
        long millis = -System.currentTimeMillis();
        int depth = 8;
        Move move = pvsRoot(depth, -Short.MAX_VALUE, Short.MAX_VALUE);
        millis += System.currentTimeMillis() + 1;
        System.out.println(nodes + " nodes / " + millis + " ms (" + (nodes / millis) + " kn/s)");
        System.out.println("effective branching: " + String.format("%s", Math.pow(nodes, 1d / depth)));
        return move;
    }

    private Move pvsRoot(final int depth, int alpha, final int beta) {
        nodes = 1;
        Move bestMove = Move.EMPTY;
        boolean foundPv = false;
        int moveLimit = moveGen.generateMoves(state, buffer, 0);
        moveOrder.sort(buffer, 0, moveLimit);
        for (int moveIndex = 0; moveIndex < moveLimit; moveIndex++) {
            Move move = buffer[moveIndex];
            exe.makeMove(state, move);
            if (moveGen.isThreateningKing(state)) {
                exe.unmakeMove(state, move);
            } else {
                int value;
                if (foundPv) {
                    value = -pvs(depth - 1, -alpha - 1, -alpha, moveLimit);
                    if (alpha < value) {
                        value = -pvs(depth - 1, -beta, -alpha, moveLimit);
                    }
                } else {
                    value = -pvs(depth - 1, -beta, -alpha, moveLimit);
                }
                exe.unmakeMove(state, move);
                if (value > alpha) {
                    bestMove = move;
                    alpha = value;
                    foundPv = true;
                }
            }
        }
        return bestMove;
    }

    private int pvs(final int depth, int alpha, int beta, int moveOffset) {
        nodes++;
        if (isRepetition()) {
            return Score.boundScore(alpha, 0, beta);
        }
        if (state.currentHistory().fiftyRule >= 100) {
            if (moveGen.isKingThreatened(state) && noLegalMoves(moveOffset)) {
                return alpha;
            }
            return Score.boundScore(alpha, 0, beta);
        }
        if (depth == 0) {
            return quiescenceSearch(alpha, beta, moveOffset);
        }

        short hashMove = 0;
        RawTranspositionEntry entry = depthEntries[depth];
        long hash = state.currentHistory().hash;
        table.attach(hash, entry);
        entry.refresh();
        if (entry.hashEquals(hash)) {
            int entryType = entry.type();
            if (entry.depth() >= depth) {
                switch (entryType) {
                    case ScoreType.IN_BOUNDS:
                        return entry.score();
                    case ScoreType.UPPER_BOUND:
                        beta = entry.score();
                        if (alpha >= beta) {
                            return alpha;
                        }
                        break;
                    case ScoreType.LOWER_BOUND:
                        alpha = entry.score();
                        if (alpha >= beta) {
                            return beta;
                        }
                        hashMove = entry.move();
                        break;
                    default:
                        throw new IllegalStateException("entry type: " + entry.type());
                }
            } else {
                switch (entryType) {
                    case ScoreType.IN_BOUNDS:
                    case ScoreType.LOWER_BOUND:
                        hashMove = entry.move();
                        break;
                }
            }
        }

        if (hashMove == 0 && depth > 4) {
            pvs(depth - 2, alpha, beta, moveOffset);
            entry.refresh();
            switch (entry.type()) {
                case ScoreType.IN_BOUNDS:
                case ScoreType.LOWER_BOUND:
                    hashMove = entry.move();
                    break;
            }
        }

        Move bestMove = Move.EMPTY;
        int entryType = ScoreType.UPPER_BOUND;
        boolean foundPv = false, noMovesFound = true;

        int moveLimit = moveGen.generateMoves(state, buffer, moveOffset);
        if (hashMove != 0) {
            for (int moveIndex = moveOffset; moveIndex < moveLimit; moveIndex++) {
                if (buffer[moveIndex].toShort() == hashMove) {
                    Move tmp = buffer[moveOffset];
                    buffer[moveOffset] = buffer[moveIndex];
                    buffer[moveIndex] = tmp;
                }
            }
            moveOrder.sort(buffer, moveOffset + 1, moveLimit);
        } else {
            moveOrder.sort(buffer, moveOffset, moveLimit);
        }
        
        
        for (int moveIndex = moveOffset; moveIndex < moveLimit; moveIndex++) {
            Move move = buffer[moveIndex];
            exe.makeMove(state, move);
            if (moveGen.isThreateningKing(state)) {
                exe.unmakeMove(state, move);
            } else {
                noMovesFound = false;
                int value;
                if (foundPv) {
                    value = -pvs(depth - 1, -alpha - 1, -alpha, moveLimit);
                    if (alpha < value) {
                        value = -pvs(depth - 1, -beta, -alpha, moveLimit);
                    }
                } else {
                    value = -pvs(depth - 1, -beta, -alpha, moveLimit);
                }
                exe.unmakeMove(state, move);
                if (value > alpha) {
                    bestMove = move;
                    if (value >= beta) {
                        entryType = ScoreType.LOWER_BOUND;
                        alpha = beta;
                        break;
                    }
                    entryType = ScoreType.IN_BOUNDS;
                    alpha = value;
                    foundPv = true;
                }
            }
        }
        if (noMovesFound && !moveGen.isKingThreatened(state)) {
            alpha = Score.boundScore(alpha, 0, beta);
        }

        entry.store(hash, bestMove.toShort(), depth, entryType, alpha);
        return alpha;
    }
    
    private int quiescenceSearch(int alpha, int beta, int moveOffset) {
        int value = eval.evaluate(state, alpha, beta);
        if(value > alpha) {
            if(value >= beta) {
                return beta;
            }
            alpha = value;
        }
        
        int moveLimit;
        if(moveGen.isKingThreatened(state)) {
            moveLimit = moveGen.generateMoves(state, buffer, moveOffset);
        } else {
            moveLimit = captGen.generateMoves(state, buffer, moveOffset);
        }
        moveOrder.sort(buffer, moveOffset, moveLimit);
        for (int moveIndex = moveOffset; moveIndex < moveLimit; moveIndex++) {
            Move move = buffer[moveIndex];
            exe.makeMove(state, move);
            if (moveGen.isThreateningKing(state)) {
                exe.unmakeMove(state, move);
            } else {
                value = -quiescenceSearch(-beta, -alpha, moveLimit);
                exe.unmakeMove(state, move);
                if (value > alpha) {
                    if (value >= beta) {
                        alpha = beta;
                        break;
                    }
                    alpha = value;
                }
            }
        }
        
        return alpha;
    }

    private boolean noLegalMoves(int moveOffset) {
        boolean noMovesLeft = true;
        int moveLimit = moveGen.generateMoves(state, buffer, moveOffset);
        for (int i = moveOffset; i < moveLimit && noMovesLeft; i++) {
            Move move = buffer[i];
            exe.makeMove(state, move);
            noMovesLeft &= moveGen.isThreateningKing(state);
            exe.unmakeMove(state, move);
        }
        return noMovesLeft;
    }

    private boolean isRepetition() {
        HistoryState history = state.currentHistory();
        long hash = history.hash;
        int limit = state.moveCounter - history.fiftyRule;
        for (int i = state.moveCounter - 4; i >= limit; i -= 2) {
            if (state.history[i].hash == hash) {
                return true;
            }
        }
        return false;
    }

}
