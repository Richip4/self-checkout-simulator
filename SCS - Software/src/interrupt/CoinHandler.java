package interrupt;

import java.math.BigDecimal;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.CoinSlot;
import org.lsmr.selfcheckout.devices.CoinStorageUnit;
import org.lsmr.selfcheckout.devices.CoinTray;
import org.lsmr.selfcheckout.devices.CoinValidator;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.CoinDispenserObserver;
import org.lsmr.selfcheckout.devices.observers.CoinSlotObserver;
import org.lsmr.selfcheckout.devices.observers.CoinStorageUnitObserver;
import org.lsmr.selfcheckout.devices.observers.CoinTrayObserver;
import org.lsmr.selfcheckout.devices.observers.CoinValidatorObserver;

import checkout.Checkout;
import user.Customer;

/**
* @author: Mohammed Allam
* @author: Michelle Cheung
*
* This class handles Coin related hardware,
* and the communication between the customer and the self-checkout station.
*
*/
public class CoinHandler implements CoinDispenserObserver, CoinSlotObserver, CoinStorageUnitObserver, CoinTrayObserver, CoinValidatorObserver {
	
	private SelfCheckoutStation scs;
	private Customer customer;
	private Checkout checkout;

	private boolean coinDetected = false;
	private boolean coinDetectedIsValid = false;
	private boolean coinDispenserFull = false;
	private BigDecimal coinValue;

	public CoinHandler(SelfCheckoutStation scs) {
		this.scs = scs;
		scs.coinTray.attach(this);
		scs.coinSlot.attach(this);
		scs.coinValidator.attach(this);
		scs.coinStorage.attach(this);
		scs.coinDispensers.forEach((k, v) -> v.attach(this));
	}

	public CoinHandler(SelfCheckoutStation scs, Checkout checkout) {
		this(scs);
		this.checkout = checkout;
	}
	
	// set Customer
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	//get Customer
	public Customer getCustomer() {
		return customer;
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {}
		
	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {}
	
	// when a coin is inserted, we set coin detected flag to True 
	@Override
	public void coinInserted(CoinSlot slot) {
		coinDetected = true;
	}
	
	public boolean getCoinDetected() {
		return coinDetected;
	}
	@Override
	public void coinAdded(CoinTray tray) {}
	
	// when an inserted coin is valid, set coin-detected-is-valid flag to True  
	@Override
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		if(customer != null && coinDetected) {
			coinDetectedIsValid = true;
		}
		coinValue = value;
	}
	
	public BigDecimal getCoinValue() {
		return coinValue;
	}
	
	public boolean getCoinDetectedIsValid() {
		return coinDetectedIsValid;
	}
	
	// when inserted coin is invalid, we notify customer that the coin is invalid 
	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		if(customer != null && coinDetected) {
			customer.notifyInvalidCoin();
		}
		coinDetectedIsValid = false;
	}
	
	@Override
	public void coinsLoaded(CoinStorageUnit unit) {}
	
	@Override
	public void coinsUnloaded(CoinStorageUnit unit) {
		scs.coinSlot.enable();
	}
	
	// disables the coin slot when coin storage is full
	@Override
	public void coinsFull(CoinStorageUnit unit) {
		scs.coinSlot.disable();
	}

	// if coin dispenser is full & coin is valid;
	// this method adds value of coin to the customers accumulated currency
	@Override
	public void coinAdded(CoinStorageUnit unit) {
		if(customer != null && coinDetectedIsValid == true) {
			customer.addCurrency(coinValue);
		}
	}
	
	// when coin dispenser is full; set coin dispenser flag is full to true 
	@Override
	public void coinsFull(CoinDispenser dispenser) {
		coinDispenserFull = true;
	}
	
	public boolean getCoinDispenserFull() {
		return coinDispenserFull;
	}

	@Override
	public void coinsEmpty(CoinDispenser dispenser) {
		// we currently don't do anything when the coin dispenser is empty 
	}

	// if coin dispenser is not full & coin is valid;
	// this method adds value of coin to the customers accumulated currency
	@Override
	public void coinAdded(CoinDispenser dispenser, Coin coin) {
		if(customer != null && coinDetectedIsValid == true) {
			customer.addCurrency(coinValue);
		}
	}

	@Override
	public void coinRemoved(CoinDispenser dispenser, Coin coin) {
		// currently we don't do anything when a coin is removed from the coin dispenser
	}

	@Override
	public void coinsLoaded(CoinDispenser dispenser, Coin... coins) {
		// currently we don't do anything when a coin is loaded in the coin dispenser
	}

	@Override
	public void coinsUnloaded(CoinDispenser dispenser, Coin... coins) {
		// currently we don't do anything when a coin is unloaded from the coin dispenser
	}

}