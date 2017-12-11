package com.etherblood.chess.bot.evaluations;

import com.etherblood.chess.engine.ChessPrinter;
import com.etherblood.chess.engine.ChessSetup;
import com.etherblood.chess.engine.ChessState;
import com.etherblood.chess.bot.Main;
import com.etherblood.chess.bot.Bot;
import com.etherblood.chess.bot.PvsBucketBot;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.moves.generators.MoveGenerator;
import com.etherblood.chess.engine.moves.handlers.MoveExecutor;
import com.etherblood.chess.engine.util.Score;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class EvalQualityGuesser {
    private final static List<String> TEST_POSITIONS = Arrays.asList(
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
            "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -",
            "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -",
            "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1",
            "8/1K1ppRPr/7p/8/3k1PBp/5PPN/2p5/2Q5 w - - 0 1",
            "8/pRp5/p2B3P/1p3P2/1P1K2N1/qrP1P3/k1bP1pBp/1n2Q1b1 w - - 0 1",
            "1B1k4/3P3P/3r3p/R2P1P2/B1P4n/ppP1p1Pq/pp1QK3/2b3N1 w - - 0 1");
    
    private final MoveGenerator moveGen = new MoveGenerator();
    private final MoveExecutor exe = new MoveExecutor();
    private final Move[] buffer = MoveGenerator.createBuffer(1000);
    private final ChessState state = new ChessState();
    private final ChessSetup setup = new ChessSetup();
    private Bot bot;
        
    public static void main(String... args) {
        new EvalQualityGuesser().runTest(new AdvancedEval());
    }
    
    public void runTest(Evaluation eval) {
        String worstFen = "none";
        int worstError = 0;
        bot = new PvsBucketBot(eval);
        bot.setState(state);
        setup.reset(state);
        long sum = 0, count = 0;
//        for (String fen : TEST_POSITIONS) {
//            setup.fromFen(state, fen);
        while(!Main.isGameOver(state)) {
            String fen = ChessPrinter.toFen(state);
            
            Move move = bot.compute(3);
            int botScore = move.score;
            if(Score.isMateScore(botScore)) {
                break;
            }
            assert !Score.isMateScore(botScore);
            int evalScore = quiescenceSearch(eval, -Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
            System.out.println(fen + " expected: " + botScore + " actual: " + evalScore);
            int error = botScore - evalScore;
            sum += error * error;
            count++;
            exe.makeMove(state, move);
            if(error * error > worstError) {
                worstError = error * error;
                worstFen = fen;
            }
        }
//        }
        System.out.println("quality: " + -sum / count);
        System.out.println("worst: " + worstFen + " error: " + (int)Math.sqrt(worstError));
    }
    
    private int quiescenceSearch(Evaluation eval, int alpha, int beta, int moveOffset) {
        int value = eval.evaluate(state, alpha, beta);
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
        for (int moveIndex = moveOffset; moveIndex < moveLimit; moveIndex++) {
            Move move = buffer[moveIndex];
            exe.makeMove(state, move);
            if (moveGen.isThreateningKing(state)) {
                exe.unmakeMove(state, move);
            } else {
                mateThreat = false;
                value = -quiescenceSearch(eval, -beta, -alpha, moveLimit);
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
}
