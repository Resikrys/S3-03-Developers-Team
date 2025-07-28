package exception;

public class DecorationNotFoundException extends RuntimeException {
    public DecorationNotFoundException(String message) {
        super(message);
    }

    public DecorationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


}
