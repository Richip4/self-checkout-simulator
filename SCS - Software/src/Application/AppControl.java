package Application;

import java.util.ArrayList;
import java.util.List;

import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;

import interrupt.BanknoteHandler;
import interrupt.CardHandler;
import interrupt.CoinHandler;
import interrupt.ProcessItemHandler;
import store.Inventory;
import store.Store;
import user.Customer;

public class AppControl {
	
	// types of present users at a self-checkout station
	Integer NO_USER		= 0;
	Integer CUSTOMER 	= 1;
	Integer ATTENDANT 	= 2;
	Integer BOTH		= 3;
	
	SupervisionStation attendantStation;
	// list of self-checkout stations and their current user
	List<Pair<SelfCheckoutStation, Integer>> selfStations;
	
	// list of handlers, one for each station
	List<BanknoteHandler> banknoteHandlers;
	List<CardHandler> cardHandlers;
	List<CoinHandler> coinHandlers;
	List<ProcessItemHandler> processItemHandlers;
	
	// list of current customers. Maximun == number of selfStations
	List<Customer> customers;
	
	public AppControl() {
		attendantStation = Store.getSupervisionStation();
		attendantStation.supervisedStations().forEach(
				(station) -> { selfStations.add(new Pair(station, NO_USER)); } );

		// initialize customers to match the number of stations
		customers = new ArrayList<>(selfStations.size());

		// initialize the lists of handlers to match the number of stations
		banknoteHandlers 	= new ArrayList<>(selfStations.size());
		cardHandlers 		= new ArrayList<>(selfStations.size());
		coinHandlers 		= new ArrayList<>(selfStations.size());
		processItemHandlers	= new ArrayList<>(selfStations.size());

		// instantiate the handlers for all stations
		for (int i = 0; i < selfStations.size(); i++) {
			banknoteHandlers.set(i,  new BanknoteHandler(selfStations.get(i).getL()));
			cardHandlers.set(i,  new CardHandler(selfStations.get(i).getL())); // CardHandler construct outdated - shouldn't take Bank or Memebership parameters
			coinHandlers.set(i,  new CoinHandler(selfStations.get(i).getL()));
			processItemHandlers.set(i,  new ProcessItemHandler(selfStations.get(i).getL())); // ProcessItemHandler construct outdated - shouldn't take Inventory paramter
		}
	}
	
	//*********************************************************
	// The following methods are SOME of the interactions 
	// between GUI and a user.  Details are not yet complete
	// but the idea is still the same.
	//*********************************************************
	
	/**
	 * customer uses certain station
	 * @param station - the index of the station accessed
	 */
	public void customerUsesStation(int station) {
		// create new customer and set it in our list of customers
		Customer customer = new Customer();
		customers.set(station, customer);
		
		// add the new customer to any component of that station that needs it
		banknoteHandlers.get(station).setCustomer(customer);
		cardHandlers.get(station).setCustomer(customer);
		coinHandlers.get(station).setCustomer(customer);
		processItemHandlers.get(station).setCustomer(customer);
		
		// set the type of user with the station
		selfStations.get(station).setR(CUSTOMER);
	}
	
	/**
	 * attendant uses certain station
	 * 
	 * if the station currently has a customer, the attendent is simply
	 * assisting the customer.  Otherwise, they are there to maintain 
	 * the station and shouldn't be allowed certain operations like
	 * adding/removing items.
	 * 
	 * @param station
	 */
	public void attendantUsesStation(int station) {
		// TODO: attendant class doesn't exist yet; can't communicate with user
		
		// set the type of user with the station
		if (selfStations.get(station).getR().compareTo(CUSTOMER) == 0) {
			selfStations.get(station).setR(BOTH);
		} else {
			selfStations.get(station).setR(ATTENDANT);
		}
	}
	
	/**
	 * User scans an item with either main scanner or handheld scanner.
	 * 
	 * GUI will check if the station is occupied by a customer, attendant, or both
	 * and will only call this if a customer is involved.
	 * 
	 * @param station
	 */
	public void addItemViaBarcode(int station) {
		// not sure how we will select items yet
		customers.get(station).scanRandomItem(); // not functional, just setting an idea
	}
	
	/**
	 * Attendant removes the banknotes from station storage.
	 * 
	 * GUI will check if the station is occupied by a customer, attendant, or both
	 * and will only call this if an attendant is involved.
	 * 
	 * @param station
	 */
	public void emptyBanknoteStorage(int station) {
		
		// TODO: attendant class doesn't exist yet; can't communicate with user
		// attendant.emptyBanknoteStorage(station);
	}
	
	
	
	/**
	 * Custom generic 2-tuple.  Doesn't need to be here but we don't 
	 * have a local utilities class and we probably don't need one.
	 * 
	 * Code source from https://stackoverflow.com/questions/4777622/creating-a-list-of-pairs-in-java
	 * 
	 * @author joshuaplosz
	 *
	 * @param <L>
	 * @param <R>
	 */
	private class Pair<L, R> {
		private L left;
		private R right;
		public Pair(L l, R r) {
			left = l;
			right = r;
		}
		
		public L getL() 		{ return left; }
		public R getR() 		{ return right; }
		public void setL(L l) 	{ left = l; }
		public void setR(R r) 	{ right = r; }
	}
}
