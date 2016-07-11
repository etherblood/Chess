package chess.moves.generators.implementations.defaults;

import chess.ChessState;

public final class DefaultCaptureGenerator extends AbstractDefaultMoveGenerator {

    @Override
    protected long targetable(ChessState state) {
        return state.playerMasks[state.opponentPlayer()];
    }
}
