package com.etherblood.chess.api.lobby;

import java.util.List;
import java.util.UUID;

import com.etherblood.chess.api.chat.ChatMessageTo;

public class LobbyDetailsTo {
	public UUID id;
	public List<UUID> memberIds;
	public List<ChatMessageTo> messages;
}
