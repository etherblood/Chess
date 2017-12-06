package com.etherblood.chess.server.matches.events;

import com.etherblood.chess.server.matches.model.CandidateMove;
import com.etherblood.chess.server.matches.model.MatchObject;
import com.etherblood.chess.server.polling.PollEvent;
import java.util.UUID;

/**
 *
 * @author Philipp
 */
public class MatchMoveEvent extends PollEvent {

    public MatchMoveEvent(UUID matchId, CandidateMove data) {
        super("matchMove", new MatchObject<>(matchId, data));
    }
}
