package az.code;

import az.code.store.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static az.code.store.Printer.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Bravo bravo = new Bravo();
    public static final String menu = Color.BLUE.asString + """
            \t1. Processes on Items.
            \t2. Processes on Purchases.""" + Color.YELLOW.asString + """
                        
            \t0. Exit.
            """ + Color.RESET.asString;
    public static final String processOnItems = Color.BLUE.asString + """
            \t1. Add new item.
            \t2. Edit item.
            \t3. Remove item.
            \t4. Select all items.
            \t5. Select items by categories.
            \t6. Select items by price range.
            \t7. Select items by item name.""" + Color.YELLOW.asString + """
                        
            \t0. Back.
            """ + Color.RESET.asString;
    public static final String processOnPurchases = Color.BLUE.asString + """
            \t1. Add new purchase.
            \t2. Return item.
            \t3. Return purchase.
            \t4. Select all purchases.
            \t5. Select purchases by Date range.
            \t6. Select purchases by price range.
            \t7. Select purchases by Date.
            \t8. Select purchase by id.""" + Color.YELLOW.asString + """
                        
            \t0. Back.
            """ + Color.RESET.asString;
    public static final String orders = Color.BLUE.asString + """
            \t\t SORT BY:
            \t1. Price.
            \t2. Descending Price.
            \t3. Category
            \t4. Descending Category.
            \t5. Quantity
            \t6. Descending Quantity.
            \t7. ID (Default)
            """ + Color.RESET.asString;

    static {
        Generator.generateDummyData(bravo);
    }

    public static void main(String[] args) {
        mainOperation(1);
    }

    public static void mainOperation(int userInput) {
        while (userInput != 0) {
            printMenu(menu);
            try {
                userInput = scanner.nextInt();
                switch (userInput) {
                    case 0:
                        println(Color.CYAN.asString + "Exiting..." + Color.RESET.asString);
                        break;
                    case 1:
                        printMenu(processOnItems);
                        int input = scanner.nextInt();
                        operationOnItem(input);
                        break;
                    case 2:
                        printMenu(processOnPurchases);
                        input = scanner.nextInt();
                        scanner.nextLine();
                        operationOnPurchase(input);
                        break;
                    default:
                        printSelectionError();
                }
            } catch (Exception e) {
                if (e instanceof InputMismatchException) {
                    scanner.nextLine();
                    printInputMismatchError();
                } else if (e instanceof NoSuchElementException) {
                    printError("This item does not exist!");
                } else {
                    println(e.getClass().getSimpleName()); //TODO 1: DELETE AFTER TESTING!
                }
                mainOperation(0);
            }
        }
    }

    public static void operationOnItem(int selection) throws Exception {
        switch (selection) {
            case 0:
                println(Color.CYAN.asString + "Returning to main menu..." + Color.RESET.asString);
                break;
            case 1:
                printSelected("Add new item.");
                Item temp = getItemFromUser();
                bravo.addItem(temp.getName(), temp.getPrice(), temp.getCategory(), temp.getQuantity());
                break;
            case 2:
                printSelected("Edit item.");
                print("Please enter item id: ");
                long id = scanner.nextLong();
                temp = getItemFromUser();
                bravo.editItem(id, temp.getName(), temp.getPrice(), temp.getCategory(), temp.getQuantity());
                break;
            case 3:
                printSelected("Remove item.");
                print("Please enter item id: ");
                id = scanner.nextLong();
                bravo.removeItem(id);
                break;
            case 4:
                printSelected("Select all items.");
                print("How many items you want to see: ");
                int count = scanner.nextInt();
                scanner.nextLine();
                String input = askForConfirmation();
                switch (input) {
                    case "Y":
                        printOrderedItems(count);
                        break;
                    case "N":
                        printAllItems(bravo.getAllItems(count));
                        break;
                }
                break;
            case 5:
                printSelected("Select items by categories.");
                Category category = getCategory();
                printAllItems(bravo.getItems(category));
                break;
            case 6:
                printSelected("Select items by price range.");
                print("Please enter min price: ");
                double min = scanner.nextDouble();
                print("Please enter max price: ");
                double max = scanner.nextDouble();
                printAllItems(bravo.getItems(min, max));
                break;
            case 7:
                printSelected("Select items by item name.");
                println("Please enter word you are searching for: ");
                String word = scanner.next();
                printAllItems(bravo.getItems(word));
                break;
            default:
                printSelectionError();
        }
    }

    public static void operationOnPurchase(int selection) {
        switch (selection) {
            case 0:
                println(Color.CYAN.asString + "Returning to main menu..." + Color.RESET.asString);
                break;
            case 1:
                printSelected("Add new purchase.\n" +
                        "Please provide program with item IDs. " +
                        "When you finish adding items then write -1 to end adding process.");
                Purchase purchase = new Purchase();
                getIds(purchase, 1);
                bravo.addPurchase(purchase);
                break;
            case 2:
                printSelected("Return item.");
                Purchase newPurchase = new Purchase();
                purchase = checkPurchaseId();
                getReturnItemIds(purchase, newPurchase, 1);
                purchase.deactivate();
                println(newPurchase.getId());
                bravo.addPurchase(newPurchase);
                break;
            case 3:
                printSelected("Return purchase.");
                purchase = checkPurchaseId();
                bravo.returnPurchase(purchase.getId());
                System.out.printf(Color.CYAN.asString + "You got %.2f\u20BC refund. Purchase %d got returned!\n" + Color.RESET.asString,
                        purchase.getAmount(),
                        purchase.getId());
                break;
            case 4:
                printSelected("Select all purchases.");
                print("How many purchases do you want to see: ");
                int count = scanner.nextInt();
                scanner.nextLine();
                String input = askForConfirmation();
                switch (input) {
                    case "Y":
                        printOrdered(count, Item.class);
                }
                printALlPurchases(bravo.getAllPurchases(count));
                break;
            case 5:
                printSelected("Select purchases by Date range.");
                printALlPurchases(bravo.getPurchases(getDate("start"), getDate("end")));
                break;
            case 6:
                printSelected("Select purchases by price range.");
                double min = getPrice("minimum");
                double max = getPrice("maximum");
                if (min >= max)
                    printError("Minimum amount can't be larger than maximum amount.");
                else
                    printALlPurchases(bravo.getPurchases(min, max));
                break;
            case 7:
                printSelected("Select purchases by Date.");
                printALlPurchases(bravo.getPurchases(getDate("purchase")));
                break;
            case 8:
                printSelected("Select purchases by id.\n");
                purchase = checkPurchaseId();
                printPurchase(purchase);
                printAllPurchaseItems(purchase.getPurchaseItems().values());
                println();
                break;
            default:
                printSelectionError();
        }
    }

    private static Purchase checkPurchaseId() {
        long id = getId("purchase");
        Purchase purchase = bravo.getPurchase(id);
        if (purchase == null) {
            printError("This purchase doesn't exist.");
            checkPurchaseId();
        }
        return purchase;
    }

    private static void getIds(Purchase purchase, int count) {
        print("ID of item " + count + ": ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            if (id == -1)
                return;
            Item item = bravo.getItem(id);
            if (item != null) {
                print("Enter item quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();
                try {
                    PurchaseItem purchaseItem = new PurchaseItem(item, quantity);
                    purchase.addPurchaseItem(purchaseItem);
                    getIds(purchase, count + 1);
                } catch (PurchaseItem.OutOfStockException e) {
                    println(e.getMessage());
                }
            } else {
                printError("Item not found!");
                getIds(purchase, count);
            }
        } catch (Exception e) {
            scanner.nextLine();
            printInputMismatchError();
            getIds(purchase, count);
        }
    }

    private static final Queue<PurchaseItem> newItems = new LinkedList<>();

    private static void getReturnItemIds(Purchase oldPurchase, Purchase newPurchase, int count) {
        println("ID of purchase item " + count + " that you want to return: ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            if (id == -1) {
                for (PurchaseItem item : oldPurchase.getPurchaseItems().values()) {
                    if (item.isActive())
                        newPurchase.addPurchaseItem(item);
                    else
                        newPurchase.addPurchaseItem(newItems.poll());
                }
                newItems.clear();
                return;
            }
            PurchaseItem item = oldPurchase.getPurchaseItems().get(id);
            if (item != null) {
                print("Enter quantity that you want to return: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();
                if (!item.returnItem(quantity)) {
                    printError("You can't return more than you bought.");
                } else {
                    newItems.add(PurchaseItem.copyReturn(item, quantity));
                    getReturnItemIds(oldPurchase, newPurchase, count + 1);
                }
            } else {
                printError("Item not found!");
                getReturnItemIds(oldPurchase, newPurchase, count);
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            printInputMismatchError();
            getReturnItemIds(oldPurchase, newPurchase, count);
        }
    }

    private static void printAllPurchaseItems(Collection<PurchaseItem> purchaseItems) {
        println("Items:\n");
        for (PurchaseItem purchaseItem : purchaseItems) {
            System.out.format("\tID=%10d \tNAME=%15s \tCATEGORY=%20s \tQUANTITY=%15d \tPRICE=%10.2f \t%10s\n",
                    purchaseItem.getId(),
                    purchaseItem.getItem().getName(),
                    purchaseItem.getItem().getCategory(),
                    purchaseItem.getQuantity(),
                    purchaseItem.getPrice(),
                    purchaseItem.isActive() ? "ACTIVE" : "INACTIVE");
        }
        println();
    }

    private static void printAllItems(Collection<Item> items) {
        println("Items:\n");
        for (Item item : items) {
            System.out.format("\tID=%10d \tNAME=%15s \tCATEGORY=%20s \tIN STOCK=%15d \tPRICE=%10.2f\n",
                    item.getId(),
                    item.getName(),
                    item.getCategory().getStringFormat(),
                    item.getQuantity(),
                    item.getPrice());
        }
        println();
    }

    private static void printPurchase(Purchase purchase) {
        System.out.format("\tID=%10d \tAMOUNT=%10.2f\u20BC \tPURCHASE TIME=%20s \t%10s\n",
                purchase.getId(),
                purchase.getAmount(),
                purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                purchase.isActive() ? "ACTIVE" : "INACTIVE");
    }

    private static void printALlPurchases(Collection<Purchase> purchases) {
        println("Purchases:\n");
        for (Purchase purchase : purchases) {
            printPurchase(purchase);
        }
        println();
    }

    private static String askForConfirmation() throws InputMismatchException {
        print("Do you want to sort items? Y/N: ");
        String input = scanner.nextLine().toUpperCase();
        if (input.equals("Y") || input.equals("N"))
            return input;
        printInputMismatchError();
        return "";
    }

    private static void printOrdered(int count, Class<Filterable> type) {
        int orderSelection = getOrderSelection();
        if (orderSelection < 7 && orderSelection > 0) {
            orderFilterable(type);
        } else {
            printAllItems(bravo.getAllItems(count));
        }
    }

    private static <T extends Filterable> void orderFilterable(T filterable, int orderSelection, int count) {
        Comparator<T> order = filterable.getOrders()[orderSelection];
        List elements;
        if (filterable instanceof Item)
            elements = new ArrayList<>(bravo.getAllItems(count));
        else
            elements = new ArrayList<>(bravo.getAllPurchases(count));
        elements.sort(order);
        printFiltered(elements);
    }

    public static void printFiltered(List list) {
        if (list.get(0) instanceof Item)
            printAllItems(list);
        else
            printALlPurchases(list);
    }

    private static void printOrderedItems(int count) {
        int orderSelection = getOrderSelection();
        if (orderSelection < 7 && orderSelection > 0) {
            Comparator<Item> order = Item.orders[orderSelection - 1];
            List<Item> elements = new ArrayList<>(bravo.getAllItems(count));
            elements.sort(order);
            printAllItems(elements);
        } else {
            printAllItems(bravo.getAllItems(count));
        }
    }

    private static int getOrderSelection() {
        println("Predefined orders:");
        println(orders);
        print(end);
        try {
            int selection = scanner.nextInt();
            if (selection > 7 || selection < 1) {
                printSelectionError();
                getOrderSelection();
            }
            return selection;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            printInputMismatchError();
            return getOrderSelection();
        }
    }

    private static long getId(String specification) {
        print("Enter " + specification + " id: ");
        long id = 0L;
        try {
            id = scanner.nextLong();
            if (id < 0) {
                printError("Amount can't be negative number.");
                getId(specification);
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            printInputMismatchError();
            getId(specification);
        }
        return id;
    }

    private static double getPrice(String specification) {
        print("Enter " + specification + " amount: ");
        double amount = 0.0;
        try {
            amount = scanner.nextDouble();
            if (amount < 0) {
                printError("Amount can't be negative number.");
                getPrice(specification);
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            printInputMismatchError();
            getPrice(specification);
        }
        return amount;
    }

    private static LocalDateTime getDate(String specification) {
        print("Enter " + specification + " date: ");
        String extension;
        if (specification.equals("end"))
            extension = " 23:59";
        else
            extension = " 00:01";
        StringBuilder input = new StringBuilder(scanner.nextLine()).append(extension);
        LocalDateTime date = null;
        try {
            date = LocalDateTime.parse(input, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (DateTimeParseException e) {
            printError("Your input was incorrect. Please enter date with this format XX.XX.XXXX");
            getDate(specification);
        }
        return date;
    }

    private static Item getItemFromUser() {
        print("Please enter the item name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        print("Please enter item price: ");
        double price = scanner.nextDouble();
        Category category = getCategory();
        print("Please enter quantity: ");
        int quantity = scanner.nextInt();
        Item item = null;
        try {
            item = new Item(name, price, category, quantity, -1);
        } catch (InputMismatchException exception) {
            printError(exception.getMessage());
        }
        return item;
    }

    private static Category getCategory() {
        println("Please select one of Categories: ");
        Category.printCats();
        print(end);
        int selection = scanner.nextInt();
        if (selection < 0 || selection > Category.values().length) {
            printSelectionError();
            return getCategory();
        }
        return Category.values()[selection - 1];
    }
}