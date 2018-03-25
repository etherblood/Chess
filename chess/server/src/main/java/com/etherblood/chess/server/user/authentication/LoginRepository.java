package com.etherblood.chess.server.user.authentication;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.etherblood.chess.server.persistence.AbstractRepository;
import com.etherblood.chess.server.user.authentication.model.Login;
import com.etherblood.chess.server.user.authentication.model.QLogin;

/**
 *
 * @author Philipp
 */
@Repository
public class LoginRepository extends AbstractRepository {

    private static final QLogin qLogin = QLogin.login;

    public Login findByAccountId(UUID accountId) {
        return from(qLogin)
                .where(qLogin.account.id.eq(accountId))
                .uniqueResult(qLogin);
    }

    public Login findByHandle(String loginHandle) {
        return from(qLogin)
                .where(qLogin.loginHandle.equalsIgnoreCase(loginHandle))
                .uniqueResult(qLogin);
    }
}
