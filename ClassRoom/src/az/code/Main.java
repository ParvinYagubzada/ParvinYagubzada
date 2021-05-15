package az.code;

import az.code.store.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Bravo bravo = new Bravo();
    public static final String start = "Please select one of selections:\n";
    public static final String end = "Your selection:\t";
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

    static {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            bravo.addItem("Item" + i, random.nextDouble(), Category.values()[random.nextInt(4)], Math.abs(random.nextInt()));
        }
        for (int i = 0; i < 20; i++) {
            int year = random.nextInt(20) + 2000;
            int month = random.nextInt(12) + 1;
            int dayOfMonth = random.nextInt(28) + 1;
            int hour = random.nextInt(24);
            int minute = random.nextInt(60);
            Purchase purchase = new Purchase(LocalDateTime.of(year, month, dayOfMonth, hour, minute));
            for (int j = 0; j < random.nextInt(25) + 1; j++) {
                try {
                    PurchaseItem purchaseItem = new PurchaseItem(bravo.getItem(random.nextInt(100) + 1), random.nextInt(100) + 1);
                    purchase.addPurchaseItem(purchaseItem);
                } catch (PurchaseItem.OutOfStockException e) {
                    System.out.println("Error");
                }
            }
            bravo.addPurchase(purchase);
        }
    }

    public static void main(String[] args) {
        mainOperation(1);
    }

    private static void printMenu(String menu) {
        System.out.print(start + menu + end);
    }

    private static void printError(String error) {
        System.out.println(Color.RED.asString + error + Color.RESET.asString);
    }

    private static void printSelected(String selected) {
        String youSelected = "You selected: ";
        System.out.println(youSelected + Color.PURPLE.asString + selected + Color.RESET.asString);
    }

    private static void printSelectionError() {
        printError("You can't select number other than provided.");
    }

    private static void printInputMismatchError() {
        printError("Your input was incorrect.");
    }

    public static void mainOperation(int userInput) {
        while (userInput != 0) {
            printMenu(menu);
            try {
                userInput = scanner.nextInt();
                switch (userInput) {
                    case 0:
                        System.out.println(Color.CYAN.asString + "Exiting..." + Color.RESET.asString);
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
                }
                mainOperation(0);
            }
        }
    }

    public static void operationOnItem(int selection) throws Exception {
        switch (selection) {
            case 0:
                System.out.println(Color.CYAN.asString + "Returning to main menu..." + Color.RESET.asString);
                break;
            case 1:
                printSelected("Add new item.");
                Item temp = getItemFromUser();
                bravo.addItem(temp.getName(), temp.getPrice(), temp.getCategory(), temp.getQuantity());
                break;
            case 2:
                printSelected("Edit item.");
                System.out.print("Please enter item id: ");
                long id = scanner.nextLong();
                temp = getItemFromUser();
                bravo.editItem(id, temp.getName(), temp.getPrice(), temp.getCategory(), temp.getQuantity());
                break;
            case 3:
                printSelected("Remove item.");
                System.out.print("Please enter item id: ");
                id = scanner.nextLong();
                bravo.removeItem(id);
                break;
            case 4:
                printSelected("Select all items.");
                printAllItems(bravo.getAllItems());
                break;
            case 5:
                printSelected("Select items by categories.");
                Category category = getCategory();
                printAllItems(bravo.getItems(category));
                break;
            case 6:
                printSelected("Select items by price range.");
                System.out.print("Please enter min price: ");
                double min = scanner.nextDouble();
                System.out.print("Please enter max price: ");
                double max = scanner.nextDouble();
                printAllItems(bravo.getItems(min, max));
                break;
            case 7:
                printSelected("Select items by item name.");
                System.out.println("Please enter word you are searching for: ");
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
                System.out.println(Color.CYAN.asString + "Returning to main menu..." + Color.RESET.asString);
                break;
            case 1:
                printSelected("Add new purchase.\n" +
                        "Please provide program with item IDs. " +
                        "When you finish adding items then write -1 to end adding process.");
                Purchase purchase = new Purchase(LocalDateTime.now());
                getIds(purchase, 1);
                bravo.addPurchase(purchase);
                break;
            case 2:
                printSelected("Return item.");
                Purchase newPurchase = new Purchase(LocalDateTime.now());
                purchase = checkPurchaseId();
                getReturnItemIds(purchase, newPurchase, 1);
                purchase.deactivate();
                System.out.println(newPurchase.getId());
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
                printALlPurchases(bravo.getAllPurchases());
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
                System.out.println();
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
        System.out.print("ID of item " + count + ": ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            if (id == -1)
                return;
            Item item = bravo.getItem(id);
            if (item != null) {
                System.out.print("Enter item quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();
                try {
                    PurchaseItem purchaseItem = new PurchaseItem(item, quantity);
                    purchase.addPurchaseItem(purchaseItem);
                    getIds(purchase, count + 1);
                } catch (PurchaseItem.OutOfStockException e) {
                    System.out.println(e.getMessage());
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
        System.out.println("ID of purchase item " + count + " that you want to return: ");
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
                System.out.print("Enter quantity that you want to return: ");
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
        System.out.println("Items:\n");
        for (PurchaseItem purchaseItem: purchaseItems) {
            System.out.format("\tID=%10d \tNAME=%15s \tCATEGORY=%20s \tQUANTITY=%15d \tPRICE=%10.2f \t%10s\n",
                    purchaseItem.getId(),
                    purchaseItem.getItem().getName(),
                    purchaseItem.getItem().getCategory(),
                    purchaseItem.getQuantity(),
                    purchaseItem.getPrice(),
                    purchaseItem.isActive() ? "ACTIVE" : "INACTIVE");
        }
        System.out.println();
    }

    private static void printAllItems(Collection<Item> items) {
        System.out.println("Items:\n");
        for (Item item : items) {
            System.out.format("\tID=%10d \tNAME=%15s \tCATEGORY=%20s \tIN STOCK=%15d \tPRICE=%10.2f\n",
                    item.getId(),
                    item.getName(),
                    item.getCategory().getStringFormat(),
                    item.getQuantity(),
                    item.getPrice());
        }
        System.out.println();
    }

    private static void printPurchase(Purchase purchase) {
        System.out.format("\tID=%10d \tAMOUNT=%10.2f\u20BC \tPURCHASE TIME=%20s \t%10s\n",
                purchase.getId(),
                purchase.getAmount(),
                purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                purchase.isActive() ? "ACTIVE" : "INACTIVE");
    }

    private static void printALlPurchases(Collection<Purchase> purchases) {
        System.out.println("Purchases:\n");
        for (Purchase purchase : purchases) {
            printPurchase(purchase);
        }
        System.out.println();
    }

    private static long getId(String specification) {
        System.out.print("Enter " + specification + " id: ");
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
        System.out.print("Enter " + specification + " amount: ");
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
        System.out.print("Enter " + specification + " date: ");
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
        System.out.print("Please enter the item name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.print("Please enter item price: ");
        double price = scanner.nextDouble();
        Category category = getCategory();
        System.out.print("Please enter quantity: ");
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