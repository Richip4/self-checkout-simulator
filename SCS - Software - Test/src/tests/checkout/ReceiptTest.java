package tests.checkout;

import checkout.Receipt;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import software.SelfCheckoutSoftware;

import java.math.BigDecimal;
import java.util.Currency;

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
    Numeral[] barcodeNumeral = {Numeral.zero, Numeral.one, Numeral.two, Numeral.three, Numeral.four};
    Barcode barcode = new Barcode(barcodeNumeral);
    FakeProduct product = new FakeProduct(barcode, "Fake Product", new BigDecimal("5.00"), 3.12);
    FakeItem item = new FakeItem(5.0);

    //    @Test
    //    public void printReceiptTest() throws EmptyException, OverloadException
    //    {
    //        Customer customer = new Customer();
    //        Inventory inventory = new Inventory();
    //        inventory.addProduct(barcode, product, item);
    //        Receipt receipt = new Receipt(selfCheckoutStation, customer, inventory);
    //        customer.addToCart(barcode);
    //        receipt.printReceipt();
    //        assertNotNull(selfCheckoutStation.printer.removeReceipt());
    //    }


    @Test
    public void outOfPaperTest() throws OverloadException
    {
        Receipt receipt = new Receipt(selfCheckoutSoftware);
        receipt.outOfPaper(selfCheckoutStation.printer);
        assertTrue(selfCheckoutStation.printer.isDisabled());
    }

    @Test
    public void outOfInkTest() throws OverloadException
    {
        Receipt receipt = new Receipt(selfCheckoutSoftware);
        receipt.outOfInk(selfCheckoutStation.printer);
        assertTrue(selfCheckoutStation.printer.isDisabled());
    }


}
