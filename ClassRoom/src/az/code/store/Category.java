package az.code.store;

public enum Category {
    DRINKS("Drinks"),
    FRUITS("Fruits"),
    VEGETABLES("Vegetables"),
    SNACKS("Snacks");

    private final String stringFormat;

    Category(String stringFormat) {
        this.stringFormat = stringFormat;
    }

    public String getStringFormat() {
        return stringFormat;
    }

    public static void printCats() {
        System.out.print("""
                \t1. Drinks
                \t2. Fruits
                \t3. Vegetables
                \t4. Snacks
                """);
    }
}
