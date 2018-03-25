package com.etherblood.chess.api.match.events;

import java.util.UUID;

import com.etherblood.chess.api.PollEvent;

public class MatchRequestedEvent extends PollEvent {

    public MatchRequestedEvent(UUID matchId) {
        super("matchRequestedEvent", matchId);
    }

}
