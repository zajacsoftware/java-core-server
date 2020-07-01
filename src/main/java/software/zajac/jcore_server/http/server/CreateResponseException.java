package software.zajac.jcore_server.http.server;

public class CreateResponseException extends Exception {
    public CreateResponseException () {
    }

    public CreateResponseException (String message) {
        super (message);
    }

    public CreateResponseException (Throwable cause) {
        super (cause);
    }

    public CreateResponseException (String message, Throwable cause) {
        super (message, cause);
    }
}
