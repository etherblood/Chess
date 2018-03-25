package com.etherblood.chess.api.lobby.events;

import java.util.UUID;

import com.etherblood.chess.api.PollEvent;
import com.etherblood.chess.api.util.MapBuilder;

public class NewLobbyMemberEvent extends PollEvent {

    public NewLobbyMemberEvent(UUID lobbyId, UUID memberId) {
        super("newLobbyMemberEvent", new MapBuilder<String, Object>().with("lobbyId", lobbyId).with("memberId", memberId).build());
    }

}
