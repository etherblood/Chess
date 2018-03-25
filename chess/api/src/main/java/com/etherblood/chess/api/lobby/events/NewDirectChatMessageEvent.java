package com.etherblood.chess.api.lobby.events;

import java.util.UUID;

import com.etherblood.chess.api.PollEvent;
import com.etherblood.chess.api.chat.ChatMessageTo;
import com.etherblood.chess.api.util.MapBuilder;

public class NewDirectChatMessageEvent extends PollEvent {

    public NewDirectChatMessageEvent(UUID lobbyId, ChatMessageTo message) {
        super("newDirectChatMessageEvent", new MapBuilder<String, Object>().with("lobbyId", lobbyId).with("message", message).build());
    }

}
