import java.math.BigDecimal;
import java.util.Currency;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

import Application.AppControl;
import Application.AppData;
import GUI.GUI;

public class Main {

	// ************* Default settings to be implemented in config file in future *****************
	private static int numSelfCheckoutStations = 1;	// default number of SelfCheckoutStations
    static int scaleMaximumWeight = 10;				// default scaleMaximumWeight
    static int scaleSensitivity = 1;				// default scaleSensitivity
	
    // default currency and set of denominations
    static Currency currency = Currency.getInstance("USD");
    static int[] banknoteDenominations = { 1, 5, 10, 20, 100 };
    static BigDecimal[] coinDenominations = { new BigDecimal("0.01"), new BigDecimal("0.05"), new BigDecimal("0.1"),
            new BigDecimal("0.25"), new BigDecimal("1.00") };
	// *******************************************************************************************
    
    private static SelfCheckoutStation[] selfStations;
    
	public static void main(String[] args) {
		AppData ad = new AppData();
		ad.config(currency, banknoteDenominations, coinDenominations, scaleMaximumWeight, scaleSensitivity);
		
		selfStations = new SelfCheckoutStation[numSelfCheckoutStations];
		
		for (int i = 0; i < numSelfCheckoutStations; i++) {
			selfStations[i] = new SelfCheckoutStation(currency, banknoteDenominations,
		            coinDenominations, scaleMaximumWeight, scaleSensitivity);
		}
		
		ad.setSelfCheckoutStations(selfStations);
		
		new GUI(new AppControl(ad));
	}

}
