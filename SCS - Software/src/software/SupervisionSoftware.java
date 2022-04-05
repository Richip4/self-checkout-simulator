package software;

import java.util.ArrayList;
import java.util.List;

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
public class SupervisionSoftware extends Software {
    private final SupervisionStation svs;
    // private Attendant attendant; // TODO: Expecting a Attendant class in the
    // future development
    private final List<SelfCheckoutSoftware> softwareList = new ArrayList<SelfCheckoutSoftware>();

    public SupervisionSoftware(SupervisionStation svs) {
        this.svs = svs;

        // TODO: Initialize handlers that supervision software needs.
    }

    public SupervisionStation getSupervisionStation() {
        return this.svs;
    }

    public void add(SelfCheckoutSoftware software) {
        this.softwareList.add(software);
        software.setSupervisionSoftware(this);
    }

    public void remove(SelfCheckoutSoftware software) {
        this.softwareList.remove(software);
        software.setSupervisionSoftware(null);
    }

    public List<SelfCheckoutSoftware> getSoftwareList() {
        return this.softwareList;
    }

}

