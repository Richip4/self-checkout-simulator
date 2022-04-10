package tests.gui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import GUI.GUI;
import GUI.Scenes;
import application.AppControl;
import application.Main;
import user.Attendant;
import user.Customer;

public class GUITest
{
	private AppControl ac;
	@Before
	public void setup() {
		Main.main(null);
		ac = new AppControl();
	}

	@Test
	public void initTest() {
		
		//GUI.init(ac);
		assertTrue(GUI.getAc() == ac);
		//JFrame window = GUI.getScenes().getScene(Scenes.SC_OVERVIEW);
		//window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}
	
	@Test 
	public void newUserCustomerTest() {
		assertTrue(GUI.newUser(AppControl.CUSTOMER));
		assertTrue(GUI.getAc().getActiveUser()==new Customer());
	}
	
	@Test
	public void newUserOneAttendantTest() {
		assertTrue(GUI.newUser(AppControl.ATTENDANT));
		assertTrue(GUI.getAc().getActiveUser()==new Attendant());
		}



}
