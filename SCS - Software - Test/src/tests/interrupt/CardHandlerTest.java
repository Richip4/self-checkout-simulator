package tests.interrupt;

public class CardHandlerTest
{
    //    // Static variables that will be used during testing
    //    final Currency currency = Currency.getInstance("CAD");
    //    final int[] banknoteDenominations = {5, 10, 20, 50};
    //    final BigDecimal[] coinDenominations = {new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"), new BigDecimal("1.00"), new BigDecimal("2.00")};
    //    final int scaleMaximumWeight = 100;
    //    final int scaleSensitivity = 10;
    //    final int totalBankAccountsCapacity = 10;
    //    final String customer1Name = "John Smith";
    //    final String customer2Name = "Samantha Smith";
    //    final String customer3Name = "Lucas Smith";
    //    final String customer1AccountType = "Debit";
    //    final String customer2AccountType = "Credit";
    //    final String customer3AccountType = "Membership";
    //    final String card1CVV = "2222";
    //    final String card2CVV = "4444";
    //    final String card3CVV = "6666";
    //    final String card1Pin = "1234";
    //    final String card2Pin = "5678";
    //    final String card3Pin = "2468";
    //    final boolean card1TapEnabled = true;
    //    final boolean card2TapEnabled = false;
    //    final boolean card3TapEnabled = true;
    //    final boolean card1HasChip = false;
    //    final boolean card2HasChip = true;
    //    final boolean card3HasChip = true;
    //    final BigDecimal[] balanceAdjustmentAmounts = {new BigDecimal("5.00"), new BigDecimal("10.00"), new BigDecimal("20.00")};
    //
    //    // Declare all relevant implementations
    //    SelfCheckoutStation selfCheckoutStation;
    //    Bank bank;
    //    Membership membershipProvider;
    //    CardHandler cardHandler;
    //    Customer customer1, customer2, customer3;
    //    Bank.BankAccount customer1BankAccount, customer2BankAccount, customer3BankAccount;
    //    Card card1, card2, card3;
    //
    //    // Setup that is run before each test case
    //    @Before
    //    public void setup() throws OverloadException
    //    {
    //        // Initialize all relevant implementations
    //        selfCheckoutStation = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations, scaleMaximumWeight, scaleSensitivity);
    //        selfCheckoutStation.printer.addInk(ReceiptPrinter.MAXIMUM_INK);
    //        selfCheckoutStation.printer.addPaper(ReceiptPrinter.MAXIMUM_PAPER);
    //        bank = new Bank(totalBankAccountsCapacity);
    //        membershipProvider = new Membership();
    //        customer1 = new Customer();
    //        customer2 = new Customer();
    //        customer3 = new Customer();
    //        cardHandler = new CardHandler(selfCheckoutStation, bank, membershipProvider);
    //        customer1BankAccount = bank.createNewAccount(customer1Name, customer1AccountType);
    //        customer2BankAccount = bank.createNewAccount(customer2Name, customer2AccountType);
    //        customer3BankAccount = bank.createNewAccount(customer3Name, customer3AccountType);
    //        card1 = new Card(customer1AccountType, customer1BankAccount.getCardNumber(), customer1Name, card1CVV, card1Pin, card1TapEnabled, card1HasChip);
    //        card2 = new Card(customer2AccountType, customer2BankAccount.getCardNumber(), customer2Name, card2CVV, card2Pin, card2TapEnabled, card2HasChip);
    //        card3 = new Card(customer3AccountType, customer3BankAccount.getCardNumber(), customer3Name, card3CVV, card3Pin, card3TapEnabled, card3HasChip);
    //    }
    //
    //    @Test
    //    public void tapPaySuccessfullyTest() throws IOException
    //    {
    //        customer1BankAccount.addFunds(balanceAdjustmentAmounts[1]);
    //        customer2BankAccount.addFunds(balanceAdjustmentAmounts[2]);
    //
    //        BigDecimal customer1PreviousBalance = customer1BankAccount.getBalance();
    //        BigDecimal customer2PreviousBalance = customer2BankAccount.getBalance();
    //
    //        performTapOrSwipeOrInsertUntilSuccessful(card1, null, customer1, balanceAdjustmentAmounts[0], "Tap");
    //        performTapOrSwipeOrInsertUntilSuccessful(card2, null, customer2, balanceAdjustmentAmounts[0], "Tap");
    //
    //        assertEquals(customer1PreviousBalance.subtract(balanceAdjustmentAmounts[0]), customer1BankAccount.getBalance());
    //        assertEquals(customer2PreviousBalance, customer2BankAccount.getBalance());
    //    }
    //
    //    @Test
    //    public void tapPayUnsuccessfullyTest() throws IOException
    //    {
    //        BigDecimal customer1PreviousBalance = customer1BankAccount.getBalance();
    //        BigDecimal customer2PreviousBalance = customer2BankAccount.getBalance();
    //
    //        performTapOrSwipeOrInsertUntilSuccessful(card1, null, customer1, balanceAdjustmentAmounts[0], "Tap");
    //        performTapOrSwipeOrInsertUntilSuccessful(card2, null, customer2, balanceAdjustmentAmounts[0], "Tap");
    //
    //        assertEquals(customer1PreviousBalance, customer1BankAccount.getBalance());
    //        assertEquals(customer2PreviousBalance, customer2BankAccount.getBalance());
    //    }
    //
    //    @Test
    //    public void insertPaySuccessfullyTest() throws IOException
    //    {
    //        customer2BankAccount.addFunds(balanceAdjustmentAmounts[2]);
    //
    //        BigDecimal customer2PreviousBalance = customer2BankAccount.getBalance();
    //
    //        performTapOrSwipeOrInsertUntilSuccessful(card2, card2Pin, customer2, balanceAdjustmentAmounts[0], "Insert");
    //
    //        assertEquals(customer2PreviousBalance.subtract(balanceAdjustmentAmounts[0]), customer2BankAccount.getBalance());
    //    }
    //
    //    @Test
    //    public void insertPayUnsuccessfullyTest() throws IOException
    //    {
    //        BigDecimal customer2PreviousBalance = customer2BankAccount.getBalance();
    //
    //        performTapOrSwipeOrInsertUntilSuccessful(card2, card2Pin, customer2, balanceAdjustmentAmounts[0], "Insert");
    //
    //        assertEquals(customer2PreviousBalance, customer2BankAccount.getBalance());
    //    }
    //
    //    @Test
    //    public void swipePaySuccessfullyTest() throws IOException
    //    {
    //        customer1BankAccount.addFunds(balanceAdjustmentAmounts[1]);
    //        customer2BankAccount.addFunds(balanceAdjustmentAmounts[2]);
    //
    //        BigDecimal customer1PreviousBalance = customer1BankAccount.getBalance();
    //        BigDecimal customer2PreviousBalance = customer2BankAccount.getBalance();
    //
    //        performTapOrSwipeOrInsertUntilSuccessful(card1, null, customer1, balanceAdjustmentAmounts[0], "Swipe");
    //        performTapOrSwipeOrInsertUntilSuccessful(card2, null, customer2, balanceAdjustmentAmounts[0], "Swipe");
    //
    //        assertEquals(customer1PreviousBalance.subtract(balanceAdjustmentAmounts[0]), customer1BankAccount.getBalance());
    //        assertEquals(customer2PreviousBalance.subtract(balanceAdjustmentAmounts[0]), customer2BankAccount.getBalance());
    //    }
    //
    //    @Test
    //    public void swipePayUnsuccessfullyTest() throws IOException
    //    {
    //        BigDecimal customer1PreviousBalance = customer1BankAccount.getBalance();
    //        BigDecimal customer2PreviousBalance = customer2BankAccount.getBalance();
    //
    //        performTapOrSwipeOrInsertUntilSuccessful(card1, null, customer1, balanceAdjustmentAmounts[0], "Swipe");
    //        performTapOrSwipeOrInsertUntilSuccessful(card2, null, customer2, balanceAdjustmentAmounts[0], "Swipe");
    //
    //        assertEquals(customer1PreviousBalance, customer1BankAccount.getBalance());
    //        assertEquals(customer2PreviousBalance, customer2BankAccount.getBalance());
    //    }
    //
    //    @Test
    //    public void membershipTest() throws IOException
    //    {
    //        performTapOrSwipeOrInsertUntilSuccessful(card3, null, customer3, null, "Tap");
    //        performTapOrSwipeOrInsertUntilSuccessful(card3, card3Pin, customer3, null, "Insert");
    //        performTapOrSwipeOrInsertUntilSuccessful(card3, null, customer3, null, "Swipe");
    //
    //        membershipProvider.addMember(customer3BankAccount.getCardNumber());
    //
    //        performTapOrSwipeOrInsertUntilSuccessful(card3, null, customer3, null, "Tap");
    //        performTapOrSwipeOrInsertUntilSuccessful(card3, card3Pin, customer3, null, "Insert");
    //        performTapOrSwipeOrInsertUntilSuccessful(card3, null, customer3, null, "Swipe");
    //    }
    //
    //    private void performTapOrSwipeOrInsertUntilSuccessful(Card card, String pin, Customer customer, BigDecimal amount, String paymentMethod) throws IOException
    //    {
    //        while (true)
    //        {
    //            try
    //            {
    //                cardHandler.setCustomer(customer);
    //
    //                if (amount != null) cardHandler.setTotal(amount);
    //
    //                if (paymentMethod.equals("Tap")) selfCheckoutStation.cardReader.tap(card);
    //                else if (paymentMethod.equals("Swipe")) selfCheckoutStation.cardReader.swipe(card);
    //                else if (paymentMethod.equals("Insert"))
    //                {
    //                    selfCheckoutStation.cardReader.insert(card, pin);
    //                    selfCheckoutStation.cardReader.remove();
    //                }
    //
    //                break;
    //            } catch (ChipFailureException | MagneticStripeFailureException ignored)
    //            {
    //                if (paymentMethod.equals("Insert")) selfCheckoutStation.cardReader.remove();
    //            }
    //        }
    //    }
}
