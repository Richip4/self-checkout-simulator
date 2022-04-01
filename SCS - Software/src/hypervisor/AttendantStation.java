package hypervisor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

import bank.Bank;
import checkout.CardHandler;
import checkout.Checkout;
import checkout.Receipt;
import interrupt.BanknoteHandler;
import interrupt.CoinHandler;
import interrupt.ProcessItemHandler;
import store.CredentialsSystem;
import store.Inventory;
import store.Membership;
import user.Attendant;
import user.Customer;

public class AttendantStation {
	
	public class StationSoftware{
		Customer customer;
		CardHandler cardHandler;
		Checkout checkoutHandler;
		Receipt receiptHandler;
		BanknoteHandler banknoteHandler;
		CoinHandler coinHandler;
		ProcessItemHandler procItemHandler;
	
		public StationSoftware(
			Customer customer,
			CardHandler cardHandler,
			Checkout checkoutHandler,
			Receipt receiptHandler,
			BanknoteHandler banknoteHandler,
			CoinHandler coinHandler,
			ProcessItemHandler procItemHandler) 
		{
			this.customer = customer;
			this.cardHandler = cardHandler;
			this.checkoutHandler = checkoutHandler;
			this.receiptHandler = receiptHandler;
			this.banknoteHandler = banknoteHandler;
			this.coinHandler = coinHandler;
			this.procItemHandler = procItemHandler; 
		}
		
	}
	
	
	// external variables
	private Bank bank;
	private Membership members;
	private Inventory inv;
	private CredentialsSystem creds;
	private Attendant attendant;
	
	// internal vars
	private boolean loggedIn;
	private HashSet<Integer> listOfUsedIDs;
	private HashMap<Integer, StationSoftware> checkoutStations;
	private final int MAX_CHILDREN = 100;		//maximum number of stations that can be monitored
	
	// Constructor 
	public AttendantStation(Bank bank, Membership members, Inventory inv, CredentialsSystem creds) {
		this.bank = bank;
		this.members = members;
		this.inv = inv;
		this.creds = creds;
		
		loggedIn = false;
		listOfUsedIDs = new HashSet<Integer>();
		checkoutStations = new HashMap<Integer, StationSoftware>();
	}
	
	/**
	 * Sets the attendant. It's here because sometimes we don't have an immediate attendant
	 * at start up or attendant can change.
	 * @param attendant
	 */
	public void setAttendant(Attendant attendant) {
		this.attendant = attendant;
	}
	
	/**
	 * Prompts the attendant for it's login creds, once that is complete, we get the username
	 * and password and check the login. If the login was successful, we change the 
	 * loggedIn flag.
	 * @return T/F whether we've logged in successfully
	 */
	public boolean login() {
		attendant.promptLogin();	//hopefully calls the GUI to enter login
		
		//the above has to be completed.
		String username = attendant.getUsername();
		String password = attendant.getPassword();
		
		if(creds.checkLogin(username, password)) {
			loggedIn = true;
			return true;
		}
		else 
			return false;		
	}
	
	/**
	 * Logout method to make sure that someone has logged out.
	 * @return true if it successfully logs out
	 */
	public boolean logout() {
		attendant.promptLogout();
		loggedIn = false;
		return true;
	}
	
	/**
	 * Given the hardware, we need to start up the station for use.
	 * @param scs - SelfCheckoutStation that it wants to boot up.
	 * @return T/F - Whether the software has been booted.
	 * @throws OverloadException
	 * TODO: Disable hardware? Boot up GUI? Should I catch OverloadException? 
	 * If it enables hardware, should it before or after? I don't think we do
	 * because the hardware team should have it enabled?
	 */
	public boolean startUpStation(SelfCheckoutStation scs) throws OverloadException {
		if (loggedIn) {
			try {
				Customer customer = new Customer();
				CardHandler cardHandler = new CardHandler(scs, bank, members);
				Checkout checkoutHandler = new Checkout(scs);
				Receipt receiptHandler = new Receipt(scs, customer, inv);
				BanknoteHandler banknoteHandler = new BanknoteHandler(scs);
				CoinHandler coinHandler = new CoinHandler(scs);
				ProcessItemHandler procItemHandler = new ProcessItemHandler(scs, inv);
				
//				boolean a = cardHandler.getHardwareState();
//				boolean b = receiptHandler.getHardwareState();
//				boolean c = banknoteHandler.getHardwareState();
//				boolean d = coinHandler.getHardwareState();
//				boolean e = procItemHandler.getHardwareState();
//				
//				if (a && b ...) {
//					int id = generateStationID();
//					checkoutStations.put(id, new StationSoftware(	//Add the newly created
//							customer,
//							cardHandler,
//							checkoutHandler,
//							receiptHandler,
//							banknoteHandler,
//							coinHandler,
//							procItemHandler
//					));
//					
//					return true;
//				} else {
//					return false;
//				}
				
				int id = generateStationID();
				checkoutStations.put(id, new StationSoftware(	//Add the newly created
						customer,
						cardHandler,
						checkoutHandler,
						receiptHandler,
						banknoteHandler,
						coinHandler,
						procItemHandler
				));
				
				return true;
			}catch (OverloadException e) {
				e.printStackTrace();
				return false;
			}
		}else {
			login();
			return false;
		}
	}
	
	/**
	 * Shuts down the software simply by removing it from the HashMap of checkoutStations
	 * 
	 * @param id - Each station should be identified by an id (could use scs but not easily
	 * identifiable.
	 * @return T/F - whether the checkoutStation has been removed. (If false
	 * the station most likely is not in the HashMap not exist)
	 */
	public boolean shutDownStation(int id) {
		if (loggedIn) {
			if (checkoutStations.containsKey(id)) {
				checkoutStations.remove(id);
				return true;
			}else 
				return false;
		}else {
			login();
			return false;
		}

	}
	
	/**
	 * This function generates a unique id for the SelfCheckoutStations it is monitoring.
	 * Generates a number from 0 to MAX_CHILDREN * 2 so that we have at least a 1/2 chance of 
	 * generating a number that isn't in already assigned.
	 * @return a unique int for the station.
	 */
	private int generateStationID() {
		int id = (int) ThreadLocalRandom.current().nextInt(0, MAX_CHILDREN * 2 + 1);
		while(listOfUsedIDs.contains(id)) 
			id = (int) ThreadLocalRandom.current().nextInt(0, MAX_CHILDREN * 2 + 1);
		listOfUsedIDs.add(id);
		return id;
	}
	
	
	
	

}
