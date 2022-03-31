package tests.bank;

import bank.Bank;
import bank.Bank.BankAccount;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class BankTest
{
    // Static variables that will be used during testing
    final int totalBankAccountsCapacity = 10;
    final String customer1Name = "John Smith";
    final String customer2Name = "Samantha Smith";
    final String customer1AccountType = "Debit";
    final String customer2AccountType = "Credit";
    final BigDecimal[] balanceAdjustmentAmounts = {new BigDecimal("5.00"), new BigDecimal("10.00"), new BigDecimal("20.00")};

    // Declare the bank
    Bank bank;

    // Declare the bank accounts
    BankAccount customer1BankAccount;
    BankAccount customer2BankAccount;

    // Setup that is run before each test case
    @Before
    public void setup()
    {
        // Initialize the bank
        bank = new Bank(totalBankAccountsCapacity);

        // Initialize the bank accounts
        customer1BankAccount = bank.createNewAccount(customer1Name, customer1AccountType);
        customer2BankAccount = bank.createNewAccount(customer2Name, customer2AccountType);
    }

    @Test
    public void accountCreationTest()
    {
        assertNotEquals(null, customer1BankAccount);
        assertNotEquals(null, customer2BankAccount);

        assertEquals(0, customer1BankAccount.getBalance().compareTo(BigDecimal.ZERO));
        assertEquals(0, customer2BankAccount.getBalance().compareTo(BigDecimal.ZERO));

        assertNotEquals(null, customer1BankAccount.getCardNumber());
        assertNotEquals(null, customer2BankAccount.getCardNumber());

        assertFalse(customer1BankAccount.getCardNumber().isBlank());
        assertFalse(customer2BankAccount.getCardNumber().isBlank());

        assertNotSame(customer2BankAccount, customer1BankAccount);
    }

    @Test
    public void addFundsTest()
    {
        customer1BankAccount.addFunds(balanceAdjustmentAmounts[1]);
        customer2BankAccount.addFunds(balanceAdjustmentAmounts[2]);

        assertEquals(balanceAdjustmentAmounts[1], customer1BankAccount.getBalance());
        assertEquals(balanceAdjustmentAmounts[2], customer2BankAccount.getBalance());

        customer1BankAccount.addFunds(balanceAdjustmentAmounts[0]);
        customer2BankAccount.addFunds(balanceAdjustmentAmounts[0]);

        assertEquals(balanceAdjustmentAmounts[1].add(balanceAdjustmentAmounts[0]), customer1BankAccount.getBalance());
        assertEquals(balanceAdjustmentAmounts[2].add(balanceAdjustmentAmounts[0]), customer2BankAccount.getBalance());
    }

    @Test
    public void deductFundsSuccessfullyTest()
    {
        customer1BankAccount.addFunds(balanceAdjustmentAmounts[1].add(balanceAdjustmentAmounts[0]));
        customer2BankAccount.addFunds(balanceAdjustmentAmounts[2].add(balanceAdjustmentAmounts[0]));

        customer1BankAccount.removeFunds(balanceAdjustmentAmounts[0]);
        customer2BankAccount.removeFunds(balanceAdjustmentAmounts[0]);

        assertEquals(balanceAdjustmentAmounts[1], customer1BankAccount.getBalance());
        assertEquals(balanceAdjustmentAmounts[2], customer2BankAccount.getBalance());

        customer1BankAccount.removeFunds(balanceAdjustmentAmounts[1]);
        customer2BankAccount.removeFunds(balanceAdjustmentAmounts[2]);

        assertEquals(0, customer1BankAccount.getBalance().compareTo(BigDecimal.ZERO));
        assertEquals(0, customer2BankAccount.getBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void deductFundsUnsuccessfullyTest()
    {
        customer1BankAccount.addFunds(balanceAdjustmentAmounts[0]);
        customer2BankAccount.addFunds(balanceAdjustmentAmounts[1]);

        customer1BankAccount.removeFunds(balanceAdjustmentAmounts[2]);
        customer2BankAccount.removeFunds(balanceAdjustmentAmounts[2]);

        assertEquals(balanceAdjustmentAmounts[0], customer1BankAccount.getBalance());
        assertEquals(balanceAdjustmentAmounts[1], customer2BankAccount.getBalance());
    }

    @Test
    public void getAccountTest()
    {
        assertSame(customer1BankAccount, bank.getAccount(customer1BankAccount.getCardNumber()));
        assertSame(customer2BankAccount, bank.getAccount(customer2BankAccount.getCardNumber()));
    }

    @Test
    public void getAccountFundsTest()
    {
        customer1BankAccount.addFunds(balanceAdjustmentAmounts[1]);
        customer2BankAccount.addFunds(balanceAdjustmentAmounts[2]);

        assertEquals(balanceAdjustmentAmounts[1], bank.getAccountFunds(customer1BankAccount.getCardNumber()));
        assertEquals(balanceAdjustmentAmounts[2], bank.getAccountFunds(customer2BankAccount.getCardNumber()));
    }

    @Test
    public void payAccountTest()
    {
        bank.addFundsToAccount(customer1BankAccount.getCardNumber(), balanceAdjustmentAmounts[1]);
        bank.addFundsToAccount(customer2BankAccount.getCardNumber(), balanceAdjustmentAmounts[2]);

        assertEquals(balanceAdjustmentAmounts[1], customer1BankAccount.getBalance());
        assertEquals(balanceAdjustmentAmounts[2], customer2BankAccount.getBalance());

        bank.addFundsToAccount(customer1BankAccount.getCardNumber(), balanceAdjustmentAmounts[0]);
        bank.addFundsToAccount(customer2BankAccount.getCardNumber(), balanceAdjustmentAmounts[0]);

        assertEquals(balanceAdjustmentAmounts[1].add(balanceAdjustmentAmounts[0]), customer1BankAccount.getBalance());
        assertEquals(balanceAdjustmentAmounts[2].add(balanceAdjustmentAmounts[0]), customer2BankAccount.getBalance());
    }

    @Test
    public void chargeAccountSuccessfullyTest()
    {
        customer1BankAccount.addFunds(balanceAdjustmentAmounts[1].add(balanceAdjustmentAmounts[0]));
        customer2BankAccount.addFunds(balanceAdjustmentAmounts[2].add(balanceAdjustmentAmounts[0]));

        bank.billAccount(customer1BankAccount.getCardNumber(), customer1Name, balanceAdjustmentAmounts[0]);
        bank.billAccount(customer2BankAccount.getCardNumber(), customer2Name, balanceAdjustmentAmounts[0]);

        assertEquals(balanceAdjustmentAmounts[1], customer1BankAccount.getBalance());
        assertEquals(balanceAdjustmentAmounts[2], customer2BankAccount.getBalance());

        bank.billAccount(customer1BankAccount.getCardNumber(), customer1Name, balanceAdjustmentAmounts[1]);
        bank.billAccount(customer2BankAccount.getCardNumber(), customer2Name, balanceAdjustmentAmounts[2]);

        assertEquals(0, customer1BankAccount.getBalance().compareTo(BigDecimal.ZERO));
        assertEquals(0, customer2BankAccount.getBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void chargeAccountUnsuccessfullyTest()
    {
        customer1BankAccount.addFunds(balanceAdjustmentAmounts[1]);
        customer2BankAccount.addFunds(balanceAdjustmentAmounts[2]);

        bank.billAccount(customer1BankAccount.getCardNumber(), customer2Name, balanceAdjustmentAmounts[0]);
        bank.billAccount(customer2BankAccount.getCardNumber(), customer1Name, balanceAdjustmentAmounts[0]);

        assertEquals(balanceAdjustmentAmounts[1], customer1BankAccount.getBalance());
        assertEquals(balanceAdjustmentAmounts[2], customer2BankAccount.getBalance());
    }
}
