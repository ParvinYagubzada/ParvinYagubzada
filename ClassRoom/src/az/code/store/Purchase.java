package az.code.store;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Purchase {
    private final long id;
    private final Map<Long, PurchaseItem> purchaseItems;
    private double amount;
    private final LocalDate purchaseDate;

    public Purchase(LocalDate purchaseDate) {
        this.id = IdGenerator.getID();
        this.purchaseItems = new HashMap<>();
        this.amount = 0.0;
        this.purchaseDate = purchaseDate;
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

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }
}
