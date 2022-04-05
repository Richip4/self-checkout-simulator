package user;

import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.Product;

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
	private boolean ownBagsUsed = false;
	private double ownBagWeight = 0;
	private int numOfPlasticBags = 0;
	private BigDecimal giftCardValue;

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

    /**
     * We prompt the customer for their memberID if they don't want to tap, insert
     * or swipe.
     */
    public String promptCustomerForMemberID(String rawMemberID)
    {
        String memberID = "";
        try
        {
            memberID = String.valueOf(Integer.parseInt(rawMemberID));
        } catch (NumberFormatException e)
        {

        }

        return memberID;
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

	//set and get methods for own bags
	public void setOwnBagsUsed(boolean ownBagsUsed) {
		this.ownBagsUsed = ownBagsUsed;
	}

	public boolean getUseOwnBags() {
		return ownBagsUsed;
	}

	public void setOwnBagWeight(double ownBagWeight)
	{
		this.ownBagWeight = ownBagWeight;
	}
	public double getOwnBagWeight()
	{
		return ownBagWeight;
	}

	//set and get methods for plastic bags
	public void setPlasticBags(int numOfPlasticBags)
	{
		this.numOfPlasticBags = numOfPlasticBags;
	}

	public int getPlasticBags()
	{
		return numOfPlasticBags;
	}

	//set and get methods for gift card value
	public BigDecimal getGiftCardValue() {
		return giftCardValue;
	}

	public void setGiftCardValue(BigDecimal giftCardValue) {
		this.giftCardValue = giftCardValue;
	}
}