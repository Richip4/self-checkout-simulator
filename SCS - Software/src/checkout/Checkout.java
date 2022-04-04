package checkout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

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
	private SelfCheckoutStation scs;
	private Customer customer;

	public Checkout(SelfCheckoutStation scs) {
		this.scs = scs;

		// Connect with interrupts
		// TODO:
	}

	/**
	 * @param customer
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
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
		}

		BigDecimal subtotal = this.customer.getCartSubtotal();

		if (subtotal.compareTo(customer.getCurrency()) < 1 // x.compareTo(y): returns 1 if x is < y
															// this may be backwards ^ consider the reverse when testing
				&& subtotal.compareTo(BigDecimal.ZERO) != 0) {
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
	public void makeChange() {
		BigDecimal change = this.customer.getAccumulatedCurrency().subtract(this.customer.getCartSubtotal());

		// Establish what banknote and coin denominations are available
		ArrayList<Cash> availableDenominations = new ArrayList<>();
		ArrayList<Cash> acceptableDenominations = new ArrayList<>();

		// go through all banknotes and record all the accepted as well as the available
		// notes
		scs.banknoteDispensers.forEach((value, dispenser) -> {
			Cash cash = new Cash(value);
			acceptableDenominations.add(cash);

			if (dispenser.size() > 0) {
				availableDenominations.add(cash);
			}
		});

		// same thing with coins
		scs.coinDispensers.forEach((value, dispenser) -> {
			Cash cash = new Cash(value);
			acceptableDenominations.add(cash);

			if (dispenser.size() > 0) {
				availableDenominations.add(cash);
			}
		});

		// sort the accepted denominations to get the lowest possible cash value
		Collections.sort(acceptableDenominations);
		Cash lowestDenom = acceptableDenominations.get(0);

		// sort the availableDenominations in descending order to prioritize
		// dispensing highest values first
		Collections.sort(availableDenominations, Collections.reverseOrder());

		// Starting from the highest value available denomination, dispense denomination
		// to customer
		// and substract value from 'change' only if the 'change' amount is not less
		// than it's value.
		// Otherwise, remove denomination from consideration.
		while (availableDenominations.size() > 0 && change.compareTo(BigDecimal.ZERO) > 0) {
			Cash cash = availableDenominations.get(0);
			if (change.compareTo(cash.value) >= 0) {

				if (cash.type.equals("banknote")) {
					try {
						scs.banknoteDispensers.get(cash.value.intValue()).emit();
						change.subtract(cash.value);
					} catch (EmptyException | DisabledException e) {
						// can no longer emit denominations from this dispenser
						availableDenominations.remove(0);
					} catch (OverloadException e) {
						// there is a banknote still in the output. try to emit again.
						continue;
					}

				} else if (cash.type.equals("coin")) {
					try {
						scs.coinDispensers.get(cash.value).emit();
						change = change.subtract(cash.value);
					} catch (EmptyException | DisabledException e) {
						// can no longer emit denominations from this dispenser
						availableDenominations.remove(0);
					} catch (OverloadException e) {
						// currently coinDispenser never throws this exception. Unnecessary to handle
						// it.
					}
				}

			} else {
				// current denomination is bigger than 'change' amount. Remove it from
				// consideration.
				availableDenominations.remove(0);
			}
		}
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
