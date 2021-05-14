package az.code.store;

public class IdGenerator {
    public static long id = 0;

    public static long getID() {
        return id++;
    }
}
