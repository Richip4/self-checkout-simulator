package checkout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.ReceiptPrinterObserver;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

import software.SelfCheckoutSoftware;
import store.Inventory;
import user.Customer;

/**
 * Handles receipt printing for a customer once the customer has finished
 * payment.
 * 
 * @author Justin Chua
 *
 */
public class Receipt implements ReceiptPrinterObserver {
	// declaration of variables used to access methods in control software/hardware
	private final SelfCheckoutSoftware scss;
	private final SelfCheckoutStation scs;
	private Customer customer;

	public Receipt(SelfCheckoutSoftware scss) {
		this.scss = scss;
		this.scs = this.scss.getSelfCheckoutStation();

		// attach observer
		this.scs.printer.attach(this);
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	/**
	 * Used to reboot/shutdown the software. Detatches the handler so that
	 * we can stop listening or assign a new handler.
	 */
	public void detatchAll(){
		this.scs.printer.detach(this);
	}

	/**
	 * Method that iterates through each item in the customer's cart, printing out a
	 * receipt
	 * including the description and price of each item, as well as a subtotal at
	 * the bottom.
	 */
	public void printReceipt() throws EmptyException, OverloadException {
		// i is used simply as a counter variable for multiple for loops through the
		// method
		int i;

		// for loop iterates through each item in customer's cart
		for (Product product : this.customer.getCart()) {
			String itemDescription = "";
			String currentPrice = product.getPrice().toString();

			if (product instanceof BarcodedProduct) {
				BarcodedProduct barcodedProduct = (BarcodedProduct) product;
				itemDescription = barcodedProduct.getDescription();
			} else if (product instanceof PLUCodedProduct) {
				PLUCodedProduct pluCodedProduct = (PLUCodedProduct) product;
				itemDescription = pluCodedProduct.getDescription();
			}
			
			String line = itemDescription + " $" + currentPrice;

			// this for loop is responsible for printing the description of the item to the receipt.
			// In order to avoid cases where the description exceeds the maximum amount of characters
			// per line, we add the condition (i < 45) to cut off the description at 45 characters. 
			for (i = 0; i < itemDescription.length() && i < 45; i++) {
				if (Character.isWhitespace(itemDescription.charAt(i))) {
					scs.printer.print(' ');
				} else {
					this.scs.printer.print(line.charAt(i));
				}
			}

			// once item description and price have been printed, start the next line before
			// returning
			// to the top of the loop
			this.scs.printer.print('\n');
		}

		// st is used to print out the Subtotal header at the bottom of the receipt
		BigDecimal subtotal = this.customer.getCartSubtotal();
		String st = "Subtotal: $" + subtotal.toString();

		// once all items (with price) have been printed to the receipt, print the
		// subtotal header at the bottom
		for (i = 0; i < st.length(); i++) {
			scs.printer.print(st.charAt(i));
		}

		// cut the receipt so that the customer can easily remove it
		scs.printer.cutPaper();
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// we don't currently handle any events when the receipt printer is disabled
	}

	/**
	 * Disables the receipt printer when the machine runs out of paper.
	 * 
	 * Further functionalities (such as notifying the attendant/maintenance guy that
	 * the machine is out of paper) to be added in future implementations.
	 */
	@Override
	public void outOfPaper(ReceiptPrinter printer) {
		// disable the receipt printer
		scs.printer.disable();
		// future implementation: announce that machine has run out of paper
	}

	/**
	 * Disables the receipt printer when the machine runs out of ink.
	 * 
	 * Further functionalities (such as notifying the attendant/maintenance guy that
	 * the machine is out of ink) to be added in future implementations.
	 */
	@Override
	public void outOfInk(ReceiptPrinter printer) {
		// disable the receipt printer
		scs.printer.disable();
		// future implementation: announce that machine has run out of ink
	}

	@Override
	public void paperAdded(ReceiptPrinter printer) {
		// we don't currently do anything when paper is added to the device
		// future implementation: announce that paper has been added
	}

	@Override
	public void inkAdded(ReceiptPrinter printer) {
		// we don't currently do anything when ink is added to the device
		// future implementation: announce that ink has been added
	}
}
