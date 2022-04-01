package tests.hypervisor;

import java.math.BigDecimal;
import java.util.Currency;

import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

import bank.Bank;
import hypervisor.AttendantStation;
import store.Inventory;
import store.Membership;

public class AttendantStationTest {
	public static void main(String[] args) {
		Bank bank = new Bank(10);
		Inventory inv = new Inventory();
		Membership members = new Membership();
		AttendantStation attendantStation = new AttendantStation(bank, members, inv);
		
		
		final Currency currency = Currency.getInstance("CAD");
	    final int[] banknoteDenominations = {5, 10, 20, 50};
	    final BigDecimal[] coinDenominations = {new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"), new BigDecimal("1.00"), new BigDecimal("2.00")};
	    final int scaleMaximumWeight = 100;
	    final int scaleSensitivity = 10;
	    
	    SelfCheckoutStation scs = new SelfCheckoutStation(currency, banknoteDenominations, coinDenominations, scaleMaximumWeight, scaleSensitivity);
		
	    boolean startedUp = false;;
		try {
			startedUp = attendantStation.startUpStation(scs);
		} catch (OverloadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		System.out.println("test");
	}
}
