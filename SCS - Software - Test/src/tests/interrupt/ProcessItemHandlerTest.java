package tests.interrupt;

import interrupt.ProcessItemHandler;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import software.SelfCheckoutSoftware;
import store.Inventory;
import user.Customer;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test class for the ProcessItemHandler class in SCS - Software.
 *
 * @author Ricky Bhatti
 * @author Tyler Chen
 */
public class ProcessItemHandlerTest
{
    // Static variables that will be used during testing
    final Currency currency = Currency.getInstance("CAD");
    final int[] banknoteDenominations = {5, 10, 20, 50};
    final BigDecimal[] coinDenominations = {new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"), new BigDecimal("1.00"), new BigDecimal("2.00")};
    final int scaleMaximumWeight = 100;
    final int scaleSensitivity = 10;
    BarcodedProduct product1 = new BarcodedProduct(new Barcode(new Numeral[] {Numeral.zero, Numeral.one, Numeral.two, Numeral.three, Numeral.four}), "N/A", new BigDecimal("5.00"), 17.5);
    PLUCodedProduct product2 = new PLUCodedProduct(new PriceLookupCode("1000"), "N/A", new BigDecimal("10.00"));

    SelfCheckoutStation selfCheckoutStation;
    SelfCheckoutSoftware selfCheckoutSoftware;

    ProcessItemHandler processItemHandler;
    Customer customer;

    @Before
    public void setup()
    {
        Inventory.clear();
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        selfCheckoutStation = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations, scaleMaximumWeight, scaleSensitivity);
        selfCheckoutSoftware = new SelfCheckoutSoftware(selfCheckoutStation);
        processItemHandler = new ProcessItemHandler(selfCheckoutSoftware);
        customer = new Customer();
        processItemHandler.setCustomer(customer);
    }

    @Test
    public void attachAndDetachTest()
    {
        selfCheckoutStation.mainScanner.detach(processItemHandler);
        selfCheckoutStation.handheldScanner.detach(processItemHandler);
        selfCheckoutStation.scanningArea.detach(processItemHandler);
        selfCheckoutStation.baggingArea.detach(processItemHandler);

        assertFalse(selfCheckoutStation.mainScanner.detach(processItemHandler));
        assertFalse(selfCheckoutStation.handheldScanner.detach(processItemHandler));
        assertFalse(selfCheckoutStation.scanningArea.detach(processItemHandler));
        assertFalse(selfCheckoutStation.baggingArea.detach(processItemHandler));

        processItemHandler.attachAll();

        assertTrue(selfCheckoutStation.mainScanner.detach(processItemHandler));
        assertTrue(selfCheckoutStation.handheldScanner.detach(processItemHandler));
        assertTrue(selfCheckoutStation.scanningArea.detach(processItemHandler));
        assertTrue(selfCheckoutStation.baggingArea.detach(processItemHandler));

        processItemHandler.attachAll();
        processItemHandler.detatchAll();

        assertFalse(selfCheckoutStation.mainScanner.detach(processItemHandler));
        assertFalse(selfCheckoutStation.handheldScanner.detach(processItemHandler));
        assertFalse(selfCheckoutStation.scanningArea.detach(processItemHandler));
        assertFalse(selfCheckoutStation.baggingArea.detach(processItemHandler));
    }

    @Test
    public void enableAndDisableHardwareTest()
    {
        assertFalse(selfCheckoutStation.mainScanner.isDisabled());
        assertFalse(selfCheckoutStation.handheldScanner.isDisabled());
        assertFalse(selfCheckoutStation.scanningArea.isDisabled());
        assertFalse(selfCheckoutStation.baggingArea.isDisabled());

        processItemHandler.disableHardware();

        assertTrue(selfCheckoutStation.mainScanner.isDisabled());
        assertTrue(selfCheckoutStation.handheldScanner.isDisabled());
        assertTrue(selfCheckoutStation.scanningArea.isDisabled());
        assertTrue(selfCheckoutStation.baggingArea.isDisabled());

        processItemHandler.enableHardware();

        assertFalse(selfCheckoutStation.mainScanner.isDisabled());
        assertFalse(selfCheckoutStation.handheldScanner.isDisabled());
        assertFalse(selfCheckoutStation.scanningArea.isDisabled());
        assertFalse(selfCheckoutStation.baggingArea.isDisabled());
    }

