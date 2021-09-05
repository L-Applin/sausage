package help.sausage.utils;

import java.util.function.Function;

public class Null {
    private Null() {}

    public static <T> T orElse(T item, T ifNull) {
        return item == null ? ifNull : item;
    }

    public static <E, T> T safe (E maybeNull, Function<E, T> f, T ifNull) {
        return maybeNull == null ? ifNull : f.apply(maybeNull);
    }

}
