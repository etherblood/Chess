package com.etherblood.chess.server.user.chat;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etherblood.chess.api.chat.ChatMessageTo;
import com.etherblood.chess.api.lobby.events.NewDirectChatMessageEvent;
import com.etherblood.chess.api.lobby.events.NewLobbyChatMessageEvent;
import com.etherblood.chess.server.poll.PollService;
import com.etherblood.chess.server.user.chat.model.ChatMessage;
import com.etherblood.chess.server.user.lobby.LobbyService;

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
	public List<ChatMessageTo> getDirectMessages(@PathVariable UUID accountId, @RequestParam Date olderThan, @RequestParam int limit) {
		return chatService.getDirectMessages(accountId, olderThan, limit).stream().map(this::makeTo).collect(Collectors.toList());
	}

	@RequestMapping("/lobby/{lobbyId}/messages")
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public List<ChatMessageTo> getLobbyMessages(@PathVariable UUID lobbyId) {
		return chatService.getLobbyMessages(lobbyId).stream().map(this::makeTo).collect(Collectors.toList());
	}

	@RequestMapping(path="/lobby/{lobbyId}/send", consumes= {MediaType.APPLICATION_JSON_VALUE}, method=RequestMethod.POST)
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public void sendLobbyMessage(@PathVariable UUID lobbyId, @RequestBody ChatMessageTo to) {
		ChatMessage message = chatService.sendLobbyMessage(lobbyId, to.text);
		pollService.sendEvent(new NewLobbyChatMessageEvent(lobbyId, makeTo(message)), lobbyService.lobbyAccountIds(lobbyId));
	}

	@RequestMapping(path="/account/{accountId}/send", consumes= {MediaType.APPLICATION_JSON_VALUE}, method=RequestMethod.POST)
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public void sendDirectMessage(@PathVariable UUID accountId, @RequestBody ChatMessageTo to) {
		ChatMessage message = chatService.sendDirectMessage(accountId, to.text);
		pollService.sendEvent(new NewDirectChatMessageEvent(accountId, makeTo(message)), accountId::equals);
	}

	private ChatMessageTo makeTo(ChatMessage message) {
		ChatMessageTo to = new ChatMessageTo();
		to.created = message.getCreated();
		to.senderId = message.getSender().getId();
		to.text = message.getText();
		return to;
	}
	
}
