package GUI;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Application.AppControl;

public class GUI {

	private AppControl ac;
	private Scenes scenes = new Scenes(this);
	
	public GUI(AppControl ac) {
		this.ac = ac;
		
		// Initializes the openning scene, Self-Checkout Overview 
		scenes.getScene(Scenes.SC_OVERVIEW);
		
		// prompt the user to reply with what type of user they are
		ac.addNewUser((promptForUserType() == 0) ? AppControl.CUSTOMER : AppControl.ATTENDANT);
	}
	
	/**
	 * 
	 * @param station - the specific station index
	 */
	public void userApproachesStation(int station) {
		if (ac.getActiveUser().getType() == AppControl.CUSTOMER) {
			if (station == 0) {
				errorMsg("You are not authorized to view the attendant station.");
			} else if (ac.getUserAt(station) == AppControl.ATTENDANT) {
				errorMsg("Station being serviced");
			} else if (ac.getUserAt(station) == AppControl.CUSTOMER ||
					   ac.getUserAt(station) == AppControl.BOTH) {
				errorMsg("A customer is already using this station");
			} else {
				System.out.println("Station " + station);
				ac.customerUsesStation(station);
				scenes.getScene(Scenes.SCS_OVERVIEW);
			}
		} else if (ac.getActiveUser().getType() == AppControl.ATTENDANT) {
			ac.attendantUsesStation(station);
		}
	}
	
	public void userLeavesStation(int station) {
		
	}
	
	// main for testing GUI
	public static void main(String[] args) {
		new GUI(new AppControl());
	}
	
	private static void errorMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, null, JOptionPane.WARNING_MESSAGE);
	}
	
	private static int promptForUserType() {
		String[] userTypes = {"Customer", "Attendant" };
		return JOptionPane.showOptionDialog(null, "Are you a Customer or Attendant?", 
				"User?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, userTypes, 0); 
	}
}
