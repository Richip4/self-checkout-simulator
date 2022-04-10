package GUI;

import java.util.List;

import javax.swing.JOptionPane;

import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

import application.AppControl;
import store.Inventory;
import user.User;

public class GUI {

	private static AppControl ac;
	private static Scenes scenes = new Scenes();
	
	private GUI() {}
	
	public static void init(AppControl appControl) {
		ac = appControl;
		// Initializes the openning scene, Self-Checkout Overview 
		getScenes().getScene(Scenes.SC_OVERVIEW);	
	}
	
	/**
	 * checks what kind of user to add to the simulation.
	 * only one attendant can be allowed at one time
	 * 
	 * @param newUserType
	 * @return true if user successfully added, false otherwise
	 */
	public static boolean newUser(int newUserType) {
		if (newUserType == AppControl.CUSTOMER) {
			getAc().addNewCustomer();
			return true;
		} else if (newUserType == AppControl.ATTENDANT) {
			// check list of users for an existing attendant
			User[] users = getAc().getActiveUsers();
			
			for (int i = 0; i < users.length; i++) {
				if (users[i] != null && users[i].getUserType() == AppControl.ATTENDANT) {
					Scenes.errorMsg("An Attendant is already on duty. Sorry.");
					return false;
				}
			}
			
			// no attendant found, add a new one
			getAc().addNewAttendant();
			return true;
		}
		return false;
	}

	/**
	 * checks if the provided station is free to use by the
	 * active user before letting them proceed.
	 * @param station - the specific station index
	 * @return true if station is free, false otherwise
	 */
	public static boolean userApproachesStation(int station) {
		if (getAc().getActiveUser().getUserType() == AppControl.CUSTOMER) {
			if (station == 0) { // this is the attendant's station
				Scenes.errorMsg("You are not authorized to view the attendant station.");
				return false;
			} else if (getAc().getUserAt(station) != null) { 
				if (getAc().getUserAt(station).getUserType() == AppControl.ATTENDANT) {
					Scenes.errorMsg("Station being serviced");
					return false;
				} else if (getAc().getUserAt(station).getUserType() == AppControl.CUSTOMER) {
					Scenes.errorMsg("A customer is already using this station");
					return false;
				} 
			} else {
				getAc().customerUsesStation(station);
				getScenes().setCurrentStation(station);
				getScenes().getScene(Scenes.SCS_OVERVIEW);
				return true;
			}
		} else if (getAc().getActiveUser().getUserType() == AppControl.ATTENDANT) {
			if (station == 0) { // this is the attendant's station
				getScenes().setCurrentStation(station);
				getScenes().getScene(Scenes.AS_TOUCH);
				return true;
			} else {
				if (getAc().isAttendantLoggedIn()) {
					getAc().attendantUsesStation(station);
					getScenes().setCurrentStation(station);
					getScenes().getScene(Scenes.SCS_OVERVIEW);
					return true;
				} else {
					Scenes.errorMsg("Please log in at the attendant station");
					return false;
				}
			}
		}
		
		return false;
	}

	public static void userLeavesStation(int station) {
		if (getAc().getActiveUser().getUserType() == AppControl.CUSTOMER) {
			getAc().customerLeavesStation(station);
		} else if (getAc().getActiveUser().getUserType() == AppControl.ATTENDANT) {
			getAc().attendantLeavesStation(station);
		}
	}

