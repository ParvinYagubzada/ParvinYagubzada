import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        ArrayList<Integer> my = new ArrayList<>();
        my.add(1);
        my.add(2);
        my.add(3);
        my.add(4);
        my.add(5);
        my.add(6);
        my.add(7);
        my.add(8);
        my.add(9);
        my.add(324242, 3);
        System.out.println(my.remove(3));
        System.out.println("Find=" + my.find(5));
        for (int i : my) {
            System.out.println(i);
        }
    }
}

class ArrayList<T> implements List<T>, Iterable<T>{
    private int capacity = 10;
    private int size = 0;
    private T[] array;

    public ArrayList() {
        this.array = (T[]) new Object[capacity];
    }


    @Override
    public void add(T givenValue) {
        if (size + 1 == capacity) {
            resizeArray();
            add(givenValue);
        }
        array[size++] = givenValue;
    }

    @Override
    public void add(T givenValue, int index) {
        checkIndex(index);
        if (size + 1 == capacity) {
            resizeArray();
            add(givenValue, index);
            return;
        }
        T[] firstHalf = copyFirst(index);
        T[] secondHalf = copyLast(index);
        System.arraycopy(firstHalf, 0, array,0, firstHalf.length);
        array[index] = givenValue;
        System.arraycopy(secondHalf, 0, array, index + 1, secondHalf.length);
        size++;
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        return array[index];
    }

    @Override
    public T remove(int index) {
        checkIndex(index);
        T returnValue = array[index];
        T[] firstHalf = copyFirst(index);
        T[] secondHalf = copyLast(index + 1);
        System.arraycopy(firstHalf, 0, array,0, firstHalf.length);
        System.arraycopy(secondHalf, 0, array, index, secondHalf.length);
        size--;
        return returnValue;
    }

    @Override
    public int find(T value) {
        int findIndex = -1;
        for (int i = 0; i < size; i++) {
            if (value.equals(array[i]))
                return i;
        }
        return findIndex;
    }

    private void checkIndex(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(index);
    }

    private void resizeArray() {
        T[] newArray = (T[]) new Object[capacity * 2];
        capacity *= 2;
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    private T[] copyFirst(int index) {
        T[] newArray = (T[]) new Object[index];
        System.arraycopy(array, 0, newArray, 0, newArray.length);
        return newArray;
    }

    private T[] copyLast(int index) {
        T[] newArray = (T[]) new Object[size - index];
        System.arraycopy(array, index, newArray, 0, newArray.length);
        return newArray;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator(this);
    }

    private class ListIterator implements Iterator<T> {

        ArrayList<T> arrayList;
        int count = 0;

        public ListIterator(ArrayList<T> arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public boolean hasNext() {
            return count != arrayList.size;
        }

        @Override
        public T next() {
            return arrayList.get(count++);
        }
    }
}

interface List<T> {

    void add(T givenValue);

    void add(T givenValue, int index);

    T get(int index);

    T remove(int index);

    int find(T value);
}
