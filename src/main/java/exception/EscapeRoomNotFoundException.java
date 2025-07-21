package exception;

import java.sql.SQLException;

public class EscapeRoomNotFoundException extends SQLException {
    public EscapeRoomNotFoundException(String message) {
        super(message);
    }

    public EscapeRoomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