	//
	public static void userBagsItem(int currentStation) {
		// TODO Auto-generated method stub
		if (getAc().getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (getAc().getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userInsertsBanknote(int currentStation) {
		// TODO Auto-generated method stub
		if (getAc().getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (getAc().getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userRemovesBanknote(int currentStation) {
		// TODO Auto-generated method stub
		if (getAc().getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (getAc().getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userServicesStation(int currentStation) {
		if (getAc().getActiveUser().getUserType() == AppControl.ATTENDANT) {
			// we assume the attendant has the key to the station
			getScenes().getScene(Scenes.SCS_MAINTENANCE);
		}
	}

	public static void userInsertsCoin(int currentStation) {
		// TODO Auto-generated method stub
		if (getAc().getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (getAc().getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}
	
	public static void userRemovesCoins(int currentStation) {
		// TODO Auto-generated method stub
		if (getAc().getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (getAc().getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userPlacesItemOnWeighScale(int currentStation) {
		// TODO Auto-generated method stub
		if (getAc().getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (getAc().getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userScansItem(int currentStation) {
		// TODO Auto-generated method stub
		if (getAc().getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (getAc().getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userRemovesReceipt(int currentStation) {
		// TODO Auto-generated method stub
		if (getAc().getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (getAc().getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userAccessCardReader(int currentStation) {
		if (getAc().getActiveUser().getUserType() == AppControl.CUSTOMER) {
			getScenes().getScene(Scenes.SCS_CARDREADER);
		} else if (getAc().getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userAccessTouchscreen(int currentStation) {
		// TODO Auto-generated method stub
		getScenes().getScene(Scenes.SCS_TOUCH);
		
		if (getAc().getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (getAc().getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void attendantLogsOut() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * @param station
	 * @return
	 */
	public static String stationStatus(int station) {

		return getAc().getStationState(station);
	}

	/**
	 * 
	 * @param station
	 */
	public static void attendantBlockToggle(int station) {
		
		getAc().toggleBlock(station);
	}

	/**
	 * 
	 * @param station
	 */
	public static void attendantApproveStation(int station) {
		
		getAc().approveStationDiscrepancy(station);
	}

	public static void userTapsCard(int cardType) {
		if (cardType == AppControl.CREDIT) {
			getAc().customerTapsCreditCard();
		} if (cardType == AppControl.DEBIT) {
			getAc().customerTapsDebitCard();
		} if (cardType == AppControl.MEMBERSHIP) {
			getAc().customerTapsMembershipCard();
		}
	}

	public static void userSwipesCard(int cardType) {
		if (cardType == AppControl.CREDIT) {
			getAc().customerSwipesCreditCard();
		} if (cardType == AppControl.DEBIT) {
			getAc().customerSwipesDebitCard();
		} if (cardType == AppControl.MEMBERSHIP) {
			getAc().customerSwipesMembershipCard();
		}
	}

	public static void userInsertCard(int cardType) {
		if (cardType == AppControl.CREDIT) {
			getAc().customerInsertCreditCard();
		} if (cardType == AppControl.DEBIT) {
			getAc().customerInsertDebitCard();
		} if (cardType == AppControl.MEMBERSHIP) {
			getAc().customerInsertMembershipCard();
		}
	}

	public static void refillBanknoteDispensers() {
		if(getAc().getActiveUser().getUserType() == AppControl.ATTENDANT)
		{
			
		}
		
	}

	public static void refillCoinDispenser() {
		// TODO Auto-generated method stub
		
	}

	public static void addPaper() {
		// TODO Auto-generated method stub
		
	}

	public static void addInk() {
		// TODO Auto-generated method stub
		
	}

	public static void emptyBanknoteStorage() {
		// TODO Auto-generated method stub
		
	}

	public static void fillBankStorage() {
		// TODO Auto-generated method stub
		
	}

	public static void emptyCoinStorage() {
		// TODO Auto-generated method stub
		
	}

	public static void fillCoinStorage() {
		// TODO Auto-generated method stub
		
	}

	public static void proceedToCheckout() {
		// TODO Auto-generated method stub
		
	}

	public static boolean stationAttendantAccess() {
		// TODO Auto-generated method stub
		return true;
	}

	public static void userUsesOwnBags() {
		// TODO Auto-generated method stub
		
	}

	public static void userEntersMembership(int num) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * user has selected a particular item from the search menu
	 * @param pluCodedProduct
	 */
	public static void selectedItem(PLUCodedProduct pluCodedProduct) {
		System.out.println(pluCodedProduct.getDescription());
	}

	public static void userEntersPLUCode(int code) {
		// TODO Auto-generated method stub
		
	}

	public static boolean attendantPassword(String password) {
		
		return getAc().attendantPassword(password);
	}

	public static void removeItem(int station, int index) {
		getAc().removeItemFromCustomersCart(station, index);
	}

	public static void shutdownStation() {
		// TODO Auto-generated method stub
		
	}

	public static boolean attendantLogin(String name, String password) {
		
		return getAc().attendantLogin(name, password);
	}

	public static List<Product> getBaggedItems(int station) {
		return getAc().getCustomerCart(station);
	}

	/**
	 * Simulate the user at the previous station
	 */
	public static void selectPreviousUser() {
		System.out.println("Select prev user");
		getAc().prevActiveUser();
		updateScene(getAc().getActiveUsersStation());
	}

	/**
	 * Simulate the user at the next station
	 */
	public static void selectNextUser() {
		System.out.println("Select next user");
		getAc().nextActiveUser();
		updateScene(getAc().getActiveUsersStation());
	}
	
	/**
	 * Take me to the scene of the currently selected user.
	 * @param station
	 */
	private static void updateScene(int station) {
		System.out.println("updated scene " + station);
		getScenes().setCurrentStation(station);
		if (station == -1) {
			return;
		} else if (station == 0) {
			getScenes().getScene(Scenes.AS_TOUCH);
		} else {
			getScenes().getScene(Scenes.SCS_OVERVIEW);
		}
	}

	public static boolean isAttendantLoggedIn() {
		return getAc().isAttendantLoggedIn();
	}

	public static String getNextItemDescription(int station) {
		String desc = "";
		Item item = getAc().getCustomersNextItem(station);
		if (item instanceof PLUCodedItem) {
			PLUCodedItem pluItem = (PLUCodedItem) item;
			PLUCodedProduct p = Inventory.getProduct(pluItem.getPLUCode()); 
			desc = "<html>PLU Coded Item<br>";
			desc += p.getDescription() +"  $"+ p.getPrice();
			desc += "<br>Code: " + p.getPLUCode() + "</html>";
		} else if (item instanceof BarcodedItem) {
			BarcodedItem barItem = (BarcodedItem) item;
			BarcodedProduct b = Inventory.getProduct(barItem.getBarcode()); 
			desc = "<html>Barcoded Item<br>";
			desc += b.getDescription() +" "+ b.getPrice() + "</html>";
		}
		System.out.println(desc);
		return desc;
	}

	public static AppControl getAc() {
		return ac;
	}

	public static Scenes getScenes() {
		return scenes;
	}
}
