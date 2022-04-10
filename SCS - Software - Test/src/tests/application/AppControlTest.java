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
		Main m = new Main();
		m.main(null);
		AppControl ac = new AppControl();
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
	@Test
	public void testcustomerTapsCreditCard() {
		// TODO Auto-generated method stub
		
	}
	@Test
	public void testcustomerTapsDebitCard() {
		// TODO Auto-generated method stub
		
	}
	@Test
	public void testcustomerTapsMembershipCard() {
		// TODO Auto-generated method stub
		
	}
	@Test
	public void testcustomerSwipesCreditCard() {
		// TODO Auto-generated method stub
		
	}
	@Test
	public void testcustomerSwipesDebitCard() {
		// TODO Auto-generated method stub
		
	}
	@Test
	public void testcustomerSwipesMembershipCard() {
		// TODO Auto-generated method stub
		
	}
	@Test
	public void testcustomerInsertCreditCard() {
		// TODO Auto-generated method stub
		
	}
	@Test
	public void testcustomerInsertDebitCard() {
		// TODO Auto-generated method stub
		
	}
	@Test
	public void testcustomerInsertMembershipCard() {
		// TODO Auto-generated method stub
		
	}
	@Test
	public void testremoveFromCart() {
		// TODO Auto-generated method stub
		
	}
	@Test
	public void testattendantLogin() {
		// TODO Auto-generated method stub
		
	}
	@Test
	public void testattendantPassword() {
		// TODO Auto-generated method stub
		
	}
	@Test
	public void testgetCart() {
		// TODO Auto-generated method stub
		
	}
	@Test
	public void testAttendantIsLoggedIn() {
		// TODO Auto-generated method stub
		
	}
	
	@Test
	public void testCustomerNextItem() {
		// TODO Auto-generated method stub
		
	}

}
