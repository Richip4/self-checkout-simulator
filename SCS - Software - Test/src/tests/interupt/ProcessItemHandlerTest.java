package tests.interupt;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.Product;

import interrupt.ProcessItemHandler;
import store.Inventory;
import user.Customer;

/**
 * The JUnit test class for the ProcessItemHandler class in SCS - Software.
 * 
 * @author Ricky Bhatti
 * @author Tyler Chen
 */
public class ProcessItemHandlerTest {
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

    Currency currency = Currency.getInstance("USD");
    int[] banknoteDenominations = { 1, 5, 10, 25, 100 };
    BigDecimal[] coinDenominations = { new BigDecimal("0.01"), new BigDecimal("0.05"), new BigDecimal("0.1"), new BigDecimal("0.25"), new BigDecimal("1.00") };
    List<BigDecimal> coinDenominationsList = java.util.Arrays.asList(coinDenominations);
    int scaleMaximumWeight = 10;
    int scaleSensitivity = 1;
    SelfCheckoutStation selfCheckoutStation = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations, scaleMaximumWeight, scaleSensitivity);
    Barcode barcode = new Barcode(new Numeral[] {Numeral.zero, Numeral.one, Numeral.two, Numeral.three, Numeral.four});
    FakeProduct product = new FakeProduct(new BigDecimal("1.00"));
    FakeItem item = new FakeItem(1.0);
    Barcode barcode2 = new Barcode(new Numeral[] {Numeral.zero, Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five});
    FakeProduct product2 = new FakeProduct(new BigDecimal("2.00"));
    FakeItem item2 = new FakeItem(2.0);
    
    Barcode barcode3 = new Barcode(new Numeral[] {Numeral.one, Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five});
    FakeProduct product3 = new FakeProduct(new BigDecimal("2.00"));
    FakeItem item3 = new FakeItem(20.0);
    
    Inventory inventory;
    ProcessItemHandler handler;
    Customer customer;

    @Before
    public void setup() {
    	inventory = new Inventory();
    	inventory.addProduct(barcode, product, item);
    	handler = new ProcessItemHandler(selfCheckoutStation, inventory);
    	customer = new Customer();
    	System.setIn(new ByteArrayInputStream("no\n".getBytes()));
        handler.setCustomer(customer);
    }

    @Test
    public void customerWantsToUseOwnBags() {
    	customer = new Customer();
    	System.setIn(new ByteArrayInputStream("yes\n".getBytes()));
        handler.setCustomer(customer);
        handler.setownBagsUsed(true);
        assertTrue("Own bags used flag should be set to true", handler.getUseOwnBags());
        
        double weightOfBags = 3.1;
        handler.weightChanged(selfCheckoutStation.baggingArea, weightOfBags);
        assertTrue("Weight of bags should be updated.", handler.getWeightBeforeBagging() == weightOfBags);
    }
 
    
    @Test
    public void barcodeScannedInInvTest() {
    	
    	assertTrue(customer.getBarcodedItemsInCart().isEmpty());
    	
        handler.barcodeScanned(selfCheckoutStation.mainScanner, barcode);
        
        ArrayList<Barcode> items = customer.getBarcodedItemsInCart();
        assertTrue("Item scanned does not match barcode inputted", barcode.equals(items.get(0)));
    }
    
    @Test
    public void barcodeScannedInNotInvTest() {
    	
    	assertTrue(customer.getBarcodedItemsInCart().isEmpty());
    	
        handler.barcodeScanned(selfCheckoutStation.mainScanner, barcode2);
        
        ArrayList<Barcode> items = customer.getBarcodedItemsInCart();
        assertTrue("Item scanned does not match barcode inputted", items.isEmpty());
    }


    @Test
    public void weightChangedTest() {
        handler.barcodeScanned(selfCheckoutStation.mainScanner, barcode);
        assertTrue("Customer should be notified to move item to bagging area.", customer.getWaitingToBag());
        handler.weightChanged(selfCheckoutStation.baggingArea, 1.0);
        assertFalse("Customer should be no longer be notified to place item in bagging area.", customer.getWaitingToBag());
    }


    @Test
    public void weightChangedTooHeavy() {
    	assertFalse("Customer should be notified that", customer.getWaitingToBag());
        handler.barcodeScanned(selfCheckoutStation.mainScanner, barcode);
        selfCheckoutStation.baggingArea.add(item); 
        handler.weightChanged(selfCheckoutStation.baggingArea, 3.0); // Causes the item to be set as "unexpectedItem"
        assertTrue("Unexpected Item flag should be true", handler.getUnexpectedItem());
    }
    
    @Test
    public void weightChangedTooLight() {
    	assertFalse("Customer should be notified that", customer.getWaitingToBag());
        handler.barcodeScanned(selfCheckoutStation.mainScanner, barcode);
        selfCheckoutStation.baggingArea.add(item); 
        handler.weightChanged(selfCheckoutStation.baggingArea, 0.1); // Causes the item to be set as "unexpectedItem"
        assertTrue("Unexpected Item flag should be true", handler.getUnexpectedItem());
    }


    @Test
    public void weightChangedOverload() {
    	inventory.addProduct(barcode3, product3, item3);
    	
        handler.barcodeScanned(selfCheckoutStation.mainScanner, barcode);
        selfCheckoutStation.baggingArea.add(item); 
        handler.weightChanged(selfCheckoutStation.baggingArea, item.getWeight()); 
        
        handler.barcodeScanned(selfCheckoutStation.mainScanner, barcode3);
        selfCheckoutStation.baggingArea.add(item3); 
    	handler.weightChanged(selfCheckoutStation.baggingArea, item.getWeight() + item3.getWeight()); 
    	selfCheckoutStation.baggingArea.remove(item3); 
        
    	//Comparing the weight of the non-overloaded item to the weight after removing the overloaded item.
        assertEquals("Weight should be the close to what the weight was.", item.getWeight(), 
        		handler.getWeightBeforeBagging(), 0.1);
    }
    
    
    @Test
    public void weightChangedUnexpectedItem() {
    	
    	handler.barcodeScanned(selfCheckoutStation.mainScanner, barcode);
    	handler.weightChanged(selfCheckoutStation.baggingArea, item.getWeight());
    	
    	handler.barcodeScanned(selfCheckoutStation.mainScanner, barcode);
        handler.weightChanged(selfCheckoutStation.baggingArea, 4.0);		//3g addition 
        
        handler.weightChanged(selfCheckoutStation.baggingArea, item.getWeight()); 
        
        assertFalse("Weight should be the close to what the weight was.", handler.getUnexpectedItem());
    }
    
    
    @Test
    public void overloadTest() {
        handler.overload(selfCheckoutStation.baggingArea);
        assertTrue(selfCheckoutStation.mainScanner.isDisabled());
    }

    @Test
    public void outOfOverloadTest() {
        handler.outOfOverload(selfCheckoutStation.baggingArea);
        assertFalse(selfCheckoutStation.mainScanner.isDisabled());
    }
}
