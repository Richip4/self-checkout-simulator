package user;

import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

import application.AppControl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer extends User {

	// used to organize list of products
	private class Triple<T> {
		private T product;
		private Double weight;
		private Integer key;

		public Triple(T product, Double weight, Integer index) {
			this.product = product;
			this.weight = weight;
			this.key = index;
		}

		public T getProduct() {
			return this.product;
		}

		public Double getWeight() {
			return this.weight;
		}

		public Integer getKey() {
			return this.key;
		}
	}

	// List of both PLU and Bar coded products using the triple data type
	private List<Triple<BarcodedProduct>> BarCodedProducts = new ArrayList<Triple<BarcodedProduct>>();
	private List<Triple<PLUCodedProduct>> PLUcodedProducts = new ArrayList<Triple<PLUCodedProduct>>();

	// Used to assign a unique key to all of the products added to the customer
	// "cart", so customer/attendant can remove item by this unique key
	private int keyIncrement = 0;

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

	public void addToCart(BarcodedProduct BarcodedProduct) {
		// Double is null and it is not needed to calculate the cost
		BarCodedProducts.add(new Triple<BarcodedProduct>(BarcodedProduct, null, keyIncrement));
		keyIncrement++;
	}

	public void addToCart(PLUCodedProduct PLU, double Weight) {
		PLUcodedProducts.add(new Triple<PLUCodedProduct>(PLU, Weight, keyIncrement));
		keyIncrement++;
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

	public void removeProduct(int key) {
		for (Triple<PLUCodedProduct> triple : PLUcodedProducts) {
			if (triple.getKey() == key) {
				PLUcodedProducts.remove(triple);
				return;
			}
		}
		for (Triple<BarcodedProduct> triple : BarCodedProducts) {
			if (triple.getKey() == key) {
				BarCodedProducts.remove(triple);
				return;
			}
		}
	}

	public BigDecimal getCartSubtotal() {
		BigDecimal subtotal = BigDecimal.ZERO;

		for (Triple<BarcodedProduct> triple : this.BarCodedProducts) {
			if (triple.getProduct().isPerUnit()) {
				subtotal = subtotal.add(triple.getProduct().getPrice());
			}
		}
		for (Triple<PLUCodedProduct> triple : this.PLUcodedProducts) {
			BigDecimal b1 = new BigDecimal(triple.getWeight().toString());
			b1 = b1.divide(new BigDecimal(1000));
			subtotal = subtotal.add(triple.getProduct().getPrice().multiply(b1));
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
		for (Triple<PLUCodedProduct> triple : PLUcodedProducts) {
			list.add(triple.getProduct());
		}
		for (Triple<BarcodedProduct> triple : BarCodedProducts) {
			list.add(triple.getProduct());
		}

		return Collections.unmodifiableList(list);
	}

	/**
	 * Returns a map containing the product key and the product itself
	 * 
	 * @return Map<Integer, Product>
	 */
	public Map<Integer, Product> getCartWithKey() {
		Map<Integer, Product> map = new HashMap<Integer, Product>();
		for (Triple<PLUCodedProduct> triple : PLUcodedProducts) {
			map.put(triple.getKey(), triple.getProduct());
		}
		for (Triple<BarcodedProduct> triple : BarCodedProducts) {
			map.put(triple.getKey(), triple.getProduct());
		}
		return Collections.unmodifiableMap(map);
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
