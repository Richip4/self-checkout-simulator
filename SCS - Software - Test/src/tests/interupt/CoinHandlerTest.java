package tests.interupt;


import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Assert.*;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.CoinSlot;
import org.lsmr.selfcheckout.devices.CoinStorageUnit;
import org.lsmr.selfcheckout.devices.CoinTray;
import org.lsmr.selfcheckout.devices.CoinValidator;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

import checkout.Checkout;
import interrupt.CoinHandler;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import org.junit.Test;
import org.junit.Assert.*;

import user.Customer;

/**
 * The JUnit test class for the CoinHandler class in SCS - Software.
 * 
 * @author Ricky Bhatti
 * @author Michelle Cheung
 */
public class CoinHandlerTest {
    Currency currency = Currency.getInstance("USD");
    int[] banknoteDenominations = { 1, 5, 10, 25, 100 };
    BigDecimal[] coinDenominations = { new BigDecimal("0.01"), new BigDecimal("0.05"), new BigDecimal("0.1"), new BigDecimal("0.25"), new BigDecimal("1.00") };
    List<BigDecimal> coinDenominationsList = java.util.Arrays.asList(coinDenominations);
    int scaleMaximumWeight = 10;
    int scaleSensitivity = 1;
    SelfCheckoutStation selfCheckoutStation = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations, scaleMaximumWeight, scaleSensitivity);
    Checkout checkout = new Checkout(selfCheckoutStation);
    CoinSlot coinSlot = new CoinSlot();
    CoinTray coinTray = new CoinTray(10);
    CoinValidator coinValidator = new CoinValidator(currency, coinDenominationsList);
    CoinStorageUnit coinStorageUnit = new CoinStorageUnit(10);
    CoinDispenser coinDispenser = new CoinDispenser(10); 
    Coin coin = new Coin(currency, new BigDecimal("0.01"));

    @Test
    public void CoinHandlerTest() {
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        assertNotNull(coinHandler);
    }
    
    @Test
    public void CoinHandlerTest2() {
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation, checkout);
        assertNotNull(coinHandler);
    }
    
    @Test
    public void getCustomerTest() {
    	Customer customer = new Customer();
    	CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
    	coinHandler.setCustomer(customer);
    	assertEquals(customer, coinHandler.getCustomer());
    }
    
    @Test
    public void setCustomerTest() {
        Customer customer = new Customer();
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.setCustomer(customer);
        assertEquals(customer, coinHandler.getCustomer());
    }
    
    @Test
    public void coinInsertedTest() {
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinInserted(coinSlot);
        assertTrue(coinHandler.getCoinDetected());
    }
    
    @Test
    public void getCoinDetectedTest() {
    	CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinInserted(coinSlot);
        assertTrue(coinHandler.getCoinDetected());
    }

    @Test
    public void coinAddedTest() {
        // TODO: Write an actual test for this method, when implemented.

        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinAdded(coinTray);
        assertTrue(true);
    }

    @Test
    public void getCoinDetectedIsValidTest() {
    	Customer customer = new Customer();
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.setCustomer(customer);
        coinHandler.coinInserted(coinSlot);
        coinHandler.validCoinDetected(coinValidator, new BigDecimal("0.01"));
        assertTrue(coinHandler.getCoinDetectedIsValid());
    }
    
    @Test
    public void getCoinValueTest() {
    	CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
    	coinHandler.coinInserted(coinSlot);
    	coinHandler.validCoinDetected(coinValidator, new BigDecimal("0.01"));
    	assertEquals(new BigDecimal("0.01"), coinHandler.getCoinValue());
    }
    
    @Test
    public void validCoinDetectedTest() {
        Customer customer = new Customer();
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.setCustomer(customer);
        coinHandler.coinInserted(coinSlot);
        coinHandler.validCoinDetected(coinValidator, new BigDecimal("0.01"));
        assertTrue(coinHandler.getCoinDetectedIsValid());
    }
    
    @Test
    public void validCoinDetectedTest2() {
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinInserted(coinSlot);
        coinHandler.validCoinDetected(coinValidator, new BigDecimal("0.01"));
        assertEquals(coinHandler.getCoinValue(), new BigDecimal("0.01"));
    }
    
    @Test
    public void validCoinDetectedTest3() {
    	Customer customer = new Customer();
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.setCustomer(customer);
        coinHandler.validCoinDetected(coinValidator, new BigDecimal("0.01"));
        assertEquals(coinHandler.getCoinValue(), new BigDecimal("0.01"));
    }
    
    @Test
    public void invalidCoinDetectedTest() {
        Customer customer = new Customer();
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.setCustomer(customer);
        coinHandler.coinInserted(coinSlot);
        coinHandler.invalidCoinDetected(coinValidator);
        assertTrue(true);
    }
    
    @Test
    public void invalidCoinDetectedTest2() { //null customer
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinInserted(coinSlot);
        coinHandler.invalidCoinDetected(coinValidator);
        assertFalse(coinHandler.getCoinDetectedIsValid());
    }
    
    @Test
    public void invalidCoinDetectedTest3() { //no coin detected
        Customer customer = new Customer();
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.setCustomer(customer);
        coinHandler.invalidCoinDetected(coinValidator);
        assertFalse(coinHandler.getCoinDetectedIsValid());
    }

    @Test
    public void coinsLoadedTest() {
        // TODO: Write an actual test for this method, when implemented.

        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinsLoaded(coinStorageUnit);
        assertTrue(true);
    }

    @Test
    public void coinsUnloadedTest() {
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinsUnloaded(coinStorageUnit);
        assertFalse(selfCheckoutStation.coinSlot.isDisabled());
    }

    @Test
    public void coinsFullTest() {
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinsFull(coinStorageUnit);
        assertTrue(selfCheckoutStation.coinSlot.isDisabled());
    }

    @Test
    public void coinAddedTest2() {
        Customer customer = new Customer();
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.setCustomer(customer);
        coinHandler.coinInserted(coinSlot);
        BigDecimal original = customer.getCashBalance();
        coinHandler.validCoinDetected(coinValidator, new BigDecimal("0.01"));
        coinHandler.coinAdded(coinStorageUnit);
        BigDecimal newValue = customer.getCashBalance();
        assertEquals(new BigDecimal("0.01"), newValue.subtract(original));
    }
    
    @Test
    public void coinAddedTest3() { //null customer
        Customer customer = new Customer();
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinInserted(coinSlot);
        BigDecimal original = customer.getCashBalance();
        coinHandler.validCoinDetected(coinValidator, new BigDecimal("0.01"));
        coinHandler.coinAdded(coinStorageUnit);
        BigDecimal newValue = customer.getCashBalance();
        assertEquals(new BigDecimal("0"), newValue.subtract(original));
    }
    
    @Test
    public void coinAddedTest4() { //invalid coin
        Customer customer = new Customer();
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.setCustomer(customer);
        coinHandler.coinInserted(coinSlot);
        coinHandler.invalidCoinDetected(coinValidator);
        BigDecimal original = customer.getCashBalance();
        coinHandler.coinAdded(coinStorageUnit);
        BigDecimal newValue = customer.getCashBalance();
        assertEquals(new BigDecimal("0"), newValue.subtract(original));
    }

    @Test
    public void getCoinDispenserFull() {
    	CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        assertEquals(false ,coinHandler.getCoinDispenserFull());
    }
    
    @Test
    public void coinsFullTest2() {
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinsFull(coinDispenser);
        assertTrue(coinHandler.getCoinDispenserFull());
    }

    @Test
    public void coinsEmptyTest() {
        // TODO: Write an actual test for this method, when implemented.

        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinsEmpty(coinDispenser);
        assertTrue(true);
    }

    @Test
    public void coinAddedTest5() { //normal
        Customer customer = new Customer();
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.setCustomer(customer);
        coinHandler.coinInserted(coinSlot);
        BigDecimal original = customer.getCashBalance();
        coinHandler.validCoinDetected(coinValidator, new BigDecimal("0.01"));
        coinHandler.coinAdded(coinDispenser, coin);
        BigDecimal newValue = customer.getCashBalance();
        assertEquals(new BigDecimal("0.01"), newValue.subtract(original));
    }

    @Test
    public void coinAddedTest6() { //null customer
        Customer customer = new Customer();
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinInserted(coinSlot);
        BigDecimal original = customer.getCashBalance();
        coinHandler.validCoinDetected(coinValidator, new BigDecimal("0.01"));
        coinHandler.coinAdded(coinDispenser, coin);
        BigDecimal newValue = customer.getCashBalance();
        assertEquals(new BigDecimal("0"), newValue.subtract(original));
    } 
    
    @Test
    public void coinAddedTest7() { //invalid coin
        Customer customer = new Customer();
        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.setCustomer(customer);
        coinHandler.coinInserted(coinSlot);
        coinHandler.invalidCoinDetected(coinValidator);
        BigDecimal original = customer.getCashBalance();
        coinHandler.coinAdded(coinDispenser, coin);
        BigDecimal newValue = customer.getCashBalance();
        assertEquals(new BigDecimal("0"), newValue.subtract(original));
    }
    
    
    @Test
    public void coinRemovedTest() {
        // TODO: Write an actual test for this method, when implemented.

        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinRemoved(coinDispenser, coin);
        assertTrue(true);
    }

    @Test
    public void coinsLoadedTest2() {
        // TODO: Write an actual test for this method, when implemented.

        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinsLoaded(coinDispenser, coin);
        assertTrue(true);
    }

    @Test
    public void coinsUnloadedTest2() {
        // TODO: Write an actual test for this method, when implemented.

        CoinHandler coinHandler = new CoinHandler(selfCheckoutStation);
        coinHandler.coinsUnloaded(coinDispenser, coin);
        assertTrue(true);
    }
}
