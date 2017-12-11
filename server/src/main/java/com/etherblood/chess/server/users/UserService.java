package com.etherblood.chess.server.users;

import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.etherblood.chess.server.users.model.Account;

/**
 *
 * @author Philipp
 */
@Service
public class UserService {

    private final static Logger LOG = LoggerFactory.getLogger(UserService.class);
    
    private final AccountRepository accountRepo;
    private final PasswordEncoder passwordEncoder;


	public UserService(AccountRepository accountRepo, PasswordEncoder passwordEncoder) {
		this.accountRepo = accountRepo;
		this.passwordEncoder = passwordEncoder;
	}


	public UUID register(String loginHandle) {
        Objects.requireNonNull(loginHandle);
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setLoginHandle(loginHandle);
        account.setPassword(passwordEncoder.encode("test"));//UUID.randomUUID().toString());
        accountRepo.persist(account);
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
