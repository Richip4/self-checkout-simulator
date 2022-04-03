package Application;

import java.math.BigDecimal;
import java.util.Currency;

import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

public class AppData {
	private Currency stationCurrency;
	private int[] banknoteDenom;
	private BigDecimal[] coinDenom;
	private int scaleMaxWeight;
	private int scaleSensitivity;
	private SelfCheckoutStation[] selfStations;
	
	public void config(Currency c, int[] banknoteDenom, BigDecimal[] coinDenom, 
									int scaleMaxWeight, int scaleSensitivity) {
		stationCurrency = c;
		this.banknoteDenom = banknoteDenom;
		this.coinDenom = coinDenom;
		this.scaleMaxWeight = scaleMaxWeight;
		this.scaleSensitivity = scaleSensitivity;
	}
	
	public void setSelfCheckoutStations(SelfCheckoutStation[] selfStations) {
		this.selfStations = selfStations;
	}
}
