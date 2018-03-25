package com.etherblood.chess.api.match.events;

import java.util.UUID;

import com.etherblood.chess.api.PollEvent;

public class MatchStartedEvent extends PollEvent {

    public MatchStartedEvent(UUID matchId) {
        super("matchStartedEvent", matchId);
    }

}
