package az.code;


import az.code.store.Item;

import java.io.Console;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Test {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.now();
        LocalDate nextMonth = localDate.minusDays(localDate.getDayOfMonth() - 1).plusMonths(1);
        System.out.println(localDate.isBefore(nextMonth));
        System.out.println(localDate.compareTo(nextMonth));
        System.out.println(localDate.format(dtf));
        System.out.println(localDate.plusMonths(1).format(dtf));
        System.out.println(nextMonth.format(dtf));
        localDate = localDate.plusDays(100);
        System.out.println(localDate.format(dtf));

    }
}
