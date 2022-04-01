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
	 * TODO: Should I catch OverloadException?
	 * @param scs - SelfCheckoutStation that it wants to boot up.
	 * @return T/F - Whether the software has been booted.
	 * @throws OverloadException
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
	
			int id = generateStationID();
			checkoutStations.put(id, new StationSoftware(
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
