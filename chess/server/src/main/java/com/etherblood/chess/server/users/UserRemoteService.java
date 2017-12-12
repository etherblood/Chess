package com.etherblood.chess.server.users;

import com.etherblood.chess.server.users.authentication.UserContextService;
import com.etherblood.chess.server.users.authentication.model.UserAuthority;
import java.util.EnumSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Philipp
 */
@RestController
public class UserRemoteService {

    private final UserService userService;
    private final UserContextService userContextService;

    @Autowired
    public UserRemoteService(UserService userService, UserContextService userContextService) {
        this.userService = userService;
        this.userContextService = userContextService;
    }

    @PreAuthorize(value = "permitAll()")
    @RequestMapping(path = "/user/register", method = RequestMethod.POST)
    public String register(@RequestParam String loginHandle) {
        userContextService.forceLogin(userService.register(loginHandle), EnumSet.of(UserAuthority.PLAYER));
        throw new UnsupportedOperationException("redirect:/private/sandbox.html");
    }

}
