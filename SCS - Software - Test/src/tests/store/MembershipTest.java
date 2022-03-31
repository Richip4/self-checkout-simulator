package tests.store;

import org.junit.Before;
import org.junit.Test;
import store.Membership;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MembershipTest
{
    // Static variables that will be used during testing
    final String membership1ID = "1";
    final String membership2ID = "2";

    // Declare the membership provider
    Membership membershipProvider;

    // Setup that is run before each test case
    @Before
    public void setup()
    {
        // Initialize the membership provider
        membershipProvider = new Membership();
    }

    @Test
    public void membershipSignupTest()
    {
        assertFalse(membershipProvider.checkMember(membership1ID));
        assertFalse(membershipProvider.checkMember(membership2ID));

        membershipProvider.addMember(membership1ID);
        membershipProvider.addMember(membership2ID);

        assertTrue(membershipProvider.checkMember(membership1ID));
        assertTrue(membershipProvider.checkMember(membership2ID));
    }
}
