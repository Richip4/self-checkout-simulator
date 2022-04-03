package interrupt;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BarcodeScannerObserver;
import org.lsmr.selfcheckout.devices.observers.ElectronicScaleObserver;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.Product;

import store.Inventory;
import user.Customer;

/**
 * Currenty handles any barcode scanner events and electronic scale events from
 * the bagging area.
 * Easily extendable to incorporate PLU codes in future iterations.
 * 
 * @author joshuaplosz
 *
 */
public class ProcessItemHandler implements BarcodeScannerObserver, ElectronicScaleObserver {

	SelfCheckoutStation scs;
	private Customer customer;
	private double currentItemsWeight = 0.0;
	private double weightBeforeBagging; // Weight on scale before most recently scanned item is bagged
	private boolean unexpectedItem = false;
	private boolean waitingForBagging;
	private double scaleResetWeight = 0.0;
	private boolean scaleOverloaded;
	private double discrepancy = 0.1; // Scales have margins of errors, this is how much we allow

	private boolean ownBagsUsed = false;
	private double ownBagWeight = 0;

	public ProcessItemHandler(SelfCheckoutStation scs) {
		this.scs = scs;

		// Attach both scanners
		this.scs.mainScanner.attach(this);
		this.scs.handheldScanner.attach(this);

		// Attach bagging area scale; to get notified
		this.scs.baggingArea.attach(this);
	}

	/**
	 * Sets the current customer to receive notifications from hardware events
	 * 
	 * @param customer
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
		this.currentItemsWeight = 0.0;
		this.weightBeforeBagging = 0.0;
		this.unexpectedItem = false;
		this.waitingForBagging = false;
		this.scaleResetWeight = 0.0;
		this.scaleOverloaded = false;
		this.ownBagsUsed = false;
		this.ownBagWeight = 0;
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// do nothing when barcode scanner or electronic scale is enabled
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// do nothing when barcode scanner or electronic scale is disabled
	}

	/**
	 * When barcode scan event occurs check if store inventory actually contains the
	 * item represented by the barcode. If item is available for purchase disable
	 * the scanner, record the weight of the item scanned, and add the item to the
	 * customers cart. Notify the customer to add the item to the bagging area.
	 */
	@Override
	public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
		if(this.customer == null) {
			return;
		}

		Product product = Inventory.getProduct(barcode);

		if (product != null) {
			this.scs.mainScanner.disable();
			this.scs.handheldScanner.disable();


			if (product instanceof BarcodedProduct) {
				BarcodedProduct barcodedProduct = (BarcodedProduct) product;
				this.currentItemsWeight = barcodedProduct.getExpectedWeight();
			} else {
				this.currentItemsWeight = 0.0;
			}

			try {
				this.weightBeforeBagging = this.scs.baggingArea.getCurrentWeight();
			} catch (OverloadException e) {
				// TODO Auto-generated catch block
			}

			this.customer.addToCart(barcode);
			this.customer.notifyPlaceInBaggingArea();
			this.waitingForBagging = true;
		}
	}

	public void setownBagsUsed(boolean ownBagsUsed) {
		this.ownBagsUsed = ownBagsUsed;
	}

	/**
	 * When electronic scale weight change event occurs under normal operation
	 * compare
	 * the weight of the current item scanned and the scales changed weight. If they
	 * match then re-enable the scanner for customer to continue scanning. If the
	 * weight
	 * change is different than the current scanned item then an unexpected item was
	 * placed in the bagging area. Record the weight the scale needs to return to
	 * and set
	 * a flag for an unexpected item. The scanner can not be re-enabled until the
	 * scale
	 * weight returns to what it was at before the unexpected item was added and the
	 * expected item has been added.
	 */
	@Override
	public void weightChanged(ElectronicScale scale, double weightInGrams) {
		if(this.customer == null) {
			return;
		}

		// Get the weight of the bag and store it, if the customer has said that they
		// want to use their own bags
		if (ownBagsUsed) {
			ownBagWeight = weightInGrams;
			ownBagsUsed = false; // reset boolean so this if statement only runs once

			weightBeforeBagging = weightInGrams; // set the weight before bagging to the weight of the bags on scale

			return; // return once weight is set
		}

		if (!(unexpectedItem || scaleOverloaded)) {
			double weightDiff = currentItemsWeight - (weightInGrams - weightBeforeBagging);
			if (weightDiff < discrepancy && weightDiff > -discrepancy) { // weightDiff > -discrepancy: weightDiff is
																			// positive
				currentItemsWeight = 0.0;
				customer.removePlaceInBaggingArea();
				waitingForBagging = false;

				this.scs.mainScanner.enable();
				this.scs.handheldScanner.enable();
			}

			else {
				customer.notifyUnexpectedItemInBaggingArea();
				unexpectedItem = true;
			}

		}
		// else if (scaleOverloaded) {
		// customer.removeUnexpectedItemInBaggingArea();
		// return;
		// }
		else {
			try {
				double weightDiff = weightBeforeBagging - scale.getCurrentWeight(); // changing weight
				if (weightDiff < discrepancy && weightDiff > -discrepancy) {
					customer.removeUnexpectedItemInBaggingArea();
					unexpectedItem = false;
				}
			} catch (OverloadException e) {

			}
		}
	}

	@Override
	public void overload(ElectronicScale scale) {
		this.scaleOverloaded = true;
		this.scs.mainScanner.disable();
		this.scs.handheldScanner.disable();
	}

	@Override
	public void outOfOverload(ElectronicScale scale) {
		this.scaleOverloaded = false;
		this.scs.mainScanner.enable();
		this.scs.handheldScanner.enable();
	}

	/*
	 * For GUI Usage
	 */
	// public void itemNotBagged(ElectronicScale scale) {
	// customer.notifyPlaceInBaggingArea();
	// }

	public boolean getUnexpectedItem() {
		return this.unexpectedItem;
	}

	public boolean getUseOwnBags() {
		return this.ownBagsUsed;
	}

	public double getWeightBeforeBagging() {
		return this.weightBeforeBagging;
	}

}