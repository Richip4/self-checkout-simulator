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
import user.Attendant;
import user.Customer;
import user.User;

public class AppControl {
	
	// types of present users at a self-checkout station
	public static final int NO_USER = 0;
	public static final int CUSTOMER = 1;
	public static final int ATTENDANT = 2;
	public static final int BOTH = 3;
	
	// the attendant station that oversees the self-checkout stations
	private SupervisionStation supervisor;
	
	// list of self-checkout stations
	private List<SelfCheckoutStation> selfStations;
	
	// list of people visiting the stations
	private List<User> users = new ArrayList<>();
	
	// the type of user combination at each station
	private int[] stationsUserType;
	
	// the user we are actively simulating
	private User activeUser;
	
	public AppControl(SupervisionStation supervisor) {
		this.supervisor = supervisor;
		selfStations = supervisor.supervisedStations();
		stationsUserType = new int[selfStations.size()]; 
	}
	
	/**
	 * add a new customer and set them as the active user
	 */
	public void addNewCustomer() {
		activeUser = new Customer();
		users.add(activeUser);
	}

	/**
	 * add a new attendant and set them as the active user
	 */
	public void addNewAttendant() {
		activeUser = new Attendant();
		users.add(activeUser);
	}
	
	/**
	 * removes a user that left the simulation
	 * @param u - a User to remove
	 */
	public void removeUser(User u) {
		users.remove(u);
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
	
	public User getUserAt(int station) {
		return users.get(station);
	}
	
	public List<User> getActiveUsers() {
		return users;
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
}
