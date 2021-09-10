package help.sausage.utils;

public class NotYetImplemented extends RuntimeException {

    public NotYetImplemented() {
    }

    public NotYetImplemented(String message) {
        super(message);
    }

    public static <T> T notYetImplemented() {
        throw new NotYetImplemented();
    }

    public static <T> T notYetImplemented(String msg) {
        throw new NotYetImplemented(msg);
    }

}
