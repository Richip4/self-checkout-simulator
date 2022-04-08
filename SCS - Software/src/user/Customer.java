package user;

import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.Product;

import application.AppControl;
import store.Inventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;

public class Customer extends User {

	// in place of a cart class the cart is a list Products
	private List<Product> cart = new ArrayList<Product>();
	private HashMap<Product, Double> PLUcodedItemsWithWeight ; 
	private BigDecimal accumulatedCurrency = BigDecimal.ZERO;
	private boolean ownBagsUsed = false;
	private int numOfPlasticBags = 0;
	private String memberID;
	private 

	public void addCurrency(BigDecimal value) {
		accumulatedCurrency = accumulatedCurrency.add(value);
	}

	public BigDecimal getCurrency() {
		return accumulatedCurrency;
	}

	public void addToCart(Product product) {
		cart.add(product);
	}

	public void removeProduct(Product p) {
		cart.remove(p);
	}

	/**
	 * The GUI handles the customer using the touch screen to find an item
	 * This method would match the PLU code and to a PLU code in the inventory.
	 * If getProduct != null then that means it matches and we would add to cart.
	 * Then it could proceed normally as if it was another PLU coded item.
	 * Both the customer and attendant would be using this method
	 */
	public void lookupProduct(PriceLookupCode plu) {
		if (Inventory.getProduct(plu) != null) {
			addToCart(Inventory.getProduct(plu));
			PLUcodedItemsWithWeight.put(Inventory.getProduct(plu), getWeightDiff());
		} else {
			// TODO Display an error on the GUI that the product is invalid
		}
	}

	public BigDecimal getAccumulatedCurrency() {
		return new BigDecimal(this.accumulatedCurrency.toString());
	}

	public BigDecimal getCartSubtotal() {
		BigDecimal subtotal = BigDecimal.ZERO;

		for (Product product : this.cart) {
			if(product.isPerUnit()){
			subtotal = subtotal.add(product.getPrice());
			}
			else{ //it is by the kilo
				subtotal = subtotal.add(product.getPrice() * new BigDecimal(PLUcodedItemsWithWeight.get(product)));
			}
		}

		return subtotal;
	}

	public List<Product> getCart() {
		return Collections.unmodifiableList(this.cart);
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

	public String getMemberID() {
		return this.memberID.toString();
	}

	// set and get methods for own bags
	// the customer should not be calling this it is used by the scale to set the
	// weight of the bags
	public void setOwnBagsUsed(boolean ownBagsUsed) {
		this.ownBagsUsed = ownBagsUsed;
	}

	public boolean getUseOwnBags() {
		return ownBagsUsed;
	}

	// set and get methods for plastic bags
	public void setPlasticBags(int numOfPlasticBags) {
		this.numOfPlasticBags = numOfPlasticBags;
	}

	@Override
	public int getUserType() {
		return AppControl.CUSTOMER;
	}

	public int getPlasticBags() {
		return numOfPlasticBags;
	}
}
