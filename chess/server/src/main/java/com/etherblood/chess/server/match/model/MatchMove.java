package com.etherblood.chess.server.match.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.etherblood.chess.api.match.ChessSquare;
import com.etherblood.chess.api.match.moves.MoveType;
import com.etherblood.chess.server.persistence.ImmutableEntity;
import com.etherblood.chess.server.persistence.PostgreSQLEnumType;

@Entity
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class MatchMove extends ImmutableEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@Type(type = "pg-uuid")
	private UUID id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private ChessMatch match;

	@NotNull
	@Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
	@Column(name="movetype")
	private MoveType type;

	@NotNull
	@Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
	@Column(name="fromsquare")
	private ChessSquare from;

	@NotNull
	@Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
	@Column(name="tosquare")
	private ChessSquare to;

	@NotNull
	@Column(name="moveindex")
	private int index;

	public MatchMove() {
		super();
	}

	public MoveType getType() {
		return type;
	}

	public void setType(MoveType type) {
		this.type = type;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ChessMatch getMatch() {
		return match;
	}

	public void setMatch(ChessMatch match) {
		this.match = match;
	}

	public ChessSquare getFrom() {
		return from;
	}

	public void setFrom(ChessSquare from) {
		this.from = from;
	}

	public ChessSquare getTo() {
		return to;
	}

	public void setTo(ChessSquare to) {
		this.to = to;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "MatchMove [id=" + id + ", matchId=" + match.getId() + ", type=" + type + ", from=" + from + ", to=" + to
				+ ", index=" + index + "]";
	}

}
