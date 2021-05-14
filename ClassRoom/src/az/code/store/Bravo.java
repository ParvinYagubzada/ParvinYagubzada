package az.code.store;

import java.time.LocalDate;
import java.util.*;

public class Bravo implements Marketable {

    private final HashMap<Long, Purchase> purchases;
    private final HashMap<Long, Item> items;

    public Bravo() {
        this.purchases = new HashMap<>();
        this.items = new HashMap<>();
    }

    @Override
    public void addItem(String name, double price, Category category, int quantity) {
        long id = IdGenerator.getID();
        Item item = new Item(name, price, category, quantity, id);
        items.put(id, item);
    }

    public Item getItem(long id) {
        return this.items.get(id);
    }

    @Override
    public void editItem(long id, String name, double price, Category category, int quantity) {
        Item editedItem = items.get(id);
        editedItem.setName(name);
        editedItem.setPrice(price);
        editedItem.setCategory(category);
        editedItem.setQuantity(quantity);
    }

    @Override
    public void removeItem(long id) {
        items.remove(id);
    }

    @Override
    public Collection<Item> getAllItems() {
        return items.values();
    }

    @Override
    public List<Item> getItems(Category category) {
        ArrayList<Item> itemsByCat = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getCategory().equals(category))
                itemsByCat.add(item);
        }
        return itemsByCat;
    }

    @Override
    public List<Item> getItems(double minPrice, double maxPrice) {
        ArrayList<Item> itemsByPrice = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getPrice() <= maxPrice && item.getPrice() >= minPrice)
                itemsByPrice.add(item);
        }
        return itemsByPrice;
    }

    @Override
    public List<Item> getItems(String name) {
        ArrayList<Item> itemsByName = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getName().startsWith(name))
                itemsByName.add(item);
        }
        return itemsByName;
    }

    @Override
    public void addPurchase(Purchase purchase) {
        purchases.put(purchase.getId(), purchase);
    }

    @Override
    public void removeItem(long purchaseItemId, long purchaseId) {
        Purchase purchase = purchases.get(purchaseItemId);
        purchase.getPurchaseItems().remove(purchaseItemId);
    }

    @Override
    public void removePurchase(long purchaseId) {
        purchases.remove(purchaseId);
    }

    @Override
    public Collection<Purchase> getAllPurchases() {
        return purchases.values();
    }

    @Override
    public List<Purchase> getPurchases(LocalDate start, LocalDate end) {
        ArrayList<Purchase> purchasesByTime = new ArrayList<>();
        for (Purchase purchase : purchases.values()) {
            if (start.isBefore(purchase.getPurchaseDate()) && end.isAfter(purchase.getPurchaseDate()))
                purchasesByTime.add(purchase);
        }
        return purchasesByTime;
    }

    @Override
    public List<Purchase> getPurchases(LocalDate date) {
        ArrayList<Purchase> purchasesByTime = new ArrayList<>();
        for (Purchase purchase : purchases.values()) {
            if (date.equals(purchase.getPurchaseDate()))
                purchasesByTime.add(purchase);
        }
        return purchasesByTime;
    }

    @Override
    public List<Purchase> getPurchases(double minAmount, double maxAmount) {
        ArrayList<Purchase> purchasesByAmount = new ArrayList<>();
        for (Purchase purchase : purchases.values()) {
            if (minAmount <= purchase.getAmount() && maxAmount >= purchase.getAmount())
                purchasesByAmount.add(purchase);
        }
        return purchasesByAmount;
    }

    @Override
    public Purchase getPurchase(long id) {
        return purchases.get(id);
    }
}
