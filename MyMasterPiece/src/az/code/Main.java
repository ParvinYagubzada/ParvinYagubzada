package az.code;

import com.sun.security.jgss.GSSUtil;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String first = "14325353356767897608945094598980459584670892347872734507543097245389053479802347098234798023475678567856785685678576833453";
        String second = "19087436908273409873459074398769834280745237908453676540987645390823";

        BigInteger firstNumber = new BigInteger(first);
        BigInteger secondNumber = new BigInteger(second);
        java.math.BigInteger fNumber = new java.math.BigInteger(first);
        java.math.BigInteger sNumber = new java.math.BigInteger(second);

        long start = System.currentTimeMillis();

        System.out.println(firstNumber.add(secondNumber));
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        System.out.println(firstNumber.subtract(secondNumber));
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        System.out.println(firstNumber.multiply(secondNumber));
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        System.out.println(firstNumber.divide(secondNumber));
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        System.out.println();

        System.out.println(fNumber.add(sNumber));
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        System.out.println(fNumber.subtract(sNumber));
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        System.out.println(fNumber.multiply(sNumber));
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        System.out.println(fNumber.divide(sNumber));
        System.out.println(System.currentTimeMillis() - start);
    }
}

class BigInteger {
    private static final BigInteger ZERO = new BigInteger("0");
    private static final BigInteger ONE = new BigInteger("1");
    private boolean sign = false;
    private final String absoluteValue;

    public BigInteger(BigInteger bigInteger) {
        this(bigInteger.toString());
    }

    public BigInteger(String value) {
        if (!checkSyntax(value))
            throw new InputMismatchException("Numbers needs to be in Integer format.");
        if (value == null || value.isEmpty()) {
            absoluteValue = "0";
        } else {
            if (value.charAt(0) == '-') {
                sign = true;
                absoluteValue = value.substring(1);
            } else {
                absoluteValue = value;
            }
        }
    }

    public BigInteger min(BigInteger secondNumber) {
        if (this.compareTo(secondNumber) == 1)
            return secondNumber;
        return this;
    }

    public BigInteger max(BigInteger secondNumber) {
        if (this.compareTo(secondNumber) == -1)
            return secondNumber;
        return this;
    }

    public BigInteger add(BigInteger sNumber) {
        BigInteger firstNumber = new BigInteger(this);
        BigInteger secondNumber = new BigInteger(sNumber);
        if (firstNumber.sign == secondNumber.sign) {
            return new BigInteger(asStringSign(sign) + addition(firstNumber.absoluteValue, secondNumber.absoluteValue));
        }
        BigInteger min = firstNumber.min(secondNumber);
        BigInteger max = firstNumber.max(secondNumber);
        return max.subtract(min.flipSign());
    }

    public BigInteger subtract(BigInteger sNumber) {
        BigInteger firstNumber = new BigInteger(this);
        BigInteger secondNumber = new BigInteger(sNumber);
        if (!firstNumber.sign && secondNumber.sign) {
            return firstNumber.add(secondNumber.flipSign());
        } else if (firstNumber.sign && !secondNumber.sign) {
            return firstNumber.flipSign().add(secondNumber).flipSign();
        } else if (firstNumber.sign && secondNumber.sign) {
            return secondNumber.flipSign().subtract(firstNumber.flipSign());
        }

        switch (firstNumber.compareTo(secondNumber)) {
            case 0:
                return ZERO;
            case -1:
                return secondNumber.subtract(this).flipSign();
        }
        return new BigInteger(subtraction(firstNumber.absoluteValue, secondNumber.absoluteValue));
    }

    public BigInteger multiply(BigInteger sNumber) {
        boolean same = this.sign == sNumber.sign;
        BigInteger firstNumber = this.sign ? new BigInteger(this).flipSign() : new BigInteger(this);
        BigInteger secondNumber = sNumber.sign ? new BigInteger(sNumber).flipSign() : new BigInteger(sNumber);
        BigInteger max = firstNumber.max(secondNumber);
        secondNumber = firstNumber.min(secondNumber);
        if (secondNumber.absoluteValue.equals("0"))
            return ZERO;
        BigInteger result = new BigInteger(multiplication(max.absoluteValue, secondNumber.absoluteValue));
        if (same)
            return result;
        return result.flipSign();
    }

