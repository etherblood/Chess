package com.etherblood.chess.api.lobby.events;

import java.util.Map;
import java.util.UUID;

import com.etherblood.chess.api.PollEvent;
import com.etherblood.chess.api.util.MapBuilder;

public class NewLobbyMemberEvent extends PollEvent<Map<String, Object>> {

    public static final String KEY = "newLobbyMemberEvent";

	public NewLobbyMemberEvent(UUID lobbyId, UUID memberId) {
        super(KEY, new MapBuilder<String, Object>().with("lobbyId", lobbyId).with("memberId", memberId).build());
    }

}
