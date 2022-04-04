package store.credentials;

import java.util.HashMap;


public class CredentialsSystem {
	

	
	//contains all accounts
	private HashMap<String, Account> accounts = new HashMap<String, Account>();
	
	/**
	 * Add an account to the database
	 * @param username - desired username
	 * @param password - desired password
	 * @return T/F whether the account has been added or not
	 */
	public boolean createAccount(String username, String password) {
		//if the username already exists
		if (accounts.containsKey(username))
			return false;
		else {
			accounts.put(username, new Account(username, password));
			return true;
		}
	}
	
	/**
	 * Remove an account from the database if the username exists and password matches
	 * @param username - the username of the account
	 * @param password - the password of the account
	 * @return T/F whether the account has been removed or not
	 */
	public boolean removeAccount(String username, String password) {
		//if the username is in the system and the 
		if (accounts.containsKey(username)) 
			if (accounts.get(username).password == password) {
				accounts.remove(username);
				return true;
			} 
		return false;
	}
	
	/**
	 * Checks to see if the login credentials are correct
	 * @param username - the username of the account
	 * @param password - the password of the account
	 * @return T/F whether the username and password are correct (or if
	 * the account even exists)
	 */
	public boolean checkLogin(String username, String password) {
		if (accounts.containsKey(username)) 
			if (accounts.get(username).password == password) {
				return true;
			} 
		return false;
	}
	
	
}
