package com.etherblood.chess.server.user.account;

import com.etherblood.chess.server.persistence.AbstractRepository;
import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.account.model.QAccount;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

/**
 *
 * @author Philipp
 */
@Repository
public class AccountRepository extends AbstractRepository {

    private static final QAccount qAccount = QAccount.account;

    public List<Account> getAccountsByIds(List<UUID> ids) {
        return from(qAccount).where(qAccount.id.in(ids)).list(qAccount);
    }

    public Account getAccountById(UUID id) {
        return from(qAccount).where(qAccount.id.eq(id)).uniqueResult(qAccount);
    }
}
