package chess;

/**
 *
 * @author Philipp
 */
public final class ChessState {
    private static final int MAX_MOVES = 2000;//TODO: some games will crash because they take to long
    public final long[] playerMasks = new long[2];
    public final long[] pieceMasks = new long[16];
    public final int[] pieces = new int[64];
    public int moveCounter;
    public final HistoryState[] history = new HistoryState[MAX_MOVES];

    public ChessState() {
        for (int i = 0; i < MAX_MOVES; i++) {
            history[i] = new HistoryState();
        }
    }
    
    public int currentPlayer() {
        return moveCounter & 1;
    }
    
    public int opponentPlayer() {
        return ~moveCounter & 1;
    }
    
    public HistoryState currentHistory() {
        return history[moveCounter];
    }
    
    public HistoryState nextHistory() {
        return history[moveCounter + 1];
    }
    
    public long allPieces() {
        return playerMasks[0] | playerMasks[1];
    }
}