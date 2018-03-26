package com.etherblood.chess.api.match.events;

import com.etherblood.chess.api.PollEvent;
import com.etherblood.chess.api.match.MatchRequestTo;

public class MatchRequestedEvent extends PollEvent {

    public MatchRequestedEvent(MatchRequestTo request) {
        super("matchRequestedEvent", request);
    }

}
