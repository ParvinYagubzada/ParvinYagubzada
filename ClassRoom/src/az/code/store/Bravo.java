package az.code.store;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Bravo implements Marketable {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
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
        if (items.containsKey(id)) {
            items.remove(id);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<Item> getAllItems(int count) {
        if (count > items.size())
            count = items.size();
        return new LinkedList<>(items.values()).subList(0, count);
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
    public void removeItem(Purchase purchase, long itemId, int quantity) {
        //purchase.returnItem(itemId, quantity);TODO
    }

    @Override
    public void returnPurchase(long purchaseId) {
        Purchase purchase = purchases.get(purchaseId);
        purchase.deactivate();
        purchase.returnAllItems();
    }

    @Override
    public Collection<Purchase> getAllPurchases() {
        return purchases.values();
    }

    @Override
    public List<Purchase> getPurchases(LocalDateTime start, LocalDateTime end) {
        ArrayList<Purchase> purchasesByTime = new ArrayList<>();
        for (Purchase purchase : purchases.values()) {
            if (start.isBefore(purchase.getPurchaseDate()) && end.isAfter(purchase.getPurchaseDate()))
                purchasesByTime.add(purchase);
        }
        return purchasesByTime;
    }

    @Override
    public List<Purchase> getPurchases(LocalDateTime time) {
        ArrayList<Purchase> purchasesByTime = new ArrayList<>();
        for (Purchase purchase : purchases.values()) {
            if (time.format(dateFormat).equals(purchase.getPurchaseDate().format(dateFormat)))
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
