package bank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class that acts as a bank.
 * Allows us to create an account, add funds to an account, bill an account.
 * @author Tyler Chen
 *
 */
public class Bank {
	
	/**
	 * Class for creating a BankAccount so that we can check whether a card has sufficient funds.
	 * @author TylerChen
	 */
	
	public class BankAccount {
		private String cardNumber;
		private String cardHolder;
		private String cvv;
		private String cardType;
		private BigDecimal accountBalance;
		
		public BankAccount(String cardNumber, String cardHolder, String cvv, String cardType){
			this.cardNumber = cardNumber;
			this.cardHolder = cardHolder;
			this.cvv = cvv;
			this.cardType = cardType;
			accountBalance = BigDecimal.ZERO;
		}
		
		public void addFunds(BigDecimal amount) {
			this.accountBalance = accountBalance.add(amount);
		}
		
		/**
		 * Removes an amount of money from an account. Does not allow negatives.
		 * @param amount - the amount wished to be removed
		 * @return true/false if the removal was complete
		 */
		public boolean removeFunds(BigDecimal amount) {
			if (amount.compareTo(accountBalance) <= 0) {
				accountBalance = accountBalance.subtract(amount);
				return true;
			}else {
				return false;
			}
		}
		
		public BigDecimal getBalance() {
			return this.accountBalance;
		}
		
		public String getCardNumber() {
			return cardNumber;
		}
	}
	
	//contains all accounts
	private HashMap<String, BankAccount> accounts = new HashMap<String, BankAccount>();
	
	//List of random generated card numbers and CVVs
	private Set<Integer> cardNums;
	private Set<Integer> cardCVVs;
	
	/**
	 * 
	 * @param maxNumOfAccounts
	 */
	public Bank(int maxNumOfAccounts) {
		cardNums = new HashSet<Integer>(maxNumOfAccounts);
		cardCVVs = new HashSet<Integer>(maxNumOfAccounts);
		
		//generate random numbers for all those accounts (no repeats)
		for(int i = 0; i < maxNumOfAccounts; i++) {
			cardNums.add((int)ThreadLocalRandom.current().nextInt(10000000, 99999999 + 1));
			cardCVVs.add((int)ThreadLocalRandom.current().nextInt(100, 999 + 1));
		}		
	}
	
	/*
	 * Creates a new account using the random generated numbers. We then remove 
	 * the used cardNums and CVVs and add that account to the accounts
	 */
	public BankAccount createNewAccount(String cardHolder, String cardType) {
		Iterator<Integer> cardNumValue = cardNums.iterator();
		Iterator<Integer> cardCVVsValue = cardCVVs.iterator();
		BankAccount account;
		if (cardNumValue.hasNext() && cardCVVsValue.hasNext()) {
			Integer cardNum = cardNumValue.next();
			Integer cvv = cardCVVsValue.next();
			cardNums.remove(cardNum);
			cardNums.remove(cvv);
			account = new BankAccount(cardNum.toString(), cardHolder, cvv.toString(), cardType);
			accounts.put(cardNum.toString(), account);			
		}
		else
			account = null;
		return account;
	}
	
	/*
	 * Returns the account info given the cardNumber
	 */
	public BankAccount getAccount(String cardNumber) {
		return accounts.get(cardNumber);
	}
	
	/*
	 * Adds funds to an account if that value is positive.
	 */
	public void addFundsToAccount(String cardNumber, BigDecimal amountToAdd) {
		if (amountToAdd.compareTo(BigDecimal.ZERO) > 0)
			accounts.get(cardNumber).addFunds(amountToAdd);
	}
	
	/*
	 * Gets the available funds an account has.
	 */
	public BigDecimal getAccountFunds(String cardNumber) {
		return accounts.get(cardNumber).getBalance();
	}
	
	/**
	 * Bills an account via their cardNumber and cardHolder number, the 2 have to match.
	 * @param cardNumbers - the numbers on the card
	 * @param cardHolder - the card holder name
	 * @param amount - the amount a system wants to bill an account
	 * @return true/false whether the transaction was complete
	 */
	public boolean billAccount(String cardNumbers, String cardHolder, BigDecimal amount) {
		String cardHolderDB = accounts.get(cardNumbers).cardHolder;
		if (cardHolderDB == cardHolder)
			return accounts.get(cardNumbers).removeFunds(amount);
		else
			return false;
	}
}
