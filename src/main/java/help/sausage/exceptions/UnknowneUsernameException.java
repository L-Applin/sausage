package help.sausage.exceptions;

public class UnknowneUsernameException extends RuntimeException {

    public UnknowneUsernameException() {
    }

    public UnknowneUsernameException(String message) {
        super(message);
    }

    public UnknowneUsernameException(String message, Throwable cause) {
        super(message, cause);
    }
}
