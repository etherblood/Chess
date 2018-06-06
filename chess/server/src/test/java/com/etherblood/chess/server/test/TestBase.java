package com.etherblood.chess.server.test;

import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test.xml" })
public abstract class TestBase {

	@Autowired
	private TestDbManager testDbManager;
	@Autowired
	protected MockTimeService mockTime;
	
	@After
	public void cleanDatabase() {
		testDbManager.clearDb();
		mockTime.reset();
	}
}
