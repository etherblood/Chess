package com.etherblood.chess.server.user.lobby.chat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.etherblood.chess.server.persistence.AbstractRepository;
import com.etherblood.chess.server.user.lobby.chat.model.ChatMessage;
import com.etherblood.chess.server.user.lobby.chat.model.QChatMessage;
import com.etherblood.chess.server.user.lobby.chat.model.ReceiverType;

@Repository
public class ChatMessageRepository extends AbstractRepository {

	private static final QChatMessage qMessage = QChatMessage.chatMessage;

	public List<ChatMessage> getLobbyMessages(UUID lobbyId, Date olderThan, int limit) {
		return from(qMessage)
				.where(qMessage.receiverType.eq(ReceiverType.LOBBY))
				.where(qMessage.receiverId.eq(lobbyId))
				.where(qMessage.created.before(olderThan))
				.limit(limit)
				.orderBy(qMessage.created.desc())
				.list(qMessage);
	}

	public List<ChatMessage> getDirectMessages(UUID firstAccountId, UUID secondAccountId, Date olderThan, int limit) {
		return from(qMessage)
				.where(qMessage.receiverType.eq(ReceiverType.ACCOUNT))
				.where(qMessage.receiverId.eq(firstAccountId).and(qMessage.senderId.eq(secondAccountId))
						.or(qMessage.receiverId.eq(secondAccountId).and(qMessage.senderId.eq(firstAccountId))))
				.where(qMessage.created.before(olderThan))
				.limit(limit)
				.orderBy(qMessage.created.desc())
				.list(qMessage);
	}

}
