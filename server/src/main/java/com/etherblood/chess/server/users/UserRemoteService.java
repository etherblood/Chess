package com.etherblood.chess.server.users;

import com.etherblood.chess.server.users.authentication.UserContextService;
import com.etherblood.chess.server.users.authentication.model.UserAuthority;
import java.util.Arrays;
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
@RequestMapping("/user")
public class UserRemoteService {

    private final UserService userService;
    private final UserContextService userContextService;

    @Autowired
    public UserRemoteService(UserService userService, UserContextService userContextService) {
        this.userService = userService;
        this.userContextService = userContextService;
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public UUID createUser(@RequestParam String username) {
        UUID id = userService.createUser(username);
        userContextService.addLogin(id, username, "password", Arrays.asList(UserAuthority.PLAYER));
        return id;
    }

//    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
//    @RequestMapping(path = "/list")
//    public List<UUID> getAllUsers() {
//        return userService.getAllUsers().stream().map(User::getId).collect(Collectors.toList());
//    }
}
