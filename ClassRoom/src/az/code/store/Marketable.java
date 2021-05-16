package az.code.store;

import java.time.LocalDateTime;
import java.util.Collection;

import java.util.List;

public interface Marketable {

    void addItem(String name, double price, Category category, int quantity);

    void editItem(long id, String name, double price, Category category, int quantity);

    void removeItem(long id);

    Collection<Item> getAllItems(int count);

    List<Item> getItems(Category category);

    List<Item> getItems(double minPrice, double maxPrice);

    List<Item> getItems(String name);

    void addPurchase(Purchase purchase);

    void removeItem(Purchase purchase, long itemId, int quantity);

    void returnPurchase(long purchaseId);

    Collection<Purchase> getAllPurchases(int count);

    List<Purchase> getPurchases(LocalDateTime start, LocalDateTime end);

    List<Purchase> getPurchases(LocalDateTime time);

    List<Purchase> getPurchases(double minAmount, double maxAmount);

    Purchase getPurchase(long id);
}
