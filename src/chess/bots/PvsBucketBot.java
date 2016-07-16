package chess.bots;

import chess.ChessPrinter;
import chess.bots.evaluations.Evaluation;
import chess.ChessState;
import chess.HistoryState;
import chess.transpositions.ScoreType;
import chess.moves.Move;
import chess.moves.generators.MoveGenerator;
import chess.moves.handlers.MoveExecutor;
import chess.transpositions.BucketTranspositionEntry;
import chess.transpositions.BucketTranspositionTable;
import chess.transpositions.TranspositionTableStatistics;
import chess.util.Player;
import chess.util.Score;

public class PvsBucketBot implements Bot {

    private final static boolean QUIESCENCE_DISABLED = false;
    private final static int MAX_DEPTH = 20;
    private final MoveGenerator moveGen = new MoveGenerator();
    private final MoveExecutor exe = new MoveExecutor();
    private final Move[] buffer = MoveGenerator.createBuffer(50 * MAX_DEPTH);//TODO: this might be to small
    private final Move[] killers = MoveGenerator.createBuffer(2 * MAX_DEPTH);
    private final Evaluation eval;
    private ChessState state;
    private long nodes;
    private final BucketTranspositionTable table = new BucketTranspositionTable(23);// 2^22 * 60B = 251 658 240B
    private final BucketTranspositionEntry[] depthEntries;
    private MoveOrder moveOrder;

    public PvsBucketBot(Evaluation eval) {
        this.eval = eval;
        depthEntries = new BucketTranspositionEntry[MAX_DEPTH];
        for (int i = 0; i < MAX_DEPTH; i++) {
            depthEntries[i] = new BucketTranspositionEntry();
        }
    }

    @Override
    public void setState(ChessState state) {
        this.state = state;
        moveOrder = new MvvLvaOrder(state);
    }

    @Override
    public Move compute() {
        int depth = 8;
        Move move = new Move();
        new ChessPrinter().print(state.pieces);
        long millis = -System.currentTimeMillis();
        for (int i = 1; i <= depth; i++) {
            clearKillers();//clear killers here because we index them by depth
            int score = pvsRoot(i, -Integer.MAX_VALUE, Integer.MAX_VALUE, move);
            String scoreString;
            if (Score.isMateScore(score)) {
                int matePly = Score.plyFromMateScore(score);
                scoreString = (Player.isWhite(~matePly & 1) ? "white" : "black") + " mates in " + (matePly - state.moveCounter);
            } else {
                scoreString = "w_score: " + String.valueOf(score * Player.sign(state.currentPlayer()));
            }
            System.out.println("(" + i + ") " + move.toString() + " " + scoreString);
        }
        millis += System.currentTimeMillis();
        String nps = millis > 0 ? "" + (nodes / millis) : "-";
        System.out.println(nodes + " nodes / " + millis + " ms (" + nps + " kn/s)");
        System.out.println("branching: " + String.format("%s", Math.pow(nodes, 1d / depth)));
        TranspositionTableStatistics.soutHitrate();
        TranspositionTableStatistics.clear();
        System.out.println("final move: " + move.toString());
        return move;
    }

    private void clearKillers() {
        for (Move killer : killers) {
            killer.clear();
        }
    }

