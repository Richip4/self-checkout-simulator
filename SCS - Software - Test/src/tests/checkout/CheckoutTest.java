package tests.checkout;

import checkout.Checkout;
import org.junit.Test;
import org.lsmr.selfcheckout.*;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.products.Product;
import store.Inventory;
import user.Customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.Assert.*;

/**
 * The JUnit test class for the Checkout class in SCS - Software.
 * 
 * @author Ricky Bhatti
 * 
 *         Fix original test cases, and add new test cases for `cancelCheckout`
 *         and `makeChanges`.
 * @author Yunfan Yang
 */
public class CheckoutTest {
    Currency currency = Currency.getInstance("USD");
    int[] banknoteDenominations = { 1, 5, 10, 20, 100 };
    BigDecimal[] coinDenominations = { new BigDecimal("0.01"), new BigDecimal("0.05"), new BigDecimal("0.1"),
            new BigDecimal("0.25"), new BigDecimal("1.00") };
    int scaleMaximumWeight = 10;
    int scaleSensitivity = 1;
    SelfCheckoutStation selfCheckoutStation = new SelfCheckoutStation(currency, banknoteDenominations,
            coinDenominations, scaleMaximumWeight, scaleSensitivity);

    Numeral[] barcodeNumeral = { Numeral.zero, Numeral.one, Numeral.two, Numeral.three, Numeral.four };
    Barcode barcode = new Barcode(barcodeNumeral);
    BigDecimal price = new BigDecimal("1.00");
    FakeProduct product = new FakeProduct(price);
    FakeItem item = new FakeItem(1.0);
    Inventory inv = new Inventory();

    @Test
    public void readyToCheckoutTest() {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutStation, customer);
        checkout.readyToCheckout();

