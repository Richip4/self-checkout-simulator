package store;
// @author Abdelhak Khalfallah
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the Membership database.
 * 
 * This class is static since membership database is universal.
 * Refer to: ProductDatabases.java in hardware package.
 * 
 * @author Sharjeel Junaid
 * @author Yunfan Yang
 */
public class Membership {
	public static final Set<String> members = new HashSet<String>();
	
	/**
	 * Instances of this class are not needed, so the constructor is private.
	 */
	private Membership() { }
	
	public boolean isMember(String memberID) {
		if (Membership.members.contains(memberID))
			return true;
		return false;
	}

}