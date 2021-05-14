package az.code.store;

public class Item {
    private final long id;
    private String name;
    private double price;
    private Category category;
    private int quantity;

    public Item(String name, double price, Category category, int quantity, long id) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void reduceQuantity(int quantity) {
        this.quantity = quantity;
    }
}
