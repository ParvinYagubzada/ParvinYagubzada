package az.code.store;

import java.util.Comparator;

public interface Filterable<T>
{
    public Comparator<T>[] getOrders();
}
