package tests.store;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;
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
public class InventoryTest {
    // Declare the products
    BarcodedProduct barcodedProduct;
    PLUCodedProduct pluCodedProduct;
    Product product1;
    Product product2;

    public class OtherProduct extends Product {
        protected OtherProduct(BigDecimal price, boolean isPerUnit) {
            super(price, isPerUnit);
        }
    }

    // Setup that is run before each test case
    @Before
    public void setup() {
        // Initialize the products
        barcodedProduct = new BarcodedProduct(new Barcode(new Numeral[] { Numeral.zero }), "N/A", new BigDecimal("5.00"), 15.50);
        pluCodedProduct = new PLUCodedProduct(new PriceLookupCode("1234"), "N/A", new BigDecimal("10.00"));
        product1 = new BarcodedProduct(new Barcode(new Numeral[] { Numeral.one }), "N/A", new BigDecimal("15.00"), 20.75);
        product2 = new PLUCodedProduct(new PriceLookupCode("5678"), "N/A", new BigDecimal("20.00"));

        // Resets the inventory
        Inventory.clear();
    }

    @Test
    public void addAndGetProductTest() {
        assertNull(Inventory.getProduct(barcodedProduct.getBarcode()));
        assertNull(Inventory.getProduct(pluCodedProduct.getPLUCode()));
        assertNull(Inventory.getProduct(((BarcodedProduct) product1).getBarcode()));
        assertNull(Inventory.getProduct(((PLUCodedProduct) product2).getPLUCode()));

        Inventory.addProduct(barcodedProduct);
        Inventory.addProduct(pluCodedProduct);
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);

        assertEquals(barcodedProduct, Inventory.getProduct(barcodedProduct.getBarcode()));
        assertEquals(pluCodedProduct, Inventory.getProduct(pluCodedProduct.getPLUCode()));
        assertEquals(product1, Inventory.getProduct(((BarcodedProduct) product1).getBarcode()));
        assertEquals(product2, Inventory.getProduct(((PLUCodedProduct) product2).getPLUCode()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addInvalidProductTest() {
        OtherProduct op = new OtherProduct(new BigDecimal(22.0), true);
        Inventory.addProduct(op);
    }

    @Test
    public void setAndGetQuantityTest() {
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
    public void setQuantityUnsuccessfullyTest() {
        Inventory.setQuantity(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setQuantityUnsuccessfullyTest2() {
        Inventory.addProduct(barcodedProduct);

        Inventory.setQuantity(barcodedProduct, -1);
    }

    @Test
    public void clearInventoryTest() {
        assertNull(Inventory.getProduct(barcodedProduct.getBarcode()));
        assertNull(Inventory.getProduct(pluCodedProduct.getPLUCode()));

        Inventory.addProduct(barcodedProduct);
        Inventory.addProduct(pluCodedProduct);
        Inventory.clear();

        assertNull(Inventory.getProduct(barcodedProduct.getBarcode()));
        assertNull(Inventory.getProduct(pluCodedProduct.getPLUCode()));
    }
}
