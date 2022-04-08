package tests.checkout;

import checkout.Receipt;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;

import software.SelfCheckoutSoftware;
import software.SupervisionSoftware;
import store.Inventory;
import user.Customer;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReceiptTest
{
    class FakeProduct extends BarcodedProduct
    {
        public FakeProduct(Barcode barcode, String description, BigDecimal price, double expectedWeight)
        {
            super(barcode, description, price, expectedWeight);
        }
    }

    class FakeItem extends Item
    {
        public FakeItem(double weight)
        {
            super(weight);
        }
    }

    Currency currency = Currency.getInstance("USD");
    int[] banknoteDenominations = {1, 5, 10, 25, 100};
    BigDecimal[] coinDenominations = {new BigDecimal("0.01"), new BigDecimal("0.05"), new BigDecimal("0.1"), new BigDecimal("0.25"), new BigDecimal("1.00")};
    int scaleMaximumWeight = 10;
    int scaleSensitivity = 1;
    SelfCheckoutStation selfCheckoutStation = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations, scaleMaximumWeight, scaleSensitivity);
    SelfCheckoutSoftware selfCheckoutSoftware = new SelfCheckoutSoftware(selfCheckoutStation);
    SupervisionStation supervisionStation = new SupervisionStation();
    SupervisionSoftware supervision = new SupervisionSoftware(supervisionStation);

    Numeral[] barcodeNumeral = {Numeral.zero, Numeral.one, Numeral.two, Numeral.three, Numeral.four};
    Barcode barcode = new Barcode(barcodeNumeral);
    FakeProduct product = new FakeProduct(barcode, "Fake Product", new BigDecimal("5.00"), 3.12);
    FakeItem item = new FakeItem(5.0);
    PriceLookupCode code = new PriceLookupCode("4011");
    BarcodedProduct b = new BarcodedProduct(barcode, "Fake Product", new BigDecimal("5.00"), 3.12);
    BarcodedProduct Long = new BarcodedProduct(barcode, ".................................................................", new BigDecimal("5.00"), 3.12);
    PLUCodedProduct plu = new PLUCodedProduct(code, "Bananas", new BigDecimal("1.00"));
   
    @Test
    public void printLineTest() throws EmptyException, OverloadException{
    	Receipt receipt = new Receipt(selfCheckoutSoftware);
    }
    
    @Test
    public void printReceiptTest() throws EmptyException, OverloadException
    {
        supervision.add(selfCheckoutSoftware);
        Customer customer = new Customer();
        

        Inventory.addProduct(b);
        Receipt receipt = new Receipt(selfCheckoutSoftware);
        receipt.setCustomer(customer);
        customer.setMemberID("10");
        customer.addToCart(b);
        selfCheckoutStation.printer.addPaper(100);
        selfCheckoutStation.printer.addInk(100);
        receipt.printReceipt();
        assertNotNull(selfCheckoutStation.printer.removeReceipt());
        }
    
    @Test(expected = NullPointerException.class)
    public void printReceiptNullIDTest() throws EmptyException, OverloadException
    {
        supervision.add(selfCheckoutSoftware);
        Customer customer = new Customer();
        

        Inventory.addProduct(b);
        Receipt receipt = new Receipt(selfCheckoutSoftware);
        receipt.setCustomer(customer);
        customer.setMemberID(null);
        customer.addToCart(b);
        selfCheckoutStation.printer.addPaper(100);
        selfCheckoutStation.printer.addInk(100);
        receipt.printReceipt();
        }

    @Test
    public void printReceiptPLU() throws EmptyException, OverloadException
    {
        supervision.add(selfCheckoutSoftware);
        Customer customer = new Customer();
        

        Inventory.addProduct(plu);
        Receipt receipt = new Receipt(selfCheckoutSoftware);
        receipt.setCustomer(customer);
        customer.setMemberID("10");
        customer.addToCart(plu);
        selfCheckoutStation.printer.addPaper(100);
        selfCheckoutStation.printer.addInk(100);
        receipt.printReceipt();
        assertNotNull(selfCheckoutStation.printer.removeReceipt());
        }
    
    @Test
    public void printLineLong() throws EmptyException, OverloadException
    {
        supervision.add(selfCheckoutSoftware);
        Customer customer = new Customer();
        

        Inventory.addProduct(Long);
        Receipt receipt = new Receipt(selfCheckoutSoftware);
        receipt.setCustomer(customer);
        customer.setMemberID("10");
        customer.addToCart(Long);
        selfCheckoutStation.printer.addPaper(100);
        selfCheckoutStation.printer.addInk(100);
        receipt.printReceipt();
        assertNotNull(selfCheckoutStation.printer.removeReceipt());
        }
    @Test
    public void outOfPaperTest() throws OverloadException
    { 
        supervision.add(selfCheckoutSoftware);
        Receipt receipt = new Receipt(selfCheckoutSoftware);
        receipt.outOfPaper(selfCheckoutStation.printer);
        assertTrue(selfCheckoutStation.printer.isDisabled());
    }

    @Test
    public void outOfInkTest() throws OverloadException
    {
        supervision.add(selfCheckoutSoftware);
        Receipt receipt = new Receipt(selfCheckoutSoftware);
        receipt.outOfInk(selfCheckoutStation.printer);
        assertTrue(selfCheckoutStation.printer.isDisabled());
    }
    
    @Test
    public void disableTest()
    {
        supervision.add(selfCheckoutSoftware);
        Receipt receipt = new Receipt(selfCheckoutSoftware);
        receipt.disableHardware();
        assertTrue(selfCheckoutStation.printer.isDisabled());
    }
    
    @Test
    public void detachAllTest()
    {
        supervision.add(selfCheckoutSoftware);
        Receipt receipt = new Receipt(selfCheckoutSoftware);
        receipt.detatchAll();
        assertTrue(!selfCheckoutSoftware.getObservers().contains(receipt));
    }
    
    @Test
    public void getCustomerTest() {
        Customer customer = new Customer();
        Receipt receipt = new Receipt(selfCheckoutSoftware);
        receipt.setCustomer(customer);
    	assertTrue(receipt.getCustomer() == customer);
    }


}
