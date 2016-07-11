package chess.moves.generators.implementations;

import chess.moves.Move;
import chess.util.Piece;


public abstract class AbstractPawnMoveGenerator extends AbstractMoveGenerator {

    protected int generatePromotions(int from, int to, int capture, int currentPlayer, Move[] buffer, int offset) {
        Move move = buffer[offset++];
        move.to = to;
        move.from = from;
        move.capture = capture;
        move.info = Piece.queen(currentPlayer);

        move = buffer[offset++];
        move.to = to;
        move.from = from;
        move.capture = capture;
        move.info = Piece.knight(currentPlayer);

        move = buffer[offset++];
        move.to = to;
        move.from = from;
        move.capture = capture;
        move.info = Piece.rook(currentPlayer);

        move = buffer[offset++];
        move.to = to;
        move.from = from;
        move.capture = capture;
        move.info = Piece.bishop(currentPlayer);
        return offset;
    }

}
