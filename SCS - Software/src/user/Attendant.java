package user;

import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.Product;

import Application.AppControl;
import software.SelfCheckoutSoftware;
import store.Inventory;

/**
 * This class represents the attendant and their actions.
 * 
 * @author Michelle Cheung
 *
 */
public class Attendant extends User {	
	
/**
 * remove item if it is in cart
 * if item is not in cart, ignore and move on
 */
	public void removeProduct(SelfCheckoutSoftware software, Product p) {	
		software.getCustomer().removeProduct(p);
	}
	
	
	public void lookupProduct(SelfCheckoutSoftware software, PriceLookupCode plu) {
		if (Inventory.getProduct(plu) != null) { 
			software.getCustomer().addToCart(Inventory.getProduct(plu));
		}
		else {}//TODO Display an error on the GUI that the product is invalid
		
	}


	@Override
	public int getUserType() {
		return AppControl.ATTENDANT;
	}

}
