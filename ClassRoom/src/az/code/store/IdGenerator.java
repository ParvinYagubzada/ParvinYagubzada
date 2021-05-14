package az.code.store;

public class IdGenerator {
    private static long id = 1;

    public static long getID() {
        return id++;
    }
}
