package com.etherblood.chess.bot.client;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.etherblood.chess.api.PollEvent;
import com.etherblood.chess.api.lobby.events.NewDirectChatMessageEvent;
import com.etherblood.chess.api.lobby.events.NewLobbyChatMessageEvent;
import com.etherblood.chess.api.lobby.events.NewLobbyEvent;
import com.etherblood.chess.api.lobby.events.NewLobbyMemberEvent;
import com.etherblood.chess.api.match.events.MatchMoveEvent;
import com.etherblood.chess.api.match.events.MatchRequestedEvent;
import com.etherblood.chess.api.match.events.MatchStartedEvent;
import com.etherblood.chess.bot.client.model.UnknownPollEvent;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PollEventDeserializer implements JsonDeserializer<PollEvent<?>> {

	private final static Map<String, Class<? extends PollEvent<?>>> EVENT_TYPES;
	
	static {
		EVENT_TYPES = new HashMap<>();
		EVENT_TYPES.put(NewDirectChatMessageEvent.KEY, NewDirectChatMessageEvent.class);
		EVENT_TYPES.put(NewLobbyChatMessageEvent.KEY, NewLobbyChatMessageEvent.class);
		EVENT_TYPES.put(NewLobbyEvent.KEY, NewLobbyEvent.class);
		EVENT_TYPES.put(NewLobbyMemberEvent.KEY, NewLobbyMemberEvent.class);
		EVENT_TYPES.put(MatchMoveEvent.KEY, MatchMoveEvent.class);
		EVENT_TYPES.put(MatchRequestedEvent.KEY, MatchRequestedEvent.class);
		EVENT_TYPES.put(MatchStartedEvent.KEY, MatchStartedEvent.class);
	}
	
	@Override
	public PollEvent<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		String type = jsonObject.get("type").getAsString();
		return context.deserialize(json, EVENT_TYPES.getOrDefault(type, UnknownPollEvent.class));
	}

}
