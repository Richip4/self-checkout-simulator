package store.credentials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CredentialsSystem {
	
	private final List<Account> ACCOUNTS = new ArrayList<Account>();

	public boolean addAccount(Account account) {
		for (Account a : ACCOUNTS){
			if (a.getUsername().equals(account.getUsername()));
				return false;
		}
		ACCOUNTS.add(account);
		return true;
	}
	
	public boolean removeAccount(Account account) {
		for (Account a : ACCOUNTS){
			if (a.equals(account)){
				ACCOUNTS.remove(a);
				return true;
			}
		}
		return false;
	}
	
	public boolean checkLogin(String username, String password) {
		Account other = new Account(username, password);
		for (Account a : ACCOUNTS)
			if (a.equals(other)) 
				return true;
		return false;
	}

}
