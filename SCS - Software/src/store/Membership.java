package store;
// @author Abdelhak Khalfallah
import java.util.HashSet;
import java.util.Set;

public class Membership {
	
	Set<String> memberIDs;
	
	public Membership() {
		memberIDs = new HashSet<String>();
	}
	
	public void addMember(String memberID) {
		memberIDs.add(memberID);
	}
	
	public boolean checkMember(String memberID) {
		if (memberIDs.contains(memberID))
			return true;
		return false;
	}

}