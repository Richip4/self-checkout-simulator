package software;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

import checkout.Checkout;
import checkout.Receipt;
import interrupt.BanknoteHandler;
import interrupt.CardHandler;
import interrupt.CoinHandler;
import interrupt.ProcessItemHandler;
import software.observers.SelfCheckoutObserver;
import user.Attendant;
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
public class SelfCheckoutSoftware extends Software<SelfCheckoutObserver> {
	// Several station states used for GUI
	public static final String OKAY_STATUS = "OKAY";
	public static final String MISSING_ITEM_STATUS = "MISSING ITEM";
	public static final String WEIGHT_DISCREPENCY_STATUS = "WEIGHT DISCREPENCY";
	public static final String BLOCKED_STATUS = "BLOCKED";
	public static final String OFFLINE_STATUS = "OFFLINE";
	
	private String state = OKAY_STATUS;
	
    private final SelfCheckoutStation scs;
    private SupervisionSoftware svs;
    private Customer customer;
    private Attendant attendant;

    private BanknoteHandler banknoteHandler;
    private CardHandler cardHandler;
    private CoinHandler coinHandler;
    private ProcessItemHandler processItemHandler;

    private Checkout checkout; // Controller for processing checkout
    private Receipt receipt; // Controller for printing receipt

    public SelfCheckoutSoftware(SelfCheckoutStation scs) {
        this.scs = scs;
        this.state = OKAY_STATUS;

        this.startSystem();
    }
    
    public void setUser(User user) {
    	if (user instanceof Customer) {
    		setCustomer((Customer)user);
    	} else if (user instanceof Attendant) {
    		setAttendant((Attendant)user);
    	}
    }

    private void setCustomer(Customer customer) {
        this.customer = customer;

        this.banknoteHandler.setCustomer(customer);
        this.cardHandler.setCustomer(customer);
        this.coinHandler.setCustomer(customer);
        this.processItemHandler.setCustomer(customer);

        this.checkout.setCustomer(customer);
        this.receipt.setCustomer(customer);
    }
    
    private void setAttendant(Attendant attendant) {
    	this.attendant = attendant;
    	
    	// attendant must be accompanied by customer to process items
    	// but an attedant alone can service the station
    	// TODO: consider if components need to be altered do to the presence of an attendant
    	
    }
    
    /**
     * Sets this stations state to one of the following:
     * 		SelfCheckoutSoftware.OKAY_STATUS
     * 		SelfCheckoutSoftware.MISSING_ITEM_STATUS
     * 		SelfCheckoutSoftware.WEIGHT_DISCREPENCY_STATUS
     * 		SelfCheckoutSoftware.BLOCKED_STATUS
     * 		SelfCheckoutSoftware.OFFLINE_STATUS
     * @param state
     */
    public void setState(String state) {
    	this.state = state;
    }
    
    public String getState() {
    	return state;
    }
    
    public void removeUser(User user) {
    	if (user instanceof Customer) {
    		customer = null;
    	} else if (user instanceof Attendant) {
    		attendant = null;
    	}
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

    public void notifyBanknoteEjected() {
        this.checkout.makeChange();
    }

    public void enableHardware() {
        this.banknoteHandler.enableHardware();
        this.cardHandler.enableHardware();
        this.coinHandler.enableHardware();
        this.processItemHandler.enableHardware();
    }

    public void disableHardware() {
        this.banknoteHandler.disableHardware();
        this.cardHandler.disableHardware();
        this.coinHandler.disableHardware();
        this.processItemHandler.disableHardware();
    }

    /**
     * This method is used for starting or restarting a system.
     * We do not want to mess with the SelfCheckoutStation because we do not create
     * new hardware
     * when something is turned on/off.
     */
    public void startSystem() {
        this.banknoteHandler = new BanknoteHandler(this);
        this.cardHandler = new CardHandler(this);
        this.coinHandler = new CoinHandler(this);
        this.processItemHandler = new ProcessItemHandler(this);
        this.checkout = new Checkout(this);
        this.receipt = new Receipt(this);

        this.enableHardware();

        this.notifyObservers(observer -> observer.softwareStarted(this));
    }

    /**
     * Turns off the system by setting everything to null, the Handlers are
     * technically turned off.
     * We do not want to mess with the SelfCheckoutStation because we do not create
     * new hardware
     * when something is turned off.
     */
    public void stopSystem() {
        this.disableHardware();

        this.banknoteHandler.detatchAll();
        this.banknoteHandler = null;

        this.cardHandler.detatchAll();
        this.cardHandler = null;

        this.coinHandler.detatchAll();
        this.coinHandler = null;

        this.processItemHandler.detatchAll();
        this.processItemHandler = null;

        this.checkout = null;

        this.receipt.detatchAll();
        this.receipt = null;

        this.notifyObservers(observer -> observer.softwareStopped(this));
    }

    public void blockSystem() {
        this.disableHardware();
        this.notifyObservers(observer -> observer.touchScreenBlocked());
    }

    protected void unblockSystem() {
        this.enableHardware();
        this.notifyObservers(observer -> observer.touchScreenUnblocked());
    }

    public void makeChange() {
        this.checkout.makeChange();
    }
}