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

import software.SelfCheckoutSoftware;
import store.Inventory;
import user.Customer;

/**
 * Currenty handles any barcode scanner events and electronic scale events from
 * the bagging area.
 * Easily extendable to incorporate PLU codes in future iterations.
 * 
 * @author joshuaplosz
 * @author Michelle Cheung
 *
 */
public class ProcessItemHandler extends Handler implements BarcodeScannerObserver, ElectronicScaleObserver {

	private final SelfCheckoutStation scs;
	private final SelfCheckoutSoftware scss;
	private Customer customer;

	private double currentItemsWeight = 0.0;
	private double weightBeforeBagging; // Weight on scale before most recently scanned item is bagged
	private boolean unexpectedItem = false;
	private boolean waitingForBagging;
	private double scaleResetWeight = 0.0;
	private boolean scaleOverloaded;
	private double discrepancy = 0.1;		//Scales have margins of errors, this is how much we allow
	private double ownBagWeight = 0;

	public ProcessItemHandler(SelfCheckoutSoftware scss) {
		this.scss = scss;
		this.scs = this.scss.getSelfCheckoutStation();

		this.attachAll();
		this.enableHardware();
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
	}

	public void attachAll() {
		// Attach both scanners
		this.scs.mainScanner.attach(this);
		this.scs.handheldScanner.attach(this);

		// Attach bagging area scale; to get notified
		this.scs.baggingArea.attach(this);
	}

	/**
	 * Used to reboot/shutdown the software. Detatches the handler so that
	 * we can stop listening or assign a new handler.
	 */
	public void detatchAll() {
		this.scs.mainScanner.detach(this);
		this.scs.handheldScanner.detach(this);
	}

	/**
	 * Used to enable all the associated hardware in a single function.
	 */
	public void enableHardware() {
		this.scs.mainScanner.enable();
		this.scs.handheldScanner.enable();
		this.scs.scanningArea.enable();
		this.scs.baggingArea.enable();
	}

	/**
	 * Used to disable all the associated hardware in a single function.
	 */
	public void disableHardware() {
		this.scs.mainScanner.disable();
		this.scs.handheldScanner.disable();
		this.scs.scanningArea.disable();
		this.scs.baggingArea.disable();
	}

	public void enableBaggingArea() {
		this.scs.baggingArea.enable();
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
		if (this.customer == null) {
			return;
		}

		Product product = Inventory.getProduct(barcode);

		if (product == null) {
			this.scss.notifyObservers(observer -> observer.productCannotFound());
			return;
		}

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

		this.customer.addToCart(product);
		this.scss.notifyObservers(observer -> observer.placeInBaggingAreaBlocked());
		this.waitingForBagging = true;
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
		if (this.customer == null) {
			return;
		}
		
		// Get the weight of the bag and store it, if the customer has said that they
		// want to use their own bags
		if (customer.getUseOwnBags()) {
			ownBagWeight = weightInGrams;
			customer.setOwnBagsUsed(false);	// reset boolean so this if statement only runs once
			
			weightBeforeBagging = weightInGrams;	// set the weight before bagging to the weight of the bags on scale
			
			return;	// return once weight is set
		}

		if (!(unexpectedItem || scaleOverloaded)) {
			double weightDiff = currentItemsWeight - (weightInGrams - weightBeforeBagging);
			if (weightDiff < discrepancy && weightDiff > -discrepancy) { // weightDiff > -discrepancy: weightDiff is
																			// positive
				currentItemsWeight = 0.0;

				this.scss.notifyObservers(observer -> observer.placeInBaggingAreaUnblocked());
				waitingForBagging = false;

				this.scs.mainScanner.enable();
				this.scs.handheldScanner.enable();
			}

			else {
				this.scss.notifyObservers(observer -> observer.unexpectedItemInBaggingAreaDetected());
				unexpectedItem = true;
			}

		} else {
			try {
				

				double weightDiff = weightBeforeBagging - scale.getCurrentWeight(); // changing weight
				if (weightDiff < discrepancy && weightDiff > -discrepancy) {
					this.scss.notifyObservers(observer -> observer.unexpectedItemInBaggingAreaRemoved());
					unexpectedItem = false;
				}
				else if(unexpectedItem) {
					//Blocks station, notifys supervision station of screenblocked, unexpected item. Also notify the gui.
					this.scss.blockSystem();
					this.scss.getSupervisionSoftware().notifyObservers(observer -> observer.unexpectedItemDetected(scss));
					this.scss.getSupervisionSoftware().notifyObservers(observer -> observer.touchScreenBlocked(scss));
					this.scss.notifyObservers(observer -> observer.touchScreenBlocked());
					if(this.scss.getSupervisionSoftware().getAttendant().getUnexpectedItemDecision()){
						unexpectedItem = false;					// ignore unexpected item, it was overridden
						weightBeforeBagging = weightInGrams;	// new weightBeforeBagging is the new weightInGrams
						this.scss.notifyObservers(observer -> observer.unexpectedItemInBaggingAreaRemoved());
						}
					// can add else statement to handle if weight discrepancy is declined
				}
			} catch (OverloadException e) {

			}
		}
	}

	@Override
	public void overload(ElectronicScale scale) {
		this.scaleOverloaded = true;
		this.scss.blockSystem();
	}

	@Override
	public void outOfOverload(ElectronicScale scale) {
		this.scaleOverloaded = false;
		this.scss.blockSystem();
	}

	public boolean getUnexpectedItem() {
		return this.unexpectedItem;
	}
	
	public double getWeightBeforeBagging() {
		return this.weightBeforeBagging;
	}
}