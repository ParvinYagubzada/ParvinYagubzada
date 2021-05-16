package az.code;

import az.code.store.*;
import az.code.store.Bravo.LoginError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

import static az.code.store.Bravo.IncomeStatisticsHolder;
import static az.code.store.Printer.*;

@SuppressWarnings("ALL")
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Bravo bravo = new Bravo();

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
                scanner.nextLine();
                switch (userInput) {
                    case 0:
                        println(colorString(Color.CYAN, "Exiting..."));
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
                    case 3:
                        if (checkLogin()) {
                            printMenu(processOnBravo);
                            input = scanner.nextInt();
                            scanner.nextLine();
                            operationOnBravo(input);
                        }
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
                } else if (e instanceof WantToExit) {
                    println(colorString(Color.CYAN, "Returning to main menu..."));
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
                println(colorString(Color.CYAN, "Returning to main menu..."));
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
                println(colorString(Color.CYAN, "Returning to main menu..."));
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
                try {
                    bravo.returnPurchase(purchase.getId());
                    System.out.printf(colorString(Color.CYAN, "You got %.2f\u20BC refund. Purchase %d got returned!\n"),
                            purchase.getAmount(),
                            purchase.getId());
                } catch (Exception e) {
                    printError(e.getMessage());
                    operationOnPurchase(selection);
                }
                break;
            case 4:
                printSelected("Select all purchases.");
                print("How many purchases do you want to see: ");
                int count = scanner.nextInt();
                scanner.nextLine();
                String input = askForConfirmation();
                switch (input) {
                    case "Y":
                        printOrderedPurchases(count);
                        break;
                    case "N":
                        printALlPurchases(bravo.getAllPurchases(count));
                        break;
                }
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
                println();
                printPurchase(purchase);
                println();
                printAllPurchaseItems(purchase.getPurchaseItems().values());
                println();
                break;
            default:
                printSelectionError();
        }
    }

    public static void operationOnBravo(int selection) {
        switch (selection) {
            case 0:
                println(colorString(Color.CYAN, "Returning to main menu..."));
                break;
            case 1:
                println(colorString(Color.CYAN, addBeginningEndingLine("Your total income is " + bravo.getTotalIncome() + "\u20BC")));
                break;
            case 2:
                println(colorString(Color.CYAN, addBeginningEndingLine("Your sold item count is " + bravo.getTotalSoldItemCount())));
                break;
            case 3:
                println();
                for (IncomeStatisticsHolder holder : bravo.getIncomeStatistics(getDate("Start"), getDate("End")))
                    println(holder);
                println();
                break;
            case 4:
                String password = getPassword();
                bravo.setPassword(password);
                break;
            default:
                printSelectionError();
        }
    }

    private static Purchase checkPurchaseId() {
        long id = getLong("purchase");
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
                    printError(e.getMessage());
                    getIds(purchase, count);
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
            System.out.format("\tID=%10d \tNAME=%15s \tCATEGORY=%20s \tQUANTITY=%15d \tPRICE=%10.2f\u20BC \t%10s\n",
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
            System.out.format("\tID=%10d \tNAME=%15s \tCATEGORY=%20s \tIN STOCK=%15d \tPRICE=%10.2f\u20BC\n",
                    item.getId(),
                    item.getName(),
                    item.getCategory().getStringFormat(),
                    item.getQuantity(),
                    item.getPrice());
        }
        println();
    }

    private static void printPurchase(Purchase purchase) {
        System.out.format("\tID=%10d \tAMOUNT=%15.2f\u20BC \tPURCHASE TIME=%20s \t%10s\n",
                purchase.getId(),
                purchase.getAmount(),
                purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                purchase.isActive() ? colorString(Color.GREEN, "ACTIVE") : colorString(Color.RED, "INACTIVE"));
    }

    private static void printALlPurchases(Collection<Purchase> purchases) {
        println("Purchases:\n");
        for (Purchase purchase : purchases) {
            printPurchase(purchase);
        }
        println();
    }

    private static boolean checkLogin() {
        print("Enter username: ");
        String username = scanner.nextLine();
        if (username.equals("-1"))
            throw new WantToExit();
        print("Enter password: ");
        String password = scanner.nextLine();
        if (password.equals("-1"))
            throw new WantToExit();
        try {
            bravo.checkCredentials(username, password);
            return true;
        } catch (LoginError loginError) {
            printError(loginError.getMessage());
            checkLogin();
        }
        return false;
    }

    private static String askForConfirmation() throws InputMismatchException {
        print("Do you want to sort items? Y/N: ");
        String input = scanner.nextLine().toUpperCase();
        if (input.equals("Y") || input.equals("N"))
            return input;
        printInputMismatchError();
        return "";
    }

    private static void printOrderedPurchases(int count) {
        int orderSelection = getOrderSelection(Purchase.class);
        if (orderSelection < 7 && orderSelection > 0) {
            Comparator<Purchase> order = Purchase.orders[orderSelection - 1];
            List<Purchase> elements = bravo.getAllPurchases(count, order);
            printALlPurchases(elements);
        } else {
            printALlPurchases(bravo.getAllPurchases(count));
        }
    }

    private static void printOrderedItems(int count) {
        int orderSelection = getOrderSelection(Item.class);
        if (orderSelection < 7 && orderSelection > 0) {
            Comparator<Item> order = Item.orders[orderSelection - 1];
            List<Item> elements = bravo.getAllItems(count, order);
            printAllItems(elements);
        } else {
            printAllItems(bravo.getAllItems(count));
        }
    }

    private static int getOrderSelection(Class type) {
        println("Predefined orders:");
        if (type.getSimpleName().equals("Item"))
            println(ordersForItems);
        else
            println(ordersForPurchases);
        print(end);
        try {
            int selection = scanner.nextInt();
            if (selection > 7 || selection < 1) {
                printSelectionError();
                getOrderSelection(type);
            }
            return selection;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            printInputMismatchError();
            return getOrderSelection(type);
        }
    }

    private static String getPassword() {
        print("Enter new password: ");
        String pass = scanner.nextLine();
        Pattern pattern = Pattern.compile("(?=.*\\d{3,})(?=\\S+$).{8,}");
        if (pattern.matcher(pass).matches())
            return pass;
        printError("Password needs to be at least 8 characters and it has to have at least 3 digits.");
        return getPassword();
    }

    private static long getLong(String specification) {
        print("Enter " + specification + " id: ");
        long id = 0L;
        try {
            id = scanner.nextLong();
            if (id < -1) {
                printError("Amount can't be negative number.");
                getLong(specification);
            } else if (id == -1) {
                throw new WantToExit();
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            printInputMismatchError();
            getLong(specification);
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
            extension = " 00:00";
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

    static class WantToExit extends RuntimeException {
        public WantToExit() {
            super("Returning to main menu...");
        }
    }
}