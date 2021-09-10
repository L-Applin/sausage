package help.sausage.utils;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Null {
    private Null() {
        /* private constructor for static class only */
    }

    public static <T> T orElse (T item, T ifNull) {
        return item == null ? ifNull : item;
    }

    public static <E, T> T safe (E maybeNull, Function<E, T> f, T ifNull) {
        return maybeNull == null ? ifNull : f.apply(maybeNull);
    }

    public static <E, T> T safe (E maybeNull, Function<E, T> f, Supplier<T> ifNull) {
        return maybeNull == null ? ifNull.get() : f.apply(maybeNull);
    }

    public static <T> Stream<T> safe (Collection<T> collection) {
        return Null.<Collection<T>, Stream<T>>
                safe(collection, Collection::stream, Stream::empty);
    }

}
