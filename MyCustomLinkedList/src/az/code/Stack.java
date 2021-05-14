package az.code;

import java.util.Iterator;

public class Stack<T> implements Iterable<T>{
    private ReverseNode<T> head = null;

    public T pop() {
        T temp = this.head.getValue();
        this.head = head.getPrevious();
        return temp;
    }

    public void push(T value) {
        ReverseNode<T> newNode = new ReverseNode<>(value);
        if (head != null) {
            newNode.setPrevious(new ReverseNode<>(head));
        }
        head = newNode;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator(head);
    }

    private class MyIterator implements Iterator<T> {

        private ReverseNode<T> head;

        public MyIterator(ReverseNode<T> head) {
            this.head = head;
        }

        @Override
        public boolean hasNext() {
            return head != null;
        }

        @Override
        public T next() {
            ReverseNode<T> temp = this.head;
            this.head = temp.getPrevious();
            return temp.getValue();
        }
    }
}

class ReverseNode<T> {
    private ReverseNode<T> previous;
    private T value;

    public ReverseNode(ReverseNode<T> original) {
        this.previous = original.previous;
        this.value = original.value;
    }

    public ReverseNode(T value) {
        this.previous = null;
        this.value = value;
    }

    public boolean hasPrevious() {
        return previous == null;
    }

    public ReverseNode<T> getPrevious() {
        return previous;
    }

    public void setPrevious(ReverseNode<T> previous) {
        this.previous = previous;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}