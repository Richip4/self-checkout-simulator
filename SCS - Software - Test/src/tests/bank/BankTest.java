package tests.bank;

import bank.Bank;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.external.CardIssuer;

import static org.junit.Assert.*;

public class BankTest
{
    // TODO: Double check if new test cases need to be implemented as Bank.java indicates that it is incomplete

    // Declare the card issuers
    CardIssuer cardIssuer1;
    CardIssuer cardIssuer2;

    // Setup that is run before each test case
    @Before
    public void setup()
    {
        // Initialize the card issuers
        cardIssuer1 = new CardIssuer("Visa");
        cardIssuer2 = new CardIssuer("Mastercard");

        // Resets the card issuers
        Bank.clearIssuers();
    }

    @Test
    public void addAndGetIssuerTest()
    {
        assertFalse(Bank.getIssuers().contains(cardIssuer1));
        assertFalse(Bank.getIssuers().contains(cardIssuer2));
        assertEquals(0, Bank.getIssuers().size());

        Bank.addIssuer(cardIssuer1);
        Bank.addIssuer(cardIssuer2);

        assertTrue(Bank.getIssuers().contains(cardIssuer1));
        assertTrue(Bank.getIssuers().contains(cardIssuer2));
        assertEquals(2, Bank.getIssuers().size());
    }

    @Test
    public void removeIssuerTest()
    {
        assertFalse(Bank.getIssuers().contains(cardIssuer1));
        assertFalse(Bank.getIssuers().contains(cardIssuer2));
        assertEquals(0, Bank.getIssuers().size());

        Bank.addIssuer(cardIssuer1);
        Bank.addIssuer(cardIssuer2);
        Bank.removeIssuer(cardIssuer1);
        Bank.removeIssuer(cardIssuer2);

        assertFalse(Bank.getIssuers().contains(cardIssuer1));
        assertFalse(Bank.getIssuers().contains(cardIssuer2));
        assertEquals(0, Bank.getIssuers().size());
    }

    @Test
    public void clearIssuerTest()
    {
        assertFalse(Bank.getIssuers().contains(cardIssuer1));
        assertFalse(Bank.getIssuers().contains(cardIssuer2));
        assertEquals(0, Bank.getIssuers().size());

        Bank.addIssuer(cardIssuer1);
        Bank.addIssuer(cardIssuer2);
        Bank.clearIssuers();

        assertFalse(Bank.getIssuers().contains(cardIssuer1));
        assertFalse(Bank.getIssuers().contains(cardIssuer2));
        assertEquals(0, Bank.getIssuers().size());
    }
}
