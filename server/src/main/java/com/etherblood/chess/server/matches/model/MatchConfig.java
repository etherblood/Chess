package com.etherblood.chess.server.matches.model;

import java.util.UUID;

/**
 *
 * @author Philipp
 */
public class MatchConfig {

    private UUID whitePlayer, blackPlayer;

    public UUID getWhitePlayer() {
        return whitePlayer;
    }

    public UUID getBlackPlayer() {
        return blackPlayer;
    }
}
