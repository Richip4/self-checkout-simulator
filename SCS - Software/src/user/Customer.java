package user;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.products.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// @author Abdelhak Khalfallah, Tyler Chen, Michelle Cheung
public class Customer {

	// in place of a cart class the cart is a list of barcodes and PLU codes
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

	public void addToCartBarcodedItem(Product barcode) {
		cart.add(barcode);
	}
	
	public void addToCartPLUCodedItem(Product plucode) {
		cart.add(plucode);
		
		//TODO PLS CHECK if it is possible for the cart to have both barcoded items and PLUCoded items
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
		// TODO notify the customer to try their card again, as their card does not match any databases.
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
