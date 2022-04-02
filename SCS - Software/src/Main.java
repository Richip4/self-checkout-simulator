import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.external.CardIssuer;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;

import bank.Bank;
import store.Membership;
import store.Store;
import store.Inventory;

/**
 * This class represents the entry point of entire software.
 * 
 * This class has a singleton Store instance.
 * This class initializes both the store, external parties, and tangibles.
 * This class initialize everything to set up a simulation world.
 * 
 * The singleton initializes the following for simulation:
 * - the bank (CardIssuer)
 * - the inventory (Inventory)
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
        Inventory.BARCODED_PRODUCT_DATABASE.clear();
        Inventory.PLU_PRODUCT_DATABASE.clear();
        Inventory.INVENTORY.clear();

        PriceLookupCode plu = new PriceLookupCode("PLU");
        PLUCodedProduct p1 = new PLUCodedProduct(plu, "Corn", new BigDecimal("2.00"));
        Inventory.PLU_PRODUCT_DATABASE.put(plu, p1);

        Numeral[] nums = new Numeral[10];
        Barcode bar = new Barcode(nums);
        BarcodedProduct p2 = new BarcodedProduct(bar, "Coffee", new BigDecimal("6.20"), 15.0);
        Inventory.BARCODED_PRODUCT_DATABASE.put(bar, p2);

        Inventory.addProduct(p1);
        Inventory.addProduct(p2);

        // Add 10 items for each product
        for (int t = 0; t < 10; t++) {
            PLUCodedItem i1 = new PLUCodedItem(plu, 4);
            BarcodedItem i2 = new BarcodedItem(bar, 15.0);
            Tangibles.ITEMS.add(i1);
            Tangibles.ITEMS.add(i2);
            Inventory.setQuantity(p1, 1);
            Inventory.setQuantity(p2, 1);
        }
    }

    private static void initializeStore() {
        Main.store = new Store();
    }

    private static void initializeMembership() {
        Membership.MEMBERS.clear();

        String card1No = "12345";
        String card2No = "49555";
        String card1Holder = "Gagan";
        String card2Holder = "Justin";

        Card card1 = new Card("membership", card1No, card1Holder, null, null, true, true);
        Card card2 = new Card("membership", card2No, card2Holder, null, null, true, true);
        Tangibles.CARDS.add(card1);
        Tangibles.CARDS.add(card2);

        Membership.createMembership(card1No, card1Holder);
        Membership.createMembership(card2No, card2Holder);
    }

    public static Store getStore() {
        if (Main.store == null) {
            Main.main(null);
        }

        return Main.store;
    }

    /**
     * This class is to keep track of all the tangibles in the simulation world.
     * 
     * GUI can grab tangibles and interact them with hardware, in order to trigger softwares.
     * Eg, GUI can grab an item and use Barcode scanner to scan it.
     * When this item is scanned, software will be notified via observers.
     * 
     * @author Yunfan Yang
     */
    public class Tangibles {
        public static final List<Item> ITEMS = new ArrayList<Item>();
        public static final List<Card> CARDS = new ArrayList<Card>();
    }
}
