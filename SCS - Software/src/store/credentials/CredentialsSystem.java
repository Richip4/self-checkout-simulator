package store.credentials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CredentialsSystem {
	
	private static final List<Account> ACCOUNTS = new ArrayList<Account>();

	/**
	 * Instances of this class are not needed, so the constructor is private.
	 */
	private CredentialsSystem(){
	}

	public static void addAccount(Account account) {
		for (Account a : ACCOUNTS)
			if (a.getUsername().equals(account.getUsername()))
				return;
		CredentialsSystem.ACCOUNTS.add(account);
	}
	
	public static void removeAccount(Account account) {
		CredentialsSystem.ACCOUNTS.remove(account);
	}
	
	/**
	 * 
	 * @param username - account username
	 * @param password - account password
	 * @return false if check failed; true if the check succeeded. 
	 */
	public static boolean checkLogin(String username, String password) {

		Account other = new Account(username, password);
		for (Account a : ACCOUNTS)
			if (a.equals(other)) 
				return true;
		return false;
	}

}
