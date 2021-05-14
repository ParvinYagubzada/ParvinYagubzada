package az.code.store;

public class PurchaseItem {
    private final long id;
    private final Item item;
    private final int quantity;

    public PurchaseItem(Item item, int quantity) throws Exception {
        this.id = IdGenerator.getID();
        if (quantity > item.getQuantity())
            throw new OutOfStockException();
        else
            item.reduceQuantity(quantity);
        this.item = item;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    static class OutOfStockException extends Exception {
        public OutOfStockException() {
            super("This item out of stock and you cant purchase it!");
        }
    }
}
