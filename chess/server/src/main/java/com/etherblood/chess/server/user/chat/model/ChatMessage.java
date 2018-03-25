package com.etherblood.chess.server.user.chat.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.etherblood.chess.server.persistence.ImmutableEntity;
import com.etherblood.chess.server.persistence.PostgreSQLEnumType;
import com.etherblood.chess.server.user.account.model.Account;

@Entity
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class ChatMessage extends ImmutableEntity {

	private static final long serialVersionUID = 1L;
	@Id
	private UUID id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Account sender;

	@NotNull
	@Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
	private ReceiverType receiverType;

	@NotNull
	private UUID receiver_id;

	@NotNull
	private String messageText;

	public String getText() {
		return messageText;
	}

	public void setText(String text) {
		this.messageText = text;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Account getSender() {
		return sender;
	}

	public void setSender(Account sender) {
		this.sender = sender;
	}

	public ReceiverType getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(ReceiverType receiverType) {
		this.receiverType = receiverType;
	}

	public UUID getReceiverId() {
		return receiver_id;
	}

	public void setReceiverId(UUID receiverId) {
		this.receiver_id = receiverId;
	}

}
