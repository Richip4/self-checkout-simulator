package tests.user;

import application.AppControl;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import store.Inventory;
import user.Customer;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * The JUnit test class for the Customer class in SCS - Software.
 *
 * @author Ricky Bhatti
 */
public class CustomerTest
{
    // Static variables that will be used during testing
    final int[] banknoteDenominations = {5, 10, 20, 50};
    final BigDecimal[] coinDenominations = {new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"), new BigDecimal("1.00"), new BigDecimal("2.00")};
    final String membershipID = "1234";
    BarcodedProduct product1 = new BarcodedProduct(new Barcode(new Numeral[] {Numeral.zero, Numeral.one, Numeral.two, Numeral.three, Numeral.four}), "N/A", new BigDecimal("5.00"), 17.5);
    PLUCodedProduct product2 = new PLUCodedProduct(new PriceLookupCode("1000"), "N/A", new BigDecimal("10.00"));

    Customer customer;

    @Before
    public void setup()
    {
        customer = new Customer();
        Inventory.clear();
    }

    @Test
    public void typeTest()
    {
        assertEquals(AppControl.CUSTOMER, customer.getUserType());
    }

    @Test
    public void addAndGetCashBalanceTest()
    {
        assertEquals(BigDecimal.ZERO, customer.getCashBalance());

        customer.addCashBalance(new BigDecimal(banknoteDenominations[0]));
        customer.addCashBalance(coinDenominations[0]);

        assertEquals(coinDenominations[0].add(new BigDecimal(banknoteDenominations[0])), customer.getCashBalance());
    }

    @Test
    public void setAndGetMembershipTest()
    {
        assertNull(customer.getMemberID());

        customer.setMemberID(membershipID);

        assertEquals(membershipID, customer.getMemberID());
    }

    @Test
    public void setAndGetUsingOwnBagsTest()
    {
        assertFalse(customer.getUseOwnBags());

        customer.setOwnBagsUsed(true);

        assertTrue(customer.getUseOwnBags());
    }

    @Test
    public void setAndGetPlasticBagsTest()
    {
        assertEquals(0, customer.getPlasticBags());

        customer.setPlasticBags(10);

        assertEquals(10, customer.getPlasticBags());
    }

    //    @Test
    //    public void lookupProductTest()
    //    {
    //        assertTrue(customer.getCart().isEmpty());
    //
    //        Inventory.addProduct(product1);
    //        Inventory.addProduct(product2);
    //        customer.lookupProduct(product2.getPLUCode());
    //        customer.lookupProduct(new PriceLookupCode(product2.getPLUCode().toString() + "1"));
    //
    //        assertTrue(customer.getCart().contains(product2));
    //        assertEquals(1, customer.getCart().size());
    //    }

    //    @Test
    //    public void cartTest()
    //    {
    //        assertTrue(customer.getCart().isEmpty());
    //        assertEquals(BigDecimal.ZERO, customer.getCartSubtotal());
    //
    //        Inventory.addProduct(product1);
    //        Inventory.addProduct(product2);
    //        customer.addToCart(product1);
    //        customer.addToCart(product1);
    //        customer.addToCart(product2);
    //
    //        assertTrue(customer.getCart().contains(product1));
    //        assertTrue(customer.getCart().contains(product2));
    //        assertEquals(3, customer.getCart().size());
    //        assertEquals(product1.getPrice().add(product1.getPrice()).add(product2.getPrice()), customer.getCartSubtotal());
    //
    //        customer.removeProduct(product1);
    //        customer.removeProduct(product2);
    //
    //        assertTrue(customer.getCart().contains(product1));
    //        assertFalse(customer.getCart().contains(product2));
    //        assertEquals(1, customer.getCart().size());
    //        assertEquals(product1.getPrice(), customer.getCartSubtotal());
    //    }
}
