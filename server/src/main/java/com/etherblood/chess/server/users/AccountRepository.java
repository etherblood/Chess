package com.etherblood.chess.server.users;

import com.etherblood.chess.server.persistence.AbstractRepository;
import com.etherblood.chess.server.users.model.Account;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Philipp
 */
@Repository
public class AccountRepository extends AbstractRepository {

    public Account findByHandle(String loginHandle) {
//        from(QAcc)
        throw new RuntimeException();
    }
}
