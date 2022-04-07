package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Main class that runs all the other tests
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // All tests under tests.application

        // All tests under tests.bank
        tests.bank.BankTest.class,
        // All tests under tests.checkout
        tests.checkout.CheckoutTest.class, tests.checkout.ReceiptTest.class,
        // All tests under tests.databases

        // All tests under tests.gui

        // All tests under tests.interrupt
        tests.interrupt.BanknoteHandlerTest.class, tests.interrupt.CardHandlerTest.class, tests.interrupt.CoinHandlerTest.class, tests.interrupt.ProcessItemHandlerTest.class,
        // All tests under tests.software

        // All tests under tests.software.observers

        // All tests under tests.store
        tests.store.GiftCardTest.class, tests.store.InventoryTest.class, tests.store.MembershipTest.class, tests.store.StoreTest.class,
        // All tests under tests.store.credentials
        tests.store.credentials.CredentialsSystemTest.class,
        // All tests under tests.user
        tests.user.AttendantTest.class, tests.user.CustomerTest.class})
public class MainTest
{

}
