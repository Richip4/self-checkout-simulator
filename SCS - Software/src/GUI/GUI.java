package GUI;

import javax.swing.JOptionPane;

import application.AppControl;
import application.Main.Tangibles;
import software.SelfCheckoutSoftware.Phase;
import user.User;

public class GUI {

	private AppControl ac;
	private Scenes scenes = new Scenes(this);
	
	public GUI(AppControl ac) {
		this.ac = ac;
		
		// Initializes the openning scene, Self-Checkout Overview 
		scenes.getScene(Scenes.SC_OVERVIEW);
	}
	
	/**
	 * checks what kind of user to add to the simulation.
	 * only one attendant can be allowed at one time
	 * 
	 * @param newUserType
	 */
	public void newUser(int newUserType) {
		if (newUserType == AppControl.CUSTOMER) {
			ac.addNewCustomer();
		} else if (newUserType == AppControl.ATTENDANT) {
			// check list of users for an existing attendant
			User[] users = ac.getActiveUsers();
			
			for (int i = 0; i < users.length; i++) {
				if (users[i] != null && users[i].getUserType() == AppControl.ATTENDANT) {
					errorMsg("An Attendant is already on duty. Sorry.");
					return;
				}
			}
			
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
			if (station == 0) { // this is the attendant's station
				errorMsg("You are not authorized to view the attendant station.");
			} else if (ac.getUserAt(station) != null) { 
				if (ac.getUserAt(station).getUserType() == AppControl.ATTENDANT) {
					errorMsg("Station being serviced");
				} else if (ac.getUserAt(station).getUserType() == AppControl.CUSTOMER) {
					errorMsg("A customer is already using this station");
				} 
			} else {
				System.out.println("Station " + station);
				ac.customerUsesStation(station);
				scenes.getScene(Scenes.SCS_OVERVIEW);
			}
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			if (station == 0) { // this is the attendant's station
				ac.attendantUsesSupervisionStation();
				scenes.getScene(Scenes.AS_TOUCH);
			} else {
				ac.attendantUsesStation(station);
				scenes.getScene(Scenes.SCS_OVERVIEW);				
			}
		}
	}

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
	

	public void attendantLogsOut() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * @param station
	 * @return
	 */
	public String stationStatus(int station) {

		return ac.getStationState(station);
	}

	/**
	 * 
	 * @param station
	 */
	public void attendantBlockToggle(int station) {
		
		ac.toggleBlock(station);
	}

	/**
	 * 
	 * @param station
	 */
	public void attendantApproveStation(int station) {
		
		ac.approveStationDiscrepancy(station);
	}
}
