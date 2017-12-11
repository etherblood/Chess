package com.etherblood.chess.server.users.authentication;

import com.etherblood.chess.server.users.AccountRepository;
import com.etherblood.chess.server.users.authentication.model.UserAuthority;
import com.etherblood.chess.server.users.authentication.model.UserDetailsImpl;
import com.etherblood.chess.server.users.model.Account;
import java.util.Arrays;
import java.util.HashSet;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Philipp
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepo;

    public UserDetailsServiceImpl(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String loginHandle) throws UsernameNotFoundException {
        Account account = accountRepo.findByHandle(loginHandle);
        if(account == null) {
            throw new UsernameNotFoundException(loginHandle);
        }
        return new UserDetailsImpl(account.getId(), account.getLoginHandle(), account.getPassword(), new HashSet<>(Arrays.asList(UserAuthority.PLAYER)));
    }

}
