package user;

import org.lsmr.selfcheckout.products.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer extends User {

	// in place of a cart class I simply used a list of barcodes
	private List<Product> cart = new ArrayList<Product>();
	private BigDecimal accumulatedCurrency = BigDecimal.ZERO;
	private boolean waitingToBag;
	private boolean removeLastAddedItem;

	public void addCurrency(BigDecimal value) {
		accumulatedCurrency = accumulatedCurrency.add(value);
	}

	public BigDecimal getCurrency() {
		return accumulatedCurrency;
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

	public void addToCart(Product product) {
		this.cart.add(product);
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

}
