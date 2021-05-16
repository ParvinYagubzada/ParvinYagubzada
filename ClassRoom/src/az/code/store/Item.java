package az.code.store;

import java.util.Comparator;
import java.util.InputMismatchException;

public class Item implements Comparable<Item> {
    private final long id;
    private String name;
    private double price;
    private Category category;
    private int quantity;

    public Item(String name, double price, Category category, int quantity, long id) {
        this.name = name;
        setPrice(price);
        this.category = category;
        setQuantity(quantity);
        this.id = id;
    }

    public static Comparator<Item>[] orders = new Comparator[6];
    public static Comparator<Item> sortByPrice = (base, second) -> (int) (base.price - second.price);
    public static Comparator<Item> sortByPriceDESC = (base, second) -> (int) (second.price - base.price);
    public static Comparator<Item> sortByCategory = (base, second) -> base.category.getStringFormat().compareTo(second.category.getStringFormat());
    public static Comparator<Item> sortByCategoryDESC = (base, second) -> second.category.getStringFormat().compareTo(base.category.getStringFormat());
    public static Comparator<Item> sortByQuantity = (base, second) -> base.quantity - second.quantity;
    public static Comparator<Item> sortByQuantityDESC = (base, second) -> second.quantity - base.quantity;

    static {
        orders[0] = sortByPrice;
        orders[1] = sortByPriceDESC;
        orders[2] = sortByCategory;
        orders[3] = sortByCategoryDESC;
        orders[4] = sortByQuantity;
        orders[5] = sortByQuantityDESC;
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
        if (price < 0)
            throw new InputMismatchException("You can't set negative price.");
        else
            this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0)
            throw new InputMismatchException("You can't set negative quantity.");
        else
            this.quantity = quantity;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void reduceQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    @Override
    public int compareTo(Item second) {
        return (int) (id - second.id);
    }
}
