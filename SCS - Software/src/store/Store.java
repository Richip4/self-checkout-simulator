package store;

import java.util.Collections;
import java.util.List;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;


public final class Store {
    private static final SupervisionStation SUPERVISION_STATION = new SupervisionStation();
    
    private Store() {
    }

    public static List<SelfCheckoutStation> getSelfCheckoutStations() {
        return Collections.unmodifiableList(SUPERVISION_STATION.supervisedStations());
    }

    public static SupervisionStation getSupervisionStation() {
        return SUPERVISION_STATION;
    }

    public static void addSelfCheckoutStation(SelfCheckoutStation station) {
        SUPERVISION_STATION.add(station);
    }

    public static void removeSelfCheckoutStation(SelfCheckoutStation station) {
        SUPERVISION_STATION.remove(station);
    }
}
