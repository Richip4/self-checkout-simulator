package Application;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Card;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.PLUCodedItem;
import org.lsmr.selfcheckout.PriceLookupCode;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.ReceiptPrinter;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.devices.SupervisionStation;
import org.lsmr.selfcheckout.external.CardIssuer;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;

import GUI.GUI;
import bank.Bank;
import store.Membership;
import store.Store;
import software.SelfCheckoutSoftware;
import software.SupervisionSoftware;
import store.Inventory;

/**
 * This class represents the entry point of entire software.
 * 
 * This class is to set up everything for simulation. This is not a part of the
 * software.
 * 
 * This class has a singleton Store instance.
 * This class initializes both the store, external parties, and tangibles.
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
        Main.initializeCardAndIssuers();
        Main.initializeProductDatabase();
        Main.initializeStore();
        Main.initializeMembership();
        
        new GUI(new AppControl(Tangibles.SUPERVISION_STATION));
    }

    private static void initializeCardAndIssuers() {
        Bank.clearIssuers();
        Bank.clearCardIssuers();

        CardIssuer rbc = new CardIssuer("RBC");
        CardIssuer scotia = new CardIssuer("Scotiabank");

        Calendar expiry1 = Calendar.getInstance();
        expiry1.set(Calendar.YEAR, expiry1.get(Calendar.YEAR) + 1);
        String cardNo1 = "4510123456789000";
        rbc.addCardData(cardNo1, "Yunfan Yang", expiry1, "054", new BigDecimal("11903.56"));

        Calendar expiry2 = Calendar.getInstance();
        expiry2.set(Calendar.YEAR, expiry2.get(Calendar.YEAR) + 1);
        String cardNo2 = "4510987654321000";
        scotia.addCardData(cardNo2, "Joshua Plosz", expiry2, "563", new BigDecimal("19532.20"));

        Calendar expiry3 = Calendar.getInstance();
        expiry3.set(Calendar.YEAR, expiry3.get(Calendar.YEAR) + 2);
        String cardNo3 = "4511220329440683";
        scotia.addCardData(cardNo3, "Tyler Chen", expiry3, "232", new BigDecimal("6046.89"));

        Bank.addIssuer(rbc);
        Bank.addIssuer(scotia);
        Bank.addCardIssuer(cardNo1, rbc);
        Bank.addCardIssuer(cardNo2, scotia);
        Bank.addCardIssuer(cardNo3, scotia);
    }

    private static void initializeProductDatabase() {
        Inventory.clear();

        PriceLookupCode plu = new PriceLookupCode("PLU");
        PLUCodedProduct p1 = new PLUCodedProduct(plu, "Corn", new BigDecimal("2.00"));
        Inventory.addProduct(p1);

        Numeral[] nums = new Numeral[10];
        Barcode bar = new Barcode(nums);
        BarcodedProduct p2 = new BarcodedProduct(bar, "Coffee", new BigDecimal("6.20"), 15.0);
        Inventory.addProduct(p2);

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
        Currency currency = Currency.getInstance("CAD");
        int[] banknoteDenominations = { 1, 5, 10, 20, 100 };
        BigDecimal[] coinDenominations = {
                new BigDecimal("0.01"),
                new BigDecimal("0.05"),
                new BigDecimal("0.1"),
                new BigDecimal("0.25"),
                new BigDecimal("1.00")
        };

        // Initialize supervision station
        SupervisionStation svs = new SupervisionStation();
        Tangibles.SUPERVISION_STATION = svs;

        // Create supervision software for this svs
        // and set it to the store
        SupervisionSoftware svss = new SupervisionSoftware(svs);
        Store.setSupervisionSoftware(svss);

        // Initialize 6 self-checkout stations
        // and add them to the supervision station to be supervised
        for (int t = 0; t < 6; t++) {
            SelfCheckoutStation station = new SelfCheckoutStation(currency, banknoteDenominations,
                    coinDenominations, 1000, 2);

            // Add ink to the station
            try {
                station.printer.addInk(ReceiptPrinter.MAXIMUM_INK);
            } catch (OverloadException e) {
                e.printStackTrace();
            }

            // Add paper to the station
            try {
                station.printer.addPaper(ReceiptPrinter.MAXIMUM_PAPER);
            } catch (OverloadException e) {
                e.printStackTrace();
            }

            // Add this station to tangibles, and add this station to the supervision
            // station
            Tangibles.SELF_CHECKOUT_STATIONS.add(station);
            Tangibles.SUPERVISION_STATION.add(station);

            // Create SelfCheckoutSoftware for this station
            // and add this softeare to supervision software
            SelfCheckoutSoftware software = new SelfCheckoutSoftware(station);
            Store.addSelfCheckoutSoftware(software);
        }
    }

    private static void initializeMembership() {
        Membership.clear();

        String card1No = "12345";
        String card2No = "49555";
        String card1Holder = "Gagan";
        String card2Holder = "Justin";

        Card card1 = new Card("membership", card1No, card1Holder, null, null, true, true);
        Card card2 = new Card("membership", card2No, card2Holder, null, null, true, true);
        Tangibles.MEMBER_CARDS.add(card1);
        Tangibles.MEMBER_CARDS.add(card2);

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
     * GUI can grab tangibles and interact them with hardware, in order to trigger
     * softwares.
     * Eg, GUI can grab an item and use Barcode scanner to scan it.
     * When this item is scanned, software will be notified via observers.
     * 
     * This is necessary, because software should never deal with tangibles, namely,
     * Cards, Items, Cash, etc.
     * For items, simulation should use barcode scanner or touch screen to input,
     * via Hardware. Once hardware has these events happened, software will be
     * notified via attached observers. At this point, software should only know
     * about Barcode or PLU code (from observer arguments), and software finds the
     * corresponding product information.
     * For cards, simulation should use, eg, CardReader to read the card
     * information. Once card information is read by hardware, CardData will be
     * passed into the attached observers, and software will be notified. At this
     * point, software would only deal with CardData instead of tangible Card.
     * (Therefore, previous implementation of Inventory class which contains dealing
     * with Item is incorrect)
     * TODO: Banknote and Coin
     * 
     * As mentioned in Membership class, it is also a type of Card. To distinguish
     * membership cards and bank payment cards, two copntants are provided.
     * 
     * This clas is for simulation and not a part of the software.
     * 
     * @author Yunfan Yang
     */
    public static class Tangibles {
        public static SupervisionStation SUPERVISION_STATION;
        public static final List<SelfCheckoutStation> SELF_CHECKOUT_STATIONS = new ArrayList<SelfCheckoutStation>();

        public static final List<Item> ITEMS = new ArrayList<Item>();
        public static final List<Card> MEMBER_CARDS = new ArrayList<Card>();
        public static final List<Card> PAYMENT_CARDS = new ArrayList<Card>();

        /**
         * This class is not to be instantiated.
         */
        private Tangibles() {
        }
    }
}
