package az.code.store;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Purchase {
    private final long id;
    private final Map<Long, PurchaseItem> purchaseItems;
    private double amount;
    private final LocalDateTime purchaseTime;

    public Purchase(LocalDateTime purchaseDate) {
        this.id = IdGenerator.getID();
        this.purchaseItems = new HashMap<>();
        this.amount = 0.0;
        this.purchaseTime = purchaseDate;
    }

    public void addPurchaseItem(PurchaseItem item) {
        this.purchaseItems.put(item.getId(), item);
        this.amount += (item.getPrice() * item.getQuantity());
    }

    public boolean returnItem(long id, int quantity) {
        PurchaseItem item = purchaseItems.get(id);
        if (item != null) {
            return item.returnItem(quantity);
        }
        return false;
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
