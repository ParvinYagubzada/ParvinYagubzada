package az.code;

public class Node <T> {
    private T value;
    private Node<T> next;

    public Node(Node<T> copy) {
        this.value = copy.value;
        this.next = copy.next;
    }

    public Node(T value) {
        this.value = value;
        this.next = null;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public boolean hasNext() {
        return next != null;
    }
}
