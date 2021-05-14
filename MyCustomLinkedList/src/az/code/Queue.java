package az.code;

import java.util.Iterator;

public class Queue <T> implements Iterable<T>{
    private Node<T> head = null;
    private Node<T> last = null;
    private int size = 0;

    public T pop() {
        T temp = head.getValue();
        if (size == 1) {
            this.head = null;
        } else {
            this.head = this.head.getNext();
        }
        return temp;
    }

    public void push(T value) {
        size++;
        Node<T> newNode = new Node<>(value);
        if (head == null) {
            head = newNode;
        } else {
            last.setNext(newNode);
        }
        last = newNode;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator(head);
    }

    private class MyIterator implements Iterator<T> {

        private Node<T> head;

        public MyIterator(Node<T> head) {
            this.head = head;
        }

        @Override
        public boolean hasNext() {
            return this.head != null;
        }

        @Override
        public T next() {
            Node<T> temp = this.head;
            this.head = temp.getNext();
            return temp.getValue();
        }
    }
}
