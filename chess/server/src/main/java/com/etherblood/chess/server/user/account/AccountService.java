package com.etherblood.chess.server.user.account;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.authentication.LoginRepository;
import com.etherblood.chess.server.user.authentication.UserContextService;
import com.etherblood.chess.server.user.authentication.model.Login;

/**
 *
 * @author Philipp
 */
@Service
public class AccountService {

    private final static Logger LOG = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepo;
    private final LoginRepository loginRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserContextService userContextService;

    public AccountService(AccountRepository accountRepo, PasswordEncoder passwordEncoder, UserContextService userContextService, LoginRepository loginRepo) {
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
        this.userContextService = userContextService;
        this.loginRepo = loginRepo;
    }

    @Transactional
    public Login register(String loginHandle) {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setUsername(loginHandle);
        accountRepo.persist(account);
        
        Login login = new Login();
        login.setId(UUID.randomUUID());
        login.setAccount(account);
        login.setLoginHandle(loginHandle);
        login.setPassword(passwordEncoder.encode(""));//UUID.randomUUID().toString());
        loginRepo.persist(login);
        LOG.info("created new account(loginHandle={})", loginHandle);
        return login;
    }

    @Transactional
    public void setPassword(String plaintextPassword) {
        UUID currentUserId = userContextService.currentUserId();
        Login currentLogin = loginRepo.findByAccountId(currentUserId);
        currentLogin.setPassword(passwordEncoder.encode(plaintextPassword));
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
