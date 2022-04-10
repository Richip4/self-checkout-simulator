package tests.gui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.Scenes;
import application.Main;

public class ScenesTest
{
	final Scenes scenes = new Scenes();
	
	@Before
	public void setup() {
		Main.initializeCardAndIssuers();
	    Main.initializeProductDatabase();
	    Main.initializeStore();
	    Main.initializeMembership();
	    Main.initializeCredentialsSytem();
	}
	@Test 
	public void DimmingTest() {
				
		assertTrue(Scenes.getFilterFrame().getHeight()==Scenes.getYresolution());
		assertTrue(Scenes.getFilterFrame().getWidth()==Scenes.getXresolution());

		assertTrue(Scenes.getFilterFrame().isResizable()==(false));
		assertTrue(Scenes.getFilterFrame().isUndecorated()==(true));
		//assertTrue(Scenes.getFilterFrame().getLocation()==Scenes.getFilterFrame().getCenterPoint());
		
		
		//assertTrue(Scenes.getFilterFrame().getComponents()[0].getBackground()==Color.black);
		
		assertTrue(Scenes.getFilterFrame().getOpacity()==((float) 0.75));
		assertTrue(Scenes.getFilterFrame().getFocusableWindowState() == (false));
	}
	
	@Test 
	public void currentStationTest() {
		scenes.setCurrentStation(0);
		assertTrue(scenes.getCurrentStation()==0);
	}
	
//	@Test
//	public void getSceneSCOverviewTest() {
//		Main.initializeCardAndIssuers();
//	    Main.initializeProductDatabase();
//	    Main.initializeStore();
//	    Main.initializeMembership();
//	    Main.initializeCredentialsSytem();
//		JFrame window = scenes.getScene(Scenes.AS_TOUCH);
//		assertTrue(window.isVisible()==true);
//		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
//	}
	
	@Test
	public void stationAttendantOptionsTest() {
		JFrame window = scenes.stationAttendantOptions();
		assertTrue(window.isVisible() == true);
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}
	
	
	
	
	


}
