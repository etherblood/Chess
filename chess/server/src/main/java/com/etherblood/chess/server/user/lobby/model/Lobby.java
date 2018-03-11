package com.etherblood.chess.server.user.lobby.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.etherblood.chess.server.persistence.MutableEntity;
import com.etherblood.chess.server.user.account.model.Account;

@Entity
public class Lobby extends MutableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Type(type = "pg-uuid")
	private UUID id;
	
	@Column(unique = true, nullable = false)
	private String name;
	
	@NotNull
	@ManyToOne
	private Account owner;

	@NotNull
	private boolean isPublic;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "Lobby [id=" + id + ", name=" + name + ", ownerId=" + owner.getId() + ", isPublic=" + isPublic + "]";
	}

}
