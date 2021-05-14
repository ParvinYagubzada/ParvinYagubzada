package az.code.store;

import java.time.LocalDate;
import java.util.Collection;

import java.util.List;

public interface Marketable {

    void addItem(String name, double price, Category category, int quantity);

    void editItem(long id, String name, double price, Category category, int quantity);

    void removeItem(long id);

    Collection<Item> getAllItems();

    List<Item> getItems(Category category);

    List<Item> getItems(double minPrice, double maxPrice);

    List<Item> getItems(String name);

    void addPurchase(Purchase purchase);

    void removeItem(Purchase purchase, long itemId, int quantity);

    void removePurchase(long purchaseId);

    Collection<Purchase> getAllPurchases();

    List<Purchase> getPurchases(LocalDate start, LocalDate end);

    List<Purchase> getPurchases(LocalDate date);

    List<Purchase> getPurchases(double minAmount, double maxAmount);

    Purchase getPurchase(long id);
}
