package tests.user;

import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Numeral;
import user.Customer;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * The JUnit test class for the Customer class in SCS - Software.
 *
 * @author Ricky Bhatti
 */
public class CustomerTest
{
    @Test
    public void addCurrencyTest()
    {
        Customer customer = new Customer();
        BigDecimal value = new BigDecimal(100.0);
        customer.addCashBalance(value);
        assertEquals(value, customer.getCashBalance());
    }

    @Test
    public void addToCartTest()
    {
        // TODO: Finish this test, needs a getter for the cart.

        Customer customer = new Customer();
        Numeral[] barcodeNumeral = {Numeral.zero, Numeral.one, Numeral.two, Numeral.three, Numeral.four};
        Barcode barcode = new Barcode(barcodeNumeral);
        customer.addProduct(barcode);
        assertTrue(customer.getCart().get(0)==barcode);
    }

    @Test
    public void getBarcodedItemsInCardTest()
    {
        Customer customer = new Customer();
        assertEquals(new ArrayList <Barcode>(), customer.getCart());
    }

    @Test
    public void notifyBanknoteInputDisabledTest()
    {
        // TODO: Write an actual test, when these functions are implemented in future iteration.
    	// Coverage won't reach this function until implementation is completed in final iteration
    }

    @Test
    public void removeBanknoteInputDisabledTest()
    {
    	// TODO: Write an actual test, when these functions are implemented in future iteration.
    	// Coverage won't reach this function until implementation is completed in final iteration
    }

    @Test
    public void notifyBanknoteEjectedTest()
    {
    	// TODO: Write an actual test, when these functions are implemented in future iteration.
    	// Coverage won't reach this function until implementation is completed in final iteration
    }

    @Test
    public void removeBanknoteEjectedTest()
    {
    	// TODO: Write an actual test, when these functions are implemented in future iteration.
    	// Coverage won't reach this function until implementation is completed in final iteration
    }

    @Test
    public void notifyInvalidBanknoteTest()
    {
    	// TODO: Write an actual test, when these functions are implemented in future iteration.
    	// Coverage won't reach this function until implementation is completed in final iteration
    }

    @Test
    public void notifyInvalidCoinTest()
    {
    	// TODO: Write an actual test, when these functions are implemented in future iteration.
    	// Coverage won't reach this function until implementation is completed in final iteration
    }

    @Test
    public void notifyPlaceInBaggingAreaTest()
    {
        // TODO: Write an actual test, when these functions are implemented.

        Customer customer = new Customer();
        customer.notifyPlaceInBaggingArea();
        assertTrue(customer.getWaitingToBag());
    }

    @Test
    public void removePlaceInBaggingAreaTest()
    {
        // TODO: Write an actual test, when these functions are implemented.

        Customer customer = new Customer();
        customer.removePlaceInBaggingArea();
        assertFalse(customer.getWaitingToBag());
    }

    @Test
    public void notifyUnexpectedItemInBaggingAreaTest()
    {
    	// TODO: Write an actual test, when these functions are implemented in future iteration.
    	// Coverage won't reach this function until implementation is completed in final iteration
    }

    @Test
    public void removeUnexpectedItemInBaggingAreaTest()
    {
    	// TODO: Write an actual test, when these functions are implemented in future iteration.
    	// Coverage won't reach this function until implementation is completed in final iteration
    }

    @Test
    public void acceptUsingCustomBagTest()
    {
        Customer customer = new Customer();

        assertTrue(customer.askForBags(true));
    }

    @Test
    public void rejectUsingCustomBagTest()
    {
        Customer customer = new Customer();

        assertFalse(customer.askForBags(false));
    }

    @Test
    public void provideValidMembershipIdentificationTest()
    {
        Customer customer = new Customer();
        String membershipNumber = "1";

        assertEquals(membershipNumber, customer.promptCustomerForMemberID(membershipNumber));
    }

    @Test()
    public void provideInvalidMembershipIdentificationTest()
    {
        Customer customer = new Customer();
        String membershipNumber = "A";

        assertEquals("", customer.promptCustomerForMemberID(membershipNumber));
    }
}
