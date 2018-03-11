package com.etherblood.chess.server.user.authentication;

import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.authentication.model.UserAuthority;
import com.etherblood.chess.server.user.authentication.model.UserDetailsImpl;

/**
 *
 * @author Philipp
 */
@Service
public class UserContextService {

    private final static Logger LOG = LoggerFactory.getLogger(UserContextService.class);

    public boolean hasCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        return principal instanceof UserDetailsImpl;
    }

    public UUID currentUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) principal).getUserId();
        }
        throw new IllegalStateException(String.valueOf(principal));
    }

    public void forceLogin(Account account, Set<UserAuthority> authorities) {
        String password = null;
        UserDetailsImpl userLogin = new UserDetailsImpl(account.getId(), account.getLoginHandle(), password, authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userLogin, userLogin.getPassword(), authorities);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        LOG.info("authentication updated {} - {}", account, authorities);
    }
//    @Override
//    public UserDetails loadUserByUsername(String loginHandle) throws UsernameNotFoundException {
//        for (UserLoginDetails value : userLogins.values()) {
//            if (value.getUsername().equalsIgnoreCase(loginHandle)) {
//                return value;
//            }
//        }
//        throw new UsernameNotFoundException(loginHandle);
//    }
//
//    public void forceLogin(String loginHandle, String password) {
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        UserDetails userDetails = new UserDetailsImpl(DEFAULT_UUID, loginHandle, password, authorities)
//        UserLoginDetails userLogin = new UserLoginDetails(UUID.randomUUID(), "anon", "none", new ArrayList<>());
//        Authentication authentication = new GuestAuthentication(UUID.randomUUID(), name);//new UsernamePasswordAuthenticationToken(userLogin, userLogin.getPassword());
//        context.setAuthentication(authentication);
//        SecurityContextHolder.setContext(context);
//        LOG.info("started session for {}", userLogin.getUserId());
//    }
}
