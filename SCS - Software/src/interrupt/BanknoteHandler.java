package interrupt;

import java.math.BigDecimal;
import java.util.Currency;

import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BanknoteDispenser;
import org.lsmr.selfcheckout.devices.BanknoteSlot;
import org.lsmr.selfcheckout.devices.BanknoteStorageUnit;
import org.lsmr.selfcheckout.devices.BanknoteValidator;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.*;

import checkout.Checkout;
import user.Customer;

/**
 * Handles the interupts from banknote related hardware.
 * Hardware such as:
 * 		BanknoteDispenser
 * 		BanknoteSlot
 * 		BanknoteStorageUnit
 * 		BanknoteValidator
 * 
 * Communicates between SelfCheckoutStation and Customer  
 * if a Customer currently exists.
 * @author joshuaplosz
 *
 */
public class BanknoteHandler implements BanknoteDispenserObserver, 
										BanknoteSlotObserver, 
										BanknoteStorageUnitObserver, 
										BanknoteValidatorObserver {

	private SelfCheckoutStation scs;
	private Customer customer = null;
	private Checkout checkout = null;;

	// record latest processed banknote(bn)
	private boolean 	bnDetected = false;
	private BigDecimal	bnValue = BigDecimal.ZERO;
	
	public BanknoteHandler(SelfCheckoutStation scs) {
		this.scs = scs;
		// attaches itself as an observer to all related hardware
		scs.banknoteInput.attach(this);
		scs.banknoteOutput.attach(this);
		scs.banknoteValidator.attach(this);
		scs.banknoteDispensers.forEach((k, v) -> v.attach(this));
	}

	public BanknoteHandler(SelfCheckoutStation scs, Checkout checkout) {
		this(scs);
		this.checkout = checkout;
	}
	
	/**
	 * Sets the current customer to receive notifications from hardware events
	 * @param customer
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	/**
	 * Gets the current customer using the station
	 * @return 	null if no customer exists
	 * 			the current customer if one exists
	 */
	public Customer getCustomer() {
		return customer;
	}
	
	/**
	 * Sets the checkout instance for handling banknotes when dispensing change.
	 * @param checkout
	 */
	public void setCheckout(Checkout checkout) {
		this.checkout = checkout;
	}
	
	/**
	 * Check each banknote related device to determine which device to handle enable.
	 * 
	 * Currently only acknowledges banknote input slot enable, removing the banknote
	 * slot input disabled notification from the customer.
	 */
	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		if (device.equals(scs.banknoteInput)) {
			if (customer != null) customer.removeBanknoteInputDisabled();
		}
		if (device.equals(scs.banknoteOutput)) 		/* Nothing happens when banknote output slot is enabled*/ ;
		if (device.equals(scs.banknoteValidator)) 	/* Nothing happens when banknote validator is enabled*/ ;
		scs.banknoteDispensers.forEach((k, d) -> { 
			if ( device.equals(d))					/* Nothing happens when banknote dispenser is enabled*/ ;});
	}

	/**
	 * Check each banknote related device to determine which device to handle disable.
	 * 
	 * Currently only acknowledges banknote input slot disable, sending a banknote slot
	 * input disable notification to the customer.
	 */
	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		if (device.equals(scs.banknoteInput)) {
			if (customer != null) customer.notifyBanknoteInputDisabled();
		}
		if (device.equals(scs.banknoteOutput)) 		/* Nothing happens when banknote output slot is disabled*/ ;
		if (device.equals(scs.banknoteValidator)) 	/* Nothing happens when banknote validator is disabled*/ ;
		scs.banknoteDispensers.forEach((k, d) -> { 
			if ( device.equals(d))					/* Nothing happens when banknote dispenser is disabled*/ ;});
	}

	/**
	 * Sets flag to acknowledge received banknote and updates the current banknotes value.
	 */
	@Override
	public void validBanknoteDetected(BanknoteValidator validator, Currency currency, int value) {
		bnDetected = true;
		bnValue = BigDecimal.valueOf(value);
	}

	/**
	 * Sends the customer an invalid banknote notification 
	 */
	@Override
	public void invalidBanknoteDetected(BanknoteValidator validator) {
		if (customer != null) customer.notifyInvalidBanknote();
	}

	/**
	 * Disables the banknote input slot
	 */
	@Override
	public void banknotesFull(BanknoteStorageUnit unit) {
		scs.banknoteInput.disable();
	}

	/**
	 * Add value of current banknote to the customers accumlated currency
	 * when a new banknote has been previously detected via validBanknoteDetected.
	 */
	@Override
	public void banknoteAdded(BanknoteStorageUnit unit) {
		if (bnDetected) {
			if (customer != null) customer.addCurrency(bnValue);
		}
		
		bnDetected = false;
		bnValue = BigDecimal.ZERO;
	}

	@Override
	public void banknotesLoaded(BanknoteStorageUnit unit) {
		// We don't currently do anything when banknote storage units are loaded
	}

	/**
	 * Enables the banknote input slot
	 */
	@Override
	public void banknotesUnloaded(BanknoteStorageUnit unit) {
		scs.banknoteInput.enable();
	}

	@Override
	public void banknoteInserted(BanknoteSlot slot) {
		// We don't currently do anything when a banknote is inserted		
	}

	/**
	 * Sends the customer a banknote ejected notification.
	 */
	@Override
	public void banknoteEjected(BanknoteSlot slot) {
		if (customer != null) customer.notifyBanknoteEjected();
		
	}

	/**
	 * Removes the banknote ejected notification from the customer.
	 * 
	 * If the checkout is in the middle of dispensing change to the customer
	 * and they have removed a banknote from the output slot then continue
	 * dispensing change.
	 */
	@Override
	public void banknoteRemoved(BanknoteSlot slot) {
		if (customer != null) customer.removeBanknoteEjected();
		
		if (checkout != null) {
			if (checkout.isMakingChange()) {
				checkout.continueMakingChange();
			}
		}
	}

	@Override
	public void moneyFull(BanknoteDispenser dispenser) {
		// We don't currently do anything with the banknote dispenser
	}

	@Override
	public void banknotesEmpty(BanknoteDispenser dispenser) {
		// We don't currently do anything with the banknote dispenser		
	}

	@Override
	public void billAdded(BanknoteDispenser dispenser, Banknote banknote) {
		// We don't currently do anything with the banknote dispenser		
	}

	@Override
	public void banknoteRemoved(BanknoteDispenser dispenser, Banknote banknote) {
		// We don't currently do anything with the banknote dispenser		
	}

	@Override
	public void banknotesLoaded(BanknoteDispenser dispenser, Banknote... banknotes) {
		// We don't currently do anything with the banknote dispenser		
	}

	@Override
	public void banknotesUnloaded(BanknoteDispenser dispenser, Banknote... banknotes) {
		// We don't currently do anything with the banknote dispenser
	}
	
	public boolean isBanknoteDetected() {
		return bnDetected;
	}
	
	public BigDecimal getBanknoteValue() {
		return bnValue;
	}

}
