package user;

import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

import application.AppControl;
import store.Inventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer extends User {

	// used to organize list of products
	private class Triple<A, B, C> {
		private A key;
		private B weight;
		private C index;

		public Triple(A key, B weight, C index) {
			this.key = key;
			this.weight = weight;
			this.index = index;
		}

		public A getKey() {
			return this.key;
		}

		public B getValue() {
			return this.weight;
		}

		public C getIndex() {
			return this.index;
		}

	}

	// List of both PLU and Bar coded products using the triple data type
	private List<Triple<BarcodedProduct, Double, Integer>> BarCodedProducts = new ArrayList<Triple<BarcodedProduct, Double, Integer>>();
	private List<Triple<PLUCodedProduct, Double, Integer>> PLUcodedProducts = new ArrayList<Triple<PLUCodedProduct, Double, Integer>>();
	private BigDecimal accumulatedCurrency = BigDecimal.ZERO;
	private boolean ownBagsUsed = false;
	private int numOfPlasticBags = 0;
	private String memberID;
	// used to index all of the products added to the customer "cart"
	private int index = 0;

	public void addCurrency(BigDecimal value) {
		accumulatedCurrency = accumulatedCurrency.add(value);
	}

	public BigDecimal getCurrency() {
		return accumulatedCurrency;
	}

	public void addToCart(BarcodedProduct BarcodedProduct) {
		// Double is null and it is not needed to calculate the cost
		BarCodedProducts.add(new Triple<BarcodedProduct, Double, Integer>(BarcodedProduct, null, index));
		index++;
	}

	public void addToCart(PLUCodedProduct PLU, double Weight) {
		PLUcodedProducts.add(new Triple<PLUCodedProduct, Double, Integer>(PLU, Weight, index));
		index++;
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
		for (Triple<PLUCodedProduct, Double, Integer> triple : PLUcodedProducts) {
			if (triple.getIndex() == index) {
				PLUcodedProducts.remove(triple);
				return;
			}
		}
		for (Triple<BarcodedProduct, Double, Integer> triple : BarCodedProducts) {
			if (triple.getIndex() == index) {
				BarCodedProducts.remove(triple);
				return;
			}
		}
	}

	public BigDecimal getAccumulatedCurrency() {
		return new BigDecimal(this.accumulatedCurrency.toString());
	}

	public BigDecimal getCartSubtotal() {
		BigDecimal subtotal = BigDecimal.ZERO;

		for (Triple<BarcodedProduct, Double, Integer> triple : this.BarCodedProducts) {
			if (triple.getKey().isPerUnit()) {
				subtotal = subtotal.add(triple.getKey().getPrice());
			}
		}
		for (Triple<PLUCodedProduct, Double, Integer> triple : this.PLUcodedProducts) {
			BigDecimal b1 = new BigDecimal(triple.getValue().toString());
			b1 = b1.divide(new BigDecimal(1000));
			subtotal = subtotal.add(triple.getKey().getPrice().multiply(b1));
		}

		return subtotal;
	}

	public List<Product> getCart() {
		List<Product> list = new ArrayList<Product>();
		for (Triple<PLUCodedProduct, Double, Integer> triple : PLUcodedProducts) {
			list.add(triple.getKey());
		}
		for (Triple<BarcodedProduct, Double, Integer> triple : BarCodedProducts) {
			list.add(triple.getKey());
		}

		return Collections.unmodifiableList(list);
	}

	public Map<Integer, Product> getCartWithKey() {
		Map<Integer, Product> map = new HashMap<Integer, Product>();
		for (Triple<PLUCodedProduct, Double, Integer> triple : PLUcodedProducts) {
			map.put(triple.getIndex(), triple.getKey());
		}
		for (Triple<BarcodedProduct, Double, Integer> triple : BarCodedProducts) {
			map.put(triple.getIndex(), triple.getKey());
		}
		return Collections.unmodifiableMap(map);
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
