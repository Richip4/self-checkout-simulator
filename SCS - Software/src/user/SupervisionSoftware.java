package user;

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

    public SupervisionSoftware(SupervisionStation svs) {
        this.svs = svs;

        // Initialize handlers that supervision software needs.
    }
}
