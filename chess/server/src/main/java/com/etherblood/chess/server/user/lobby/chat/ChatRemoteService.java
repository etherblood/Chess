package com.etherblood.chess.server.user.lobby.chat;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etherblood.chess.api.chat.MessageTo;
import com.etherblood.chess.server.poll.PollService;
import com.etherblood.chess.server.user.lobby.LobbyService;
import com.etherblood.chess.server.user.lobby.chat.model.ChatMessage;
import com.etherblood.chess.server.user.lobby.events.NewMessageEvent;

@RestController
public class ChatRemoteService {
	
	private final ChatService chatService;
	private final PollService pollService;
	private final LobbyService lobbyService;

	@Autowired
	public ChatRemoteService(ChatService chatService, PollService pollService, LobbyService lobbyService) {
		this.chatService = chatService;
		this.pollService = pollService;
		this.lobbyService = lobbyService;
	}

	@RequestMapping("/account/{accountId}/messages")
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public List<MessageTo> getDirectMessages(@PathVariable UUID accountId, @RequestParam Date olderThan, @RequestParam int limit) {
		return chatService.getDirectMessages(accountId, olderThan, limit).stream().map(this::makeTo).collect(Collectors.toList());
	}

	@RequestMapping("/lobby/{lobbyId}/messages")
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public List<MessageTo> getLobbyMessages(@PathVariable UUID lobbyId, @RequestParam Date olderThan, @RequestParam int limit) {
		return chatService.getLobbyMessages(lobbyId, olderThan, limit).stream().map(this::makeTo).collect(Collectors.toList());
	}

	@RequestMapping(path="/lobby/{lobbyId}/send", method=RequestMethod.POST)
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public void sendLobbyMessage(@PathVariable UUID lobbyId, @RequestParam String text) {
		ChatMessage message = chatService.sendLobbyMessage(lobbyId, text);
		pollService.sendEvent(new NewMessageEvent(makeTo(message)), lobbyService.lobbyAccountIds(lobbyId));
	}

	@RequestMapping(path="/account/{accountId}/send", method=RequestMethod.POST)
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public void sendDirectMessage(@PathVariable UUID accountId, @RequestParam String text) {
		ChatMessage message = chatService.sendDirectMessage(accountId, text);
		pollService.sendEvent(new NewMessageEvent(makeTo(message)), accountId::equals);
	}

	private MessageTo makeTo(ChatMessage message) {
		MessageTo to = new MessageTo();
		to.created = message.getCreated();
		to.sender = message.getSenderId();
		to.text = message.getText();
		return to;
	}
	
}
