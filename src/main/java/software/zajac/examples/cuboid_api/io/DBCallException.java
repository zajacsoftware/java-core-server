package software.zajac.examples.cuboid_api.io;

public class DBCallException extends Exception {
    public DBCallException() {
    }

    public DBCallException(String message) {
        super (message);
    }

    public DBCallException(Throwable cause) {
        super (cause);
    }

    public DBCallException(String message, Throwable cause) {
        super (message, cause);
    }
}
