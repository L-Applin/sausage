package help.sausage.exceptions;

public class UnknownUsernameException extends RuntimeException {

    public UnknownUsernameException() {
    }

    public UnknownUsernameException(String message) {
        super(message);
    }

    public UnknownUsernameException(String message, Throwable cause) {
        super(message, cause);
    }
}
