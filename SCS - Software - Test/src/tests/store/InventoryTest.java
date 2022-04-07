package tests.store;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import store.Inventory;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * The JUnit test class for the Inventory class in SCS - Software.
 *
 * @author Ricky Bhatti
 * @author Michelle Cheung
 */
public class InventoryTest
{
    // TODO: addProduct(Product p) cannot be reached as the only 2 types of products have their own overloaded method

    // Declare the products
    BarcodedProduct barcodedProduct;
    PLUCodedProduct pluCodedProduct;

    // Setup that is run before each test case
    @Before
    public void setup()
    {
        // Initialize the products
        barcodedProduct = new BarcodedProduct(new Barcode(new Numeral[] {Numeral.zero}), "N/A", new BigDecimal("5.00"), 15.50);
        pluCodedProduct = new PLUCodedProduct(new PriceLookupCode("1234"), "N/A", new BigDecimal("10.00"));

        // Resets the inventory
        Inventory.clear();
    }

    @Test
    public void addAndGetProductTest()
    {
        assertNull(Inventory.getProduct(barcodedProduct.getBarcode()));
        assertNull(Inventory.getProduct(pluCodedProduct.getPLUCode()));

        Inventory.addProduct(barcodedProduct);
        Inventory.addProduct(pluCodedProduct);

        assertEquals(barcodedProduct, Inventory.getProduct(barcodedProduct.getBarcode()));
        assertEquals(pluCodedProduct, Inventory.getProduct(pluCodedProduct.getPLUCode()));
    }

    @Test
    public void setAndGetQuantityTest()
    {
        Inventory.addProduct(barcodedProduct);
        Inventory.addProduct(pluCodedProduct);

        assertEquals(0, Inventory.getQuantity(barcodedProduct));
        assertEquals(0, Inventory.getQuantity(pluCodedProduct));

        Inventory.setQuantity(barcodedProduct, 1);
        Inventory.setQuantity(pluCodedProduct, 2);

        assertEquals(1, Inventory.getQuantity(barcodedProduct));
        assertEquals(2, Inventory.getQuantity(pluCodedProduct));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setQuantityUnsuccessfullyTest()
    {
        Inventory.setQuantity(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setQuantityUnsuccessfullyTest2()
    {
        Inventory.addProduct(barcodedProduct);
        Inventory.addProduct(pluCodedProduct);

        Inventory.setQuantity(barcodedProduct, -1);
        Inventory.setQuantity(pluCodedProduct, -1);
    }

    @Test
    public void clearInventoryTest()
    {
        assertNull(Inventory.getProduct(barcodedProduct.getBarcode()));
        assertNull(Inventory.getProduct(pluCodedProduct.getPLUCode()));

        Inventory.addProduct(barcodedProduct);
        Inventory.addProduct(pluCodedProduct);
        Inventory.clear();

        assertNull(Inventory.getProduct(barcodedProduct.getBarcode()));
        assertNull(Inventory.getProduct(pluCodedProduct.getPLUCode()));
    }
}
