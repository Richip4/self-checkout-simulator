package checkout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.Product;

import interrupt.BanknoteHandler;
import interrupt.CoinHandler;
import store.Inventory;
import user.Customer;

/**
 * Handles the configuration of each device in a self checkout station when a
 * customer
 * is ready to checkout.
 * 
 * @author Justin Chua
 * 
 *         Calculates and dispenses change to customer when paying with cash.
 * @author joshuaplosz
 *
 *         Customer can choose to cancel their checkout, go back and add more
 *         items.
 * @author Yunfan Yang
 */
public class Checkout {
	private BigDecimal subtotal; // TODO: Let's name this variable the same everywhere... -Yunfan

	private SelfCheckoutStation scs;
	private Inventory inv;
	private Customer customer;
	private BanknoteHandler banknoteHandler;
	private CoinHandler coinHandler; 
	
	private ArrayList<Cash> acceptableDenominations = new ArrayList<>();
	private Cash lowestDenom;

	public Checkout(SelfCheckoutStation scs) {
		this.scs = scs;
		
		// go through all banknote dispensers and record all the accepted notes
		scs.banknoteDispensers.forEach((value, dispenser) -> {
			acceptableDenominations.add(new Cash(value));
		});
		
		// same thing with coins
		scs.coinDispensers.forEach((value, dispenser) -> {
			acceptableDenominations.add(new Cash(value));
		});
		
		// sort the accepted denominations to get the lowest possible cash value
		Collections.sort(acceptableDenominations);
		lowestDenom = acceptableDenominations.get(0);

		// Connect with interrupts
		this.banknoteHandler = new BanknoteHandler(scs, this);
		this.coinHandler = new CoinHandler(scs, this);
	}

	public Checkout(SelfCheckoutStation scs, Customer customer) {
		this(scs);
		this.customer = customer;
	}

	public Checkout(SelfCheckoutStation scs, Customer customer, Inventory inv) {
		this(scs, customer);
		this.inv = inv;
	}

	/**
	 * @param customer
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public BigDecimal getSubtotal() {
		return new BigDecimal(this.subtotal.toString());
	}

	/**
	 * Customer wish ro proceed to checkout.
	 * Enables/disables each device in a self checkout station.
	 * 
	 * This method will only be invoked after a customer has completed inputting all
	 * of their items.
	 * 
	 */
	public void readyToCheckout() {
		// devices are only configured if there is a customer at the station
		if (this.customer == null) {
			throw new IllegalStateException("No customer at checkout station.");
		}

		// disable the barcode scanner and electronic scale
		scs.mainScanner.disable();
		scs.handheldScanner.disable();
		scs.baggingArea.disable();
		scs.scanningArea.disable();

		// Calculate the subtotal of the items in the customer's cart
		this.subtotal = BigDecimal.ZERO;
		for (Product product : this.customer.getCart()) {
			this.subtotal = this.subtotal.add(product.getPrice());
		}

		this.enableCardReader();
		this.enableBanknoteInput();
		this.enableCoinInput();
	}

	/**
	 * Customer wish to cancel checkout and go back to add more items.
	 * Enables/disables each device in a self checkout station.
	 * 
	 * This method will only be invoked after a customer is ready to checkout.
	 * of their items.
	 * 
	 */
	public void cancelCheckout() {
		// devices are only configured if there is a customer at the station
		// or the customer has not paid clear or the total is just 0
		if (this.customer == null) {
			throw new IllegalStateException("No customer at checkout station.");
		} else if (this.subtotal.compareTo(customer.getCurrency()) < 1 // x.compareTo(y): returns 1 if x is < y
														// this may be backwards ^ consider the reverse when testing
				&& this.subtotal.compareTo(BigDecimal.ZERO) != 0) {
			throw new IllegalStateException("Customer has paid clear");
		}

		// enable the barcode scanner and electronic scale
		this.scs.mainScanner.enable();
		this.scs.handheldScanner.enable();
		this.scs.scanningArea.enable();

		this.disableCardReader();
		this.disableBanknoteInput();
		this.disableCoinInput();
	}

	private void enableBanknoteInput() {
		// enable all input/output devices relating to the banknote slot
		this.scs.banknoteInput.enable();
		this.scs.banknoteOutput.enable();
		this.scs.banknoteValidator.enable();
	}

	private void disableBanknoteInput() {
		// disable all input/output devices relating to the banknote slot
		this.scs.banknoteInput.disable();
		this.scs.banknoteOutput.disable();
		this.scs.banknoteValidator.disable();
	}

	private void enableCoinInput() {
		// enable all input/output devices relating to the coin slot
		this.scs.coinSlot.enable();
		this.scs.coinTray.enable();
		this.scs.coinValidator.enable();
	}

