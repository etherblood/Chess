package com.etherblood.chess.server.time;

import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

/**
 *
 * @author Philipp
 */
public interface TimeService {

    DateTime now();
    
    boolean sleep(long time, TimeUnit unit);
    
}
