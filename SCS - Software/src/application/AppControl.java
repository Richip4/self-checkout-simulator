package application;

import java.util.List;

import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;
import org.lsmr.selfcheckout.products.Product;

import application.Main.Tangibles;
import software.SelfCheckoutSoftware;
import software.SupervisionSoftware;
import software.SelfCheckoutSoftware.Phase;
import store.Store;
import store.credentials.AuthorizationRequiredException;
import store.credentials.CredentialsSystem;
import store.credentials.IncorrectCredentialException;
import user.Attendant;
import user.Customer;
import user.User;

public class AppControl {

	// types of present users at a self-checkout station
	public static final int NO_USER = 0;
	public static final int CUSTOMER = 1;
	public static final int ATTENDANT = 2;
	public static final int BOTH = 3;
	
	// types of cards a customer can use
	public static final int CREDIT = 0;
	public static final int DEBIT = 1;
	public static final int MEMBERSHIP = 2;
	public static final int GIFTCARD = 3;

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
			//System.out.println("Checking station " + i + " for active user");
			if (users[i] == activeUser) {
				do {
					//System.out.println("Checking station " + i + " for next valid user");
					i++;
					if (i > users.length - 1) {
						i = 0;
					}
					activeUser = users[i];
				} while (activeUser == null);
				break;
			}
		}
	}

	/**
	 * sets the active user to the previous user in the list of users
	 */
	public void prevActiveUser() {
		for (int i = 0; i < users.length; i++) {
			//System.out.println("Checking station " + i + " for active user");
			if (users[i] == activeUser) {
				do {
					//System.out.println("Checking station " + i + " for next valid user");
					i--;
					if (i < 0) {
						i = users.length - 1;
					}
					activeUser = users[i];
				} while (activeUser == null);
				break;
			}
		}
		for (int i = 0; i < users.length; i++) {
			if (users[i] == activeUser) { 
				do {
					if (i > 0) {
						activeUser = users[i - 1];
					} else {
						activeUser = users[users.length - 1];
					}
				} while (activeUser == null);
				break;
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
	 * Checks all the stations to see if the active user is 
	 * currently at that station.  
	 * @return the index of the station if found and -1 if 
	 * 			user is not at a station yet.
	 */
	public int getActiveUsersStation() {
		
		if (activeUser.getUserType() == ATTENDANT) {
			// check the attendant station first
			if (users[0] == activeUser) {
				return 0;
			}
		}
		
		// check the self checkout stations
		for (int i = 1; i < users.length; i++) {
			if (users[i] == activeUser) {
				return i;
			}
		}
		
		return -1;
	}

	/**
	 * customer approaches a station
	 * 
	 * @param station - specific stations index
	 */
	public void customerUsesStation(int station) {
		addStationUserType(station, CUSTOMER);
		users[station] = activeUser;
		selfStationSoftwares.get(station - 1).setUser(activeUser);
	}

	/**
	 * attendant approaches a station
	 * 
	 * @param station - specific stations index
	 */
	public void attendantUsesStation(int station) {
		addStationUserType(station, ATTENDANT);
		users[station] = activeUser;
		selfStationSoftwares.get(station - 1).setUser(activeUser);
	}

	/**
	 * update the stations user type and set the user to null
	 * 
	 * @param station - self-checkout station index to update
	 */
	public void customerLeavesStation(int station) {
		removeStationUserType(station, CUSTOMER);
		for (int i = 0; i < users.length; i++) {
			if (users[i] == activeUser) {
				users[i] = null;
				selfStationSoftwares.get(station - 1).removeUser(activeUser);
				return;
			}
		}
	}

	/**
	 * update the stations user type, set the given stations user
	 * to null and remove the attendant from the self stations
	 * software if that is where they left from.
	 * 
	 * @param station - self-checkout station index to update
	 */
	public void attendantLeavesStation(int station) {
		removeStationUserType(station, ATTENDANT);
		users[station] = null;
		if (station > 0) {
			selfStationSoftwares.get(station-1).removeUser(activeUser);
		}
	}

	/**
	 * update stations new type based on it's previous state
	 * Stations can have one of four combiniations of users:
	 * - no users
	 * - just a customer
	 * - just an attendant
	 * - or both a customer and attendant
	 * 
	 * @param station - specific stations index
	 * @param user    - type of user approaching the station
	 */
	private void addStationUserType(int station, int user) {
		if (stationsUserType[station] == NO_USER) {
			stationsUserType[station] = user;
		} else if (stationsUserType[station] == CUSTOMER) {
			if (user == ATTENDANT)
				stationsUserType[station] = BOTH;
		}

	}

	/**
	 * update stations new type based on it's previous state
	 * Stations can have one of four combiniations of users:
	 * - no users
	 * - just a customer
	 * - just an attendant
	 * - or both a customer and attendant
	 * 
	 * @param station - specific stations index
	 * @param user    - type of user approaching the station
	 */
	private void removeStationUserType(int station, int user) {
		if (stationsUserType[station] == CUSTOMER) {
			if (user == CUSTOMER)
				stationsUserType[station] = NO_USER;
		} else if (stationsUserType[station] == ATTENDANT) {
			if (user == ATTENDANT)
				stationsUserType[station] = NO_USER;
		} else if (stationsUserType[station] == BOTH) {
			if (user == CUSTOMER) {
				stationsUserType[station] = ATTENDANT;
			} else if (user == ATTENDANT) {
				stationsUserType[station] = CUSTOMER;
			}
		}
	}

	/**
	 * 
	 * @param station
	 * @return
	 */
	public String getStationState(int station) {
		if (selfStationSoftwares.get(station).getPhase() == Phase.BLOCKING) {
			return "BLOCKED";
		} else if (selfStationSoftwares.get(station).getPhase() == Phase.HAVING_WEIGHT_DISCREPANCY) {
			return "WEIGHT DISCREPANCY";
		} else if (selfStationSoftwares.get(station).getPhase() == Phase.MISSING_ITEM) {
			return "MISSING ITEM";
		} else {
			return "OKAY";
		}
	}

	/**
	 * 
	 * @param station
	 */
	public void toggleBlock(int station) {
		if (selfStationSoftwares.get(station).getPhase() != Phase.BLOCKING) {
			try {
				supervisorSoftware.blockStation(selfStationSoftwares.get(station));
			} catch (AuthorizationRequiredException e) {}

		} else if (selfStationSoftwares.get(station).getPhase() == Phase.BLOCKING) {
			try {
				supervisorSoftware.unblockStation(selfStationSoftwares.get(station));
			} catch (AuthorizationRequiredException e) {}
		}
	}

	/**
	 * 
	 * @param station
	 */
	public void approveStationDiscrepancy(int station) {
		if (selfStationSoftwares.get(station).getPhase() == Phase.HAVING_WEIGHT_DISCREPANCY) {
			try {
				supervisorSoftware.approveWeightDiscrepancy(selfStationSoftwares.get(station));
			} catch (AuthorizationRequiredException e) {}			
		} else if (selfStationSoftwares.get(station).getPhase() == Phase.MISSING_ITEM) {
			try {
				supervisorSoftware.approveMissingItem(selfStationSoftwares.get(station));
			} catch (AuthorizationRequiredException e) {}
		}
	}

	public void customerTapsCreditCard() {
		// TODO Auto-generated method stub
		
	}

	public void customerTapsDebitCard() {
		// TODO Auto-generated method stub
		
	}
	
	public void customerTapsMembershipCard() {
		// TODO Auto-generated method stub
		
	}

	public void customerSwipesCreditCard() {
		// TODO Auto-generated method stub
		
	}

	public void customerSwipesDebitCard() {
		// TODO Auto-generated method stub
		
	}

	public void customerSwipesMembershipCard() {
		// TODO Auto-generated method stub
		
	}

	public void customerInsertCreditCard() {
		// TODO Auto-generated method stub
		
	}

	public void customerInsertDebitCard() {
		// TODO Auto-generated method stub
		
	}

	public void customerInsertMembershipCard() {
		// TODO Auto-generated method stub
		
	}

	public void removeItemFromCustomersCart(int station, int item) {
		List<Product> cart = selfStationSoftwares.get(station).getCustomer().getCart();
		cart.remove(item);
	}

	/**
	 * Attempts to log in the user as an attendant.
	 * @param name input username from user
	 * @param password input password from user
	 * @return true if log in was successful, false otherwise
	 */
	public boolean attendantLogin(String name, String password) {
		try {
			supervisorSoftware.login(name, password);
			activeUser = supervisorSoftware.getAttendant();
			users[0] = activeUser;
			return true;
		} catch (IncorrectCredentialException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Checks if the password provided matches the attendant
	 * currently logged in to the attendant station.
	 * @param password
	 * @return true if password is valid, false otherwise
	 */
	public boolean attendantPassword(String password) {
		Attendant a = supervisorSoftware.getAttendant(); 
		if (a != null) {
			return (CredentialsSystem.checkLogin(a.getUsername(), password)) ? true : false;
		}
		return false;
	}

	public List<Product> getCustomerCart(int station) {
		Customer c = selfStationSoftwares.get(station).getCustomer();
		if (c != null) {
			return c.getCart();
		}
		
		return null;
	}

	public boolean isAtendantLoggedIn() {
		return supervisorSoftware.isLoggedIn();
	}


}