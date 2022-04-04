package user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

/**
 * This class represents the attendant and their actions.
 * 
 * @author Michelle Cheung
 *
 */
public class Attendant {

	private Customer customer;	
	private List<Product> cart = customer.getCart(); //Keeping the cart as a list of barcodes
	
/**
 * remove item if it is in cart
 * if item is not in cart, ignore and move on
 */
	public void removeBarcodedProductFromPurchases(BarcodedProduct p) {
		
		if (cart.contains(p.getBarcode())) {
			cart.remove(p.getBarcode());
		}
	}
	
	public void removePLUProductFromPurchases(PLUCodedProduct p) {
		if (cart.contains(p.getPLUCode())) {
			cart.remove(p.getPLUCode());
		}
	}
	
}
