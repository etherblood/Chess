package com.etherblood.chess.server.users.authentication;

import com.etherblood.chess.server.users.authentication.model.GuestAuthentication;
import com.etherblood.chess.server.users.authentication.model.UserAuthority;
import com.etherblood.chess.server.users.authentication.model.UserLoginDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Philipp
 */
@Service
public class UserContextService {

    private static final UUID DEFAULT_UUID = UUID.fromString("c1c2cad2-2f9a-4e25-ba46-14872851c5dd");//TODO: remove
    private final static Logger LOG = LoggerFactory.getLogger(UserContextService.class);
    private final Map<UUID, UserLoginDetails> userLogins = new ConcurrentHashMap<>();

    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public UserContextService(PasswordEncoder passwordEncoder
//            , AuthenticationProvider authenticationProvider
    ) {
        this.passwordEncoder = passwordEncoder;
//        this.authenticationProvider = authenticationProvider;
    }

    public void addLogin(UUID id, String loginHandle, String plaintextPassword, List<UserAuthority> authorities) {
        userLogins.put(id, new UserLoginDetails(id, loginHandle, passwordEncoder.encode(plaintextPassword), authorities));
        LOG.info("created new UserLogin for {}", loginHandle);
    }

    public UUID currentUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserLoginDetails) {
            return ((UserLoginDetails) principal).getUserId();
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

    public void guestLogin(String name) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserLoginDetails userLogin = new UserLoginDetails(UUID.randomUUID(), "anon", "none", new ArrayList<>());
        Authentication authentication = new GuestAuthentication(UUID.randomUUID(), name);//new UsernamePasswordAuthenticationToken(userLogin, userLogin.getPassword());
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        LOG.info("started session for {}", userLogin.getUserId());
    }
}
