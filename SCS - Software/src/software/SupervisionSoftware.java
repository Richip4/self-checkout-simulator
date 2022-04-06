package software;

import java.util.ArrayList;
import java.util.List;

import org.lsmr.selfcheckout.devices.SupervisionStation;

import store.credentials.AuthorizationRequiredException;
import store.credentials.CredentialsSystem;
import store.credentials.IncorrectCredentialException;
import user.Attendant;
import software.observers.SupervisionObserver;

/**
 * A software for a supervision station.
 * 
 * The creation of this object can be seen as a supervision station software
 * system is launched. The constructor initializes / binds the hardware
 * handlers, the hardware devices have observers.
 * 
 * Use cases implemented here:
 * 	- Attendant logs in to their control console
 * 	- Attendant logs out from their control console
 *  - Attendant starts up a station
 * 	- Attendant sthuts down a station
 * 
 * 
 * @author Yunfan Yang
 * @author Tyler Chen
 */
public class SupervisionSoftware extends Software<SupervisionObserver> {
    private final SupervisionStation svs;
    private Attendant attendant;
    private boolean logged_in;
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

    /**
	 * Sets the attendant. It's here because sometimes we don't have an immediate attendant
	 * at start up or attendant can change.
	 * @param attendant
	 */
    public void setAttendant(Attendant attendant){
        this.attendant = attendant;
        this.logged_in = false;
    }

    /**
	 * Prompts the attendant for it's login creds, once that is complete, we get the username
	 * and password and check the login. If the login was successful, we change the 
	 * loggedIn flag.
	 * @return T/F whether we've logged in successfully
	 */
	public void login() throws IncorrectCredentialException {
		String username = attendant.getUsername();
		String password = attendant.getPassword();
		
		if(CredentialsSystem.checkLogin(username, password)) {
			this.logged_in = true;
		} else {
			throw new IncorrectCredentialException("Attendant credential is invalid");
		}
	}
	
	/**
	 * Logout method to make sure that someone has logged out.
	 * @return true if it successfully logs out
	 */
	public void logout() {
		this.logged_in = false;
	}

    /**
	 * Given the hardware, we need to start up the station for use. Assuming that the station
	 * has been shut off due to some reason.
	 * @param scs - SelfCheckoutStation that it wants to boot up.
	 * @return T/F - Whether the software has been booted.
	 * TODO: Disable hardware? Boot up GUI? 
	 * If it enables hardware, should it before or after? I don't think we do
	 * because the hardware team should have it enabled?
	 */
	public void startUpStation(SelfCheckoutSoftware scss) throws AuthorizationRequiredException  {
		if (this.logged_in) {
			scss.startSystem();	
		}else {
			throw new AuthorizationRequiredException("Attendant needs to log in");
		}
	}

    /**
	 * Shuts down the software simply by removing it from the HashMap of checkoutStations
	 * 
	 * @return T/F - whether the checkoutStation has been removed. (If false
	 * the station most likely is not in the HashMap not exist)
	 */
	public void shutDownStation(SelfCheckoutSoftware scss) throws AuthorizationRequiredException {
        if (this.logged_in) {
			scss.stopSystem();
		}else {
			throw new AuthorizationRequiredException("Attendant needs to log in");
		}
	}

	/**
	 * This function should block the Station. We disable the hardware
	 * so that we cannot receive events.
	 * 
	 * @param scss - the SelfCheckoutSoftware
	 * @return T/F whether the station has been blocked.
	 */
	public void blockStation(SelfCheckoutSoftware scss) throws AuthorizationRequiredException {
		if (this.logged_in){
			scss.blockSystem();
		}else{
			throw new AuthorizationRequiredException("Attendant needs to log in");
		}
	}

}

