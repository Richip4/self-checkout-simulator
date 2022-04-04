package software;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import software.observers.Observer;

/**
 * 
 * @author Yunfan Yang
 */
public abstract class Software<T extends Observer> {
    private List<T> observers = new ArrayList<T>();

    public void addObserver(T observer) {
        this.observers.add(observer);
    }

    public void removeObserver(T observer) {
        this.observers.remove(observer);
    }

    public List<T> getObservers() {
        return this.observers;
    }

    /**
     * Usage: <pre>software.notifyObservers(observer -> observer.event(parameter));</pre>
     * 
     * @param function
     * 
     * @author Yunfan Yang
     */
    public void notifyObservers(Function<T, Void> function) {
        for (T observer : observers) {
            function.apply(observer);
        }
    }
}
