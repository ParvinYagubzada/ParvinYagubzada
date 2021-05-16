package az.code.store;

import java.time.LocalDateTime;
import java.util.Random;
import java.lang.Math;

public final class Generator {
    private static long id = 1000000;

    public static long getID() {
        return id++;
    }

    public static void generateDummyData(Bravo bravo) {
        long startingID = id;
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            bravo.addItem("Item" + i,
                    random.nextDouble() * Math.pow(10, Math.abs(random.nextInt(4)) + 1),
                    Category.values()[random.nextInt(4)],
                    Math.abs(random.nextInt(10000)) + 1);
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
                    PurchaseItem purchaseItem = new PurchaseItem(bravo.getItem(random.nextInt(100) + startingID), random.nextInt(100) + 1);
                    purchase.addPurchaseItem(purchaseItem);
                } catch (PurchaseItem.OutOfStockException e) {
                    System.out.println("Error");
                }
            }
            bravo.addPurchase(purchase);
        }
    }
}
