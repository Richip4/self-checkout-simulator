package software.observers;

/**
 * The Observer class for SelfCheckoutSoftware.
 * 
 * PLEASE NOTICE: When you define the return type, instead of {@code Void},
 * please use its wrapper class {@code Void} object, the one with capital V.
 * 
 * Please notice the naming pattern. It should be noun + verb past tense. 
 * 
 * @author Yunfan Yang
 */
public interface SelfCheckoutObserver extends Observer {
    public Void banknoteEjected();

    public Void banknoteInputEnabled();

    public Void banknoteInputDisabled();
    
    public Void invalidCardTypeDetected();
    
    public Void invalidCoinDetected();
    
    public Void invalidBanknoteDetected();

    public Void placeInBaggingAreaBlocked();

    public Void placeInBaggingAreaUnblocked();

    public Void unexpectedItemInBaggingAreaDetected();

    public Void unexpectedItemInBaggingAreaRemoved();

    public Void cardTransactionSucceeded();

    public Void invalidMembershipCardDetected();
}
