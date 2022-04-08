package user;

import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.products.Product;

import application.AppControl;
import software.SelfCheckoutSoftware;
import store.Inventory;

/**
 * This class represents the attendant and their actions.
 * 
 * @author Michelle Cheung
 *
 */
public class Attendant extends User {	

	private boolean unexpectedItemDecision;

	private String username;
	private String password;
	
/**
 * remove item if it is in cart
 * if item is not in cart, ignore and move on
 */
	public void removeProduct(SelfCheckoutSoftware software, int index) {	
		software.getCustomer().removeProduct(index);
	}
	
	/**
	 * Method used to simulate the attendant adding paper to the receipt printer device.
	 * @param software a SelfCheckoutSoftware object
	 * @param amount the amount of paper to be inserted into the paper cartridge.
	 */
	// public void addPaper(SelfCheckoutSoftware software, int amount) {
	// 	try {
	// 		software.getSelfCheckoutStation().printer.addPaper(amount);
	// 		software.updatePaperUsed(amount);
	// 	} catch (OverloadException e) {
	// 		// notify the attendant that the maximum capacity of paper for the receipt printer has been overloaded
	// 		software.getSupervisionSoftware().notifyObservers(observer -> observer.receiptPrinterPaperOverloaded(software));
	// 	}
	// }
	
	/**
	 * Method used to simulate the attendant adding ink to the receipt printer device.
	 * @param software a SelfCheckoutSoftware object
	 * @param amount the amount of ink to be inserted into the ink cartridge.
	 */
	// public void addInk(SelfCheckoutSoftware software, int amount) {
	// 	try {
	// 		software.getSelfCheckoutStation().printer.addInk(amount);
	// 		software.updateInkUsed(amount);
	// 	} catch (OverloadException e) {
	// 		// notify the attendant that the maximum capacity of ink for the receipt printer has been overloaded
	// 		software.getSupervisionSoftware().notifyObservers(observer -> observer.receiptPrinterInkOverloaded(software));
	// 	}
	// }
	
	@Override
	public int getUserType() {
		return AppControl.ATTENDANT;
	}

	/**
	 * The GUI will set the what the attendant made about the unexpected item.
	 */
	public void setUnexpectedItemDecision(boolean value) {
		this.unexpectedItemDecision = value;
	}

	/**
	 * Get the decision the attendant made about the unexpected Item
	 */
	public boolean getUnexpectedItemDecision() {
		return this.unexpectedItemDecision;
	}

	///////////////////// SupervisionSoftware.java /////////////////////

	/**
	 * Set the attendant login information.
	 */
	public void setLogin(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * Get the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Get the password
	 */
	public String getPassword() {
		return password;
	}

	///////////////////// SupervisionSoftware.java /////////////////////

}
