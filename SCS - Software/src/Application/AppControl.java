package Application;

import java.util.List;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;

import software.SelfCheckoutSoftware;
import software.SupervisionSoftware;
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
	private static SupervisionStation supervisor;
	private SupervisionSoftware supervisorSoftware;
	
	// list of self-checkout stations
	private List<SelfCheckoutStation> selfStations;
	private List<SelfCheckoutSoftware> selfStationSoftwares;
	
	// list of people visiting the stations
	// NOTE: active users not at an actual station are excluded
	private User[] users;
	
	// the type of user combination at each station
	private int[] stationsUserType;
	
	// the user we are actively simulating
	private User activeUser;
	
	public AppControl() {
		supervisor = Main.Tangibles.SUPERVISION_STATION;
		supervisorSoftware = Store.getSupervisionSoftware();
		
		selfStations = supervisor.supervisedStations();
		selfStationSoftwares = supervisorSoftware.getSoftwareList();
		
		// max number of users equals number of customer stations + 1 attendant
		users = new User[selfStations.size() + 1]; 
		stationsUserType = new int[selfStations.size() + 1]; 
	}
	
	/**
	 * add a new customer and set them as the active user
	 * NOTE: overrides previous active user if they were not at a station
	 */
	public void addNewCustomer() {
		activeUser = new Customer();
	}

	/**
	 * add a new attendant and set them as the active user
	 * NOTE: overrides previous active user if they were not at a station
	 */
	public void addNewAttendant() {
		activeUser = new Attendant();
	}
	
	/**
	 * sets the active user to the next user in the list of users
	 */
	public void nextActiveUser() {
		for (int i = 0; i < users.length; i++) {
			if (users[i] == activeUser) {
				if (i < users.length - 1) {
					activeUser = users[i+1];
				} else {
					activeUser = users[0];
				}
			}
		}
	}
	
	/**
	 * sets the active user to the previous user in the list of users
	 */
	public void prevActiveUser() {
		for (int i = 0; i < users.length; i++) {
			if (users[i] == activeUser) {
				if (i > 0) {
					activeUser = users[i-1];
				} else {
					activeUser = users[users.length - 1];
				}
			}
		}
	}
	
	public User getActiveUser() {
		return activeUser;
	}
	
	public User getUserAt(int station) {
		return users[station];
	}
	
	public User[] getActiveUsers() {
		return users;
	}
	
	/**
	 * customer approaches a station
	 * @param station - specific stations index
	 */
	public void customerUsesStation(int station) {
		addStationUserType(station, CUSTOMER);
		users[station] = activeUser;
		selfStationSoftwares.get(station).setUser(activeUser);
	}
	
	/**
	 * attendant approaches a station
	 * @param station - specific stations index
	 */
	public void attendantUsesStation(int station) {
		addStationUserType(station, ATTENDANT);
		users[station] = activeUser;
		selfStationSoftwares.get(station).setUser(activeUser);
	}
	
	/**
	 * update the stations user type and set the user to null
	 * @param station - self-checkout station index to update
	 */
	public void customerLeavesStation(int station) {
		removeStationUserType(station, CUSTOMER);
		for (int i = 0; i < users.length; i++) {
			if (users[i] == activeUser) {
				users[i] = null;
				selfStationSoftwares.get(station).removeUser(activeUser);
				return;
			}
		}
	}

	/**
	 * update the stations user type and set the user to null
	 * @param station - self-checkout station index to update
	 */ 
	public void attendantLeavesStation(int station) {
		removeStationUserType(station, ATTENDANT);
		for (int i = 0; i < users.length; i++) {
			if (users[i] == activeUser) {
				users[i] = null;
				selfStationSoftwares.get(station).removeUser(activeUser);
				return;
			}
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
