package chess;

import chess.bots.Bot;
import chess.bots.PvsTableBot;
import chess.bots.evaluations.PstEvaluation;
import chess.moves.handlers.MoveExecutor;
import chess.moves.Move;
import chess.moves.generators.MoveGenerator;
import chess.transpositions.PerftTranspositionTable;
import chess.util.Board;
import chess.util.Piece;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Philipp
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        botGame();
    }

    private static void perft() {
        PerftTranspositionTable table = new PerftTranspositionTable(26);
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.reset(state);
        Move[] buffer = MoveGenerator.createBuffer(500);
        Perft instance = new Perft(table);
        long millis = -System.currentTimeMillis();
        System.out.println("perft(7): " + instance.perft(state, buffer, 0, 7));
        millis += System.currentTimeMillis();
        System.out.println(millis + "ms");
        long used = table.used();
        long size = table.size();
        System.out.println(used + "/" + size);
        System.out.println(100 * used / size + "%");
        
        millis = -System.currentTimeMillis();
        System.out.println("perft(8): " + instance.perft(state, buffer, 0, 8));
        millis += System.currentTimeMillis();
        System.out.println(millis + "ms");
        used = table.used();
        size = table.size();
        System.out.println(used + "/" + size);
        System.out.println(100 * used / size + "%");
    }

    private static void botGame() {
        ChessPrinter printer = new ChessPrinter();
        ChessState state = new ChessState();
        ChessSetup setup = new ChessSetup();
        setup.reset(state);
        printer.print(state.pieces);
        System.out.println("your turn");
        MoveGenerator gen = new MoveGenerator();
        MoveExecutor exe = new MoveExecutor();
        Bot bot = new PvsTableBot(new PstEvaluation());
        bot.setState(state);
        Move[] buffer = MoveGenerator.createBuffer(500);
        
        for (int i = 0; i < 5; i++) {
            System.out.println("computing...");
            Move botMove = bot.compute();
            System.out.println(botMove.toString());
            exe.makeMove(state, botMove);
            printer.print(state.pieces);
        }
        if(true)return;
        
        try (Scanner s = new Scanner(System.in)) {
            String line;
            while (!(line = s.nextLine()).equals("exit")) {
                try {
                    String[] parts = line.split(" ");
                    int from = parseSquare(parts[0]);
                    int to = parseSquare(parts[1]);
                    List<Move> candidates = new ArrayList<>();
                    int moves = gen.generateMoves(state, buffer, 0);
                    for (int i = 0; i < moves; i++) {
                        Move move = buffer[i];
                        if (move.from == from && move.to == to) {
                            candidates.add(move);
                        }
                    }

                    if (candidates.size() > 1) {
                        int promotion = -1;
                        if (parts.length > 2) {
                            promotion = parsePiece(parts[2].charAt(0));
                        }
                        for (int i = candidates.size() - 1; i >= 0; i--) {
                            if (candidates.get(i).info != promotion) {
                                candidates.remove(i);
                            }
                        }
                    }

                    if (candidates.size() != 1) {
                        System.out.println("no unique move found");
                        throw new IllegalStateException();
                    }

                    exe.makeMove(state, candidates.get(0));
                    
                    if(gen.isThreateningKing(state)) {
                        exe.unmakeMove(state, candidates.get(0));
                        System.out.println("can't leave your king checked");
                        throw new IllegalStateException();
                    }
                    
                    printer.print(state.pieces);
                    System.out.println("computing...");
                    Move botMove = bot.compute();
                    System.out.println(botMove.toString());
                    exe.makeMove(state, botMove);
                    printer.print(state.pieces);
                    System.out.println("your turn");

                } catch (Throwable t) {
                    System.out.println("invalid input");
                    t.printStackTrace(System.out);
                }
            }
        }
    }

    private static int parseSquare(String sq) {
        return Board.square(sq.charAt(0) - 'a', sq.charAt(1) - '1');
    }

    private static int parsePiece(char piece) {
        switch (piece) {
            case 'p':
                return Piece.B_PAWN;
            case 'k':
                return Piece.B_KING;
            case 'b':
                return Piece.B_BISHOP;
            case 'n':
                return Piece.B_KNIGHT;
            case 'r':
                return Piece.B_ROOK;
            case 'q':
                return Piece.B_QUEEN;
            case 'P':
                return Piece.W_PAWN;
            case 'K':
                return Piece.W_KING;
            case 'B':
                return Piece.W_BISHOP;
            case 'N':
                return Piece.W_KNIGHT;
            case 'R':
                return Piece.W_ROOK;
            case 'Q':
                return Piece.W_QUEEN;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private static void test3() {
        long millis = System.currentTimeMillis();
        ChessState state = new ChessState();
        new ChessSetup().fromFen(state, "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq –");//reset(state);
        MoveGenerator gen = new MoveGenerator();
        Move[] buffer = MoveGenerator.createBuffer(500);
        int offset = gen.generateMoves(state, buffer, 0);
        long[] result = new long[offset];
        new Perft().divide(state, buffer, result, 5);
        long sum = 0;
        for (int i = 0; i < result.length; i++) {
            if (result[i] == 0) {
                continue;
            }
            sum += result[i];
            System.out.println(buffer[i].toString() + " " + result[i]);
        }
        System.out.println("total: " + sum);
        millis = System.currentTimeMillis() - millis;
        System.out.println(millis + "ms");
    }

    private static void test2() {
        ChessState state = new ChessState();
        new ChessSetup().fromFen(state, "8/p7/8/1P6/K1k3p1/6P1/7P/8 w - -");
        Move[] buffer = MoveGenerator.createBuffer(500);
        MoveGenerator gen = new MoveGenerator();
        int offset = gen.generateMoves(state, buffer, 0);
        long[] result = new long[offset];
        new Perft().divide(state, buffer, result, 3);
        for (int i = 0; i < result.length; i++) {
            if (result[i] == 0) {
                continue;
            }
            System.out.println(buffer[i].toString() + " " + result[i]);
        }
        MoveExecutor exec = new MoveExecutor();
        System.out.println("");
        offset = gen.generateMoves(state, buffer, 0);
        Move sM = buffer[4];
        Move dM = buffer[6];
        Move answer = null;
        exec.makeMove(state, sM);
        offset = gen.generateMoves(state, buffer, 0);
        long[] result2 = new long[offset];
        new Perft().divide(state, buffer, result2, 2);
        for (int i = 0; i < result2.length; i++) {
            if (result2[i] == 0) {
                continue;
            }
            System.out.println(buffer[i].toString() + " " + result2[i]);
        }
        new ChessPrinter().print(state.pieces);
//        new ChessPrinter().printMoves(state.pieces, buffer, p, offset);
        for (int i = 0; i < offset; i++) {
            if (buffer[i].from == Board.G4) {
                answer = buffer[i];
//                break;
            }
        }
        exec.makeMove(state, answer);
        offset = gen.generateMoves(state, buffer, 0);

        long[] result3 = new long[offset];
        new Perft().divide(state, buffer, result3, 2);
        for (int i = 0; i < result3.length; i++) {
            if (result3[i] == 0) {
                continue;
            }
            System.out.println(buffer[i].toString() + " " + result3[i]);
        }

//        new ChessPrinter().printMoves(state.pieces, buffer, 0, offset);
        new ChessPrinter().print(state.pieces);
        System.out.println("a");
    }

    private static void test1() {
        ChessPrinter printer = new ChessPrinter();
        ChessState state = new ChessState();
        new ChessSetup().reset(state);
        printer.print(state.pieces);
        Move[] buffer = new Move[1000];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = new Move();
        }
        int offset = new MoveGenerator().generateMoves(state, buffer, 0);
        printer.printMoves(state.pieces, buffer, 0, offset);

        new MoveExecutor().makeMove(state, buffer[3]);
        printer.print(state.pieces);
        offset = new MoveGenerator().generateMoves(state, buffer, 0);
        printer.printMoves(state.pieces, buffer, 0, offset);

        new MoveExecutor().makeMove(state, buffer[3]);
        printer.print(state.pieces);
        offset = new MoveGenerator().generateMoves(state, buffer, 0);
        printer.printMoves(state.pieces, buffer, 0, offset);
    }

}
