package com.etherblood.chess.server;

import java.util.Date;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Philipp
 */
@RestController
public class TestRemoteService {

    @PreAuthorize(value = "permitAll()")
    @RequestMapping(path = "/public/test", method = RequestMethod.GET)
    public String test() {
        return new Date().toString();
    }
}
