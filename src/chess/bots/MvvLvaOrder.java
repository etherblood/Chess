package chess.bots;

import chess.ChessState;
import chess.moves.Move;
import chess.util.Piece;

/**
 *
 * @author Philipp
 */
public class MvvLvaOrder implements MoveOrder {

    private static final int[] SCORES;

    static {
        SCORES = new int[16];
        SCORES[Piece.W_PAWN] = 1;
        SCORES[Piece.W_KING] = 100;
        SCORES[Piece.W_BISHOP] = 3;
        SCORES[Piece.W_KNIGHT] = 3;
        SCORES[Piece.W_ROOK] = 5;
        SCORES[Piece.W_QUEEN] = 9;

        SCORES[Piece.B_PAWN] = 1;
        SCORES[Piece.B_KING] = 100;
        SCORES[Piece.B_BISHOP] = 3;
        SCORES[Piece.B_KNIGHT] = 3;
        SCORES[Piece.B_ROOK] = 5;
        SCORES[Piece.B_QUEEN] = 9;
    }

    private final ChessState state;

    public MvvLvaOrder(ChessState state) {
        this.state = state;
    }

    @Override
    public void sort(Move[] buffer, int from, int to) {
        int firstQuiet = partition(buffer, from, to);
        selectionSort(buffer, from, firstQuiet);
    }

    private int partition(Move[] buffer, int from, int to) {
        int store = from;
        for (int i = from; i < to; i++) {
            if (buffer[i].capture != Piece.EMPTY || Piece.isValidPromotion(buffer[i].info)) {
                swap(buffer, i, store);
                store++;
            }
        }
        return store;
    }
    
    private void selectionSort(Move[] buffer, int from, int to) {
        for (int i = from; i < to; i++) {
            Move move = buffer[i];
            move.score = 16 * SCORES[move.capture] - SCORES[state.pieces[move.from]];
        }
        for (int i = from; i < to - 1; i++) {
            int best = i;
            for (int j = i + 1; j < to; j++) {
                if(buffer[best].score < buffer[j].score) {
                    best = j;
                }
            }
            swap(buffer, i, best);
        }
    }

    private void swap(Move[] buffer, int a, int b) {
        Move tmp = buffer[a];
        buffer[a] = buffer[b];
        buffer[b] = tmp;
    }

}
