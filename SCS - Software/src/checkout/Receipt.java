package checkout;

import java.math.BigDecimal;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.ReceiptPrinterObserver;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

import software.SelfCheckoutSoftware;
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
	 * Method that iterates through each item in the customer's cart, printing out a
	 * receipt
	 * including the description and price of each item, as well as a subtotal at
	 * the bottom.
	 * @throws OverloadException
	 * @throws EmptyException
	 */
	private void printLine(String line) throws EmptyException, OverloadException {
		for (int t = 0; t < line.length(); t++) {
			// When reaches the maximum character of a line, start a new line
			if (t % (ReceiptPrinter.CHARACTERS_PER_LINE - 1) == 0 && t != 0) {
				this.scs.printer.print('\n');
			}

			if (Character.isWhitespace(line.charAt(t))) {
				this.scs.printer.print(' ');
			} else {
				this.scs.printer.print(line.charAt(t));
			}
		}
		this.scs.printer.print('\n');
	}

	public void printReceipt() throws EmptyException, OverloadException {
		// Print Membership 
		if (this.customer.getMemberID() != null) {
			String membership = "Member ID: " + this.customer.getMemberID();
			this.printLine(membership);
		}

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
			this.printLine(line);
		}

		// once all items (with price) have been printed to the receipt, print the
		// subtotal header at the bottom
		// st is used to print out the Subtotal header at the bottom of the receipt
		BigDecimal subtotal = this.customer.getCartSubtotal();
		String st = "Subtotal: $" + subtotal.toString();
		this.printLine(st);

		// cut the receipt so that the customer can easily remove it
		scs.printer.cutPaper();
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// we don't currently handle any events when the receipt printer is enabled
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
