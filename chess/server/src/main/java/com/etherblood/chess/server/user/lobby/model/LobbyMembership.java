package com.etherblood.chess.server.user.lobby.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.etherblood.chess.server.persistence.MutableEntity;
import com.etherblood.chess.server.persistence.PostgreSQLEnumType;
import com.etherblood.chess.server.user.account.model.Account;

@Entity
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class LobbyMembership extends MutableEntity {

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

	@NotNull
	@Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
	private MembershipType membershipType;

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

	public MembershipType getType() {
		return membershipType;
	}

	public void setType(MembershipType type) {
		this.membershipType = type;
	}

	@Override
	public String toString() {
		return "LobbyMembership [id=" + id + ", lobbyId=" + lobby.getId() + ", accountId=" + account.getId() + ", type=" + membershipType + "]";
	}

}
