package help.sausage.utils;

public class NotYetImplemented extends RuntimeException {

    public static <T> T notYetImplemented() {
        throw new NotYetImplemented();
    }
}
