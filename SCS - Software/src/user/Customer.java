package user;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.Product;

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
	private boolean waitingToBag;
	private boolean removeLastAddedItem;

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
	public void lookupProduct(PriceLookupCode plu, double weightInGrams) {
		if (Inventory.getProduct(plu) != null && weightInGrams > 0) { 
			addToCart(Inventory.getProduct(plu));
			PLUcodedItemsWithWeight.put(Inventory.getProduct(plu), weightInGrams);
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

	// following methods are to be implemented with the customer UI

	public void notifyBanknoteInputDisabled() {
		// TODO notify customer of disabled banknote input slot
	}

	public void removeBanknoteInputDisabled() {
		// TODO remove the disabled banknote input slot notification
	}

	public void notifyBanknoteEjected() {
		// TODO notify customer of banknote being ejected in the banknote slot
	}

	public void removeBanknoteEjected() {
		// TODO remove the banknote ejected notification
	}

	public void notifyInvalidBanknote() {
		// TODO notify customer that an invalid banknote was detected
	}

	public void notifyInvalidCoin() {
		// TODO notify customer that an invalid coin was detected
	}

	public void notifyPlaceInBaggingArea() {
		// TODO notify customer must place item in bagging area to proceed
		waitingToBag = true;
	}

	public void notifyCustomerTransactionSuccessful() {
		// TODO notify the customer that their payment was succesful
	}

	public void notifyCustomerToTryCardAgain() {
		// TODO notify the customer to try their card again, as their card does not
		// match any databases.
	}

	public void notifyCustomerInvalidCardType() {
		// TODO either notify them to try again or try a different card.
	}

	public void notifyCustomerIsMember() {
		// Say welcome to the member
		// TODO in the GUI
	}

	public void removePlaceInBaggingArea() {

		waitingToBag = false;

	}

	public void notifyUnexpectedItemInBaggingArea() {
		// TODO notify customer to remove unexpected item in bagging area
	}

	public void notifyItemTooLight() {
		// TODO notify customer to remove unexpected light item in bagging area
	}

	public void removeUnexpectedItemInBaggingArea() {
		// TODO remove the unexpected item notification
		removeLastAddedItem = true;
	}

	/**
	 * We prompt the customer for their memberID if they don't want to tap, insert
	 * or swipe.
	 */
	public String promptCustomerForMemberID(String rawMemberID) {
		String memberID = "";
		try {
			memberID = String.valueOf(Integer.parseInt(rawMemberID));
		} catch (NumberFormatException e) {

		}

		return memberID;
	}

	/*
	 * Asks the customer if they are using their own bags Gets the bags weight in
	 * bagging area scale, so that it can be accounted for in that class.
	 */
	public boolean askForBags(boolean usingOwnBag) {
		if (usingOwnBag) {

			return true;
		} else {
			return false;
		}

	}

	public boolean getWaitingToBag() {
		return waitingToBag;
	}

}
