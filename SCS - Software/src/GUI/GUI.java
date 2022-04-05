package GUI;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Application.AppControl;
import Application.Main.Tangibles;
import user.Customer;
import user.User;

public class GUI {

	private AppControl ac;
	private Scenes scenes = new Scenes(this);
	
	public GUI(AppControl ac) {
		this.ac = ac;
		
		// Initializes the openning scene, Self-Checkout Overview 
		scenes.getScene(Scenes.SC_OVERVIEW);
		
		// prompt the user to reply with what type of user they are
		int newUserType = (promptForUserType() == 0) ? AppControl.CUSTOMER : AppControl.ATTENDANT;
		newUser(newUserType);
	}
	
	/**
	 * checks what kind of user to add to the simulation.
	 * only one attendant can be allowed at one time
	 * 
	 * @param newUserType
	 */
	private void newUser(int newUserType) {
		if (newUserType == AppControl.CUSTOMER) {
			ac.addNewCustomer();
		} else if (newUserType == AppControl.ATTENDANT) {
			// check list of users for an existing attendant
			ac.getActiveUsers().forEach(u -> {
				if (u.getUserType() == AppControl.ATTENDANT) {
					errorMsg("An Attendant is already on duty. Sorry.");
					return;
				}
			});
			
			// no attendant found, add a new one
			ac.addNewAttendant();
		}
	}

	/**
	 * checks if the provided station is free to use by the
	 * active user before letting them proceed.
	 * @param station - the specific station index
	 */
	public void userApproachesStation(int station) {
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			if (station == 0) {
				errorMsg("You are not authorized to view the attendant station.");
			} else if (ac.getUserAt(station).getUserType() == AppControl.ATTENDANT) {
				errorMsg("Station being serviced");
			} else if (ac.getUserAt(station).getUserType() == AppControl.CUSTOMER) {
				errorMsg("A customer is already using this station");
			} else {
				System.out.println("Station " + station);
				ac.customerUsesStation(station);
				scenes.getScene(Scenes.SCS_OVERVIEW);
			}
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			ac.attendantUsesStation(station);
			scenes.getScene(Scenes.SCS_OVERVIEW);
		}
	}
	
//	// main for testing GUI
//	public static void main(String[] args) {
//		new GUI(new AppControl(Tangibles.SUPERVISION_STATION));
//	}
	
	public void userLeavesStation(int station) {
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			ac.customerLeavesStation(station);
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			ac.attendantLeavesStation(station);
		}
	}

	//
	public void userBagsItem(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public void userInsertsBanknote(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public void userRemovesBanknote(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public void userServicesStation(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public void userInsertsCoin(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}
	
	public void userRemovesCoins(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public void userPlacesItemOnWeighScale(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public void userScansItem(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public void userRemovesReceipt(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public void userAccessCardReader(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public void userAccessTouchscreen(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
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
