package tests.interrupt;

import checkout.Checkout;
import interrupt.BanknoteHandler;
import org.junit.Test;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.NullPointerSimulationException;
import org.lsmr.selfcheckout.SimulationException;
import org.lsmr.selfcheckout.devices.*;
import software.SelfCheckoutSoftware;
import user.Customer;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map.Entry;

import static org.junit.Assert.*;

/**
 * The JUnit test class for the BanknoteHandler class in SCS - Software.
 *
 * @author Ricky Bhatti
 */
public class BanknoteHandlerTest
{
    Currency currency = Currency.getInstance("USD");
    int[] banknoteDenominations = {1, 5, 10, 25, 100};
    BigDecimal[] coinDenominations = {new BigDecimal("0.01"), new BigDecimal("0.05"), new BigDecimal("0.1"), new BigDecimal("0.25"), new BigDecimal("1.00")};
    int scaleMaximumWeight = 10;
    int scaleSensitivity = 1;
    SelfCheckoutStation selfCheckoutStation = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations, scaleMaximumWeight, scaleSensitivity);
    SelfCheckoutSoftware selfCheckoutSoftware = new SelfCheckoutSoftware(selfCheckoutStation);
    BanknoteValidator banknoteValidator = new BanknoteValidator(currency, banknoteDenominations);
    BanknoteStorageUnit banknoteStorageUnit = new BanknoteStorageUnit(10);
    BanknoteSlot banknoteSlot = new BanknoteSlot(false);
    BanknoteDispenser banknoteDispenser = new BanknoteDispenser(10);
    Banknote banknote = new Banknote(currency, 10);

    private void addBanknotesToBanknotesDispenser()
    {
        // For each dispenser, add 100 banknotes
        for (Entry <Integer, BanknoteDispenser> cds : selfCheckoutStation.banknoteDispensers.entrySet())
        {
            int denom = cds.getKey();
            BanknoteDispenser cd = cds.getValue();

            try
            {
                // Clear the dispenser first, so dispenser is never overloaded by repeatedly
                // adding 100 coins.
                cd.unload();
            } catch (Exception e)
            {
                fail("Coin dispenser unload failed");
            }

            // Add 100 pieces
            for (int t = 0; t < 100; t++)
            {
                try
                {
                    cd.load(new Banknote(currency, denom));
                } catch (OverloadException e)
                {
                    fail("Coin Dispenser is full");
                } catch (SimulationException e)
                {
                    e.printStackTrace();
                    fail("Not operated at this point");
                }
            }

            assertEquals("Banknote dispenser for $" + denom + " should have 100 pieces", 100, cd.size());
        }

        // Remove any left banknotes dangling at the output
        while (true)
        {
            try
            {
                selfCheckoutStation.banknoteOutput.removeDanglingBanknotes();
            } catch (NullPointerSimulationException e)
            {
                break;
            }
        }
    }

    @Test
    public void BanknoteHandlerTest()
    {
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        assertNotNull(banknoteHandler);
    }

    @Test
    public void setCustomerTest()
    {
        Customer customer = new Customer();
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.setCustomer(customer);
        assertTrue("The customer returned from banknoteHandler is not the customer that got set", banknoteHandler.getCustomer() == customer);
    }

    @Test
    public void enabledTest()
    {
        Customer customer = new Customer();
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.setCustomer(customer);
        banknoteHandler.enabled(selfCheckoutStation.banknoteInput);
        banknoteHandler.enabled(selfCheckoutStation.banknoteOutput);
        banknoteHandler.enabled(selfCheckoutStation.banknoteValidator);

        // TODO the implementation calls a stubbed method for future GUI implementation
        // 		implement test when stubbed method has a body
        assertTrue(true);
    }

    @Test
    public void disabledTest()
    {
        Customer customer = new Customer();
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.setCustomer(customer);
        banknoteHandler.disabled(selfCheckoutStation.banknoteInput);
        banknoteHandler.disabled(selfCheckoutStation.banknoteOutput);
        banknoteHandler.disabled(selfCheckoutStation.banknoteValidator);

        // TODO the implementation calls a stubbed method for future GUI implementation
        // 		implement test when stubbed method has a body
        assertTrue(true);
    }

    @Test
    public void validBanknoteDetectedTest()
    {
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        int value = 10;
        banknoteHandler.validBanknoteDetected(banknoteValidator, currency, value);
        BigDecimal returnValue = banknoteHandler.getBanknoteValue();

        assertTrue("Banknote not detected", banknoteHandler.isBanknoteDetected());
        assertTrue("Expected banknote of value " + value + ", actual value " + returnValue, returnValue.compareTo(BigDecimal.valueOf(value)) == 0);
    }

    @Test
    public void invalidBanknoteDetectedTest_validCustomer()
    {
        Customer customer = new Customer();
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.setCustomer(customer);
        banknoteHandler.invalidBanknoteDetected(banknoteValidator);

        // TODO the implementation calls a stubbed method for future GUI implementation
        // 		implement test when stubbed method has a body
        assertTrue(true);
    }

    @Test
    public void invalidBanknoteDetectedTest_nullCustomer()
    {
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.invalidBanknoteDetected(banknoteValidator);

        // TODO the implementation calls a stubbed method for future GUI implementation
        // 		implement test when stubbed method has a body
        assertTrue(true);
    }

    @Test
    public void banknotesFullTest()
    {
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.banknotesFull(banknoteStorageUnit);
        assertTrue(selfCheckoutStation.banknoteInput.isDisabled());
    }

    @Test
    public void banknoteAddedTest_validCustomer()
    {
        Customer customer = new Customer();
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.setCustomer(customer);
        banknoteHandler.validBanknoteDetected(banknoteValidator, currency, 10);
        banknoteHandler.banknoteAdded(banknoteStorageUnit);
        BigDecimal returnValue = banknoteHandler.getBanknoteValue();

        assertFalse("Banknote still detected after processing", banknoteHandler.isBanknoteDetected());
        assertTrue("Expected banknote of value " + BigDecimal.ZERO + ", actual value " + returnValue, returnValue.compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    public void banknoteAddedTest_nullCustomer()
    {
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.validBanknoteDetected(banknoteValidator, currency, 10);
        banknoteHandler.banknoteAdded(banknoteStorageUnit);
        BigDecimal returnValue = banknoteHandler.getBanknoteValue();

        assertFalse("Banknote still detected after processing", banknoteHandler.isBanknoteDetected());
        assertTrue("Expected banknote of value " + BigDecimal.ZERO + ", actual value " + returnValue, returnValue.compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    public void banknoteAddedTest2()
    {
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.setCustomer(null);
        banknoteHandler.banknoteAdded(banknoteStorageUnit);
        BigDecimal returnValue = banknoteHandler.getBanknoteValue();

        assertFalse("Banknote still detected after processing", banknoteHandler.isBanknoteDetected());
        assertTrue("Expected banknote of value " + BigDecimal.ZERO + ", actual value " + returnValue, returnValue.compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    public void banknotesUnloadedTest()
    {
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.banknotesUnloaded(banknoteStorageUnit);
        assertFalse(selfCheckoutStation.banknoteInput.isDisabled());
    }

    @Test
    public void banknoteEjectedTest_validCustomer()
    {
        Customer customer = new Customer();
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.setCustomer(customer);
        banknoteHandler.banknotesEjected(banknoteSlot);

        // TODO the implementation calls a stubbed method for future GUI implementation
        // 		implement test when stubbed method has a body
        assertTrue(true);
    }

    @Test
    public void banknoteEjectedTest_nullCustomer()
    {
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.banknotesEjected(banknoteSlot);

        // TODO the implementation calls a stubbed method for future GUI implementation
        // 		implement test when stubbed method has a body
        assertTrue(true);
    }

    @Test
    public void banknoteRemovedTest_validCustomer()
    {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutSoftware);
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.setCustomer(customer);
        addBanknotesToBanknotesDispenser();
        checkout.makeChange();
        banknoteHandler.banknoteRemoved(banknoteSlot);

        // TODO the implementation calls a stubbed method for future GUI implementation
        // 		implement test when stubbed method has a body
        //		no local state is changed when checkout is null or otherwise; nothing to test
        assertTrue(true);
    }

    @Test
    public void banknoteRemovedTest_nullCustomer()
    {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutSoftware);
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        addBanknotesToBanknotesDispenser();
        checkout.makeChange();
        banknoteHandler.banknoteRemoved(banknoteSlot);

        // TODO the implementation calls a stubbed method for future GUI implementation
        // 		implement test when stubbed method has a body
        //		no local state is changed when checkout is null or otherwise; nothing to test
        assertTrue(true);
    }

    @Test
    public void banknoteRemovedTest2_validCheckout()
    {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutSoftware);
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.setCustomer(customer);
        addBanknotesToBanknotesDispenser();
        checkout.makeChange();
        banknoteHandler.banknoteRemoved(banknoteSlot);

        // TODO the implementation calls a stubbed method for future GUI implementation
        // 		implement test when stubbed method has a body
        //		no local state is changed when checkout is null or otherwise; nothing to test
        assertTrue(true);
    }

    @Test
    public void banknoteRemovedTest2_nullCheckout()
    {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutSoftware);
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.setCustomer(customer);
        addBanknotesToBanknotesDispenser();
        checkout.makeChange();
        banknoteHandler.banknoteRemoved(banknoteSlot);

        // TODO the implementation calls a stubbed method for future GUI implementation
        // 		implement test when stubbed method has a body
        //		no local state is changed when checkout is null or otherwise; nothing to test
        assertTrue(true);
    }

    @Test
    public void banknoteRemovedTest2_noBanknotesDispensed()
    {
        Customer customer = new Customer();
        Checkout checkout = new Checkout(selfCheckoutSoftware);
        BanknoteHandler banknoteHandler = new BanknoteHandler(selfCheckoutSoftware);
        banknoteHandler.setCustomer(customer);
        addBanknotesToBanknotesDispenser();
        checkout.makeChange();
        banknoteHandler.banknoteRemoved(banknoteSlot);

        // TODO the implementation calls a stubbed method for future GUI implementation
        // 		implement test when stubbed method has a body
        //		no local state is changed when checkout is null or otherwise; nothing to test
        assertTrue(true);
    }
}
