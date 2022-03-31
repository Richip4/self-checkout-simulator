package checkout;

import java.math.BigDecimal;

import org.lsmr.selfcheckout.Card.CardData;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.CardReaderObserver;

import bank.Bank;
import store.Membership;
import user.Customer;

/**
 * This class handles interactions with the CardReader hardware.
 * This includes, payment and membership cards.
 * @author Tyler Chen
 *
 */
public class CardHandler implements CardReaderObserver {

    private boolean isSwipe;                                                        // if swipe is used, there is no CVV
    private Bank bank;
    private SelfCheckoutStation scs;
    private BigDecimal total;
    private Membership members;
    private Customer customer;

    private boolean isMember;

    /*
     * Constructor for creating a CardHandler. Attaches itself to the cardReader.
     */
    public CardHandler(SelfCheckoutStation scs, Bank bank, Membership members)
    {
        this.scs = scs;
        scs.cardReader.attach(this);
        this.bank = bank;
        isSwipe = false;
        isMember = false;
        this.members = members;
    }

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// we don't have to do anything when the device is enabled

	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// we don't have to do anything when the device is disabled
		// TODO: Future implementations we may need to warn the customer that this device does not work.
	}

	
	@Override
	public void cardInserted(CardReader reader) {
		// we currently do not do anything.
		// future implementations could have a sound play or a please wait message appear.
	}

	/*
	 * On card removal, we enable the reader to allow it to continue reading cards.
	 */
	@Override
	public void cardRemoved(CardReader reader) {
		// we currently also do not do anything here
		// future implementations could revert the card reader screen to it's normal/ready state.
	}

	/*
	 * On card tap, we disable the reader from reading any further cards
	 * until the transaction with the current tap, insertion, swipe
	 * has been completed and then we re-enable it.
	 */
	@Override
	public void cardTapped(CardReader reader) {
		scs.cardReader.disable();
		//notify the customer that the card has been tapped.
		//wait for cardDataRead to finish running before allowing more taps.
		scs.cardReader.enable();
	}

	@Override
	public void cardSwiped(CardReader reader) {
		scs.cardReader.disable();
		isSwipe = true;
		//notify the customer that the card has been swiped.
		//wait for cardDataRead to finish running before allowing more taps.
		scs.cardReader.enable();
		
	}

	/**
	 * On a successful card inserted, tapped, or swiped, we read the data on the card.
	 * The card can be debit, credit or a membership card.
	 * If the card was a membership card, we only want the numbers.
	 * If the card was debit, credit then we will attempt to charge the card.
	 * If the card was swiped, we cannot get the cvv. 
	 * After completion of payment
	 */
	@Override
	public void cardDataRead(CardReader reader, CardData data) {
		//Get the type of card first and strip all whitespace and make it lowercase
		String type = data.getType().toLowerCase().strip();
		
		if (type.equals("membership")) {
			String memberID = data.getNumber();
			isMember = members.checkMember(memberID);
			if (isMember) {
				for (char s : memberID.toCharArray())
                {
                    try
                    {
                        scs.printer.print(s);										//assuming that the printer has ink and paper
                    } catch (EmptyException e)
                    {
                        e.printStackTrace();
                    } catch (OverloadException e)
                    {
                        e.printStackTrace();
                    }
                }
			}else
				customer.notifyCustomerToTryCardAgain();
		}else if (type.equals("debit") || type.equals("credit")) {
			String cardNumbers = data.getNumber();
			String cardHolder = data.getCardholder();
			String cvv = null;

			//if the card wasn't swiped then we want to get the cvv.
			if (!isSwipe)
				cvv = data.getCVV();
			
			//We then bill the account through the bank and if it's completed (checks to see if the cardHolder matches)
			boolean transactionStatus = bank.billAccount(cardNumbers, cardHolder, total);
			
			if (transactionStatus) {
				total = BigDecimal.ZERO;
				customer.notifyCustomerTransactionSuccessful();
			}else
				customer.notifyCustomerToTryCardAgain();
		}else
			customer.notifyCustomerInvalidCardType();
		resetVars();
	}
	
	/*
	 * Resets any vars for the next customer or next card try.
	 */
	private void resetVars() {
		isSwipe = false;
		isMember = false;
	}
	
	/*
	 * For if we want to set the total amount paid.
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	

    // set Customer
    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

	

}