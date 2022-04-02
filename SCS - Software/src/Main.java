import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.external.CardIssuer;
import org.lsmr.selfcheckout.external.ProductDatabases;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;

import bank.Bank;
import store.Membership;
import store.Store;

/**
 * This class represents the entry point of entire software.
 * 
 * This class has a singleton Store instance.
 * This class initializes both the store and any external parties.
 * 
 * The singleton initializes the following for simulation:
 * - the bank (CardIssuer)
 * - the inventory (ProductDatabases)
 * - the store (Store)
 * 
 * @author Yunfan Yang
 */

public final class Main {
    private static Store store;

    public static void main(String[] args) {
        Main.initializeCardIssuers();
        Main.initializeProductDatabase();
        Main.initializeStore();
        Main.initializeMembership();
    }
    
    private static void initializeCardIssuers() {
        Bank.issuers.clear();
        
        CardIssuer rbc = new CardIssuer("RBC");
        CardIssuer scotia = new CardIssuer("Scotiabank");

        Calendar expiry1 = Calendar.getInstance();
        expiry1.set(Calendar.YEAR, expiry1.get(Calendar.YEAR) + 1);
        rbc.addCardData("4510123456789000", "Yunfan Yang", expiry1, "054", new BigDecimal("11903.56"));

        Calendar expiry2 = Calendar.getInstance();
        expiry2.set(Calendar.YEAR, expiry2.get(Calendar.YEAR) + 1);
        scotia.addCardData("4510987654321000", "Joshua Plosz", expiry2, "563", new BigDecimal("19532.20"));

        Calendar expiry3 = Calendar.getInstance();
        expiry3.set(Calendar.YEAR, expiry3.get(Calendar.YEAR) + 2);
        scotia.addCardData("4511220329440683", "Tyler Chen", expiry3, "232", new BigDecimal("6046.89"));

        Bank.issuers.add(rbc);
        Bank.issuers.add(scotia);
    }

    private static void initializeProductDatabase() {
        ProductDatabases.BARCODED_PRODUCT_DATABASE.clear();
        ProductDatabases.PLU_PRODUCT_DATABASE.clear();
        ProductDatabases.INVENTORY.clear();

        PriceLookupCode plu = new PriceLookupCode("PLU");
        PLUCodedProduct p1 = new PLUCodedProduct(plu, "Corn", new BigDecimal("2.00"));
        ProductDatabases.PLU_PRODUCT_DATABASE.put(plu, p1);

        Numeral[] nums = new Numeral[10];
        Barcode bar = new Barcode(nums);
        BarcodedProduct p2 = new BarcodedProduct(bar, "Coffee", new BigDecimal("6.20"), 15.0);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(bar, p2);

        ProductDatabases.INVENTORY.put(p1, 23);
        ProductDatabases.INVENTORY.put(p2, 60);
    }

    private static void initializeStore() {
        Main.store = new Store();
    }

    private static void initializeMembership() {
        Membership.members.clear();

        Membership.createMembership("12345", "Gagan");
        Membership.createMembership("49555", "Justin");
    }

    public static Store getStore() {
        if(Main.store == null) {
            Main.main(null);
        }

        return Main.store;
    }
}
