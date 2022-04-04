package software;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

import checkout.Checkout;
import checkout.Receipt;
import interrupt.BanknoteHandler;
import interrupt.CardHandler;
import interrupt.CoinHandler;
import interrupt.ProcessItemHandler;
import user.Customer;
import user.User;

/**
 * A software for a self-checkout station.
 * 
 * The creation of this object can be seen as a self-checkout station software
 * system is launched. The constructor initializes / binds the hardware
 * handlers, the hardware devices have observers.
 * 
 * @author Yunfan Yang
 */
public class SelfCheckoutSoftware extends Software {
    private final SelfCheckoutStation scs;
    private SupervisionSoftware svs;
    private Customer customer;

    private final BanknoteHandler banknoteHandler;
    private final CardHandler cardHandler;
    private final CoinHandler coinHandler;
    private final ProcessItemHandler processItemHandler;

    private final Checkout checkout; // Controller for processing checkout
    private final Receipt receipt; // Controller for printing receipt

    public SelfCheckoutSoftware(SelfCheckoutStation scs) {
        this.scs = scs;

        this.banknoteHandler = new BanknoteHandler(this);
        this.cardHandler = new CardHandler(this);
        this.coinHandler = new CoinHandler(this);
        this.processItemHandler = new ProcessItemHandler(this);
        
        this.checkout = new Checkout(this);
        this.receipt = new Receipt(this);
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

    /**
     * This method should not be used.
     * If want to set supersivion software for this self-checkout software,
     * please use {@link SupervisionSoftware#add(SelfCheckoutSoftware)}.
     * 
     * @param svs
     * @author Yunfan Yang
     */
    protected void setSupervisionSoftware(SupervisionSoftware svs) {
        this.svs = svs;
    }

    public SupervisionSoftware getSupervisionSoftware() {
        return this.svs;
    }
}