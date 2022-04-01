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
import store.Inventory;
import store.Membership;
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
	
	// internal vars
	private HashSet<Integer> listOfUsedIDs = new HashSet<Integer>();
	private HashMap<Integer, StationSoftware> checkoutStations = new HashMap<Integer, StationSoftware>();
	private final int MAX_CHILDREN = 100;		//maximum number of stations that can be monitored
	
	// Constructor 
	public AttendantStation(Bank bank, Membership members, Inventory inv) {
		this.bank = bank;
		this.members = members;
		this.inv = inv;
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
		try {
			Customer customer = new Customer();
			CardHandler cardHandler = new CardHandler(scs, bank, members);
			Checkout checkoutHandler = new Checkout(scs);
			Receipt receiptHandler = new Receipt(scs, customer, inv);
			BanknoteHandler banknoteHandler = new BanknoteHandler(scs);
			CoinHandler coinHandler = new CoinHandler(scs);
			ProcessItemHandler procItemHandler = new ProcessItemHandler(scs, inv);
			
//			boolean a = cardHandler.getHardwareState();
//			boolean b = receiptHandler.getHardwareState();
//			boolean c = banknoteHandler.getHardwareState();
//			boolean d = coinHandler.getHardwareState();
//			boolean e = procItemHandler.getHardwareState();
//			
//			if (a && b ...) {
//				int id = generateStationID();
//				checkoutStations.put(id, new StationSoftware(	//Add the newly created
//						customer,
//						cardHandler,
//						checkoutHandler,
//						receiptHandler,
//						banknoteHandler,
//						coinHandler,
//						procItemHandler
//				));
//				
//				return true;
//			} else {
//				return false;
//			}
			
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
		if (checkoutStations.containsKey(id)) {
			checkoutStations.remove(id);
			return true;
		}else 
			return false;

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
