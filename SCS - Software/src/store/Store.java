package store;

import java.util.List;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;


public final class Store {
    public static final SupervisionStation SUPERVISION_STATION = new SupervisionStation();
    public static final List<SelfCheckoutStation> SELF_CHECKOUT_STATIONS = SUPERVISION_STATION.supervisedStations();
    
    private Store() {
    }
}
