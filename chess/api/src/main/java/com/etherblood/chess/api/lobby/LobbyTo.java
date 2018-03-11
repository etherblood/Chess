package com.etherblood.chess.api.lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.etherblood.chess.api.chat.MessageTo;
import com.etherblood.chess.api.player.PlayerTo;

public class LobbyTo {
	public UUID id;
	public String name;
	public List<PlayerTo> members = new ArrayList<>();
	public List<MessageTo> messages = new ArrayList<>();
}
