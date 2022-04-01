package user;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

public class Attendant {
	
	
	public class LoginInfo{
		String username;
		String password;
		
		public LoginInfo(String username, String password) {
			this.username = username;
			this.password = password;
		}
	}
	
	
	public LoginInfo promptLogin(String username, String password) {
		return new LoginInfo(username, password);
	}
	
	
	public boolean startUpStation(SelfCheckoutStation scs) {
		
		
		
		return false;
	}
	
	
	
	
	

}
