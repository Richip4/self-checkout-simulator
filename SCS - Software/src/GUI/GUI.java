package GUI;

import java.util.List;

import javax.swing.JOptionPane;

import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

import application.AppControl;
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
	 * @return true if user successfully added, false otherwise
	 */
	public boolean newUser(int newUserType) {
		if (newUserType == AppControl.CUSTOMER) {
			ac.addNewCustomer();
			return true;
		} else if (newUserType == AppControl.ATTENDANT) {
			// check list of users for an existing attendant
			User[] users = ac.getActiveUsers();
			
			for (int i = 0; i < users.length; i++) {
				if (users[i] != null && users[i].getUserType() == AppControl.ATTENDANT) {
					errorMsg("An Attendant is already on duty. Sorry.");
					return false;
				}
			}
			
			// no attendant found, add a new one
			ac.addNewAttendant();
			return true;
		}
		return false;
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
		if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			// we assume the attendant has the key to the station
			scenes.getScene(Scenes.SCS_MAINTENANCE);
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
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			scenes.getScene(Scenes.SCS_CARDREADER);
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public void userAccessTouchscreen(int currentStation) {
		// TODO Auto-generated method stub
		scenes.getScene(Scenes.SCS_TOUCH);
		
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

	public void userTapsCard(int cardType) {
		if (cardType == AppControl.CREDIT) {
			ac.customerTapsCreditCard();
		} if (cardType == AppControl.DEBIT) {
			ac.customerTapsDebitCard();
		} if (cardType == AppControl.MEMBERSHIP) {
			ac.customerTapsMembershipCard();
		}
	}

	public void userSwipesCard(int cardType) {
		if (cardType == AppControl.CREDIT) {
			ac.customerSwipesCreditCard();
		} if (cardType == AppControl.DEBIT) {
			ac.customerSwipesDebitCard();
		} if (cardType == AppControl.MEMBERSHIP) {
			ac.customerSwipesMembershipCard();
		}
	}

	public void userInsertCard(int cardType) {
		if (cardType == AppControl.CREDIT) {
			ac.customerInsertCreditCard();
		} if (cardType == AppControl.DEBIT) {
			ac.customerInsertDebitCard();
		} if (cardType == AppControl.MEMBERSHIP) {
			ac.customerInsertMembershipCard();
		}
	}

	public void refillBanknoteDispensers() {
		// TODO Auto-generated method stub
		
	}

	public void refillCoinDispenser() {
		// TODO Auto-generated method stub
		
	}

	public void addPaper() {
		// TODO Auto-generated method stub
		
	}

	public void addInk() {
		// TODO Auto-generated method stub
		
	}

	public void emptyBanknoteStorage() {
		// TODO Auto-generated method stub
		
	}

	public void fillBankStorage() {
		// TODO Auto-generated method stub
		
	}

	public void emptyCoinStorage() {
		// TODO Auto-generated method stub
		
	}

	public void fillCoinStorage() {
		// TODO Auto-generated method stub
		
	}

	public void proceedToCheckout() {
		// TODO Auto-generated method stub
		
	}

	public boolean stationAttendantAccess() {
		// TODO Auto-generated method stub
		return true;
	}

	public void userUsesOwnBags() {
		// TODO Auto-generated method stub
		
	}

	public void userEntersMembership(int num) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * user has selected a particular item from the search menu
	 * @param pluCodedProduct
	 */
	public void selectedItem(PLUCodedProduct pluCodedProduct) {
		System.out.println(pluCodedProduct.getDescription());
	}

	public void userEntersPLUCode(int code) {
		// TODO Auto-generated method stub
		
	}

	public boolean attendantPassword(String password) {
		
		return ac.attendantPassword(password);
	}

	public void removeItem(int station, int index) {
		ac.removeItemFromCustomersCart(station, index);
	}

	public void shutdownStation() {
		// TODO Auto-generated method stub
		
	}

	public boolean attendantLogin(String name, String password) {
		
		return ac.attendantLogin(name, password);
	}

	public List<Product> getBaggedItems(int station) {
		return ac.getCustomerCart(station);
	}

	/**
	 * Simulate the user at the previous station
	 */
	public void selectPreviousUser() {
		System.out.println("Select prev user");
		ac.prevActiveUser();
		updateScene(ac.getActiveUsersStation());
	}

	/**
	 * Simulate the user at the next station
	 */
	public void selectNextUser() {
		System.out.println("Select next user");
		ac.nextActiveUser();
		updateScene(ac.getActiveUsersStation());
	}
	
	/**
	 * Take me to the scene of the currently selected user.
	 * @param station
	 */
	private void updateScene(int station) {
		scenes.setCurrentStation(station);
		if (station == 0) {
			scenes.getScene(Scenes.AS_TOUCH);
		} else {
			scenes.getScene(Scenes.SCS_OVERVIEW);
		}
	}

	public boolean isAttendantLoggedIn() {
		return ac.isAtendantLoggedIn();
	}
}
