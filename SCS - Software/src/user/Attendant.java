package user;

public class Attendant {

	private boolean unexpectedItemDecision;

	private String username;
	private String password;

	/**
	 * The GUI will set the what the attendant made about the unexpected item.
	 */
	public void setUnexpectedItemDecision(boolean value) {
		this.unexpectedItemDecision = value;
	}

	/**
	 * Get the decision the attendant made about the unexpected Item
	 */
	public boolean getUnexpectedItemDecision() {
		return this.unexpectedItemDecision;
	}

	///////////////////// SupervisionSoftware.java /////////////////////

	/**
	 * Set the attendant login information.
	 */
	public void setLogin(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * Get the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Get the password
	 */
	public String getPassword() {
		return password;
	}

	///////////////////// SupervisionSoftware.java /////////////////////

}
