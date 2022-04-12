package tests.application;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import application.Main;

public class MainTest {


	@Test
	public void testGetStore() {
		Main m = new Main();
		m.main(null);
		assertTrue(m.getStore()==null);
	}

}
