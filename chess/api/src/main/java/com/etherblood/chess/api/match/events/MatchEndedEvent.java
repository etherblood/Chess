package com.etherblood.chess.api.match.events;

import com.etherblood.chess.api.PollEvent;
import com.etherblood.chess.api.match.events.data.MatchEndedTo;

public class MatchEndedEvent extends PollEvent<MatchEndedTo> {

    public static final String KEY = "matchEndedEvent";

	public MatchEndedEvent(MatchEndedTo data) {
        super(KEY, data);
    }

}
