package bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lsmr.selfcheckout.external.CardIssuer;

/**
 * This class represents the banks database.
 * 
 * This class is static since bank database is universal.
 * Refer to: ProductDatabases.java in hardware package.
 * 
 * Conceptually, each bank is a card issuer.
 * 
 * FIXME: The implementation of this class is not complete, see:
 * https://d2l.ucalgary.ca/d2l/le/422926/discussions/threads/1636487/View
 * 
 * @author Tyler Chen
 * @author Yunfan Yang
 *
 */
public class Bank {
	private static final List<CardIssuer> ISSUERS = new ArrayList<CardIssuer>();

	/**
	 * Instances of this class are not needed, so the constructor is private.
	 */
	private Bank() {
	}

	public static List<CardIssuer> getIssuers() {
		return Collections.unmodifiableList(Bank.ISSUERS);
	}

	public static void addIssuer(CardIssuer issuer) {
		Bank.ISSUERS.add(issuer);
	}

	public static void removeIssuer(CardIssuer issuer) {
		Bank.ISSUERS.remove(issuer);
	}

	public static void clearIssuers() {
		Bank.ISSUERS.clear();
	}
}
