package software.observers;

import software.SelfCheckoutSoftware;

public interface SupervisionObserver extends Observer {
    public Void banknoteStorageFull(SelfCheckoutSoftware scs);
    public Void coinStorageFull(SelfCheckoutSoftware scs);
}
