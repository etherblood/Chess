package com.etherblood.chess.api.match.events;

import java.util.UUID;

import com.etherblood.chess.api.PollEvent;

public class MatchStartedEvent extends PollEvent<UUID> {

    public static final String KEY = "matchStartedEvent";

	public MatchStartedEvent(UUID matchId) {
        super(KEY, matchId);
    }

}
