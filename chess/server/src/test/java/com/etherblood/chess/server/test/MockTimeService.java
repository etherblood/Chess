package com.etherblood.chess.server.test;

import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.etherblood.chess.server.time.TimeService;

@Service
@Profile("test")
public class MockTimeService implements TimeService {

	private DateTime now;

	public MockTimeService() {
		reset();
	}

	@Override
	public DateTime now() {
		return now;
	}

	public void setNow(DateTime time) {
		now = time;
	}

	public void reset() {
		now = new DateTime();
	}

	@Override
	public boolean sleep(long time, TimeUnit unit) {
		now = now.plusMillis(Math.toIntExact(unit.toMillis(time)));
		return true;
	}
}
