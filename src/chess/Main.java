package chess;

import chess.moves.handlers.MoveExecutor;
import chess.moves.Move;
import chess.moves.generators.MoveGenerator;
import chess.util.Board;
import chess.util.Piece;

/**
 *
 * @author Philipp
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        test3();
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
            if(result[i] == 0) {
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
            if(result[i] == 0) {
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
            if(result2[i] == 0) {
                continue;
            }
            System.out.println(buffer[i].toString() + " " + result2[i]);
        }
        new ChessPrinter().print(state.pieces);
//        new ChessPrinter().printMoves(state.pieces, buffer, p, offset);
        for (int i = 0; i < offset; i++) {
            if(buffer[i].from == Board.G4) {
                answer = buffer[i];
//                break;
            }
        }
        exec.makeMove(state, answer);
        offset = gen.generateMoves(state, buffer, 0);
        
        long[] result3 = new long[offset];
        new Perft().divide(state, buffer, result3, 2);
        for (int i = 0; i < result3.length; i++) {
            if(result3[i] == 0) {
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
