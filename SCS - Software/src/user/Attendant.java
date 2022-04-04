package user;

import java.util.List;

import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

import store.Inventory;

/**
 * This class represents the attendant and their actions.
 * 
 * @author Michelle Cheung
 *
 */
public class Attendant {

	private Customer customer;

	/**
	 * remove item if it is in cart
	 * if item is not in cart, ignore and move on
	 */
	public void removeBarcodedProductFromPurchases(BarcodedProduct p) {
		this.customer.removeProduct(p);
	}

	public void removePLUProductFromPurchases(PLUCodedProduct p) {
		this.customer.removeProduct(p);
	}

	public void lookupProduct(PriceLookupCode plu) {
		if (Inventory.getProduct(plu) != null) {
			this.customer.addToCart(Inventory.getProduct(plu));
		} else {
		} // TODO Display an error on the GUI that the product is invalid

	}

}
