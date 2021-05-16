package az.code.store;

public class PurchaseItem {
    private final long id;
    private final double price;
    private final Item item;
    private final int quantity;
    private boolean isActive = true;

    public PurchaseItem(Item item, int quantity) throws OutOfStockException {
        this.id = Generator.getID();
        this.item = item;
        this.quantity = quantity;
        if (quantity > item.getQuantity()) {
            throw new OutOfStockException(item.getQuantity());
        } else {
            item.reduceQuantity(quantity);
            this.price = this.item.getPrice();
        }
    }

    public PurchaseItem(PurchaseItem purchaseItem, int quantity) {
        this.id = Generator.getID();
        this.item = purchaseItem.getItem();
        this.quantity = quantity;
        this.price = purchaseItem.price;
    }

    public static PurchaseItem copyReturn(PurchaseItem oldItem, int quantity) {
        if (quantity == oldItem.getQuantity())
            return null;
        return new PurchaseItem(oldItem, oldItem.getQuantity() - quantity);
    }

    public boolean returnItem(int quantity) {
        isActive = false;
        if (quantity <= this.quantity) {
            this.item.increaseQuantity(quantity);
            return true;
        }
        return false;
    }

    public boolean isActive() {
        return isActive;
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
