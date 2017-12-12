package com.etherblood.chess.server.users.authentication;

import com.etherblood.chess.server.users.authentication.model.UserDetailsImpl;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Philipp
 */
@Service
public class UserContextService {

    private final static Logger LOG = LoggerFactory.getLogger(UserContextService.class);

    public UUID currentUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) principal).getUserId();
        }
        throw new IllegalStateException(String.valueOf(principal));
    }

//    public void login(String loginHandle, String password) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        if (authentication == null) {
////            return;
////        }
//        if (authentication instanceof UsernamePasswordAuthenticationToken) {
//            UserLoginDetails userLogin = new UserLoginDetails(((UserLoginDetails) authentication.getCredentials()).getUserId(), loginHandle, password, new ArrayList<>());
//            authentication = new UsernamePasswordAuthenticationToken(userLogin, userLogin.getPassword());
//            authenticationProvider.authenticate(authentication);
//            LOG.info("authentication updated {} - {}", loginHandle, userLogin.getUserId());
//        }
//    }
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
//    public void login(String loginHandle, String password) {
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        UserDetails userDetails = new UserDetailsImpl(DEFAULT_UUID, loginHandle, password, authorities)
//        UserLoginDetails userLogin = new UserLoginDetails(UUID.randomUUID(), "anon", "none", new ArrayList<>());
//        Authentication authentication = new GuestAuthentication(UUID.randomUUID(), name);//new UsernamePasswordAuthenticationToken(userLogin, userLogin.getPassword());
//        context.setAuthentication(authentication);
//        SecurityContextHolder.setContext(context);
//        LOG.info("started session for {}", userLogin.getUserId());
//    }
}
