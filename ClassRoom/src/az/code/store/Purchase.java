package az.code.store;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Purchase {
    private final long id;
    private final Map<Long, PurchaseItem> purchaseItems;
    private double amount;
    private final LocalDateTime purchaseTime;
    private boolean isActive = true;

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
