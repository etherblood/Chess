package com.etherblood.chess.server.matches.model;

import java.util.UUID;

/**
 *
 * @author Philipp
 */
public class MatchObject<T> {

    private final UUID matchId;
    private final T object;

    public MatchObject(UUID matchId, T object) {
        this.matchId = matchId;
        this.object = object;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public T getObject() {
        return object;
    }
}
