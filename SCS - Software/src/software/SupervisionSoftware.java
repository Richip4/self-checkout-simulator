package software;

import java.util.ArrayList;
import java.util.List;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;

import user.Attendant;
import software.observers.SupervisionObserver;

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
    // private Attendant attendant; // TODO: Expecting a Attendant class in the
    // future development
    private final List<SelfCheckoutSoftware> softwareList = new ArrayList<SelfCheckoutSoftware>();
    private Attendant attendant;
    
    public SupervisionSoftware(SupervisionStation svs) {
        this.svs = svs;

        // TODO: Initialize handlers that supervision software needs.
    }

    public SupervisionStation getSupervisionStation() {
        return this.svs;
    }
    
    public void setAttendant(Attendant attendant) {
    	this.attendant = attendant;
    }
    
    public Attendant getAttendant() {
    	return this.attendant;
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
    
    public void notifyBanknoteStorageFull(SelfCheckoutStation scs) {
    	//GUI method that notifies the supervision station screen that the banknote storage
    	// in the given scs is full, attendant will see the message and interact with
    	// the selfcheckout station via the GUI to notify that they have loaded or unloaded
    	// we would need to call on the hardware methods in the GUI to simulate this
    }
    
    public void notifyCoinStorageFull(SelfCheckoutStation scs) {
    	//GUI method that notifies the supervision station screen that the coin storage
    	// in the given scs is full, attendant will see the message and interact with
    	// the selfcheckout station via the GUI to notify that they have loaded or unloaded
    	// we would need to call on the hardware methods in the GUI to simulate this
    }

}

