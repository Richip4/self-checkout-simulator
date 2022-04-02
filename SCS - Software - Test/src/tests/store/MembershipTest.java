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


    @Test
    public void membershipSignupTest()
    {
        assertFalse(Membership.isMember(membership1ID));
        assertFalse(Membership.isMember(membership2ID));

        Membership.createMembership(membership1ID, "Joshua");
        Membership.createMembership(membership2ID, "Gagan");

        assertTrue(Membership.isMember(membership1ID));
        assertTrue(Membership.isMember(membership2ID));
    }
}
