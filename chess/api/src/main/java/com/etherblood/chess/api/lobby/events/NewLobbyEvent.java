package com.etherblood.chess.api.lobby.events;

import com.etherblood.chess.api.PollEvent;
import com.etherblood.chess.api.lobby.LobbyTo;

public class NewLobbyEvent extends PollEvent<LobbyTo> {

	public static final String KEY = "newLobbyEvent";

	public NewLobbyEvent(LobbyTo data) {
		super(KEY, data);
	}

}
