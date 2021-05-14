package az.code.cache;

import java.util.Iterator;

public class Cache<T> implements ICache<T>, Iterable<T> {
    private Node<T> head;
    private Node<T> last;
    private final int capacity;
    private int size = 0;

    public Cache(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void addElement(T item) {
        Node<T> newElement = new Node<>(item);
        if (++size <= capacity) {
            if (head == null) {
                head = newElement;
                last = head;
            } else {
                last.setNext(newElement);
                last = last.getNext();
            }
        } else {
            head = head.getNext();
            last.setNext(newElement);
            last = last.getNext();
        }
    }

    @Override
    public T getElement(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(index);
        Node<T> temp = new Node<>(head);
        while (--index != -1) {
            temp = temp.getNext();
        }
        return temp.getValue();
    }

    @Override
    public void printAll() {
        Node<T> temp = new Node<>(head);
        while (temp != null) {
            System.out.print(temp.getValue() + " ");
            temp = temp.getNext();
        }
        System.out.println();
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomIterator(head);
    }

    class CustomIterator implements Iterator<T> {

        private Node<T> head;

        public CustomIterator(Node<T> head) {
            this.head = head;
        }

        @Override
        public boolean hasNext() {
            return this.head != null;
        }

        @Override
        public T next() {
            T value = this.head.getValue();
            this.head = this.head.getNext();
            return value;
        }
    }
}