    private <T extends Number> BigInteger multiply(T sNumber) {
        return this.multiply(new BigInteger(String.valueOf(sNumber)));
    }

    public BigInteger divide(BigInteger sNumber) {
        boolean same = this.sign == sNumber.sign;
        if (sNumber.equals(ZERO))
            throw new ArithmeticException("You cannot divide by zero.");
        else if (this.equals(ZERO))
            throw new ArithmeticException("You cannot divide zero. Because this is my class and I make the rules here.");
        BigInteger firstNumber = new BigInteger(this).abs();
        BigInteger secondNumber = new BigInteger(sNumber).abs();
        switch (firstNumber.compareTo(secondNumber)) {
            case 0:
                return ONE;
            case -1:
                return ZERO;
        }
        BigInteger result = new BigInteger(dividation(firstNumber.absoluteValue, secondNumber.absoluteValue));
        if (same)
            return result;
        return result.flipSign();
    }

    private static String addition(String firstNumber, String secondNumber) {
        firstNumber = new StringBuilder(firstNumber).reverse().toString();
        secondNumber = new StringBuilder(secondNumber).reverse().toString();
        StringBuilder result = new StringBuilder();
        int counter = Math.max(firstNumber.length(), secondNumber.length());
        boolean greater = false;
        for (int i = 0; i < counter + 1; i++) {
            char chFirst = i < firstNumber.length() ? firstNumber.charAt(i) : '0';
            char chSecond = i < secondNumber.length() ? secondNumber.charAt(i) : '0';
            int integer = chFirst + chSecond - 96 + asInt(greater);
            greater = integer >= 10;
            result.append(integer % 10);
        }
        if (result.toString().isEmpty())
            return "0";
        return deleteLastZeroes(result, "Addition").reverse().toString();
    }

    private static String subtraction(String firstNumber, String secondNumber) {
        firstNumber = new StringBuilder(firstNumber).reverse().toString();
        secondNumber = new StringBuilder(secondNumber).reverse().toString();
        StringBuilder result = new StringBuilder(firstNumber);
        boolean smaller = false;
        for (int i = 0; i < firstNumber.length(); i++) {
            char chFirst = firstNumber.charAt(i);
            char chSecond = i < secondNumber.length() ? secondNumber.charAt(i) : '0';
            int integer = chFirst - chSecond - asInt(smaller);
            smaller = integer < 0;
            result.replace(i, i + 1, String.valueOf((10 + integer) % 10));
        }
        return deleteLastZeroes(result, "Subtraction").reverse().toString();
    }

    private static String multiplication(String firstNumber, String secondNumber) {
        ArrayList<String> numbers = new ArrayList<>();
        secondNumber = new StringBuilder(secondNumber).reverse().toString();
        for (int i = 0; i < secondNumber.length(); i++) {
            numbers.add(multiplySingleDigit(firstNumber, secondNumber.charAt(i)));
        }
        return sumElements(numbers);
    }

    private static String dividation(String firstNumber, String secondNumber) {
        StringBuilder selectedNumber = new StringBuilder();
        StringBuilder result = new StringBuilder();
        int counter = Integer.MIN_VALUE;
        for (int i = 0; i < firstNumber.length(); i++) {
            selectedNumber.append(firstNumber.charAt(i));
            counter++;
            if (new BigInteger(selectedNumber.toString()).compareTo(new BigInteger(secondNumber)) >= 0) {
                Subtract subtract = findLargestOutgoing(new BigInteger(selectedNumber.toString()), new BigInteger(secondNumber));
                if (counter > 1)
                    result.append("0".repeat(counter - 1));
                counter = 0;
                result.append(subtract.divisor);
                BigInteger remaining = new BigInteger(selectedNumber.toString()).subtract(subtract.number);
                if (remaining.equals(BigInteger.ZERO))
                    selectedNumber = new StringBuilder();
                else
                    selectedNumber = new StringBuilder(remaining.absoluteValue);
            }
        }
        return result.toString();
    }

