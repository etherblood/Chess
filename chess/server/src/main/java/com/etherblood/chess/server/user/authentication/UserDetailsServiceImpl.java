package com.etherblood.chess.server.user.authentication;

import java.util.EnumSet;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.etherblood.chess.server.user.authentication.model.Login;
import com.etherblood.chess.server.user.authentication.model.UserAuthority;
import com.etherblood.chess.server.user.authentication.model.UserDetailsImpl;

/**
 *
 * @author Philipp
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final LoginRepository loginRepo;

    public UserDetailsServiceImpl(LoginRepository loginRepo) {
        this.loginRepo = loginRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String loginHandle) throws UsernameNotFoundException {
        Login login = loginRepo.findByHandle(loginHandle);
        if(login == null) {
            throw new UsernameNotFoundException(loginHandle);
        }
        return new UserDetailsImpl(login.getAccount().getId(), login.getLoginHandle(), login.getPassword(), EnumSet.of(UserAuthority.PLAYER));
    }

}
