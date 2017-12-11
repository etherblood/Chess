package com.etherblood.chess.server.users;

import java.util.UUID;
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

    @Autowired
    public UserRemoteService(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize(value = "permitAll()")
    @RequestMapping(path = "/public/user/register", method = RequestMethod.POST)
    public UUID register(@RequestParam String loginHandle) {
        UUID id = userService.register(loginHandle);
        return id;
    }
}
