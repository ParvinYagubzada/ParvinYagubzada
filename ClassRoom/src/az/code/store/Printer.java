package az.code.store;

import az.code.Color;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public final class Printer {
    public static final String start = "Please select one of selections:\n";
    public static final String end = "Your selection:\t";
    public static final String menu = Color.BLUE.asString + """
            \t1. Processes on Items.
            \t2. Processes on Purchases.
            \t3. Processes on Bravo.""" + Color.YELLOW.asString + """
             Requires admin login.
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
    public static final String processOnBravo = Color.BLUE.asString + """
            \t1. Show total income.
            \t2. Show total item count witch is sold.
            \t3. Show income by Date.
            \t4. Change password.""" + Color.YELLOW.asString + """
                        
            \t0. Back.
            """ + Color.RESET.asString;
    public static final String ordersForItems = Color.BLUE.asString + """
            \t\t SORT BY:
            \t1. Price.
            \t2. Descending Price.
            \t3. Category.
            \t4. Descending Category.
            \t5. Quantity.
            \t6. Descending Quantity.
            \t7. ID (Default)
            """ + Color.RESET.asString;
    public static final String ordersForPurchases = Color.BLUE.asString + """
            \t\t SORT BY:
            \t1. Amount.
            \t2. Descending Amount.
            \t3. Date and Time. From oldest to newest.
            \t4. Date and Time. From newest to oldest.
            \t5. Status. Inactive first.
            \t6. Status. Actives first.
            \t7. ID (Default)
            """ + Color.RESET.asString;

    public static <T> String colorString(Color color, T word) {
        return color.asString + word + Color.RESET.asString;
    }

    public static String addBeginningEndingLine(String str) {
        return "\n\t" + str + "\n";
    }

    public static String hashString(String input) {
        try {
            return Arrays.toString(MessageDigest.getInstance("MD5").digest(input.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return input;
    }

    public static void print(String string) {
        System.out.print(string);
    }

    public static void println() {
        System.out.println();
    }

    public static <T> void println(T value) {
        System.out.println(value);
    }

    public static void printMenu(String menu) {
        System.out.print(start + menu + end);
    }

    public static void printError(String error) {
        System.out.println(Color.RED.asString + error + Color.RESET.asString);
    }

    public static void printSelected(String selected) {
        String youSelected = "You selected: ";
        System.out.println(youSelected + Color.PURPLE.asString + selected + Color.RESET.asString);
    }

    public static void printSelectionError() {
        printError("You can't select number other than provided.");
    }

    public static void printInputMismatchError() {
        printError("Your input was incorrect.");
    }
}
