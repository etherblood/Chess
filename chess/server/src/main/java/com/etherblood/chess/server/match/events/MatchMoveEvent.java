package com.etherblood.chess.server.match.events;

import com.etherblood.chess.api.match.moves.ChessMove;
import com.etherblood.chess.engine.moves.Move;
import com.etherblood.chess.server.match.ChessModelConverter;
import com.etherblood.chess.server.poll.PollEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MatchMoveEvent extends PollEvent {

    public MatchMoveEvent(UUID matchId, Move selected) {
        this(createData(matchId, selected));
    }

    public MatchMoveEvent(Map<UUID, ChessMove> moves) {
        super("matchMoveEvent", moves);
    }

    private static Map<UUID, ChessMove> createData(UUID matchId, Move selected) {
        Map<UUID, ChessMove> result = new HashMap<>();
        result.put(matchId, ChessModelConverter.convertMove(selected));
        return result;
    }

}
