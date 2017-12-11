package com.etherblood.chess.server.users;

import com.etherblood.chess.server.persistence.AbstractRepository;
import com.etherblood.chess.server.users.model.Account;
import com.etherblood.chess.server.users.model.QAccount;

import org.springframework.stereotype.Repository;

/**
 *
 * @author Philipp
 */
@Repository
public class AccountRepository extends AbstractRepository {

	private static final QAccount qAccount = QAccount.account;

	public Account findByHandle(String loginHandle) {
		return from(qAccount)
				.where(qAccount.loginHandle.equalsIgnoreCase(loginHandle))
				.uniqueResult(qAccount);
	}
}
