package help.sausage.utils;

public class Null {
    private Null() {}

    public static <T> T orElse(T item, T ifNull) {
        return item == null ? ifNull : item;
    }

}
