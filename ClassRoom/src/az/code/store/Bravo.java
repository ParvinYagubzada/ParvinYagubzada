package az.code.store;

import az.code.Color;

import static az.code.store.Printer.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Bravo implements Marketable {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final HashMap<Long, Purchase> purchases;
    private final HashMap<Long, Item> items;
    private Long totalIncome = 0L;
    private Long totalSoldItemCount = 0L;
    private final String username = hashString("admin");
    private String password = hashString("admin");

    public Bravo() {
        this.purchases = new HashMap<>();
        this.items = new HashMap<>();
    }

    public void increaseIncome(double amount) {
        totalIncome += (long) amount;
    }

    public void increaseSoldItemCount(int count) {
        totalSoldItemCount += count;
    }

    public void setPassword(String password) {
        this.password = hashString(password);
    }

    public long getTotalIncome() {
        return totalIncome;
    }

    public Long getTotalSoldItemCount() {
        return totalSoldItemCount;
    }

    public void checkCredentials(String username, String password) throws LoginError {
        username = hashString(username);
        password = hashString(password);
        if (!username.equals(this.username) || !password.equals(this.password))
            throw new LoginError();
    }

    public List<IncomeStatisticsHolder> getIncomeStatistics(LocalDateTime start, LocalDateTime end) {
        List<IncomeStatisticsHolder> holder = new LinkedList<>();
        end = end.toLocalDate().atTime(23, 59);
        LocalDateTime endOfMonth = start
                .minusDays(start.getDayOfMonth() - 1)
                .plusMonths(1)
                .minusDays(1)
                .toLocalDate()
                .atTime(23, 59);
        while (start.isBefore(end)) {
            long amount = 0L;
            for (Purchase purchase : purchases.values()) {
                if (purchase.getPurchaseDate().compareTo(start) >= 0 &&
                        purchase.getPurchaseDate().compareTo(endOfMonth) <= 0) {
                    amount += purchase.getAmount();
                }
            }
            holder.add(new IncomeStatisticsHolder(
                    amount, start.toLocalDate(),
                    endOfMonth.toLocalDate())
            );
            start = endOfMonth.plusMinutes(1);
            if (endOfMonth.plusMonths(1).compareTo(end) <= 0)
                endOfMonth = endOfMonth.plusMonths(1);
            else
                endOfMonth = end;
        }
        return holder;
    }

    @Override
    public void addItem(String name, double price, Category category, int quantity) {
        long id = Generator.getID();
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
        return new ArrayList<>(items.values()).subList(0, checkCount(count, items));
    }

    @Override
    public List<Item> getAllItems(int count, Comparator<Item> order) {
        ArrayList<Item> sorted = new ArrayList<>(items.values());
        sorted.sort(order);
        return sorted.subList(0, checkCount(count, items));
    }

    private static int checkCount(int count, @SuppressWarnings("rawtypes") Map map) {
        if (count > map.size())
            count = map.size();
        return count;
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
        increaseIncome(purchase.getAmount());
        increaseSoldItemCount(purchase
                .getPurchaseItems()
                .values()
                .stream()
                .mapToInt(PurchaseItem::getQuantity)
                .sum());
        purchases.put(purchase.getId(), purchase);
    }

    @Override
    public void returnPurchase(long purchaseId) throws Exception {
        Purchase purchase = purchases.get(purchaseId);
        if (purchase.isActive()) {
            purchase.deactivate();
            purchase.returnAllItems();
        } else {
            throw new Exception("This purchase already refunded!");
        }
    }

    @Override
    public Collection<Purchase> getAllPurchases(int count) {
        if (count > purchases.size())
            count = purchases.size();
        return new LinkedList<>(purchases.values()).subList(0, count);
    }

    @Override
    public List<Purchase> getAllPurchases(int count, Comparator<Purchase> order) {
        ArrayList<Purchase> sorted = new ArrayList<>(purchases.values());
        sorted.sort(order);
        return sorted.subList(0, checkCount(count, purchases));
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

    @SuppressWarnings("ClassCanBeRecord")
    public static class IncomeStatisticsHolder {
        private final long amount;
        private final LocalDate startDate;
        private final LocalDate endDate;

        public IncomeStatisticsHolder(long amount, LocalDate startDate, LocalDate endDate) {
            this.amount = amount;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        public String toString() {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return "\tFrom %s date to %s date total income is %s".formatted(
                    colorString(Color.GREEN, this.startDate.format(dtf)),
                    colorString(Color.GREEN, this.endDate.format(dtf)),
                    colorString(Color.CYAN, amount));
        }
    }

    public static class LoginError extends Exception {
        public LoginError() {
            super("Username or password was wrong!");
        }
    }
}
