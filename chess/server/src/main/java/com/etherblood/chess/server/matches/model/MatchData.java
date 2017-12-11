package com.etherblood.chess.server.matches.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Philipp
 */
public class MatchData {

    private UUID id;
    private MatchConfig config;
    private Date created;
    private Collection<MatchMove> moves;
    private Collection<UUID> spectators;

    public MatchData() {
    }

    public MatchData(UUID id, MatchConfig config, Date created) {
        this.id = id;
        this.config = config;
        this.created = created;
        this.moves = new ConcurrentLinkedQueue<>();
        this.spectators = Collections.newSetFromMap(new ConcurrentHashMap<UUID, Boolean>());
        this.spectators.add(config.getWhitePlayer());
        this.spectators.add(config.getBlackPlayer());
    }

    public UUID getId() {
        return id;
    }

    public MatchConfig getConfig() {
        return config;
    }

    public Date getCreated() {
        return created;
    }

    public Collection<MatchMove> getMoves() {
        return moves;
    }

    public Collection<UUID> getSpectators() {
        return spectators;
    }
}