    private static Subtract findLargestOutgoing(BigInteger number, BigInteger divider) {
        byte times = 2;
        BigInteger selected = divider;
        BigInteger multiplication = divider.multiply(times);
        while (multiplication.compareTo(number) == -1) {
            selected = multiplication;
            times++;
            multiplication = divider.multiply(times);
        }
        return new Subtract(selected, times - 1);
    }

    private static class Subtract {
        public final BigInteger number;
        public final int divisor;

        public Subtract(BigInteger number, int divisor) {
            this.number = number;
            this.divisor = divisor;
        }
    }

    private static String multiplySingleDigit(String firstNumber, char digitCharacter) {
        if (digitCharacter == '0')
            return "0";
        int digit = digitCharacter - 48;
        firstNumber = new StringBuilder(firstNumber).reverse().toString();
        StringBuilder result = new StringBuilder();
        int holder = 0;
        for (int i = 0; i < firstNumber.length() + 1; i++) {
            int chFirst = i < firstNumber.length() ? firstNumber.charAt(i) - 48 : 0;
            int integer = chFirst * digit + holder;
            holder = integer / 10;
            result.append(integer % 10);
        }
        return deleteLastZeroes(result, "SingleDigit").reverse().toString();
    }

    private static String sumElements(List<String> elements) {
        String sum = elements.get(0);
        StringBuilder zeros = new StringBuilder("0");
        for (int i = 1; i < elements.size(); i++) {
            sum = addition(sum, elements.get(i) + zeros);
            zeros.append("0");
        }
        return sum;
    }

    private static StringBuilder deleteLastZeroes(StringBuilder number, String caller) {
        int lastZero = number.length() - 1;
        while (number.charAt(lastZero) == '0') {
            number.deleteCharAt(lastZero);
            lastZero--;
        }
        return number;
    }

    private BigInteger abs() {
        BigInteger positive = new BigInteger(this);
        positive.sign = false;
        return positive;
    }

    private static int asInt(boolean bool) {
        if (bool)
            return 1;
        return 0;
    }

    private static String asStringSign(boolean sign) {
        if (sign)
            return "-";
        return "";
    }

    private BigInteger flipSign() {
        this.sign = !this.sign;
        return this;
    }

    private boolean checkSyntax(String number) {
        Pattern pattern = Pattern.compile("[-]?(([\\d&&[^0]]\\d*)|(0))");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public boolean equals(BigInteger secondNumber) {
        if (this == secondNumber)
            return true;
        return this.sign == secondNumber.sign && this.absoluteValue.equals(secondNumber.absoluteValue);
    }

    public int compareTo(BigInteger secondNumber) {
        if (this.equals(secondNumber))
            return 0;
        if (!this.sign && secondNumber.sign)
            return 1;
        if (this.sign && !secondNumber.sign)
            return -1;
        return checkLength(this, secondNumber);
    }

    private static int checkLength(BigInteger firstNumber, BigInteger secondNumber) {
        int result;
        if (firstNumber.absoluteValue.length() > secondNumber.absoluteValue.length())
            result = 1;
        else if (firstNumber.absoluteValue.length() < secondNumber.absoluteValue.length())
            result = -1;
        else
            result = compareSameLength(firstNumber.absoluteValue, secondNumber.absoluteValue);
        if (!firstNumber.sign)
            return result;
        return -result;
    }

    private static int compareSameLength(String firstNumber, String secondNumber) {
        for (int i = 0; i < firstNumber.length(); i++) {
            if (firstNumber.charAt(i) != secondNumber.charAt(i)) {
                if (firstNumber.charAt(i) > secondNumber.charAt(i))
                    return 1;
                else
                    return -1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.absoluteValue);
        sb.insert(0, sign ? "-" : "");
        return sb.toString();
    }
}
