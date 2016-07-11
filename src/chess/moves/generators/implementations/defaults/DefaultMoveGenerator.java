package chess.moves.generators.implementations.defaults;

import chess.ChessState;

public class DefaultMoveGenerator extends AbstractDefaultMoveGenerator {

    @Override
    protected long targetable(ChessState state) {
        return ~state.playerMasks[state.currentPlayer()];
    }
}
