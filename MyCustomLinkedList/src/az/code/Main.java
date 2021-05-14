package az.code;

import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
//        LinkedList<Object> linkedList = new LinkedList<>();
//        linkedList.add(1);
//        linkedList.add("sadsa");
//        linkedList.add(3.2);
//        linkedList.add(4L);
//        linkedList.add(5.9f);
//        for (Object i : linkedList) {
//            System.out.println(i.getClass().getSimpleName());
//        }

//        Stack<Integer> stack = new Stack<>();
//        stack.push(1);
//        stack.push(2);
//        stack.push(3);
//        stack.push(4);
//        stack.push(5);
//        stack.pop();
//        for (int i : stack) {
//            System.out.println(i);
//        }

        Queue<Integer> queue = new Queue<>();
        queue.push(1);
        queue.push(2);
        queue.push(3);
        queue.push(4);
        queue.push(5);
        System.out.println("Deleted=" + queue.pop());
        for (int i : queue) {
            System.out.println(i);
        }
    }
}