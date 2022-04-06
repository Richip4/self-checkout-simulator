package software;

import java.util.ArrayList;
import java.util.List;

import org.lsmr.selfcheckout.devices.SupervisionStation;

import software.observers.SupervisionObserver;
import user.Attendant;

/**
 * A software for a supervision station.
 * 
 * The creation of this object can be seen as a supervision station software
 * system is launched. The constructor initializes / binds the hardware
 * handlers, the hardware devices have observers.
 * 
 * @author Yunfan Yang
 */
public class SupervisionSoftware extends Software<SupervisionObserver> {
    private final SupervisionStation svs;
    private Attendant attendant; 
    // future development
    private final List<SelfCheckoutSoftware> softwareList = new ArrayList<SelfCheckoutSoftware>();

    public SupervisionSoftware(SupervisionStation svs) {
        this.svs = svs;

        // TODO: Initialize handlers that supervision software needs.
    }
    
    public void setAttendant(Attendant attendant) {
    	this.attendant = attendant;
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

