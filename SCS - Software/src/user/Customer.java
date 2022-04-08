package user;

import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

import application.AppControl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer extends User {

	// used to organize list of products
	public class CartEntry {
		private Product product;
		private Double weight;

		private CartEntry(Product product, Double weight) {
			this.product = product;
			this.weight = weight;
		}

		private CartEntry(CartEntry triple) {
			this.product = triple.product;
			this.weight = triple.weight;
		}

		public Product getProduct() {
			return this.product;
		}

		public Double getWeight() {
			return this.weight;
		}
	}

	// List of both PLU and Bar coded products
	private List<CartEntry> cart = new ArrayList<CartEntry>();

	private BigDecimal cashBalance = BigDecimal.ZERO;
	private boolean ownBagsUsed = false;
	private int numOfPlasticBags = 0;
	private String memberID;

	public void addCashBalance(BigDecimal value) {
		this.cashBalance = this.cashBalance.add(value);
	}

	public BigDecimal getCashBalance() {
		return new BigDecimal(this.cashBalance.toString());
	}

	public void addToCart(BarcodedProduct product) {
		this.cart.add(new CartEntry(product, product.getExpectedWeight()));
	}

	/**
	 * Weight in grams
	 * 
	 * @param product
	 * @param weight
	 */
	public void addToCart(PLUCodedProduct product, double weight) {
		this.cart.add(new CartEntry(product, weight));
	}

	public void addToCart(Product product) {
		if (product instanceof BarcodedProduct) {
			this.addToCart((BarcodedProduct) product);
		} else if (product instanceof PLUCodedProduct) {
			// Due to the need for PLU products to have a weight
			throw new IllegalArgumentException("Use proper addToCart for PLU coded items");
		} else {
			throw new IllegalArgumentException("Not a valid product type");
		}
	}

	public void removeProduct(int index) {
		this.cart.remove(index);
	}

	public void removeProduct(CartEntry product) {
		this.cart.remove(product);
	}

	public BigDecimal getCartSubtotal() {
		BigDecimal subtotal = BigDecimal.ZERO;

		for (CartEntry triple : this.cart) {
			if (triple.getProduct().isPerUnit()) { // If per-unit (barcoded)
				subtotal = subtotal.add(triple.getProduct().getPrice());
			} else { // Else per-kilogram (PLU coded)
				subtotal = subtotal.add(triple.getProduct().getPrice().multiply(new BigDecimal(triple.getWeight()).divide(new BigDecimal(1000))));
			}
		}
		
		return subtotal;
	}

	/**
	 * Returns a list containing all the products in the cart
	 * 
	 * @return List<Product>
	 */
	public List<Product> getCart() {
		List<Product> list = new ArrayList<Product>();

		for (CartEntry triple : this.cart) {
			list.add(triple.getProduct());
		}

		return Collections.unmodifiableList(list);
	}

	/**
	 * Returns a map containing the product itself and its weight
	 * 
	 * @return List<Product>
	 */
	public List<CartEntry> getCartWithWeight() {
		List<CartEntry> list = new ArrayList<CartEntry>();

		for (CartEntry triple : this.cart) {
			list.add(new CartEntry(triple));
		}

		return Collections.unmodifiableList(list);
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

	public String getMemberID() {
		return this.memberID == null ? null : this.memberID.toString();
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
