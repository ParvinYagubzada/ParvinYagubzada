package az.code.store;

public class PurchaseItem {
    private final long id;
    private final double price;
    private final Item item;
    private int quantity;

    public PurchaseItem(Item item, int quantity) throws OutOfStockException {
        this.id = IdGenerator.getID();
        this.item = item;
        this.quantity = quantity;
        if (quantity > item.getQuantity()) {
            throw new OutOfStockException(item.getQuantity());
        } else {
            item.reduceQuantity(quantity);
            this.price = this.item.getPrice();
        }
    }

    public boolean returnItem(int quantity) {
        if (quantity <= this.quantity)
            this.quantity -= quantity;
        return false;
    }

    public long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return String.format("Item \tName= %s \tID= %d \tPrice= %.2f \tQuantity= %d",
                item.getName(),
                id,
                price,
                quantity);
    }

    public static class OutOfStockException extends Exception {
        public OutOfStockException(int maxQuantity) {
            super("Quantity you want exceeds our stock quantity. Max quantity is: " + maxQuantity);
        }
    }
}
