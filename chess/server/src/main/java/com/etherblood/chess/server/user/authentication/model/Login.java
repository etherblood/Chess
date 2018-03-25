package com.etherblood.chess.server.user.authentication.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.etherblood.chess.server.persistence.MutableEntity;
import com.etherblood.chess.server.user.account.model.Account;

@Entity
public class Login extends MutableEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@Type(type = "pg-uuid")
	private UUID id;
	@Column(unique = true, nullable = false)
	private String loginHandle;
	@NotNull
	private String password;
	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	private Account account;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getLoginHandle() {
		return loginHandle;
	}

	public void setLoginHandle(String loginHandle) {
		this.loginHandle = loginHandle;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Login [id=" + id + ", loginHandle=" + loginHandle + ", password=*, accountId=" + account.getId() + "]";
	}
}
