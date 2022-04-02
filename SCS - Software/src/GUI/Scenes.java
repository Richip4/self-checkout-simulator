package GUI;

import javax.swing.JFrame;

public class Scenes {
	
	public final int SC_OVERVIEW 		= 0;
	public final int AS_TOUCH 			= 1;
	public final int SCS_OVERVIEW 		= 2;
	public final int SCS_TOUCH 			= 3;
	public final int SCS_KEYBOARD 		= 4;
	public final int SCS_CARDREADER 	= 5;
	public final int SCS_MAINTENANCE 	= 6;
	
	public JFrame getScene(int scene) {
		switch (scene) {
		case SC_OVERVIEW:
			return null;
		case AS_TOUCH:
			return null;
		case SCS_OVERVIEW:
			return null;
		case SCS_TOUCH:
			return null;
		case SCS_KEYBOARD:
			return null;
		case SCS_CARDREADER:
			return null;
		case SCS_MAINTENANCE:
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
