package GUI;

import javax.swing.JFrame;

public class Scenes {
	
	public static int SC_OVERVIEW 		= 0;
	public static int AS_TOUCH 			= 1;
	public static int SCS_OVERVIEW 		= 2;
	public static int SCS_TOUCH 			= 3;
	public static int SCS_KEYBOARD 		= 4;
	public static int SCS_CARDREADER 	= 5;
	public static int SCS_MAINTENANCE 	= 6;
	
	public JFrame getScene(int scene) {
		if (scene == SC_OVERVIEW) {
			
			return null;
		} else if (scene == AS_TOUCH) {
			
			return null;
		} else if (scene == SCS_OVERVIEW) {
			
			return null;
		} else if (scene == SCS_TOUCH) {
			
			return null;
		} else if (scene == SCS_KEYBOARD) {
			
			return null;
		} else if (scene == SCS_CARDREADER) {
			
			return null;
		} else if (scene == SCS_MAINTENANCE) {
			
			return null;
		}
		
		return null;
	}

	// Self-Checkout Overview Scene
	
	// Attendant Station Touch Screen Scene
	
	// Self-Checkout Station Overview Scene
	
	// Self-Checkout Station Touch Screen Scene
	
	// Self-Checkout Station Keyboard Scene
	
	// Self-Checkout Station Card Reader Scene
	
	// Self-Checkout Station Maintenance Scene
	
}
