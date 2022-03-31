package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Main class that runs all the other tests
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // All tests under tests.bank
        tests.bank.BankTest.class,
        // All tests under tests.checkout
        tests.checkout.CardHandlerTest.class, tests.checkout.CheckoutTest.class, tests.checkout.ReceiptTest.class,
        // All tests under tests.interupt
        tests.interupt.BanknoteHandlerTest.class, tests.interupt.CoinHandlerTest.class, tests.interupt.ProcessItemHandlerTest.class,
        // All tests under tests.store
        tests.store.InventoryTest.class, tests.store.MembershipTest.class,
        // All tests under tests.user
        tests.user.CustomerTest.class})
public class MainTest
{

}
