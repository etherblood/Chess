package com.etherblood.chess.api.match.events;

import java.util.Map;
import java.util.UUID;

import com.etherblood.chess.api.PollEvent;
import com.etherblood.chess.api.match.moves.ChessMove;
import com.etherblood.chess.api.util.MapBuilder;

public class MatchMoveEvent extends PollEvent<Map<UUID, ChessMove>> {

    public static final String KEY = "matchMoveEvent";

	public MatchMoveEvent(UUID matchId, ChessMove selected) {
        this(new MapBuilder<UUID, ChessMove>().with(matchId, selected).build());
    }

    public MatchMoveEvent(Map<UUID, ChessMove> moves) {
        super(KEY, moves);
    }

}
