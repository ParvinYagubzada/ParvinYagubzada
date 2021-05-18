package az.code;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private static List<Person> persons;

    static {
        persons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            persons.add(new Person(i, "Person " + i, "Sur " + i, String.valueOf(new Random().nextInt(50) + 1)));
        }
    }

    public static void main(String[] args) {
        persons
                .forEach(System.out::println);
        System.out.println();

        persons
                .stream()
                .mapToInt(person -> Integer.parseInt(person.age))
                .filter(age -> age > 18)
                .forEach(System.out::println);

        System.out.println(persons
                .stream()
                .mapToInt(person -> Integer.parseInt(person.age))
                .average()
                .getAsDouble()
        );

        System.out.println(persons
                .stream()
                .mapToInt(person -> Integer.parseInt(person.age))
                .filter(age -> age > 18)
                .sum()
        );
    }

    static class Person {
        int id;
        String name;
        String surname;
        String age;

        public Person(int id, String name, String surname, String age) {
            this.id = id;
            this.name = name;
            this.surname = surname;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", surname='" + surname + '\'' +
                    ", age='" + age + '\'' +
                    '}';
        }
    }


}
