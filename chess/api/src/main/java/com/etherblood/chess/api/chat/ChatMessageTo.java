package com.etherblood.chess.api.chat;

import java.util.Date;
import java.util.UUID;

public class ChatMessageTo {
	public UUID id;
	public String text;
	public UUID senderId;
	public Date created;
}
