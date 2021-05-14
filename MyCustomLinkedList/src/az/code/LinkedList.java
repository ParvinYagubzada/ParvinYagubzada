package az.code;

import java.util.Iterator;

public class LinkedList<T> implements Iterable<T> {
    private Node<T> head = null;
    private int size = 0;

    public void add(T givenValue) {
        if (head == null)
            head = new Node<>(givenValue);
        else
            nodeAt(size).setNext(new Node<>(givenValue));
        size++;
    }

    public void add(T givenValue, int index) {
        checkIndex(index);
        Node<T> newNode = new Node<>(givenValue);
        if (index != 0) {
            Node<T> selected = nodeAt(index - 1);
            Node<T> temp = selected.getNext();
            selected.setNext(newNode);
            newNode.setNext(temp);
        } else {
            Node<T> temp = this.head;
            this.head = newNode;
            newNode.setNext(temp);
        }
    }

    public T get(int index) {
        checkIndex(index);
        return nodeAt(index).getValue();
    }

    public T remove(int index) {
        checkIndex(index);
        T temp = nodeAt(index).getValue();
        if (index != 0) {
            nodeAt(index - 1).setNext(nodeAt(index + 1));
        } else {
            if (size == 1) {
                this.head = null;
            } else {
                this.head = this.head.getNext();
            }
        }
        return temp;
    }

    public int find(T value) {
        int index = 0;
        Node<T> selected = head;
        while (selected != null) {
            if (selected.getValue().equals(value))
                return index;
            selected = selected.getNext();
            index++;
        }
        return -1;
    }

    public void printAll() {
        Node<T> temp = head;
        while (temp != null) {
            System.out.print(temp.getValue() + " ");
            temp = temp.getNext();
        }
    }

    private void checkIndex(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(index);
    }

    private Node<T> nodeAt(int index) {
        Node<T> selected = head;
        while (selected.hasNext() && --index >= 0) {
            selected = selected.getNext();
        }
        return selected;
    }

    public int getSize() {
        return size;
    }

    private class CustomIterator implements Iterator<T> {

        private Node<T> head;

        public CustomIterator(Node<T> head) {
            this.head = new Node<>(head);
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

    @Override
    public Iterator<T> iterator() {
        return new CustomIterator(this.head);
    }
}
