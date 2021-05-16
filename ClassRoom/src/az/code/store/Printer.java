package az.code.store;

import az.code.Color;

public final class Printer {
    public static final String start = "Please select one of selections:\n";
    public static final String end = "Your selection:\t";

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
