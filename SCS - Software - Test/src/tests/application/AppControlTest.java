package tests.application;

import static org.junit.Assert.*;

import org.junit.Test;

import application.AppControl;
import application.Main;
import user.Attendant;
import user.Customer;

public class AppControlTest {

	@Test
	public void testConstructor() {
		Main m = new Main();
		m.main(null);
		AppControl ac = new AppControl();
		System.out.println(ac.getActiveUser());
		assertTrue(true);
	}

	@Test
	public void testAddNewCustomer() {
		Main m = new Main();
		m.main(null);
		AppControl ac = new AppControl();
		ac.addNewCustomer();
		ac.getActiveUser();
		assertTrue(ac.getActiveUser() instanceof Customer);
	}

	@Test
	public void testAddNewAttendant() {
		Main m = new Main();
		m.main(null);
		AppControl ac = new AppControl();
		ac.addNewAttendant();
		ac.getActiveUser();
		assertTrue(ac.getActiveUser() instanceof Attendant);
	}

	@Test
	public void testNextActiveUser() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testPrevActiveUser() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetActiveUser() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetUserAt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetActiveUsers() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCustomerUsesStation() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testAttendantUsesStation() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCustomerLeavesStation() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testAttendantLeavesStation() {
		fail("Not yet implemented"); // TODO
	}

}
