package checkout;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.ReceiptPrinter;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.ReceiptPrinterObserver;
import org.lsmr.selfcheckout.products.BarcodedProduct;

import store.Inventory;
import user.Customer;

/**
 * Handles receipt printing for a customer once the customer has finished payment.
 * @author Justin Chua
 *
 */
public class Receipt implements ReceiptPrinterObserver{
	
	// declaration of variables used to capture properties of the items in the
	// customer's cart
	private BarcodedProduct currentBarcodedProduct;
	private String currentPrice;
	private BigDecimal subtotal = BigDecimal.ZERO;
	private String itemDescription;
	private ArrayList<Barcode> customerItems;
	
	// declaration of variables used to access methods in control software/hardware
	private SelfCheckoutStation scs;
	private Customer customer;
	private Inventory inventory;
	
	public Receipt(SelfCheckoutStation scs, Customer customer, Inventory inventory) {
		this.scs = scs;
		this.customer = customer;
		this.inventory = inventory;
		
		//attach observer
		scs.printer.attach(this);
		scs.printer.addInk(ReceiptPrinter.MAXIMUM_INK);
		scs.printer.addPaper(ReceiptPrinter.MAXIMUM_PAPER);
		
		customerItems = customer.getBarcodedItemsInCart();
	}
	
	/**
	 * Method that iterates through each item in the customer's cart, printing out a receipt
	 * including the description and price of each item, as well as a subtotal at the bottom.
	 */
	public void printReceipt() {
		// st is used to print out the Subtotal header at the bottom of the receipt
		String st = "Subtotal:";
		// i is used simply as a counter variable for multiple for loops through the method
		int i;
		
		// for loop iterates through each item in customer's cart
		for (Barcode bc: customerItems) {
			// use checkForItem() method to see if the item exists in the store inventory database
			if (inventory.checkForItem(bc) == true) {
				currentBarcodedProduct = (BarcodedProduct)inventory.getProduct(bc);	
			}
			
			// update class variables with appropriate values
			itemDescription = currentBarcodedProduct.getDescription();
			subtotal = subtotal.add(currentBarcodedProduct.getPrice());
			currentPrice = currentBarcodedProduct.getPrice().toString();
			
			// this for loop is responsible for printing the description of the item to the receipt.
			// In order to avoid cases where the description exceeds the maximum amount of characters
			// per line, we add the condition (i < 45) to cut off the description at 45 characters. 
			for (i = 0; i < itemDescription.length() && i < 45; i++) {
				if (Character.isWhitespace(itemDescription.charAt(i))) {
					scs.printer.print(' ');
				} else {
					scs.printer.print(itemDescription.charAt(i));
				}
			}
			
			// print whitespace followed by a dollar sign to the receipt, to separate item description and price
			scs.printer.print(' ');
			scs.printer.print('$');
			
			// this for loop is responsible for printing the price of the item to the receipt
			for (i = 0; i < currentPrice.length(); i++) {
				scs.printer.print(currentPrice.charAt(i));
			}
			
			// once item description and price have been printed, start the next line before returning
			// to the top of the loop
			scs.printer.print('\n');
		}
		
		// once all items (with price) have been printed to the receipt, print the subtotal header at the bottom
		for (i = 0; i < st.length(); i++) {
			scs.printer.print(st.charAt(i));
		}
		
		// print whitespace followed by a dollar sign to the receipt, to separate subtotal header and price
		scs.printer.print(' ');
		scs.printer.print('$');
		
		// this for loop is responsible for printing out the final subtotal value
		for (i = 0; i < subtotal.toString().length(); i++) {
			scs.printer.print(subtotal.toString().charAt(i));
		}
		
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