	private void disableCoinInput() {
		// disable all input/output devices relating to the coin slot
		this.scs.coinSlot.disable();
		this.scs.coinTray.disable();
		this.scs.coinValidator.disable();
	}

	private void enableCardReader() {
		// enable all input/output devices relating to the card reader
		this.scs.cardReader.enable();
	}

	private void disableCardReader() {
		// disable all input/output devices relating to the card reader
		this.scs.cardReader.disable();
	}
	
	private BigDecimal remainingChange = BigDecimal.ZERO;
	private boolean doneDispensingChange = true;
	private ArrayList<Cash> availableDenominations = new ArrayList<>();
	
	/**
	 * Returns change to the customer by dispensing banknotes and coins,
	 * prioritizing the highest denomination value.
	 * 
	 * Coins are dispensed consecutively to the coin tray.
	 * 
	 * Banknotes are emitted one at a time, in which the BanknoteSlot assigned
	 * to output will trigger a removedEjectedBanknote event that recursively 
	 * calls this method until we no longer consider banknotes for dispensing
	 * change or the change has been fully dispensed.
	 * 
	 * @param change - total amount to be returned to the customer
	 */
	public void makeChange(BigDecimal change) {
		remainingChange = change;
		
		// only process available denominations if just beggining to make change
		if (doneDispensingChange) {
			doneDispensingChange = false;
	
			// go through all banknote dispensers and record all the accepted notes
			scs.banknoteDispensers.forEach((value, dispenser) -> {
				if (dispenser.size() > 0) 
					availableDenominations.add(new Cash(value));
			});
			
			// same thing with coins
			scs.coinDispensers.forEach((value, dispenser) -> {
				if (dispenser.size() > 0) 
					availableDenominations.add(new Cash(value));
			});
	
			// sort the availableDenominations in descending order to prioritize
			// dispensing highest values first
			Collections.sort(availableDenominations, Collections.reverseOrder());
		}

		// Starting from the highest value available denomination, dispense denomination
		// to customer
		// and substract value from 'change' only if the 'change' amount is not less
		// than it's value.
		// Otherwise, remove denomination from consideration.
		while (availableDenominations.size() > 0 && remainingChange.compareTo(BigDecimal.ZERO) > 0) {
			Cash cash = availableDenominations.get(0);
			if (remainingChange.compareTo(cash.value) >= 0) {
				
				if (cash.type.equals("banknote")) {
					try {
						scs.banknoteDispensers.get(cash.value.intValue()).emit();
						remainingChange = remainingChange.subtract(cash.value);
						return; // wait for removedEjectedBanknote event
					} catch (EmptyException | DisabledException e) {
						// can no longer emit denominations from this dispenser
						availableDenominations.remove(0);
					} catch (OverloadException e) {
						// banknote in the output.  wait for removedEjectedBanknote event
						return;
					}
					
				} else if (cash.type.equals("coin")) {
					try {
						scs.coinDispensers.get(cash.value).emit();
						remainingChange = remainingChange.subtract(cash.value);
					} catch (EmptyException | DisabledException e) {
						// can no longer emit denominations from this dispenser
						availableDenominations.remove(0);
					} catch (OverloadException e) {
						// currently coinDispenser never throws this exception.  Unnecessary to handle it.
					}
				}
				
			} else {
				// current denomination is bigger than 'change' amount.  Remove it from consideration.
				availableDenominations.remove(0);
			}
		}
		
		doneDispensingChange = true;
	}
	
	/**
	 * Used by banknote slot hardware event 'removedEjectedBanknote' to automatically
	 * continue dispensing change when a banknote as been dispensed and successfully removed.
	 * 
	 * Use makeChange(BigDecimal change) to initiate dispensing change to the customer.
	 */
	public void continueMakingChange() {
		makeChange(remainingChange);
	}

	/**
	 * After dispensing all available denominations, when the remaining change is less than
	 * the smallest acceptable denomination, all change was properly dispensed.
	 * Otherwise, the dispensers ran out of cash to complete dispensing change.
	 * 
	 * @return 	true if change is less than the lowest denomination
	 * 			false if the dispensers contain inadequate amounts
	 */
	public boolean changeComplete() {
		return (remainingChange.compareTo(lowestDenom.value) < 0) ? true : false;
	}

	/**
	 * Encapsulates banknotes and coins into a single class.
	 * Used to provide a generic interface for calculating customers change.
	 * 
	 * @author joshuaplosz
	 *
	 */
	private class Cash implements Comparable<Cash> {
		String type;
		BigDecimal value;

		Cash(Integer value) {
			type = "banknote";
			this.value = new BigDecimal(value);
		}

		Cash(BigDecimal value) {
			type = "coin";
			this.value = value;
		}

		@Override
		public int compareTo(Cash other) {
			return this.value.compareTo(other.value);
		}
	}
	
	public boolean isMakingChange() {
		return !doneDispensingChange;
	}
}
