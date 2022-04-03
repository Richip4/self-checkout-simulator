package user;

import java.util.HashSet;
import java.util.Set;

import org.lsmr.selfcheckout.devices.SupervisionStation;

/**
 * A software for a supervision station.
 * 
 * The creation of this object can be seen as a supervision station software
 * system is launched. The constructor initializes / binds the hardware
 * handlers, the hardware devices have observers.
 * 
 * @author Yunfan Yang
 */
public class SupervisionSoftware {
    private SupervisionStation svs;
    // private Attendant attendant; // TODO: Expecting a Attendant class in the
    // future development
    private final Set<SelfCheckoutSoftware> softwareList = new HashSet<SelfCheckoutSoftware>();

    public SupervisionSoftware(SupervisionStation svs) {
        this.svs = svs;

        // TODO: Initialize handlers that supervision software needs.
    }

    public SupervisionStation getSupervisionStation() {
        return this.svs;
    }

    public void add(SelfCheckoutSoftware software) {
        this.softwareList.add(software);
    }

    public Set<SelfCheckoutSoftware> getSoftwareList() {
        return this.softwareList;
    }
}
