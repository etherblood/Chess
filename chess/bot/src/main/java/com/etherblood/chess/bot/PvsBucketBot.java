package com.etherblood.chess.bot;

import com.etherblood.chess.engine.ChessPrinter;
import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.engine.HistoryState;
import com.etherblood.chess.bot.evaluations.Evaluation;
import com.etherblood.chess.bot.evaluations.PstEvaluation;
import com.etherblood.chess.engine.transpositions.ScoreType;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.moves.NullMoveExecutor;
import com.etherblood.chess.engine.moves.generators.MoveGenerator;
import com.etherblood.chess.engine.moves.handlers.MoveExecutor;
import com.etherblood.chess.engine.transpositions.TranspositionEntry;
import com.etherblood.chess.engine.transpositions.BucketTranspositionTable;
import com.etherblood.chess.engine.transpositions.TranspositionTable;
import com.etherblood.chess.engine.transpositions.TranspositionTableStatistics;
import com.etherblood.chess.engine.util.Mask;
import com.etherblood.chess.engine.util.Piece;
import com.etherblood.chess.engine.util.Player;
import com.etherblood.chess.engine.util.Score;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PvsBucketBot implements Bot {

    public /*private static final*/ boolean QUIESCENCE_ENABLED = true, KILLERS_ENABLED = true, NULLMOVE_ENABLED = true, WINDOW_ENABLED = true;
    private final static int MAX_DEPTH = 20;
    private final MoveGenerator moveGen = new MoveGenerator();
    private final MoveExecutor exe = new MoveExecutor();
    private final NullMoveExecutor nullExe = new NullMoveExecutor();
    private final Move[] buffer = MoveGenerator.createBuffer(50 * MAX_DEPTH);//TODO: this might be to small
    private final Move[] killers = MoveGenerator.createBuffer(2 * MAX_DEPTH);
    private final Evaluation eval;
    private ChessState state;
    private long nodes, totalNodes, totalMillis, totalSearches;
    private final TranspositionTable table = new BucketTranspositionTable(20);// 2^22 * 60B = 251 658 240B
    private final TranspositionEntry[] depthEntries;
    private MoveOrder moveOrder;
    private int lastScore = 0;
    private final List<Move> rootMoves = new ArrayList<>();

    public PvsBucketBot(Evaluation eval) {
        this.eval = eval;
        depthEntries = new TranspositionEntry[MAX_DEPTH];
        for (int i = 0; i < MAX_DEPTH; i++) {
            depthEntries[i] = new TranspositionEntry();
        }
    }

    @Override
    public void setState(ChessState state) {
        this.state = state;
        moveOrder = new MvvLvaOrder(state);
    }

    @Override
    public Move compute(int depth) {
        nodes = 0;
        generateRootMoves();
        new ChessPrinter().print(state.pieces);
        lastScore *= Player.sign(state.currentPlayer());
        long millis = -System.currentTimeMillis();
        for (int i = 1; i <= depth; i++) {
            clearKillers();
            if (WINDOW_ENABLED) {
                lastScore = windowedIteration(i, lastScore);
            } else {
                lastScore = pvsRoot(depth, -Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
            Collections.sort(rootMoves, (Move m1, Move m2) -> Integer.compare(m2.score, m1.score));
            String scoreString;
            if (Score.isMateScore(lastScore)) {
                int matePly = Score.plyFromMateScore(lastScore);
                scoreString = (Player.isWhite(~matePly & 1) ? "white" : "black") + " mates in " + (matePly - state.moveCounter);
            } else {
                scoreString = "w_score: " + String.valueOf(lastScore * Player.sign(state.currentPlayer()));
            }
            System.out.println("(" + i + ") " + rootMoves.get(0).toString() + " " + scoreString);
        }
        millis += System.currentTimeMillis();
        Move move = rootMoves.get(0);
        rootMoves.clear();
        move.score = lastScore;
        lastScore *= Player.sign(state.currentPlayer());
        totalMillis += millis;
        totalNodes += nodes;
        String nps = millis > 0 ? "" + (nodes / millis) : "-";
        System.out.println(nodes + " nodes / " + millis + " ms (" + nps + " kn/s)");
        System.out.println("total: " + totalNodes + " nodes / " + totalMillis + " ms (" + (totalNodes / totalMillis) + " kn/s)");
        totalSearches++;
        System.out.println("avg nodes: " + totalNodes / totalSearches);
        System.out.println("branching: " + String.format("%s", Math.pow(nodes, 1d / depth)));
        System.out.println("avg branching: " + String.format("%s", Math.pow(totalNodes / totalSearches, 1d / depth)));
        TranspositionTableStatistics.soutHitrate();
        TranspositionTableStatistics.clear();
        System.out.println("final move: " + move.toString());
        return move;
    }

    private void generateRootMoves() {
        int moveLimit = moveGen.generateMoves(state, buffer, 0);
        for (int i = 0; i < moveLimit; i++) {
            Move move = new Move();
            move.fromMove(buffer[i]);
            exe.makeMove(state, move);
            if (!moveGen.isThreateningKing(state)) {
                rootMoves.add(move);
            }
            exe.unmakeMove(state, move);
        }
    }

    private int windowedIteration(int depth, int expectedScore) {
        int delta = 25;
        int alpha = expectedScore - delta;
        int beta = expectedScore + delta;
        int resultScore = pvsRoot(depth, alpha, beta);
        if (resultScore <= alpha || resultScore >= beta) {
            resultScore = pvsRoot(depth, -Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
        return resultScore;
    }

    private void clearKillers() {
        for (Move killer : killers) {
            killer.clear();
        }
    }

    private int pvsRoot(final int depth, int alpha, final int beta) {
        nodes++;
        boolean foundPv = false;
        for (Move move : rootMoves) {
            exe.makeMove(state, move);
            assert !moveGen.isThreateningKing(state);
            int value;
            if (foundPv) {
                value = -principalVariationSearch(depth - 1, -alpha - 1, -alpha, 0);
                if (alpha < value) {
                    value = -principalVariationSearch(depth - 1, -beta, -alpha, 0);
                }
            } else {
                value = -principalVariationSearch(depth - 1, -beta, -alpha, 0);
            }
            exe.unmakeMove(state, move);
            if (value > alpha) {
                move.score += depth;
                if (value >= beta) {
                    return beta;
                }
                alpha = value;
                foundPv = true;
            }
        }
        return alpha;
    }

    private int principalVariationSearch(int depth, int alpha, int beta, int moveOffset) {
        assert alpha < beta;
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

        if (depth <= 0) {
            return quiescenceSearch(alpha, beta, moveOffset);
        }

        if (NULLMOVE_ENABLED && 0 < alpha && alpha + 1 == beta && !moveGen.isKingThreatened(state) && Mask.count(state.playerMasks[state.currentPlayer()] ^ state.pieceMasks[Piece.pawn(state.currentPlayer())]) > 2) {
            int reduction = (depth + 19) / 5;
            nullExe.makeNullMove(state);
            int score = -principalVariationSearch(depth - reduction, -beta, -alpha, moveOffset);
            nullExe.unmakeNullMove(state);
            if (score >= beta) {
                depth = Math.max(0, depth - 4);
            }
        }

        short hashMove = 0;
        TranspositionEntry entry = depthEntries[depth];
        long hash = state.currentHistory().hash;
        TranspositionTableStatistics.requests++;
        if (table.load(hash, entry)) {
            TranspositionTableStatistics.hits++;
            byte entryType = entry.type;
            if (entry.depth >= depth) {
                switch (entryType) {
                    case ScoreType.EXACT:
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
                    case ScoreType.EXACT:
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
                    case ScoreType.EXACT:
                    case ScoreType.LOWER_BOUND:
                        hashMove = entry.move;
                        break;
                }
            }
        }

        Move bestMove = Move.empty();
        byte entryType = ScoreType.UPPER_BOUND;
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

        if (KILLERS_ENABLED) {
            for (int moveIndex = moveOffset + preMoveIndex; moveIndex < moveLimit; moveIndex++) {
                int killerIndex = depth << 1;
                assert killers[killerIndex].capture == Piece.EMPTY;
                if (buffer[moveIndex].identityEquals(killers[killerIndex]) || buffer[moveIndex].identityEquals(killers[killerIndex + 1])) {
                    Move tmp = buffer[moveOffset + preMoveIndex];
                    buffer[moveOffset + preMoveIndex] = buffer[moveIndex];
                    buffer[moveIndex] = tmp;
                    preMoveIndex++;
                }
            }
        }

        assert alpha < beta;
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
                    entryType = ScoreType.EXACT;
                    alpha = value;
                    foundPv = true;
                }
            }
        }
        if (noMovesFound) {
            entryType = ScoreType.EXACT;
            if (moveGen.isKingThreatened(state)) {
                alpha = Score.mateLossScore(state.moveCounter);
            } else {
                alpha = 0;
            }
        } else if (KILLERS_ENABLED && bestMove.capture == Piece.EMPTY && entryType != ScoreType.UPPER_BOUND) {
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
        assert alpha == (short) alpha;

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
        if (!QUIESCENCE_ENABLED) {
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
