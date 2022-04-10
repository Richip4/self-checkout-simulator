package GUI;

import java.math.BigDecimal;
import java.util.List;

import javax.swing.JOptionPane;

import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

import application.AppControl;
import application.Main;
import software.SelfCheckoutSoftware.PaymentMethod;
import software.SelfCheckoutSoftware.Phase;
import software.SelfCheckoutSoftware;
import software.SelfCheckoutSoftware.Phase;
import store.Inventory;
import store.Store;
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
		SelfCheckoutSoftware software = ac.getSelfCheckoutSoftware(currentStation);
		SelfCheckoutStation hardware = software.getSelfCheckoutStation();
		
		hardware.baggingArea.add(ac.getLastCheckedOutItem());
	}

	public static void userInsertsBanknote(int value, int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}
	
	
	public static void userRemovesBanknote(int currentStation) {
		/* MAKE CHANGE METHOD MUST BE FIXED BEFORE THIS CAN BE TESTED
		SelfCheckoutSoftware scss = ac.getSelfCheckoutSoftware(currentStation);
		SelfCheckoutStation scs = scss.getSelfCheckoutStation();
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER 
				&& scss.getPhase() == Phase.PAYMENT_COMPLETE
				&& !(scs.banknoteOutput.hasSpace()))
		{
			scs.banknoteOutput.removeDanglingBanknotes();
			
		} */
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
		/*
		SelfCheckoutSoftware scss = ac.getSelfCheckoutSoftware(currentStation);
		SelfCheckoutStation scs = scss.getSelfCheckoutStation();
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER &&
				scss.getPhase() == Phase.PAYMENT_COMPLETE) {
			scs.coinTray.collectCoins();
		} */
	}

	public static void userPlacesItemOnWeighScale(int currentStation) {
		// TODO Auto-generated method stub
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
			
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userScansItem(int currentStation, boolean usedMainScanner) {
		//we assume that it scans the first item in our list of auto generated items
		Item item = ac.getCustomersNextItem(currentStation);
		try {
			BarcodedItem product = (BarcodedItem) item;
			
			SelfCheckoutSoftware software = ac.getSelfCheckoutSoftware(currentStation);
			SelfCheckoutStation hardware = software.getSelfCheckoutStation();
			
			software.addItem();
			
			if (usedMainScanner) {
				hardware.mainScanner.scan(item);
			}else {
				hardware.handheldScanner.scan(item);
			}
			ac.removeCustomerNextItem(currentStation);
		}catch (Exception e) {
			Scenes.errorMsg("You cannot scan this item");
		}
	}
	
	public static void userRemovesReceipt(int currentStation) {
		// TODO Auto-generated method stub
		SelfCheckoutSoftware scss = ac.getSelfCheckoutSoftware(currentStation);
		SelfCheckoutStation scs = scss.getSelfCheckoutStation();
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER
				&& scss.getPhase() == Phase.PAYMENT_COMPLETE || scss.getPhase() == Phase.IDLE) {
			try {
				scs.printer.cutPaper();
				scs.printer.removeReceipt();
			}catch(Exception e){
				Scenes.errorMsg("You are trying to remove a non-existent receipt");
			}	
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
	
	public static void removePaidItemsFromBagging() {
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
		if(ac.getActiveUser().getUserType() == AppControl.ATTENDANT)
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
	
	/* Emptying the banknote storage is done with a key, but we assume the attendant would have
	 * this. This can happen during any phase*/
	public static void emptyBanknoteStorage() {
		
		int currentStation = scenes.getCurrentStation();
		SelfCheckoutSoftware scss = ac.getSelfCheckoutSoftware(currentStation);
		SelfCheckoutStation scs = scss.getSelfCheckoutStation();
		
		if(ac.getActiveUser().getUserType() == AppControl.ATTENDANT)
		{
			scs.banknoteStorage.unload();	
		}
		
	}

	public static void fillBankStorage() {
		// TODO Auto-generated method stub
		
	}
	
	/* Emptying the coin storage is done with a key, but we assume the attendant would have
	 * this. This can happen during any phase*/
	public static void emptyCoinStorage() {
		int currentStation = scenes.getCurrentStation();
		SelfCheckoutSoftware scss = ac.getSelfCheckoutSoftware(currentStation);
		SelfCheckoutStation scs = scss.getSelfCheckoutStation();
		
		if(ac.getActiveUser().getUserType() == AppControl.ATTENDANT)
		{
			scs.coinStorage.unload();	
		}
		
	}

	public static void fillCoinStorage() {
		// TODO Auto-generated method stub
		
	}

	public static void proceedToCheckout() {
		// TODO Auto-generated method stub
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
		try {
			Item item = ac.getCustomersNextItem(currentStation);

			
			PriceLookupCode plu = new PriceLookupCode(Integer.toString(code));
			PLUCodedItem pluItem = (PLUCodedItem)ac.getCustomersNextItem(currentStation);
			
			//for simulation purpose only
			if (!plu.equals(pluItem.getPLUCode())) {
				Scenes.errorMsg("Simulation error! Please enter the PLU at the top of your cart!");
				return;
			}
			
			//check if the PLU exists in the Inventory
			if (Inventory.getProduct(plu).getPLUCode().equals(plu)) {
				//get software and set phase
				SelfCheckoutSoftware software = ac.getSelfCheckoutSoftware(currentStation);
				software.addPLUItem();
				
				//get the customer and set the PLU code
				Customer customer = software.getCustomer();
				customer.enterPLUCode(plu);
				
				//get the hardware and weighs the item.
				SelfCheckoutStation hardware = software.getSelfCheckoutStation();
				hardware.scanningArea.add(item);
				
				
				ac.removeCustomerNextItem(currentStation);
				hardware.scanningArea.remove(item);
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
		
		if (item == null)
			return "No more items";
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
	
	public static Phase getPhase(int stationNumber) {
		System.out.println(ac.getSelfCheckoutSoftware(stationNumber).getPhase());
		return ac.getSelfCheckoutSoftware(stationNumber).getPhase();
	}
}
