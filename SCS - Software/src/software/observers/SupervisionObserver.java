package software.observers;

import software.SelfCheckoutSoftware;

public interface SupervisionObserver extends Observer {
    // Banknore Handler
    public Void banknoteStorageFull(SelfCheckoutSoftware scs);

    public Void banknoteDispenserEmpty(SelfCheckoutSoftware scs);

    // Coin Handler
    public Void coinStorageFull(SelfCheckoutSoftware scs);

    public Void coinDispenserEmpty(SelfCheckoutSoftware scs);

    // Checkout Handler
    public Void dispenseChangeFailed(SelfCheckoutSoftware scs);

    // Receipt Handler
    public Void receiptPrinterOutOfPaper(SelfCheckoutSoftware scs);

    public Void receiptPrinterOutOfInk(SelfCheckoutSoftware scs);
}