    private int pvsRoot(final int depth, int alpha, final int beta, final Move bestMove) {
        nodes = 1;
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
                    value = -principalVariationSearch(depth - 1, -alpha - 1, -alpha, moveLimit);
                    if (alpha < value) {
                        value = -principalVariationSearch(depth - 1, -beta, -alpha, moveLimit);
                    }
                } else {
                    value = -principalVariationSearch(depth - 1, -beta, -alpha, moveLimit);
                }
                exe.unmakeMove(state, move);
                if (value > alpha) {
                    bestMove.fromMove(move);
                    alpha = value;
                    foundPv = true;
                }
            }
        }
        return alpha;
    }

    private int principalVariationSearch(final int depth, int alpha, int beta, int moveOffset) {
        nodes++;
        if (isRepetition()) {
            return 0;
        }
        if (state.currentHistory().fiftyRule >= 100) {
            if (moveGen.isKingThreatened(state) && noLegalMoves(moveOffset)) {
                return Score.mateLossScore(state.moveCounter);
            }
            return 0;
        }
        if (depth == 0) {
            return quiescenceSearch(alpha, beta, moveOffset);
        }

        short hashMove = 0;
        BucketTranspositionEntry entry = depthEntries[depth];
        long hash = state.currentHistory().hash;
        TranspositionTableStatistics.requests++;
        if (table.load(hash, entry)) {
            TranspositionTableStatistics.hits++;
            int entryType = entry.type;
            if (entry.depth >= depth) {
                switch (entryType) {
                    case ScoreType.IN_BOUNDS:
                        return Score.fromTableScore(entry.score, state.moveCounter);
                    case ScoreType.UPPER_BOUND:
                        beta = Score.fromTableScore(entry.score, state.moveCounter);
                        if (alpha >= beta) {
                            return beta;
                        }
                        break;
                    case ScoreType.LOWER_BOUND:
                        alpha = Score.fromTableScore(entry.score, state.moveCounter);
                        if (alpha >= beta) {
                            return beta;
                        }
                        hashMove = entry.move;
                        break;
                    default:
                        throw new IllegalStateException("entry type: " + entry.type);
                }
            } else {
                switch (entryType) {
                    case ScoreType.IN_BOUNDS:
                    case ScoreType.LOWER_BOUND:
                        hashMove = entry.move;
                        break;
                }
            }
        }

        if (hashMove == 0 && depth >= 3) {
            principalVariationSearch(depth - 2, alpha, beta, moveOffset);
            if (table.load(hash, entry)) {
                switch (entry.type) {
                    case ScoreType.IN_BOUNDS:
                    case ScoreType.LOWER_BOUND:
                        hashMove = entry.move;
                        break;
                }
            }
        }

        Move bestMove = Move.empty();
        int entryType = ScoreType.UPPER_BOUND;
        boolean foundPv = false, noMovesFound = true;
        int preMoveIndex = 0;

        int moveLimit = moveGen.generateMoves(state, buffer, moveOffset);
        if (hashMove != 0) {
            for (int moveIndex = moveOffset; moveIndex < moveLimit; moveIndex++) {
                if (buffer[moveIndex].toShort() == hashMove) {
                    Move tmp = buffer[moveOffset];
                    buffer[moveOffset] = buffer[moveIndex];
                    buffer[moveIndex] = tmp;
                }
            }
            preMoveIndex++;
        }

        for (int moveIndex = moveOffset + preMoveIndex; moveIndex < moveLimit; moveIndex++) {
            int killerIndex = depth << 1;
            if (buffer[moveIndex].identityEquals(killers[killerIndex]) || buffer[moveIndex].identityEquals(killers[killerIndex + 1])) {
                Move tmp = buffer[moveOffset + preMoveIndex];
                buffer[moveOffset + preMoveIndex] = buffer[moveIndex];
                buffer[moveIndex] = tmp;
                preMoveIndex++;
            }
        }

        moveOrder.sort(buffer, moveOffset + preMoveIndex, moveLimit);

        for (int moveIndex = moveOffset; moveIndex < moveLimit; moveIndex++) {
            Move move = buffer[moveIndex];
            exe.makeMove(state, move);
            if (moveGen.isThreateningKing(state)) {
                exe.unmakeMove(state, move);
            } else {
                noMovesFound = false;
                int value;
                if (foundPv) {
                    value = -principalVariationSearch(depth - 1, -alpha - 1, -alpha, moveLimit);
                    if (alpha < value) {
                        value = -principalVariationSearch(depth - 1, -beta, -alpha, moveLimit);
                    }
                } else {
                    value = -principalVariationSearch(depth - 1, -beta, -alpha, moveLimit);
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
        if (noMovesFound) {
            entryType = ScoreType.IN_BOUNDS;
            if (moveGen.isKingThreatened(state)) {
                alpha = Score.mateLossScore(state.moveCounter);
            } else {
                alpha = 0;
            }
        } else if (entryType != ScoreType.UPPER_BOUND) {
            int killerIndex = depth << 1;
            if (!bestMove.identityEquals(killers[killerIndex])) {
                Move tmp = killers[killerIndex + 1];
                killers[killerIndex + 1] = killers[killerIndex];
                killers[killerIndex] = tmp;
                if (!bestMove.identityEquals(killers[killerIndex])) {
                    killers[killerIndex].fromMove(bestMove);
                }
            }
        }

//        if(alpha == 0 || alpha >= Short.MAX_VALUE / 2 || alpha <= Short.MAX_VALUE / -2) {
//            return alpha;
//        }
        entry.hash = hash;
        entry.move = bestMove.toShort();
        entry.depth = depth;
        entry.type = entryType;
        entry.score = Score.toTableScore(alpha, state.moveCounter);
        table.store(entry);
        return alpha;
    }

    private int quiescenceSearch(int alpha, int beta, int moveOffset) {
        int value = eval.evaluate(state, alpha, beta);
        if (QUIESCENCE_DISABLED) {
            return value;
        }
        if (value > alpha) {
            if (value >= beta) {
                return beta;
            }
            alpha = value;
        }

        int moveLimit;
        boolean mateThreat = moveGen.isKingThreatened(state);
        if (mateThreat) {
            moveLimit = moveGen.generateMoves(state, buffer, moveOffset);
        } else {
            moveLimit = moveGen.generateAttacks(state, buffer, moveOffset);
        }
        moveOrder.sort(buffer, moveOffset, moveLimit);
        for (int moveIndex = moveOffset; moveIndex < moveLimit; moveIndex++) {
            Move move = buffer[moveIndex];
            exe.makeMove(state, move);
            if (moveGen.isThreateningKing(state)) {
                exe.unmakeMove(state, move);
            } else {
                mateThreat = false;
                value = -quiescenceSearch(-beta, -alpha, moveLimit);
                exe.unmakeMove(state, move);
                if (value > alpha) {
                    if (value >= beta) {
                        return beta;
                    }
                    alpha = value;
                }
            }
        }

        if (mateThreat) {
            return Score.mateLossScore(state.moveCounter);
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
