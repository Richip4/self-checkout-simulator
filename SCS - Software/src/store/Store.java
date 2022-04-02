package store;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;

/**
 * This class represents the entry point of store.
 * 
 * The store initializes:
 * - the attendant station (SupervisionStation)
 * - the self-checkout stations (SelfCheckoutStation)
 * 
 * @author Yunfan Yang
 */
public final class Store {
    private SupervisionStation supervisionStation;

    public Store() {
        Currency currency = Currency.getInstance("CAD");
        int[] banknoteDenominations = { 1, 5, 10, 20, 100 };
        BigDecimal[] coinDenominations = {
                new BigDecimal("0.01"),
                new BigDecimal("0.05"),
                new BigDecimal("0.1"),
                new BigDecimal("0.25"),
                new BigDecimal("1.00")
        };

        this.supervisionStation = new SupervisionStation();

        // Initialize 6 self-checkout stations
        // and add them to the supervision station to be supervised
        for (int t = 0; t < 6; t++) {
            SelfCheckoutStation station = new SelfCheckoutStation(currency, banknoteDenominations,
                    coinDenominations, 1000, 2);
            this.supervisionStation.add(station);
        }
    }

    public List<SelfCheckoutStation> getSelfCheckoutStations() {
        return this.supervisionStation.supervisedStations();
    }

    public SupervisionStation getSupervisionStation() {
        return this.supervisionStation;
    }
}