    //    @Test
    //    public void customerWantsToUseOwnBags()
    //    {
    //        customer.setOwnBagsUsed(true);
    //
    //        double weightOfBags = 3.1;
    //        processItemHandler.weightChanged(selfCheckoutStation.baggingArea, weightOfBags);
    //        assertTrue("Weight of bags should be updated.", processItemHandler.getWeightBeforeBagging() == weightOfBags);
    //    }
    //
    //
    //    @Test
    //    public void barcodeScannedInInvTest()
    //    {
    //        assertTrue(customer.getCart().isEmpty());
    //
    //        processItemHandler.barcodeScanned(selfCheckoutStation.mainScanner, product1.getBarcode());
    //
    //        List <Product> items = customer.getCart();
    //        assertTrue("Item scanned does not match barcode inputted", product1.equals(items.get(0)));
    //    }
    //
    //    @Test
    //    public void barcodeScannedInNotInvTest()
    //    {
    //        assertTrue(customer.getCart().isEmpty());
    //
    //        processItemHandler.barcodeScanned(selfCheckoutStation.mainScanner, product1.getBarcode());
    //
    //        List <Product> items = customer.getCart();
    //        assertTrue("Item scanned does not match barcode inputted", items.isEmpty());
    //    }
    //
    //
    //    @Test
    //    public void weightChangedTest()
    //    {
    //        processItemHandler.barcodeScanned(selfCheckoutStation.mainScanner, product1.getBarcode());
    //        assertTrue("Customer should be notified to move item to bagging area.", customer.getWaitingToBag());
    //        processItemHandler.weightChanged(selfCheckoutStation.baggingArea, 1.0);
    //        assertFalse("Customer should be no longer be notified to place item in bagging area.", customer.getWaitingToBag());
    //    }
    //
    //
    //    @Test
    //    public void weightChangedTooHeavy()
    //    {
    //        assertFalse("Customer should be notified that", customer.getWaitingToBag());
    //        processItemHandler.barcodeScanned(selfCheckoutStation.mainScanner, product1.getBarcode());
    //        selfCheckoutStation.baggingArea.add(new BarcodedItem(product1.getBarcode(), 3.0));
    //        processItemHandler.weightChanged(selfCheckoutStation.baggingArea, 3.0); // Causes the item to be set as "unexpectedItem"
    //        assertTrue("Unexpected Item flag should be true", processItemHandler.getUnexpectedItem());
    //    }
    //
    //    @Test
    //    public void weightChangedTooLight()
    //    {
    //        assertFalse("Customer should be notified that", customer.getWaitingToBag());
    //        processItemHandler.barcodeScanned(selfCheckoutStation.mainScanner, product1.getBarcode());
    //        selfCheckoutStation.baggingArea.add(new BarcodedItem(product1.getBarcode(), 3.0));
    //        processItemHandler.weightChanged(selfCheckoutStation.baggingArea, 0.1); // Causes the item to be set as "unexpectedItem"
    //        assertTrue("Unexpected Item flag should be true", processItemHandler.getUnexpectedItem());
    //    }
    //
    //
    //    //    @Test
    //    //    public void weightChangedOverload()
    //    //    {
    //    //        Inventory.addProduct(barcode3, product3, item3);
    //    //
    //    //        processItemHandler.barcodeScanned(selfCheckoutStation.mainScanner, barcode);
    //    //        selfCheckoutStation.baggingArea.add(item);
    //    //        processItemHandler.weightChanged(selfCheckoutStation.baggingArea, item.getWeight());
    //    //
    //    //        processItemHandler.barcodeScanned(selfCheckoutStation.mainScanner, barcode3);
    //    //        selfCheckoutStation.baggingArea.add(item3);
    //    //        processItemHandler.weightChanged(selfCheckoutStation.baggingArea, item.getWeight() + item3.getWeight());
    //    //        selfCheckoutStation.baggingArea.remove(item3);
    //    //
    //    //        //Comparing the weight of the non-overloaded item to the weight after removing the overloaded item.
    //    //        assertEquals("Weight should be the close to what the weight was.", item.getWeight(), processItemHandler.getWeightBeforeBagging(), 0.1);
    //    //    }
    //
    //
    //    @Test
    //    public void weightChangedUnexpectedItem()
    //    {
    //
    //        processItemHandler.barcodeScanned(selfCheckoutStation.mainScanner, product1.getBarcode());
    //        processItemHandler.weightChanged(selfCheckoutStation.baggingArea, product1.getExpectedWeight());
    //
    //        processItemHandler.barcodeScanned(selfCheckoutStation.mainScanner, product1.getBarcode());
    //        processItemHandler.weightChanged(selfCheckoutStation.baggingArea, 4.0);        //3g addition
    //
    //        processItemHandler.weightChanged(selfCheckoutStation.baggingArea, product1.getExpectedWeight());
    //
    //        assertFalse("Weight should be the close to what the weight was.", processItemHandler.getUnexpectedItem());
    //    }
    //
    //
    //    @Test
    //    public void overloadTest()
    //    {
    //        processItemHandler.overload(selfCheckoutStation.baggingArea);
    //        assertTrue(selfCheckoutStation.mainScanner.isDisabled());
    //    }
    //
    //    @Test
    //    public void outOfOverloadTest()
    //    {
    //        processItemHandler.outOfOverload(selfCheckoutStation.baggingArea);
    //        assertFalse(selfCheckoutStation.mainScanner.isDisabled());
    //    }
}
