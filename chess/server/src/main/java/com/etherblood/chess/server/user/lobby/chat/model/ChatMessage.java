package com.etherblood.chess.server.user.lobby.chat.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.etherblood.chess.server.persistence.ImmutableEntity;

@Entity
public class ChatMessage extends ImmutableEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	private UUID id;
	
	@NotNull
	private String text;

	private UUID senderId;
	
	@Enumerated(EnumType.STRING)
	private ReceiverType receiverType;

	@NotNull
	private UUID receiverId;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public UUID getSenderId() {
		return senderId;
	}

	public void setSenderId(UUID senderId) {
		this.senderId = senderId;
	}

	public ReceiverType getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(ReceiverType receiverType) {
		this.receiverType = receiverType;
	}

	public UUID getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(UUID receiverId) {
		this.receiverId = receiverId;
	}
}
