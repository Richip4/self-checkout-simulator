package interrupt;

public abstract class Middleware {
    public abstract void attachAll();

    public abstract void enableHardware();

    public abstract void disableHardware();

    public abstract void detatchAll();
}
