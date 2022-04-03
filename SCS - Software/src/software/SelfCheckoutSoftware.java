package software;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

import checkout.Checkout;
import checkout.Receipt;
import interrupt.BanknoteHandler;
import interrupt.CardHandler;
import interrupt.CoinHandler;
import interrupt.ProcessItemHandler;
import user.Customer;

/**
 * A software for a self-checkout station.
 * 
 * The creation of this object can be seen as a self-checkout station software
 * system is launched. The constructor initializes / binds the hardware
 * handlers, the hardware devices have observers.
 * 
 * @author Yunfan Yang
 */
public class SelfCheckoutSoftware {
    private SelfCheckoutStation scs;
    private Customer customer;

    private final BanknoteHandler banknoteHandler;
    private final CardHandler cardHandler;
    private final CoinHandler coinHandler;
    private final ProcessItemHandler processItemHandler;

    private final Checkout checkout; // Controller for processing checkout
    private final Receipt receipt; // Controller for printing receipt

    public SelfCheckoutSoftware(SelfCheckoutStation scs) {
        this.scs = scs;

        this.banknoteHandler = new BanknoteHandler(scs);
        this.cardHandler = new CardHandler(scs);
        this.coinHandler = new CoinHandler(scs);
        this.processItemHandler = new ProcessItemHandler(scs);
        
        this.checkout = new Checkout(scs);
        this.receipt = new Receipt(scs);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;

        this.banknoteHandler.setCustomer(customer);
        this.cardHandler.setCustomer(customer);
        this.coinHandler.setCustomer(customer);
        this.processItemHandler.setCustomer(customer);

        this.checkout.setCustomer(customer);
        this.receipt.setCustomer(customer);
    }

    public SelfCheckoutStation getSelfCheckoutStation() {
        return this.scs;
    }

    public Customer getCustomer() {
        return this.customer;
    }
}