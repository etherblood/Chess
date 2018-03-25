package com.etherblood.chess.server.match.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.etherblood.chess.server.persistence.ImmutableEntity;
import com.etherblood.chess.server.user.account.model.Account;

@Entity
public class MatchRequest extends ImmutableEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@Type(type = "pg-uuid")
	private UUID id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Account player;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private ChessMatch match;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Account getPlayer() {
		return player;
	}

	public void setPlayer(Account player) {
		this.player = player;
	}

	public ChessMatch getMatch() {
		return match;
	}

	public void setMatch(ChessMatch match) {
		this.match = match;
	}

	@Override
	public String toString() {
		return "MatchRequest[id=" + id + ", playerId=" + player.getId() + ", matchId=" + match.getId() + "]";
	}

}
