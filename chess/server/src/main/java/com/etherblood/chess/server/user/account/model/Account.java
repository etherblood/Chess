package com.etherblood.chess.server.user.account.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.etherblood.chess.server.persistence.MutableEntity;

/**
 *
 * @author Philipp
 */
@Entity
public class Account extends MutableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Type(type = "pg-uuid")
	private UUID id;

	@NotNull
	private String username;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", username=" + username + "]";
	}

}
