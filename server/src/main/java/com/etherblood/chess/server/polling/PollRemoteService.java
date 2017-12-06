package com.etherblood.chess.server.polling;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 *
 * @author Philipp
 */
@RestController
@RequestMapping("/poll")
public class PollRemoteService {

    private final PollService pollService;

    @Autowired
    public PollRemoteService(PollService pollService) {
        this.pollService = pollService;
    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @RequestMapping(path = "/subscribe", method = RequestMethod.POST)
    public long subscribe() {
        return pollService.subscribe();
    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @ResponseBody
    @RequestMapping("/")
    public DeferredResult<List<PollEvent>> poll(@CookieValue("clientId") long clientId) {
        return pollService.poll(clientId);
    }
}
