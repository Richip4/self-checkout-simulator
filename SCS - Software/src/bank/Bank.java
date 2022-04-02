package bank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

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
	public static final List<CardIssuer> issuers = new ArrayList<CardIssuer>();

	/**
	 * Instances of this class are not needed, so the constructor is private.
	 */
	private Bank() {
	}
}
