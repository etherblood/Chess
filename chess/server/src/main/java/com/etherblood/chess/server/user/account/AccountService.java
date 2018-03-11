package com.etherblood.chess.server.user.account;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.authentication.UserContextService;

/**
 *
 * @author Philipp
 */
@Service
public class AccountService {

    private final static Logger LOG = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserContextService userContextService;

    public AccountService(AccountRepository accountRepo, PasswordEncoder passwordEncoder, UserContextService userContextService) {
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
        this.userContextService = userContextService;
    }

    @Transactional
    public Account register(String loginHandle) {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setLoginHandle(loginHandle);
        account.setPassword(passwordEncoder.encode("test"));//UUID.randomUUID().toString());
        accountRepo.persist(account);
        LOG.info("created new account(loginHandle={})", loginHandle);
        return account;
    }

    @Transactional
    public void setPassword(String plaintextPassword) {
        UUID currentUserId = userContextService.currentUserId();
        Account currentAccount = accountRepo.proxyById(Account.class, currentUserId);
        currentAccount.setPassword(passwordEncoder.encode(plaintextPassword));
        LOG.info("Changed password of account(id={})", currentUserId);
    }

    public List<Account> getAccountsByIds(List<UUID> ids) {
        return accountRepo.getAccountsByIds(ids);
    }

    public Account getAccountById(UUID id) {
        return accountRepo.getAccountById(id);
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
