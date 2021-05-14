package az.code;

class Node<T> {
    private T value;
    private Node<T> next = null;

    public Node(Node<T> copy) {
        this.next = copy.next;
        this.value = copy.value;
    }

    public Node(T value) {
        this.value = value;
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

    public boolean hasNext() {
        return next != null;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}
