package user;

import java.util.ArrayList;
import java.util.List;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;

import store.credentials.CredentialsSystem;

/**
 * A software for a supervision station.
 * 
 * The creation of this object can be seen as a supervision station software
 * system is launched. The constructor initializes / binds the hardware
 * handlers, the hardware devices have observers.
 * 
 * @author Yunfan Yang
 * @author Tyler Chen
 */
public class SupervisionSoftware {
    private SupervisionStation svs;
    private Attendant attendant; // TODO: Expecting a Attendant class in the
    private CredentialsSystem creds;
    
    private boolean logged_in;
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
	public boolean login() {
		this.attendant.promptLogin();	//hopefully calls the GUI to enter login
		
		//the above has to be completed.
		String username = attendant.getUsername();
		String password = attendant.getPassword();
		
		if(creds.checkLogin(username, password)) {
			this.logged_in = true;
			return true;
		}
		else 
			return false;		
	}
	
	/**
	 * Logout method to make sure that someone has logged out.
	 * @return true if it successfully logs out
	 */
	public boolean logout() {
		this.attendant.promptLogout();
		this.logged_in = false;
		return true;
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
	public boolean startUpStation(SelfCheckoutStation scs)  {
		if (logged_in) {
			SelfCheckoutSoftware software = new SelfCheckoutSoftware(scs);
			List<SelfCheckoutSoftware> software_list = Store.getSelfCheckoutSoftwareList();
			
				

				
			return true;
		}else {
			login();
			return false;
		}
	}

    /**
	 * Shuts down the software simply by removing it from the HashMap of checkoutStations
	 * 
	 * @param id - Each station should be identified by an id (could use scs but not easily
	 * identifiable.
	 * @return T/F - whether the checkoutStation has been removed. (If false
	 * the station most likely is not in the HashMap not exist)
	 */
	public boolean shutDownStation(int id) {
		if (logged_in) {
			
            return true;
		}else {
			login();
			return false;
		}
	}



}
