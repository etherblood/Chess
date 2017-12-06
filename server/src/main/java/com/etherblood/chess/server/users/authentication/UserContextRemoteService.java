package com.etherblood.chess.server.users.authentication;

import com.etherblood.chess.server.polling.PollService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Philipp
 */
@RestController
@RequestMapping("/auth")
public class UserContextRemoteService {

    private final UserContextService userContextService;
    private final PollService pollService;

    public UserContextRemoteService(UserContextService userContextService, PollService pollService) {
        this.userContextService = userContextService;
        this.pollService = pollService;
    }
    
    @RequestMapping(path = "/login/guest", method = RequestMethod.POST)
    public ModelAndView guestLogin(@RequestParam String name, HttpServletResponse response) {
        userContextService.guestLogin(name);
        response.addCookie(new Cookie("clientId", Long.toString(pollService.subscribe())));
        return new ModelAndView("redirect:/sandbox.html");
    }

//    @PreAuthorize(value = "permitAll()")
//    @RequestMapping(path = "/login", method = RequestMethod.POST)
//    public void login(@RequestParam String email, @RequestParam String password) {
//        userContextService.login(email, password);
//    }
}
