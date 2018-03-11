package com.etherblood.chess.server.user.lobby.chat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etherblood.chess.server.user.authentication.UserContextService;
import com.etherblood.chess.server.user.lobby.LobbyService;
import com.etherblood.chess.server.user.lobby.chat.model.ChatMessage;
import com.etherblood.chess.server.user.lobby.chat.model.ReceiverType;

@Service
public class ChatService {

	private final ChatMessageRepository chatMessageRepo;
	private final UserContextService userContextService;
	private final LobbyService lobbyService;

	@Autowired
	public ChatService(ChatMessageRepository chatMessageRepo, UserContextService userContextService,
			LobbyService lobbyService) {
		this.chatMessageRepo = chatMessageRepo;
		this.userContextService = userContextService;
		this.lobbyService = lobbyService;
	}

	public List<ChatMessage> getLobbyMessages(UUID lobbyId, Date olderThan, int limit) {
		if (!lobbyService.isAccountMemberOfLobby(userContextService.currentUserId(), lobbyId)) {
			throw new AccessDeniedException("can't read messages of lobby without membership.");
		}
		return chatMessageRepo.getLobbyMessages(lobbyId, olderThan, limit);
	}

	public List<ChatMessage> getDirectMessages(UUID accountId, Date olderThan, int limit) {
		return chatMessageRepo.getDirectMessages(accountId, userContextService.currentUserId(), olderThan, limit);
	}

	@Transactional
	public ChatMessage sendLobbyMessage(UUID lobbyId, String text) {
		return persistChatMessage(lobbyId, ReceiverType.LOBBY, text);
	}

	@Transactional
	public ChatMessage sendDirectMessage(UUID accountId, String text) {
		return persistChatMessage(accountId, ReceiverType.ACCOUNT, text);
	}

	private ChatMessage persistChatMessage(UUID receiverId, ReceiverType receiverType, String text) {
		UUID currentUserId = userContextService.currentUserId();
		ChatMessage message = new ChatMessage();
		message.setId(UUID.randomUUID());
		message.setSenderId(currentUserId);
		message.setReceiverId(receiverId);
		message.setReceiverType(receiverType);
		message.setText(text);
		chatMessageRepo.persist(message);
		return message;
	}
}
