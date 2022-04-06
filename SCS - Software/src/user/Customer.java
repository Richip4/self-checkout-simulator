package user;

import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.Product;

import Application.AppControl;
import store.Inventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer extends User {

	// in place of a cart class the cart is a list Products
	private List<Product> cart = new ArrayList<Product>();
	private BigDecimal accumulatedCurrency = BigDecimal.ZERO;
	private boolean waitingToBag;
	private boolean removeLastAddedItem;
	private String memberID;

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
			subtotal = subtotal.add(product.getPrice());
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

    /*
     * Asks the customer if they are using their own bags Gets the bags weight in
     * bagging area scale, so that it can be accounted for in that class.
     */
    public boolean askForBags(boolean usingOwnBag)
    {
        if (usingOwnBag)
        {

            return true;
        } else
        {
            return false;
        }

    }

	public boolean getWaitingToBag() {
		return waitingToBag;
	}

	@Override
	public int getUserType() {
		return AppControl.CUSTOMER;
	}

}
