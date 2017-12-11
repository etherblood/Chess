package com.etherblood.chess.server.users;

import com.etherblood.chess.server.users.model.Account;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author Philipp
 */
@Service
public class UserService {

    private final static Logger LOG = LoggerFactory.getLogger(UserService.class);

    public UUID register(String loginHandle) {
        Objects.requireNonNull(loginHandle);
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setLoginHandle(loginHandle);
        account.setPassword(UUID.randomUUID().toString());
        LOG.info("created new account {}", loginHandle);
        return account.getId();
    }
//
//    public User getUser(UUID userId) {
//        return users.get(userId);
//    }
//
//    public Collection<User> getAllUsers() {
//        return users.values();
//    }

}
