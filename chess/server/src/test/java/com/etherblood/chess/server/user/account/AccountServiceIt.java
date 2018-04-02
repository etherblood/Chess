package com.etherblood.chess.server.user.account;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.etherblood.chess.server.test.TestBase;
import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.authentication.model.Login;

public class AccountServiceIt extends TestBase {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountRepository accountRepo;
	
	@Test
	public void register() {
		String username = "loginHandle";
		Login login = accountService.register(username);
		Account account = accountRepo.getAccountById(login.getAccount().getId());
		assertEquals(username, account.getUsername());
	}
}
