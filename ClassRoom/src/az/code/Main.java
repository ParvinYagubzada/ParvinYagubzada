package az.code;

import az.code.store.*;

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    private static final Bravo bravo = new Bravo();
    public static final String start = "Please select one of selections:\n";
    public static final String end = "Your selection:\t";
    public static final String menu = """
            \t1. Processes on Items.
            \t2. Processes on Purchases.
            \t3. Exit.
            """;
    public static final String processOnItems = """
            \t1. Add new item.
            \t2. Edit item.
            \t3. Remove item.
            \t4. Select all items.
            \t5. Select items by categories.
            \t6. Select items by price range.
            \t7. Select items by item name.
            """;
    public static final String processOnPurchases = """
            \t1. Add new purchase.
            \t2. Return item.
            \t3. Remove purchase.
            \t4. Select all purchases.
            \t5. Select purchases by Date range.
            \t6. Select purchases by price range.
            \t7. Select purchases by Date.
            \t8. Select purchase by id.
            """;

    static {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            bravo.addItem("Item" + i, random.nextDouble(), Category.values()[random.nextInt(4)], random.nextInt());
        }
    }

    public static void main(String[] args) {
        mainOperation(0);
    }

    public static void printMenu(String menu) {
        System.out.print(start + menu + end);
    }

    public static void printSelectionError() {
        System.out.print(ANSI_RED + "You can't select number other than provided.\n" + ANSI_RESET);
    }

    public static void printInputMismatchError() {
        System.out.print(ANSI_RED + "Your input was incorrect.\n" + ANSI_RESET);
    }

    public static void mainOperation(int userInput) {
        while (userInput != 3) {
            printMenu(menu);
            try {
                userInput = scanner.nextInt();
                switch (userInput) {
                    case 1:
                        printMenu(processOnItems);
                        int input = scanner.nextInt();
                        operationOnItem(input);
                        break;
                    case 2:
                        printMenu(processOnPurchases);
                        input = scanner.nextInt();
                        operationOnPurchase(input);
                        break;
                    default:
                        printSelectionError();
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                printInputMismatchError();
                mainOperation(0);
            }
        }
    }

    private static void operationOnPurchase(int selection) {
        switch (selection) {
            case 1:
                System.out.println("You selected: Add new purchase.\nPlease provide program with item ids");
                int count = 1;
                String input = "end";
                do {
                    System.out.print("Enter item id: ");
                    long id = Long.parseLong(scanner.next());
                    if (bravo.getItem())
                    System.out.println("Enter item quantity: ");
                    int quantity = scanner.nextInt();
                    PurchaseItem item = new PurchaseItem();
                } while (!input.equals("end"));
//                bravo.addPurchase();
        }
    }

    public static void addPurchaseItem(String input) {

    }

    public static void operationOnItem(int selection) {
        switch (selection) {
            case 1:
                System.out.println("You selected: Add new item.");
                Item temp = getItemFromUser();
                bravo.addItem(temp.getName(), temp.getPrice(), temp.getCategory(), temp.getQuantity());
                break;
            case 2:
                System.out.println("You selected: Edit item.");
                System.out.print("Please enter item id: ");
                long id = scanner.nextLong();
                temp = getItemFromUser();
                bravo.editItem(id, temp.getName(), temp.getPrice(), temp.getCategory(), temp.getQuantity());
                break;
            case 3:
                System.out.println("You selected: Remove item.");
                System.out.print("Please enter item id: ");
                id = scanner.nextLong();
                bravo.removeItem(id);
                break;
            case 4:
                System.out.println("You selected: Select all items.");
                printAllItems(bravo.getAllItems());
                break;
            case 5:
                System.out.println("You selected: Select items by categories.");
                Category category = getCategory();
                printAllItems(bravo.getItems(category));
                break;
            case 6:
                System.out.println("You selected: Select items by price range.");
                System.out.print("Please enter min price: ");
                double min = scanner.nextDouble();
                System.out.print("Please enter max price: ");
                double max = scanner.nextDouble();
                printAllItems(bravo.getItems(min, max));
                break;
            case 7:
                System.out.println("You selected: Select items by item name.");
                System.out.println("Please enter word you are searching for: ");
                String word = scanner.next();
                printAllItems(bravo.getItems(word));
                break;
            default:
                printSelectionError();
        }
    }

    public static Item getItemFromUser() {
        System.out.print("Please enter the item name: ");
        String name = scanner.next();
        System.out.print("Please enter item price: ");
        double price = scanner.nextDouble();
        Category category = getCategory();
        System.out.print("Please enter quantity: ");
        int quantity = scanner.nextInt();
        return new Item(name, price, category, quantity, -1);
    }

    public static void printAllItems(Collection<Item> items) {
        System.out.println("Items:\n");
        for (Item item : items) {
            System.out.format("\tID=%10d \tNAME=%15s \tCATEGORY=%20s \tQUANTITY=%15d \tPRICE=%10.2f\n",
                    item.getId(),
                    item.getName(),
                    item.getCategory().getStringFormat(),
                    item.getQuantity(),
                    item.getPrice());
        }
        System.out.println();
    }

    public static Category getCategory() {
        System.out.println("Please select one of Categories: ");
        Category.printCats();
        System.out.print(end);
        int selection = scanner.nextInt();
        if (selection > Category.values().length) {
            printSelectionError();
            getCategory();
        }
        return Category.values()[selection - 1];
    }
}

