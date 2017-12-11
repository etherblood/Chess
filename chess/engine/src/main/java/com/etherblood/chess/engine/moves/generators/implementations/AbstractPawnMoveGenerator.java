package com.etherblood.chess.engine.moves.generators.implementations;

import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.engine.util.Piece;


public abstract class AbstractPawnMoveGenerator {

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