        assertTrue("Customer wish to checkout, main scanner should be disabled",
                selfCheckoutStation.mainScanner.isDisabled());
        assertTrue("Customer wish to checkout, handheld scanner should be disabled",
                selfCheckoutStation.handheldScanner.isDisabled());
        assertTrue("Customer wish to checkout, scanning area should be disabled",
                selfCheckoutStation.scanningArea.isDisabled());
    }

    @Test
    public void readyToCheckoutTest2() {
        Checkout checkout = new Checkout(selfCheckoutStation);
        try {
            checkout.readyToCheckout();
        } catch (IllegalStateException e) {
            // This is expected because no customer is set.

            // Test the rest of the devices.
            assertFalse("There is no customer intiialized, scanner should not be disabled",
                    selfCheckoutStation.mainScanner.isDisabled());
            assertFalse("There is no customer intiialized, scanner should not be disabled",
                    selfCheckoutStation.handheldScanner.isDisabled());
            assertFalse("There is no customer intiialized, scanning area should not be disabled",
                    selfCheckoutStation.scanningArea.isDisabled());

            return;
        }

        fail("Customer is not set and readyToCheckout() should throw an IllegalStateException");
    }

    @Test
    public void cancelCheckout() {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutStation, customer);
        checkout.readyToCheckout();
        checkout.cancelCheckout();

        assertFalse("Customer wish to checkout, main scanner should be disabled",
                selfCheckoutStation.mainScanner.isDisabled());
        assertFalse("Customer wish to checkout, handheld scanner should be disabled",
                selfCheckoutStation.handheldScanner.isDisabled());
        assertFalse("Customer wish to checkout, scanning area should be disabled",
                selfCheckoutStation.scanningArea.isDisabled());
    }

    @Test
    public void cancelCheckout2() {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutStation, customer);
        checkout.readyToCheckout();
        checkout.setCustomer(null);

        try {
            checkout.cancelCheckout();
        } catch (IllegalStateException e) {
            // This is expected because no customer is set.

            // Test the rest of the devices.
            assertTrue("There is no customer intiialized, scanner should not be disabled",
                    selfCheckoutStation.mainScanner.isDisabled());
            assertTrue("There is no customer intiialized, scanner should not be disabled",
                    selfCheckoutStation.handheldScanner.isDisabled());
            assertTrue("There is no customer intiialized, scanning area should not be disabled",
                    selfCheckoutStation.scanningArea.isDisabled());

            return;
        }

        fail("Customer is not set and cancelCheckout() should throw an IllegalStateException");
    }

    private void clearDispensers() {
        for (BanknoteDispenser dispenser : selfCheckoutStation.banknoteDispensers.values()) {
            try {
                dispenser.unload();
            } catch (SimulationException e) {
                fail("Failed to clear dispenser");
            }
        }

        for (CoinDispenser dispenser : selfCheckoutStation.coinDispensers.values()) {
            try {
                dispenser.unload();
            } catch (SimulationException e) {
                fail("Failed to clear dispenser");
            }
        }
    }

    /**
     * This is a helper function, to put 100 coins for every denomination in
     * corresponding dispenser.
     */
    private void addCoinsToCoinDispensers() {
        // For each dispenser, add 100 coins
        for (Map.Entry<BigDecimal, CoinDispenser> cds : selfCheckoutStation.coinDispensers.entrySet()) {
            BigDecimal denom = cds.getKey();
            CoinDispenser cd = cds.getValue();

            try {
                // Clear the dispenser first, so dispenser is never overloaded by repeatedly
                // adding 100 coins.
                cd.unload();
            } catch (Exception e) {
                fail("Coin dispenser unload failed");
            }

            // Add 100 coins
            for (int t = 0; t < 100; t++) {
                try {
                    cd.load(new Coin(denom));
                } catch (OverloadException e) {
                    fail("Coin Dispenser is full");
                } catch (SimulationException e) {
                    e.printStackTrace();
                    fail("Not operated at this point");
                }
            }

            assertEquals("Coin dispenser for $" + denom + " should have 100 coins", 100, cd.size());
        }

        // Clear coin tray
        selfCheckoutStation.coinTray.collectCoins();
    }

    private void addBanknotesToBanknotesDispenser() {
        // For each dispenser, add 100 banknotes
        for (Entry<Integer, BanknoteDispenser> cds : selfCheckoutStation.banknoteDispensers.entrySet()) {
            int denom = cds.getKey();
            BanknoteDispenser cd = cds.getValue();

            try {
                // Clear the dispenser first, so dispenser is never overloaded by repeatedly
                // adding 100 coins.
                cd.unload();
            } catch (Exception e) {
                fail("Coin dispenser unload failed");
            }

            // Add 100 pieces
            for (int t = 0; t < 100; t++) {
                try {
                    cd.load(new Banknote(currency, denom));
                } catch (OverloadException e) {
                    fail("Coin Dispenser is full");
                } catch (SimulationException e) {
                    e.printStackTrace();
                    fail("Not operated at this point");
                }
            }

            assertEquals("Banknote dispenser for $" + denom + " should have 100 pieces", 100, cd.size());
        }

        // Remove any left banknotes dangling at the output
        while (true) {
            try {
                selfCheckoutStation.banknoteOutput.removeDanglingBanknotes();
            } catch (NullPointerSimulationException e) {
                break;
            }
        }
    }

    /**
     * This is a helper function, to take all the coins in the tray and sum them up
     * 
     * @return the sum of all the coins in the tray
     */
    private BigDecimal getSumOfCoinsInCoinDispenser() {
        List<Coin> coins = selfCheckoutStation.coinTray.collectCoins();

        // Sum up all coins in the coin tray
        BigDecimal sum = BigDecimal.ZERO;
        for (Coin coin : coins) {
            if (coin != null) {
                sum = sum.add(coin.getValue());
            }
        }

        return sum;
    }

    private BigDecimal getSumOfBanknotesInBanknoteOutput() {
        BigDecimal sum = BigDecimal.ZERO;
        Banknote[] danglingBanknotes = new Banknote[] {new Banknote(currency, 0)};

        // Take all the banknotes until there is no more
        try
        {
            danglingBanknotes = selfCheckoutStation.banknoteOutput.removeDanglingBanknotes();
        } catch (NullPointerSimulationException e)
        {
            // No more banknotes
            System.out.println("no more banknote");
        }

        // Sum of all the banknotes
        for (Banknote next : danglingBanknotes)
        {
            sum = sum.add(new BigDecimal(next.getValue()));
            System.out.println("add " + sum);
        }
        
        return sum;
    }

    @Test
    public void makeChangeSingleCoin1() {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutStation, customer);

        BigDecimal change = new BigDecimal("0.01");
        Coin.DEFAULT_CURRENCY = currency;

        assertEquals("dispenser should have 5 denominations", 5, selfCheckoutStation.coinDispensers.size());

        this.addCoinsToCoinDispensers();

        checkout.makeChange(change);

        BigDecimal sum = this.getSumOfCoinsInCoinDispenser();

        assertEquals("Coin tray should have coins with sum of $0.01", change.doubleValue(), sum.doubleValue(), 0.01);
    }

    // @Test
    // public void makeChangeSingleCoin2() {
    //     Customer customer = new Customer();
    //     Checkout checkout = new Checkout(selfCheckoutStation, customer);

    //     BigDecimal change = new BigDecimal("1.00");
    //     Coin.DEFAULT_CURRENCY = currency;

    //     assertEquals("dispenser should have 5 denominations", 5, selfCheckoutStation.coinDispensers.size());

    //     this.addCoinsToCoinDispensers();

    //     checkout.makeChange(change);

    //     BigDecimal sum = this.getSumOfCoinsInCoinDispenser();

    //     assertEquals("Coin tray should have coins with sum of $1.00", change.doubleValue(), sum.doubleValue(), 0.01);
    // }

    // @Test
    // public void makeChangeMultipleCoins1() {
    //     Customer customer = new Customer();
    //     Checkout checkout = new Checkout(selfCheckoutStation, customer);

    //     BigDecimal change = new BigDecimal("0.35"); // Could be 0.25 + 0.01 or other combinations
    //     Coin.DEFAULT_CURRENCY = currency;

    //     this.addCoinsToCoinDispensers();

    //     checkout.makeChange(change);

    //     BigDecimal sum = this.getSumOfCoinsInCoinDispenser();

    //     assertEquals("Coin tray should have coins with sum of $0.35", change.doubleValue(), sum.doubleValue(), 0.01);
    // }

    @Test
    public void makeChangeMultipleCoins2() {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutStation, customer);

        BigDecimal change = new BigDecimal("0.75"); // Could be 0.50 + 0.25 or other combinations
        Coin.DEFAULT_CURRENCY = currency;

        this.addCoinsToCoinDispensers();

        checkout.makeChange(change);

        BigDecimal sum = this.getSumOfCoinsInCoinDispenser();

        assertEquals("Coin tray should have coins with sum of $0.75", change.doubleValue(), sum.doubleValue(), 0.01);
    }

    @Test
    public void makeChangeNoChange() {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutStation, customer);

        BigDecimal change = new BigDecimal("0.00");
        Coin.DEFAULT_CURRENCY = currency;

        this.addCoinsToCoinDispensers();

        checkout.makeChange(change);

        BigDecimal sum = this.getSumOfCoinsInCoinDispenser();

        assertEquals("Coin tray should have coins with sum of $0.00", change.doubleValue(), sum.doubleValue(), 0.01);
    }

    @Test
    public void makeChangeNoCoins() {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutStation, customer);

        BigDecimal change = new BigDecimal("0.75");
        Coin.DEFAULT_CURRENCY = currency;

        checkout.makeChange(change);

        BigDecimal sum = this.getSumOfCoinsInCoinDispenser();

        assertEquals("Coin tray should have coins with sum of $0.00, because no coins in dispenser to give to customer",
                0.0, sum.doubleValue(), 0.01);
    }

    @Test
    public void makeChangeSingleBanknote1() {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutStation, customer);

        BigDecimal change = new BigDecimal("1.00");
        Coin.DEFAULT_CURRENCY = currency;

        this.clearDispensers(); // Force using banknote for change, since coins are empty
        this.addBanknotesToBanknotesDispenser();

        checkout.makeChange(change);

        BigDecimal sum = this.getSumOfBanknotesInBanknoteOutput();

        assertEquals("Banknote output should have banknotes with sum of $1.00", change.doubleValue(), sum.doubleValue(),
                0.01);
    }

    // @Test
    // public void makeChangeSingleBanknote2() {
    //     Customer customer = new Customer();
    //     Checkout checkout = new Checkout(selfCheckoutStation, customer);

    //     BigDecimal change = new BigDecimal("20.00");
    //     Coin.DEFAULT_CURRENCY = currency;

    //     this.clearDispensers(); // Force using banknote for change, since coins are empty
    //     this.addBanknotesToBanknotesDispenser();

    //     checkout.makeChange(change);

    //     BigDecimal sum = this.getSumOfBanknotesInBanknoteOutput();

    //     assertEquals("Banknote output should have banknotes with sum of $20.00", change.doubleValue(),
    //             sum.doubleValue(),
    //             0.01);
    // }

    // @Test
    // public void makeChangeMultipleBanknote1() {
    //     Customer customer = new Customer();
    //     Checkout checkout = new Checkout(selfCheckoutStation, customer);

    //     BigDecimal change = new BigDecimal("40.00"); // Could be 20.00 + 20.00, or other combinations
    //     Coin.DEFAULT_CURRENCY = currency;

    //     this.clearDispensers(); // Force using banknote for change, since coins are empty
    //     this.addBanknotesToBanknotesDispenser();

    //     checkout.makeChange(change);

    //     BigDecimal sum = this.getSumOfBanknotesInBanknoteOutput();

    //     assertEquals("Banknote output should have banknotes with sum of $40.00", change.doubleValue(),
    //             sum.doubleValue(),
    //             0.01);
    // }

    @Test
    public void makeChangeMultipleBanknote2() {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutStation, customer);

        BigDecimal change = new BigDecimal("101.00"); // Could be 100.00 + 1.00, or other combinations
        Coin.DEFAULT_CURRENCY = currency;

        this.clearDispensers(); // Force using banknote for change, since coins are empty
        this.addBanknotesToBanknotesDispenser();

        checkout.makeChange(change);

        BigDecimal sum = this.getSumOfBanknotesInBanknoteOutput();

        assertEquals("Banknote output should have banknotes with sum of $21.00", change.doubleValue(),
                sum.doubleValue(),
                0.01);
    }

    @Test
    public void makeChangeOneCoinAndOneBanknote() {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutStation, customer);

        BigDecimal change = new BigDecimal("20.25"); // Could be 20.00 bn + 0.25 c, or other combinations
        Coin.DEFAULT_CURRENCY = currency;

        this.addBanknotesToBanknotesDispenser();
        this.addCoinsToCoinDispensers();

        checkout.makeChange(change);

        BigDecimal bs = this.getSumOfBanknotesInBanknoteOutput();
        BigDecimal cs = this.getSumOfCoinsInCoinDispenser();

        BigDecimal sum = BigDecimal.ZERO;
        sum = sum.add(bs);
        sum = sum.add(cs);

        assertEquals("Banknote output should have banknotes with sum of $20.25", change.doubleValue(),
                sum.doubleValue(),
                0.01);
    }

    // @Test
    // public void makeChangeMultipleCoinsAndOneBanknote() {
    //     Customer customer = new Customer();
    //     Checkout checkout = new Checkout(selfCheckoutStation, customer);

    //     BigDecimal change = new BigDecimal("20.85"); // Could be 20.00 bn + 0.25 c + 0.50 c + 0.10 c, or other
    //                                                  // combinations
    //     Coin.DEFAULT_CURRENCY = currency;

    //     this.addBanknotesToBanknotesDispenser();
    //     this.addCoinsToCoinDispensers();

    //     checkout.makeChange(change);

    //     BigDecimal bs = this.getSumOfBanknotesInBanknoteOutput();
    //     BigDecimal cs = this.getSumOfCoinsInCoinDispenser();

    //     BigDecimal sum = BigDecimal.ZERO;
    //     sum = sum.add(bs);
    //     sum = sum.add(cs);

    //     assertEquals("Banknote output should have banknotes with sum of $20.85", change.doubleValue(),
    //             sum.doubleValue(),
    //             0.01);
    // }

    // @Test
    // public void makeChangeOneCoinAndMultipleBanknotes() {
    //     Customer customer = new Customer();
    //     Checkout checkout = new Checkout(selfCheckoutStation, customer);

    //     BigDecimal change = new BigDecimal("120.25"); // 100.00 bn + 20.00 bn + 0.25 c, or other combinations
    //     Coin.DEFAULT_CURRENCY = currency;

    //     this.addBanknotesToBanknotesDispenser();
    //     this.addCoinsToCoinDispensers();

    //     checkout.makeChange(change);

    //     BigDecimal bs = this.getSumOfBanknotesInBanknoteOutput();
    //     BigDecimal cs = this.getSumOfCoinsInCoinDispenser();

    //     BigDecimal sum = BigDecimal.ZERO;
    //     sum = sum.add(bs);
    //     sum = sum.add(cs);

    //     assertEquals("Banknote output should have banknotes with sum of $120.25", change.doubleValue(),
    //             sum.doubleValue(),
    //             0.01);
    // }

    @Test
    public void makeChangeMultipleCoinsAndMultipleBanknotes() {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutStation, customer);

        BigDecimal change = new BigDecimal("136.78");
        Coin.DEFAULT_CURRENCY = currency;

        this.addBanknotesToBanknotesDispenser();
        this.addCoinsToCoinDispensers();

        checkout.makeChange(change);
        assertFalse("Change is not completed", checkout.changeComplete());
        assertTrue("Should be making change", checkout.isMakingChange());

        BigDecimal bs = this.getSumOfBanknotesInBanknoteOutput();
        BigDecimal cs = this.getSumOfCoinsInCoinDispenser();

        assertFalse("Should not be making change", checkout.isMakingChange());
        assertTrue("Change should be completed", checkout.changeComplete());

        BigDecimal sum = BigDecimal.ZERO;
        sum = sum.add(bs);
        sum = sum.add(cs);

        assertEquals("Banknote output should have banknotes with sum of $136.78", change.doubleValue(),
                sum.doubleValue(),
                0.01);
    }

    // @Test
    // public void makeChangeMultipleCoinsAndMultipleBanknotesBigAmount() {
    //     Customer customer = new Customer();
    //     Checkout checkout = new Checkout(selfCheckoutStation, customer);

    //     BigDecimal change = new BigDecimal("5948.94");
    //     Coin.DEFAULT_CURRENCY = currency;

    //     this.addBanknotesToBanknotesDispenser();
    //     this.addCoinsToCoinDispensers();

    //     checkout.makeChange(change);

    //     BigDecimal bs = this.getSumOfBanknotesInBanknoteOutput();
    //     BigDecimal cs = this.getSumOfCoinsInCoinDispenser();

    //     BigDecimal sum = BigDecimal.ZERO;
    //     sum = sum.add(bs);
    //     sum = sum.add(cs);

    //     assertEquals("Banknote output should have banknotes with sum of $5948.94", change.doubleValue(),
    //             sum.doubleValue(),
    //             0.01);
    // }

    @Test
    public void constructorTest() {
        Inventory inv = new Inventory();
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutStation, customer, inv);

        assertNotNull("Checkout should never be null", checkout);
    }

    class FakeItem extends Item {
        public FakeItem(double weight) {
            super(weight);
        }
    }

    class FakeProduct extends Product {
        public FakeProduct(BigDecimal price) {
            super(price, false);
        }
    }


    /**
     * Testing the checkout "sums up the subtotal" loop
     */
    @Test
    public void inventorySumUpTotal() {
        inv.addToInventory(barcode, product, item);

        Item item_test = inv.getItem(barcode);
        assertEquals("item should be the same", item, item_test);

        Product product_test = inv.getProduct(barcode);
        assertEquals("product should be the same", product, product_test);
        assertEquals("price should be the same", price, product_test.getPrice());

        Customer customer = new Customer();
        customer.addToCart(barcode);

        ArrayList<Barcode> cart = customer.getBarcodedItemsInCart();
        assertEquals("cart should have one item", 1, cart.size());
        assertEquals("cart should have the same item", barcode, cart.get(0));

        Checkout checkout = new Checkout(selfCheckoutStation, customer, inv);
        checkout.readyToCheckout();

        BigDecimal subtotal = checkout.getSubtotal();
        assertEquals("Subtotal should be 1.00", price, subtotal);
    }
}