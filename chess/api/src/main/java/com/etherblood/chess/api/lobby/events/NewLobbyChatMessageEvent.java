package com.etherblood.chess.api.lobby.events;

import java.util.UUID;

import com.etherblood.chess.api.PollEvent;
import com.etherblood.chess.api.chat.ChatMessageTo;
import com.etherblood.chess.api.util.MapBuilder;

public class NewLobbyChatMessageEvent extends PollEvent {

    public NewLobbyChatMessageEvent(UUID lobbyId, ChatMessageTo message) {
        super("newLobbyChatMessageEvent", new MapBuilder<String, Object>().with("lobbyId", lobbyId).with("message", message).build());
    }

}
