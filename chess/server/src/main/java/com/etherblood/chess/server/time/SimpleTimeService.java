package com.etherblood.chess.server.time;

import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 *
 * @author Philipp
 */
@Profile("!test")
@Service
public class SimpleTimeService implements TimeService {

    @Override
    public DateTime now() {
        return new DateTime();
    }

    @Override
    public boolean sleep(long time, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(time));
        } catch (InterruptedException ex) {
            return false;
        }
        return true;
    }
    
}
