package az.code;

public class Main {

    public static void main(String[] args) {
        Cache<Integer> s = new Cache<>(5);
        s.addElement(1);
        s.addElement(2);
        s.addElement(3);
        s.addElement(4);
        s.addElement(5);
        s.printAll();
        s.addElement(6);
        s.addElement(7);
        s.addElement(8);
        s.addElement(9);
        s.addElement(10);
        s.printAll();
        System.out.println(s.getElement(0));
    }
}

