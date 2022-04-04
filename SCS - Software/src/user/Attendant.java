package user;

import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;

import software.SelfCheckoutSoftware;
import store.Inventory;

/**
 * This class represents the attendant and their actions.
 * 
 * @author Michelle Cheung
 *
 */
public class Attendant extends User {

	private SelfCheckoutSoftware software;	
	private List<Product> cart = software.getCustomer().getCart(); //Keeping the cart as a list of Products
	
/**
 * remove item if it is in cart
 * if item is not in cart, ignore and move on
 */
	public void removeProduct(SelfCheckoutSoftware software, Product p) {
		software = this.software;	
		software.getCustomer().removeProduct(p);
	}
	
	
	public void lookupProduct(PriceLookupCode plu) {
		if (Inventory.getProduct(plu) != null) { 
			software.getCustomer().addToCart(Inventory.getProduct(plu));
		}
		else {}//TODO Display an error on the GUI that the product is invalid
		
	}

}
