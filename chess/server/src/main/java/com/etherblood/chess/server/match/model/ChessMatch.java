package com.etherblood.chess.server.match.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.etherblood.chess.api.match.ChessResult;
import com.etherblood.chess.server.persistence.MutableEntity;
import com.etherblood.chess.server.persistence.PostgreSQLEnumType;
import com.etherblood.chess.server.user.account.model.Account;

@Entity
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class ChessMatch extends MutableEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@Type(type = "pg-uuid")
	private UUID id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Account white;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Account black;

	@Temporal(TemporalType.TIMESTAMP)
	private Date started;

	@Temporal(TemporalType.TIMESTAMP)
	private Date ended;

	@NotNull
	@Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
	private ChessResult result;
	
	@NotNull
	private String startFen;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Account getWhite() {
		return white;
	}

	public void setWhite(Account white) {
		this.white = white;
	}

	public Account getBlack() {
		return black;
	}

	public void setBlack(Account black) {
		this.black = black;
	}

	public Date getStarted() {
		return started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public Date getEnded() {
		return ended;
	}

	public void setEnded(Date ended) {
		this.ended = ended;
	}

	public String getStartFen() {
		return startFen;
	}

	public void setStartFen(String startFen) {
		this.startFen = startFen;
	}

	public ChessResult getResult() {
		return result;
	}

	public void setResult(ChessResult result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ChessMatch [id=" + id + ", white=" + white + ", black=" + black + ", started=" + started + ", ended="
				+ ended + ", result=" + result + ", startFen=" + startFen + "]";
	}

}
