package com.etherblood.chess.server.user.chat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.authentication.UserContextService;
import com.etherblood.chess.server.user.chat.model.ChatMessage;
import com.etherblood.chess.server.user.lobby.model.Lobby;

@Service
public class ChatService {

	private final ChatMessageRepository chatMessageRepo;
	private final UserContextService userContextService;

	@Autowired
	public ChatService(ChatMessageRepository chatMessageRepo, UserContextService userContextService) {
		this.chatMessageRepo = chatMessageRepo;
		this.userContextService = userContextService;
	}

	public List<ChatMessage> getLobbyMessages(UUID lobbyId) {
		return chatMessageRepo.getLobbyMessages(lobbyId);
	}

	public List<ChatMessage> getDirectMessages(UUID accountId, Date olderThan, int limit) {
		return chatMessageRepo.getDirectMessages(accountId, userContextService.currentUserId(), olderThan, limit);
	}

	@Transactional
	public ChatMessage sendLobbyMessage(UUID lobbyId, String text) {
		return persistChatMessage(chatMessageRepo.proxyById(Lobby.class, lobbyId), null, text);
	}

	@Transactional
	public ChatMessage sendDirectMessage(UUID accountId, String text) {
		return persistChatMessage(null, chatMessageRepo.proxyById(Account.class, accountId), text);
	}

	private ChatMessage persistChatMessage(Lobby lobby, Account account, String text) {
		UUID currentUserId = userContextService.currentUserId();
		ChatMessage message = new ChatMessage();
		message.setId(UUID.randomUUID());
		message.setSender(chatMessageRepo.proxyById(Account.class, currentUserId));
		message.setReceiverLobby(lobby);
		message.setReceiverAccount(account);
		message.setText(text);
		chatMessageRepo.persist(message);
		return message;
	}
}
