package GUI;

import java.math.BigDecimal;
import java.util.List;

import javax.swing.JOptionPane;

import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

import application.AppControl;
import software.SelfCheckoutSoftware.PaymentMethod;
import software.SelfCheckoutSoftware.Phase;
import software.SelfCheckoutSoftware;
import store.Inventory;
import user.Customer;
import user.User;

public class GUI {

	private static AppControl ac;
	private static Scenes scenes = new Scenes();
	
	private GUI() {}
	
	public static void init(AppControl appControl) {
		ac = appControl;
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
	public static boolean newUser(int newUserType) {
		if (newUserType == AppControl.CUSTOMER) {
			ac.addNewCustomer();
			return true;
		} else if (newUserType == AppControl.ATTENDANT) {
			// check list of users for an existing attendant
			User[] users = ac.getActiveUsers();
			
			for (int i = 0; i < users.length; i++) {
				if (users[i] != null && users[i].getUserType() == AppControl.ATTENDANT) {
					Scenes.errorMsg("An Attendant is already on duty. Sorry.");
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
	 * @return true if station is free, false otherwise
	 */
	public static boolean userApproachesStation(int station) {
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			if (station == 0) { // this is the attendant's station
				Scenes.errorMsg("You are not authorized to view the attendant station.");
				return false;
			} else if (ac.getUserAt(station) != null) { 
				if (ac.getUserAt(station).getUserType() == AppControl.ATTENDANT) {
					Scenes.errorMsg("Station being serviced");
					return false;
				} else if (ac.getUserAt(station).getUserType() == AppControl.CUSTOMER) {
					Scenes.errorMsg("A customer is already using this station");
					return false;
				} 
			} else {
				ac.customerUsesStation(station);
				scenes.setCurrentStation(station);
				scenes.getScene(Scenes.SCS_OVERVIEW);
				return true;
			}
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			if (station == 0) { // this is the attendant's station
				scenes.setCurrentStation(station);
				scenes.getScene(Scenes.AS_TOUCH);
				return true;
			} else {
				if (ac.isAttendantLoggedIn()) {
					ac.attendantUsesStation(station);
					scenes.setCurrentStation(station);
					scenes.getScene(Scenes.SCS_OVERVIEW);
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
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			ac.customerLeavesStation(station);
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			ac.attendantLeavesStation(station);
		}
	}

	//
	public static void userBagsItem(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userInsertsBanknote(int value, int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userRemovesBanknote(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userServicesStation(int currentStation) {
		if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			// we assume the attendant has the key to the station
			scenes.getScene(Scenes.SCS_MAINTENANCE);
		}
	}

	public static void userInsertsCoin(BigDecimal value, int currentStation) {
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}
	
	public static void userRemovesCoins(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userPlacesItemOnWeighScale(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userScansItem(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userRemovesReceipt(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userAccessCardReader(int currentStation) {
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			scenes.getScene(Scenes.SCS_CARDREADER);
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userAccessTouchscreen(int currentStation) {
		// TODO Auto-generated method stub
		scenes.getScene(Scenes.SCS_TOUCH);
		
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
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

		return ac.getStationState(station);
	}

	/**
	 * 
	 * @param station
	 */
	public static void attendantBlockToggle(int station) {
		
		ac.toggleBlock(station);
	}

	/**
	 * 
	 * @param station
	 */
	public static void attendantApproveStation(int station) {
		
		ac.approveStationDiscrepancy(station);
	}

	public static void userTapsCard(int cardType) {
		if (cardType == AppControl.CREDIT) {
			ac.customerTapsCreditCard();
		} if (cardType == AppControl.DEBIT) {
			ac.customerTapsDebitCard();
		} if (cardType == AppControl.MEMBERSHIP) {
			ac.customerTapsMembershipCard();
		}
	}

	public static void userSwipesCard(int cardType) {
		if (cardType == AppControl.CREDIT) {
			ac.customerSwipesCreditCard();
		} if (cardType == AppControl.DEBIT) {
			ac.customerSwipesDebitCard();
		} if (cardType == AppControl.MEMBERSHIP) {
			ac.customerSwipesMembershipCard();
		}
	}

	public static void userInsertCard(int cardType) {
		if (cardType == AppControl.CREDIT) {
			ac.customerInsertCreditCard();
		} if (cardType == AppControl.DEBIT) {
			ac.customerInsertDebitCard();
		} if (cardType == AppControl.MEMBERSHIP) {
			ac.customerInsertMembershipCard();
		}
	}

	public static void refillBanknoteDispensers() {
		// TODO Auto-generated method stub
		
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

	public static void userEntersPLUCode(int code, int currentStation) {
		//errorMsg("No items have been bagged");
		try {
			Item item = ac.getCustomersNextItem(currentStation);
			PLUCodedItem pluItem = (PLUCodedItem)item;

			//check if the plu exists in the Inventory
			PriceLookupCode plu = new PriceLookupCode(Integer.toString(code));
			if (Inventory.getProduct(plu).getPLUCode().equals(plu)) {
				//get software and customer
				SelfCheckoutSoftware software = ac.getSelfCheckoutSoftware(currentStation);
				
				Customer customer = software.getCustomer();
				customer.enterPLUCode(plu);
				
				SelfCheckoutStation station = software.getSelfCheckoutStation();
				station.scanningArea.add(item);
			} else 
				Scenes.errorMsg("PLU code does not exist!");
		} catch(Exception e) {
			Scenes.errorMsg("The item you're trying to checkout is not a PLU item");
		}

	}

	public static boolean attendantPassword(String password) {
		
		return ac.attendantPassword(password);
	}

	public static void removeItem(int station, int index) {
		ac.removeItemFromCustomersCart(station, index);
	}

	public static void shutdownStation() {
		// TODO Auto-generated method stub
		
	}

	public static boolean attendantLogin(String name, String password) {
		
		return ac.attendantLogin(name, password);
	}

	public static List<Product> getBaggedItems(int station) {
		return ac.getCustomerCart(station);
	}

	/**
	 * Simulate the user at the previous station
	 */
	public static void selectPreviousUser() {
		System.out.println("Select prev user");
		ac.prevActiveUser();
		updateScene(ac.getActiveUsersStation());
	}

	/**
	 * Simulate the user at the next station
	 */
	public static void selectNextUser() {
		System.out.println("Select next user");
		ac.nextActiveUser();
		updateScene(ac.getActiveUsersStation());
	}
	
	/**
	 * Take me to the scene of the currently selected user.
	 * @param station
	 */
	private static void updateScene(int station) {
		System.out.println("updated scene " + station);
		scenes.setCurrentStation(station);
		if (station == -1) {
			return;
		} else if (station == 0) {
			scenes.getScene(Scenes.AS_TOUCH);
		} else {
			scenes.getScene(Scenes.SCS_OVERVIEW);
		}
	}

	public static boolean isAttendantLoggedIn() {
		return ac.isAttendantLoggedIn();
	}

	public static String getNextItemDescription(int station) {
		String desc = "";
		Item item = ac.getCustomersNextItem(station);
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

	/**
	 * 
	 * @return
	 */
	public static String getUserInstruction(int scene) {
		String instruction = null;
		switch (scene) {
		// Self-Checkout Station Overview Scene
		case(Scenes.SCS_OVERVIEW):
			
			if (ac.getStationPhase(scenes.getCurrentStation()) == Phase.CHOOSING_PAYMENT_METHOD) {
				instruction = "Insert Banknote/Coin or pay with Card";
			} else if (ac.getStationPhase(scenes.getCurrentStation()) == Phase.PAYMENT_COMPLETE) {
				instruction = "Take Change and Receipt";
			} else if (ac.getStationPhase(scenes.getCurrentStation()) == Phase.BAGGING_ITEM) {
				instruction = "Put item in bagging area or request to Skip Bagging";
			} else {
				Item item = ac.getCustomersNextItem(scenes.getCurrentStation());
				if (item instanceof PLUCodedItem) {
					instruction = "Look up Product or enter PLU code on Touchscreen";
				} else if (item instanceof BarcodedItem) {
					instruction = "Scan Barcoded Item";
				}
			}
		
		// Attendant station scene
		case(Scenes.AS_TOUCH):
			break;
		
		// Self-Checkout Station Touchscreen Scene
		case(Scenes.SCS_TOUCH):
			break;
		
		// Self-Checkout Station Card Reader Scene
		case(Scenes.SCS_CARDREADER):
			break;
		
		// Self-Checkout Station Maintenance Scene
		case(Scenes.SCS_MAINTENANCE):
			break;
		
		}
		return instruction;
	}
}
