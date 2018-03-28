package com.etherblood.chess.server.poll;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.etherblood.chess.api.PollEvent;

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
    public int subscribe() {
        return pollService.subscribe(Instant.now());
    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @ResponseBody
    @RequestMapping("/{clientId}")
    public DeferredResult<List<PollEvent<?>>> poll(@PathVariable("clientId") int clientId) {
        return pollService.poll(clientId);
    }
}
