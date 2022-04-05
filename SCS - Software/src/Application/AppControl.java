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
	public static final int NO_USER = 0;
	public static final int CUSTOMER = 1;
	public static final int ATTENDANT = 2;
	public static final int BOTH = 3;
	
	// the types of user currently at each 
	// station. Index matches stations number
	private int[] stationsUserType;
	
	// list of people visiting the stations
	private List<User> users = new ArrayList<>();
	private User activeUser;
	
	public AppControl() {
		stationsUserType = new int[1 + 6]; //Store.getSelfCheckoutStations().size()];
	}
	
	/**
	 * adds another user to the simulation
	 * @param userType
	 */
	public void addNewUser(int userType) {
		// TODO: null user to be replaced with attendant when implemented
		activeUser = new User((userType == CUSTOMER) ? new Customer() : null); 
		users.add(activeUser);	
	}
	
	/**
	 * sets the active user to the next user in the list of users
	 */
	public void nextActiveUser() {
		int index = users.indexOf(activeUser);
		if (index == users.size()-1) {
			activeUser = users.get(0);
		} else {
			activeUser = users.get(index+1);
		}
	}
	
	/**
	 * sets the active user to the previous user in the list of users
	 */
	public void prevActiveUser() {
		int index = users.indexOf(activeUser);
		if (index == 0) {
			activeUser = users.get(users.size()-1);
		} else {
			activeUser = users.get(index-1);
		}
	}
	
	public User getActiveUser() {
		return activeUser;
	}
	
	public int getUserAt(int station) {
		return stationsUserType[station];
	}
	
	/**
	 * customer approaches a station
	 * @param station - specific stations index
	 */
	public void customerUsesStation(int station) {
		addStationUserType(station, CUSTOMER);
	}
	
	/**
	 * attendant approaches a station
	 * @param station - specific stations index
	 */
	public void attendantUsesStation(int station) {
		addStationUserType(station, ATTENDANT);
	}
	
	public void customerLeavesStation(int station) {
		removeStationUserType(station, CUSTOMER);
	}
	
	public void attendantLeavesStation(int station) {
		removeStationUserType(station, ATTENDANT);
	}
	
	/**
	 * update stations new type based on it's previous state
	 * Stations can have one of four combiniations of users:
	 * 		- no users
	 * 		- just a customer
	 * 		- just an attendant
	 * 		- or both a customer and attendant
	 * 
	 * @param station - specific stations index
	 * @param user - type of user approaching the station
	 */
	private void addStationUserType(int station, int user) {
		if (stationsUserType[station] == NO_USER) {
			stationsUserType[station] = user;
		} else if (stationsUserType[station] == CUSTOMER) {
			if (user == ATTENDANT) stationsUserType[station] = BOTH; 
		}
		
	}
	
	/**
	 * update stations new type based on it's previous state
	 * Stations can have one of four combiniations of users:
	 * 		- no users
	 * 		- just a customer
	 * 		- just an attendant
	 * 		- or both a customer and attendant
	 * 
	 * @param station - specific stations index
	 * @param user - type of user approaching the station
	 */
	private void removeStationUserType(int station, int user) {
		if (stationsUserType[station] == CUSTOMER) {
			if (user == CUSTOMER) stationsUserType[station] = NO_USER;
		} else if (stationsUserType[station] == ATTENDANT) {
			if (user == ATTENDANT) stationsUserType[station] = NO_USER;
		} else if (stationsUserType[station] == BOTH) {
			if (user == CUSTOMER) {
				stationsUserType[station] = ATTENDANT;
			} else if (user == ATTENDANT) {
				stationsUserType[station] = CUSTOMER;
			}
		}
	}
	
	/**
	 * Represents a person whom interacts with 
	 * with the self-checkout stations.
	 * @author joshuaplosz
	 *
	 */
	public class User {
		
		int station;
		Customer customer = null;
		//Attendant attendant = null;  <-- Attendant needs to be implemented yet
		
		public User(Customer c) {
			customer = c;
		}
		/*
		public User(Attendant a) {
			attendant = a;
		}
		*/
		public int getType() {
			if (customer != null) {
				return CUSTOMER;
			}
			
			return ATTENDANT;
		}
		
		public void setStation(int station) {
			this.station = station;
		}
		
		public int getStation() {
			return station;
		}
	}
}
