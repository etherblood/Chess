package com.etherblood.chess.server.user.lobby.events;

import com.etherblood.chess.api.chat.MessageTo;
import com.etherblood.chess.server.poll.PollEvent;

public class NewMessageEvent extends PollEvent {

    public NewMessageEvent(MessageTo message) {
        super("newMessageEvent", message);
    }

}
