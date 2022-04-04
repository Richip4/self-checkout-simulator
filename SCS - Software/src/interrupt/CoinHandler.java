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

import user.Customer;

/**
 * @author: Mohammed Allam
 * @author: Michelle Cheung
 *
 *          This class handles Coin related hardware,
 *          and the communication between the this.customer and the self-checkout
 *          station.
 *
 */
public class CoinHandler implements CoinDispenserObserver, CoinSlotObserver, CoinStorageUnitObserver, CoinTrayObserver,
		CoinValidatorObserver {

	private SelfCheckoutStation scs;
	private Customer customer;

	private boolean coinDetected = false;
	private boolean coinDetectedIsValid = false;
	private boolean coinDispenserFull = false;
	private BigDecimal coinValue;
	
	private boolean hardwareState;

	public CoinHandler(SelfCheckoutStation scs) {
		this.scs = scs;

		scs.coinTray.attach(this);
		scs.coinSlot.attach(this);
		scs.coinValidator.attach(this);
		scs.coinStorage.attach(this);
		scs.coinDispensers.forEach((k, v) -> v.attach(this));
	}

	// Set this.customer
	public void setCustomer(Customer customer) {
		this.customer = customer;
		this.coinDetected = false;
		this.coinDetectedIsValid = false;
		this.coinDispenserFull = false;
		this.coinValue = BigDecimal.ZERO;
	}

	// Get this.customer
	public Customer getCustomer() {
		return this.customer;
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	// when a coin is inserted, we set coin detected flag to True
	@Override
	public void coinInserted(CoinSlot slot) {
		this.coinDetected = true;
	}

	public boolean getCoinDetected() {
		return this.coinDetected;
	}

	@Override
	public void coinAdded(CoinTray tray) {
	}

	// when an inserted coin is valid, set coin-detected-is-valid flag to True
	@Override
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		if (this.customer != null && this.coinDetected) {
			this.coinDetectedIsValid = true;
		}

		this.coinValue = value;
	}

	public BigDecimal getCoinValue() {
		return this.coinValue;
	}

	public boolean getCoinDetectedIsValid() {
		return this.coinDetectedIsValid;
	}

	// when inserted coin is invalid, we notify this.customer that the coin is invalid
	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		if (this.customer != null && this.coinDetected) {
			this.customer.notifyInvalidCoin();
		}

		this.coinDetectedIsValid = false;
	}

	@Override
	public void coinsLoaded(CoinStorageUnit unit) {
	}

	@Override
	public void coinsUnloaded(CoinStorageUnit unit) {
		this.scs.coinSlot.enable();
	}

	// disables the coin slot when coin storage is full
	@Override
	public void coinsFull(CoinStorageUnit unit) {
		this.scs.coinSlot.disable();
	}

	// if coin dispenser is full & coin is valid;
	// this method adds value of coin to the this.customers accumulated currency
	@Override
	public void coinAdded(CoinStorageUnit unit) {
		if (this.customer != null && coinDetectedIsValid == true) {
			this.customer.addCurrency(coinValue);
		}
	}

	// when coin dispenser is full; set coin dispenser flag is full to true
	@Override
	public void coinsFull(CoinDispenser dispenser) {
		this.coinDispenserFull = true;
	}

	public boolean getCoinDispenserFull() {
		return this.coinDispenserFull;
	}

	@Override
	public void coinsEmpty(CoinDispenser dispenser) {
		// we currently don't do anything when the coin dispenser is empty
	}

	// if coin dispenser is not full & coin is valid;
	// this method adds value of coin to the this.customers accumulated currency
	@Override
	public void coinAdded(CoinDispenser dispenser, Coin coin) {
		if (this.customer != null && this.coinDetectedIsValid == true) {
			this.customer.addCurrency(this.coinValue);
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
		// currently we don't do anything when a coin is unloaded from the coin
		// dispenser
	}

}