package com.etherblood.chess.server.user.chat.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.etherblood.chess.server.persistence.ImmutableEntity;
import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.lobby.model.Lobby;

@Entity
public class ChatMessage extends ImmutableEntity {

	private static final long serialVersionUID = 1L;
	@Id
	private UUID id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Account sender;

	@ManyToOne(fetch = FetchType.LAZY)
	private Lobby receiver_lobby;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Account receiver_account;

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
		return receiver_account == null? ReceiverType.LOBBY: ReceiverType.ACCOUNT;
	}

	public Lobby getReceiverLobby() {
		return receiver_lobby;
	}

	public void setReceiverLobby(Lobby lobby) {
		this.receiver_lobby = lobby;
	}

	public Account getReceiverAccount() {
		return receiver_account;
	}

	public void setReceiverAccount(Account account) {
		this.receiver_account = account;
	}

}
