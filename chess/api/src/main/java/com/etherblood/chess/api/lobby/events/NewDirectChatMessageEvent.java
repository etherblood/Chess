package com.etherblood.chess.api.lobby.events;

import java.util.Map;
import java.util.UUID;

import com.etherblood.chess.api.PollEvent;
import com.etherblood.chess.api.chat.ChatMessageTo;
import com.etherblood.chess.api.util.MapBuilder;

public class NewDirectChatMessageEvent extends PollEvent<Map<String, Object>> {

    public static final String KEY = "newDirectChatMessageEvent";

	public NewDirectChatMessageEvent(UUID lobbyId, ChatMessageTo message) {
        super(KEY, new MapBuilder<String, Object>().with("lobbyId", lobbyId).with("message", message).build());
    }

}
