package com.etherblood.chess.server.user.chat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.etherblood.chess.server.persistence.AbstractRepository;
import com.etherblood.chess.server.user.chat.model.ChatMessage;
import com.etherblood.chess.server.user.chat.model.QChatMessage;

@Repository
public class ChatMessageRepository extends AbstractRepository {

	private static final QChatMessage qMessage = QChatMessage.chatMessage;

	public List<ChatMessage> getLobbyMessages(UUID lobbyId) {
		return from(qMessage)
				.where(qMessage.receiver_lobby.id.eq(lobbyId))
				.orderBy(qMessage.created.desc())
				.list(qMessage);
	}

	public List<ChatMessage> getDirectMessages(UUID firstAccountId, UUID secondAccountId, Date olderThan, int limit) {
		return from(qMessage)
				.where(qMessage.receiver_account.id.eq(firstAccountId).and(qMessage.sender.id.eq(secondAccountId))
						.or(qMessage.receiver_account.id.eq(secondAccountId).and(qMessage.sender.id.eq(firstAccountId))))
				.where(qMessage.created.before(olderThan))
				.limit(limit)
				.orderBy(qMessage.created.desc())
				.list(qMessage);
	}

}
