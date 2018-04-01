package com.etherblood.chess.server.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test.xml" })
public class TestBase {

	@Autowired
	private TestDbManager testDbManager;
	
	@Before
	public void cleanDatabase() {
		testDbManager.cleanDb();
	}
}
