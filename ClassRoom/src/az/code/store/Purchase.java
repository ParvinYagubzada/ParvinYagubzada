package az.code.store;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Purchase {
    private final long id;
    private final Map<Long, PurchaseItem> purchaseItems;
    private double amount;
    private final LocalDateTime purchaseTime;
    private boolean isActive = true;

    public Purchase() {
        this(LocalDateTime.now());
    }

    public Purchase(LocalDateTime purchaseDate) {
        this.id = Generator.getID();
        this.purchaseItems = new HashMap<>();
        this.amount = 0.0;
        this.purchaseTime = purchaseDate;
    }

    public static Comparator<Purchase>[] orders = new Comparator[6];
    public static Comparator<Purchase> sortByAmount = (base, second) -> (int) (base.amount - second.amount);
    public static Comparator<Purchase> sortByAmountDESC = (base, second) -> (int) (second.amount - base.amount);
    public static Comparator<Purchase> sortByTime = (base, second) -> base.purchaseTime.compareTo(second.purchaseTime);
    public static Comparator<Purchase> sortByTimeDESC = (base, second) -> second.purchaseTime.compareTo(base.purchaseTime);
    public static Comparator<Purchase> inactiveFirst = (base, second) -> asInt(base.isActive) - asInt(second.isActive);
    public static Comparator<Purchase> activeFirst = (base, second) -> asInt(second.isActive) - asInt(base.isActive);

    static {
        orders[0] = sortByAmount;
        orders[1] = sortByAmountDESC;
        orders[2] = sortByTime;
        orders[3] = sortByTimeDESC;
        orders[4] = inactiveFirst;
        orders[5] = activeFirst;
    }

    private static int asInt(boolean bool) {
        return bool ? 1 : 0;
    }

    public void addPurchaseItem(PurchaseItem item) {
        this.purchaseItems.put(item.getId(), item);
        this.amount += (item.getPrice() * item.getQuantity());
    }

    public void returnAllItems() {
        for (PurchaseItem item : this.purchaseItems.values()) {
            item.returnItem(item.getQuantity());
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public Map<Long, PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseTime;
    }
}
