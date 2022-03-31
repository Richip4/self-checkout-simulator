package store;

import java.util.HashMap;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.products.Product;

/**
 * Represents the stores inventory.  
 * @author joshuaplosz
 *
 */
public class Inventory {

	/**
	 * Combines the Product and Item classes to represent a 
	 * purchasable product from the store.  Also stores the
	 * quantity of products available for purchase.
	 * @author joshuaplosz
	 * @author Michelle Cheung
	 */
	private class Purchasable {
		public Product product;
		public Item item;
		public int quantity;
		
		public Purchasable(Product p, Item i) {
			product = p;
			item = i;
			quantity = 1;
		}
	}
	
	// data structure used to map Barcode's to Purchasable products
	private HashMap<Barcode, Purchasable> availableProducts = new HashMap<Barcode, Purchasable>();
	
	/**
	 * Used to add products to the stores inventory
	 * @param barcode
	 * @param p - Product that matches the barcode
	 * @param i - Item that matches the barcode
	 */
	public void addToInventory(Barcode barcode, Product p, Item i) {
		if (availableProducts.containsKey(barcode)) {
			availableProducts.get(barcode).quantity += 1;
		} else {
			availableProducts.put(barcode, new Purchasable(p, i));
		}
	}
	
	/**
	 * Used to remove products from the stores inventory
	 * @param barcode
	 * @return true if product has been successfully removed, false otherwise
	 */
	public boolean removeFromInventory(Barcode barcode) {
		if (availableProducts.containsKey(barcode) && availableProducts.get(barcode).quantity >= 1) {
			availableProducts.get(barcode).quantity -= 1;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Check if barcode represents an item in the stores inventory
	 * @param barcode
	 * @return true if item exist, false otherwise
	 */
	public boolean checkForItem(Barcode barcode) {
		return availableProducts.containsKey(barcode) ? true : false;
	}
	
	/**
	 * Retrieve the Item that matches the barcode
	 * @param barcode
	 * @return Item if the barcode exists, null otherwise
	 */
	public Item getItem(Barcode barcode) {
		return availableProducts.get(barcode).item;
	}
	
	/**
	 * Retrieve the Product that matches the barcode
	 * @param barcode
	 * @return Product if the barcode exists, null otherwise
	 */
	public Product getProduct(Barcode barcode) {
		return availableProducts.get(barcode).product;
	}
	
	/**
	 * Retrieve the quantity of items that matches the barcode
	 * @param barcode
	 * @return quantity if the barcode exists, 0 otherwise
	 */
	public int getQuantity(Barcode barcode) {
		return availableProducts.get(barcode).quantity;
	}
}
