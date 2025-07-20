package exception;

public class DecorationNotFoundException extends RuntimeException {
    // Constructor que recibe solo un mensaje
    public DecorationNotFoundException(String message) {
        super(message); // Llama al constructor de SQLException
    }

    // Constructor que recibe un mensaje y una causa (otra excepción)
    public DecorationNotFoundException(String message, Throwable cause) {
        super(message, cause); // Llama al constructor de SQLException con causa
    }


}
