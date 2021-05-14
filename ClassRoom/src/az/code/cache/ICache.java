package az.code.cache;

interface ICache<T> {
    void addElement(T item);

    T getElement(int index);

    void printAll();
}
