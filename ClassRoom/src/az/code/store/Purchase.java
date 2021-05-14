package az.code.store;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Purchase {
    private final long id;
    private final Map<Long, PurchaseItem> purchaseItem;
    private final double amount;
    private final LocalDate purchaseDate;

    public Purchase(long id, PurchaseItem purchaseItem, Map<Long, PurchaseItem> purchaseItems, double amount, LocalDate purchaseDate) {
        this.id = id;
        this.purchaseItem = purchaseItems;
        this.amount = amount;
        this.purchaseDate = purchaseDate;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public Map<Long, PurchaseItem> getPurchaseItems() {
        return purchaseItem;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }
}
