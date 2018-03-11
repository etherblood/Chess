package com.etherblood.chess.server.user.lobby.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.etherblood.chess.server.persistence.ImmutableEntity;
import com.etherblood.chess.server.user.account.model.Account;

@Entity
public class LobbyMembership extends ImmutableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Type(type = "pg-uuid")
	private UUID id;

	@NotNull
	@ManyToOne
	private Lobby lobby;

	@NotNull
	@ManyToOne
	private Account account;

	public Lobby getLobby() {
		return lobby;
	}

	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "LobbyMembership [id=" + id + ", lobby=" + lobby + ", accountId=" + account.getId() + "]";
	}

}
