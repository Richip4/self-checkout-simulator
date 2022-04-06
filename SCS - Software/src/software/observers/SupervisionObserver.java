package software.observers;

public interface SupervisionObserver extends Observer {
    
    public Void loginRequired();
    public Void logoutSuccessful();
}
