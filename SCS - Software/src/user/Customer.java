package user;

import org.lsmr.selfcheckout.products.Product;

import application.AppControl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer extends User {

	// in place of a cart class the cart is a list Products
	private List<Product> cart = new ArrayList<Product>();
	private BigDecimal accumulatedCurrency = BigDecimal.ZERO;
	private boolean ownBagsUsed = false;
	private int numOfPlasticBags = 0;
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
