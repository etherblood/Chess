package com.etherblood.chess.server.user.authentication;

import com.etherblood.chess.server.user.account.AccountRepository;
import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.authentication.model.UserAuthority;
import com.etherblood.chess.server.user.authentication.model.UserDetailsImpl;

import java.util.EnumSet;
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
        return new UserDetailsImpl(account.getId(), account.getLoginHandle(), account.getPassword(), EnumSet.of(UserAuthority.PLAYER));
    }

}
