package interrupt;

import org.lsmr.selfcheckout.Card.CardData;
import org.lsmr.selfcheckout.devices.*;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.CardReaderObserver;
import org.lsmr.selfcheckout.external.CardIssuer;

import bank.Bank;
import databases.GiftCard;
import software.SelfCheckoutSoftware;
import store.Membership;
import user.Customer;

/**
 * This class handles interactions with the CardReader hardware.
 * This includes, payment and membership cards.
 * 
 * @author Tyler Chen
 *
 */
public class CardHandler extends Handler implements CardReaderObserver {

	private final SelfCheckoutSoftware scss;
	private final SelfCheckoutStation scs;
	private Customer customer;

	/*
	 * Constructor for creating a CardHandler. Attaches itself to the cardReader.
	 */
	public CardHandler(SelfCheckoutSoftware scss) {
		this.scss = scss;
		this.scs = this.scss.getSelfCheckoutStation();
		this.scs.cardReader.attach(this);
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// we don't have to do anything when the device is enabled

	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// we don't have to do anything when the device is disabled
		// TODO: Future implementations we may need to warn the customer that this
		// device does not work.
	}

	@Override
	public void cardInserted(CardReader reader) {
		// we currently do not do anything.
		// future implementations could have a sound play or a please wait message
		// appear.
	}

	/*
	 * On card removal, we enable the reader to allow it to continue reading cards.
	 */
	@Override
	public void cardRemoved(CardReader reader) {
		// we currently also do not do anything here
		// future implementations could revert the card reader screen to it's
		// normal/ready state.
	}

	/*
	 * On card tap, we disable the reader from reading any further cards
	 * until the transaction with the current tap, insertion, swipe
	 * has been completed and then we re-enable it.
	 */
	@Override
	public void cardTapped(CardReader reader) {
		// notify the customer that the card has been tapped.
		// wait for cardDataRead to finish running before allowing more taps.
	}

	@Override
	public void cardSwiped(CardReader reader) {
		scs.cardReader.disable();
		// notify the customer that the card has been swiped.
		// wait for cardDataRead to finish running before allowing more taps.
		scs.cardReader.enable();
	}

	/**
	 * On a successful card inserted, tapped, or swiped, we read the data on the
	 * card.
	 * The card can be debit, credit or a membership card.
	 * If the card was a membership card, we only want the numbers.
	 * If the card was debit, credit then we will attempt to charge the card.
	 * If the card was swiped, we cannot get the cvv.
	 * After completion of payment
	 */
	@Override
	public void cardDataRead(CardReader reader, CardData data) {
		// Get the type of card first and strip all whitespace and make it lowercase
		String type = data.getType().toLowerCase().strip();

		if (type.equals("membership")) {
			String memberID = data.getNumber();
			boolean isMember = Membership.isMember(memberID);

			if (!isMember) {
				this.scss.notifyObservers(observer -> observer.invalidMembershipCardDetected());
				return;
			}

			this.customer.setMemberID(memberID);

			this.scss.notifyObservers(observer -> observer.membershipCardDetected(memberID));
		} else if (type.equals("debit") || type.equals("credit")) {
			String cardNumber = data.getNumber();

			CardIssuer issuer = Bank.getCardIssuer(cardNumber);
			int holdNumber = issuer.authorizeHold(cardNumber, this.customer.getCartSubtotal());

			// Fail to hold the authorization
			if (holdNumber == -1) {
				this.scss.notifyObservers(observer -> observer.paymentHoldingAuthorizationFailed());
				return;
			}

			boolean posted = issuer.postTransaction(cardNumber, holdNumber, this.customer.getCartSubtotal());

			// Fail to post transaction
			if (!posted) {
				this.scss.notifyObservers(observer -> observer.paymentPostingTransactionFailed());
				return;
			}

			this.scss.notifyObservers(observer -> observer.paymentCompleted());
		} else if(type.equals("gift")){
			if(GiftCard.isGiftCard(data.getNumber()))
			{
				String cardNumber = data.getNumber();
				CardIssuer issuer = GiftCard.getCardIssuer();
				int holdNumber = issuer.authorizeHold(cardNumber, this.customer.getCartSubtotal());
	
				// Fail to hold the authorization
				if (holdNumber == -1) 
				{
					this.scss.notifyObservers(observer -> observer.paymentHoldingAuthorizationFailed());
					return;
				}
	
				boolean posted = issuer.postTransaction(cardNumber, holdNumber, this.customer.getCartSubtotal());
	
				// Fail to post transaction
				if (!posted) {
					this.scss.notifyObservers(observer -> observer.paymentPostingTransactionFailed());
					return;
				}
					this.scss.notifyObservers(observer -> observer.paymentCompleted());
					return;
				} else {
					this.scss.notifyObservers(observer -> observer.invalidGiftCardDetected());
				}
		} else {
		this.scss.notifyObservers(observer -> observer.invalidCardTypeDetected());
		}

		// Variables will be reset after when the next customer is binded.
	}
}