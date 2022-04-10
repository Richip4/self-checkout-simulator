package GUI;

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
import software.SelfCheckoutSoftware;
import software.SupervisionSoftware;
import software.SelfCheckoutSoftware.Phase;
import store.Inventory;
import store.Membership;
import store.Store;
import store.credentials.AuthorizationRequiredException;
import user.Attendant;
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
		//todo
		if (ac.getActiveUser().getUserType() == AppControl.CUSTOMER) {
//			software.bagItem();
//			hardware.baggingArea.add(item);
			//remove item from auto generated list here because we are still dealing
			//with the same item
		} else if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			
		}
	}

	public static void userInsertsBanknote(int currentStation) {
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

	public static void userInsertsCoin(int currentStation) {
		// TODO Auto-generated method stub
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
			
			software.bagItem();
			
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
		scenes.getScene(Scenes.SCS_TOUCH);
	}

	public static void attendantLogsOut() {
		SupervisionSoftware svs = Store.getSupervisionSoftware();
		svs.logout();
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

	public static void addPaper(int currentStation, int amount) {
		if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			try {
				ac.getSelfCheckoutSoftware(currentStation).getSelfCheckoutStation().printer.addPaper(amount);
			} catch (OverloadException e) {
				Scenes.errorMsg("The paper cartridge is already full");
			}
		}
	}

	public static void addInk(int currentStation, int amount) {
		if (ac.getActiveUser().getUserType() == AppControl.ATTENDANT) {
			try {
				ac.getSelfCheckoutSoftware(currentStation).getSelfCheckoutStation().printer.addInk(amount);
			} catch (OverloadException e) {
				Scenes.errorMsg("The ink cartridge is already full");
			}
		}
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

	public static void proceedToCheckout() {
		SelfCheckoutSoftware scs = ac.getSelfCheckoutSoftware(scenes.getCurrentStation());
		scs.checkout();
	}

	public static boolean stationAttendantAccess() {
		// TODO Auto-generated method stub
		return true;
	}

	public static void userUsesOwnBags() {
		// TODO Auto-generated method stub
		
	}

	public static void userEntersMembership(int num) {

		if(Membership.isMember(Integer.toString(num)))
		{
			SelfCheckoutSoftware scs = ac.getSelfCheckoutSoftware(scenes.getCurrentStation());
			scs.getCustomer().setMemberID(Integer.toString(num));
		}
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

			//check if the plu exists in the Inventory
			PriceLookupCode plu = new PriceLookupCode(Integer.toString(code));
			if (Inventory.getProduct(plu).getPLUCode().equals(plu)) {
				//get software and set phase
				SelfCheckoutSoftware software = ac.getSelfCheckoutSoftware(currentStation);
				software.addItem();
				
				//get the customer and set the PLU code
				Customer customer = software.getCustomer();
				customer.enterPLUCode(plu);
				
				//get the hardware and weighs the item.
				SelfCheckoutStation hardware = software.getSelfCheckoutStation();
				hardware.scanningArea.add(item);
				
				software.bagItem();
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

	public static void shutdownStation() throws AuthorizationRequiredException {
		if(ac.getActiveUser().getUserType() == AppControl.ATTENDANT)
		{
			SelfCheckoutSoftware scs = ac.getSelfCheckoutSoftware(scenes.getCurrentStation());
			scs.stopSystem();
		}
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
}
