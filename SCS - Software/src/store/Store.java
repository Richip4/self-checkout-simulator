package store;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;

import user.SelfCheckoutSoftware;
import user.SupervisionSoftware;

public final class Store {
    private static final SupervisionStation SUPERVISION_STATION = new SupervisionStation();
    private static final SupervisionSoftware SUPERVISION_SOFTWARE = new SupervisionSoftware(SUPERVISION_STATION);
    private static final Map<SelfCheckoutStation, SelfCheckoutSoftware> SOFTWARE_MAP = new HashMap<SelfCheckoutStation, SelfCheckoutSoftware>();
    
    private Store() {
    }

    public static List<SelfCheckoutStation> getSelfCheckoutStations() {
        return Collections.unmodifiableList(SUPERVISION_STATION.supervisedStations());
    }

    public static SupervisionStation getSupervisionStation() {
        return SUPERVISION_STATION;
    }

    public static SupervisionSoftware getSupervisionSoftware() {
        return SUPERVISION_SOFTWARE;
    }

    public static void addSelfCheckoutStation(SelfCheckoutStation station) {
        SUPERVISION_STATION.add(station);

        // Create a new software object for the station.
        SOFTWARE_MAP.put(station, new SelfCheckoutSoftware(station));
    }

    public static void removeSelfCheckoutStation(SelfCheckoutStation station) {
        SUPERVISION_STATION.remove(station);
    }

    public static SelfCheckoutSoftware getSelfCheckoutStationSoftware(SelfCheckoutStation station) {
        return SOFTWARE_MAP.get(station);
    }
}
