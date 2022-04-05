package software.observers;

public interface SupervisionObserver extends Observer {
    
    public Void loginRequired();
    public Void logoutSuccessful();

    public Void touchScreenBlock();                     //GUI should block the touch screen
    public Void stationBlockSuccessful();

}
