package tests.checkout;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.EmptyException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.ReceiptPrinter;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.Product;

import checkout.Receipt;
import store.Inventory;
import user.Customer;

public class ReceiptTest {
    class FakeProduct extends BarcodedProduct {
        public FakeProduct(Barcode barcode, String description, BigDecimal price, double expectedWeight) {
            super(barcode, description, price, expectedWeight);
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
    int scaleMaximumWeight = 10;
    int scaleSensitivity = 1;
    SelfCheckoutStation selfCheckoutStation = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations, scaleMaximumWeight, scaleSensitivity);
    Numeral[] barcodeNumeral = {Numeral.zero, Numeral.one, Numeral.two, Numeral.three, Numeral.four};
    Barcode barcode = new Barcode(barcodeNumeral);
    FakeProduct product = new FakeProduct(barcode, "Fake Product", new BigDecimal("5.00"), 3.12);
    FakeItem item = new FakeItem(5.0);

    @Test
    public void printReceiptTest() throws EmptyException, OverloadException
    {
        Customer customer = new Customer();
        Inventory inventory = new Inventory();
        inventory.addToInventory(barcode, product, item);
        Receipt receipt = new Receipt(selfCheckoutStation, customer, inventory);
        customer.addToCart(barcode);
        receipt.printReceipt();
        assertNotNull(selfCheckoutStation.printer.removeReceipt());
    }



    @Test
    public void outOfPaperTest() throws OverloadException
    {
        Receipt receipt = new Receipt(selfCheckoutStation, new Customer(), new Inventory());
        receipt.outOfPaper(selfCheckoutStation.printer);
        assertTrue(selfCheckoutStation.printer.isDisabled());
    }

    @Test
    public void outOfInkTest() throws OverloadException
    {
        Receipt receipt = new Receipt(selfCheckoutStation, new Customer(), new Inventory());
        receipt.outOfInk(selfCheckoutStation.printer);
        assertTrue(selfCheckoutStation.printer.isDisabled());
    }


}