package tests.store;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.products.Product;

import store.Inventory;

/**
 * The JUnit test class for the Inventory class in SCS - Software.
 * 
 * @author Ricky Bhatti
 * @author Michelle Cheung
 */
public class InventoryTest {
    class FakeProduct extends Product {
        public FakeProduct(BigDecimal price) {
            super(price, false);
        }
    }
    
    class FakeItem extends Item {
        public FakeItem(double weight) {
            super(weight);
        }
    }

    Numeral[] barcodeNumeral = {Numeral.zero, Numeral.one, Numeral.two, Numeral.three, Numeral.four};
    Barcode barcode = new Barcode(barcodeNumeral);
    FakeProduct product = new FakeProduct(new BigDecimal("1.00"));
    FakeItem item = new FakeItem(1.0);

    @Test
    public void InventoryTest() {
        Inventory inventory = new Inventory();
        assertNotNull(inventory);
    }

    // Purchasble test?

    @Test
    public void addToInventoryTest() {
        Inventory inventory = new Inventory();
        inventory.addProduct(barcode, product, item);
        assertEquals(inventory.getQuantity(barcode), 1);
    }

    @Test
    public void addToInventoryTest2() {
        Inventory inventory = new Inventory();
        inventory.addProduct(barcode, product, item);
        inventory.addProduct(barcode, product, item);
        assertEquals(inventory.getQuantity(barcode), 2);
    }

    @Test
    public void removeFromInventoryTest() {
        Inventory inventory = new Inventory();
        inventory.addProduct(barcode, product, item);        
        assertTrue(inventory.removeInventory(barcode));
    }

    @Test
    public void removeFromInventoryTest2() {
        Inventory inventory = new Inventory();
        assertFalse(inventory.removeInventory(barcode));
    }

    @Test
    public void checkForItemTest() {
        Inventory inventory = new Inventory();
        inventory.addProduct(barcode, product, item);
        assertTrue(inventory.checkForItem(barcode));
    }

    @Test
    public void getItemTest() {
        Inventory inventory = new Inventory();
        inventory.addProduct(barcode, product, item);
        assertEquals(item, inventory.getItem(barcode));
    }

    @Test
    public void getProductTest() {
        Inventory inventory = new Inventory();
        inventory.addProduct(barcode, product, item);
        assertEquals(product, inventory.getProduct(barcode));
    }
    
    @Test
    public void getQuantityTest() {
    	Inventory inventory = new Inventory();
        inventory.addProduct(barcode, product, item);
        inventory.addProduct(barcode, product, item);
        inventory.addProduct(barcode, product, item);
        inventory.addProduct(barcode, product, item);
        inventory.removeInventory(barcode);
        assertEquals(inventory.getQuantity(barcode), 3);
    }
    
    @Test
    public void getQuantityTest2() {
    	Inventory inventory = new Inventory();
        inventory.addProduct(barcode, product, item);
        inventory.addProduct(barcode, product, item);
        inventory.addProduct(barcode, product, item);
        inventory.addProduct(barcode, product, item);
        inventory.removeInventory(barcode);
        assertNotEquals(inventory.getQuantity(barcode), 4);
    }
    
    @Test
    public void getQuantityTest3() {
    	Inventory inventory = new Inventory();
        inventory.addProduct(barcode, product, item);
        inventory.removeInventory(barcode);
        assertEquals(inventory.getQuantity(barcode), 0);
    }
}
