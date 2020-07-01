package software.zajac.jcore_server;

public class HeaderConstants {
    public final static String SP = " ";
    public final static String CL = ":";
    public final static String CRLF = "\r\n";
    public final static String PROTOCOL = "HTTP/1.1";
    public final static String CONTENT_TYPE = "Content-Type";
    public final static String DATE = "Date";
    public final static String CONTENT_LENGTH = "Content-length";
    public final static String CONTENT_LANGUAGE = "Content-Language";
    public final static String TYPE_TEXT = "text/plain";
    public final static String TYPE_JSON = "application/json; charset=utf-8";
    public final static String TYPE_FORM = "application/x-www-form-urlencoded";
    public final static String EN_US = "en-US";


    /**
     * HTTP response status
     */
    public static enum STATUS {
        OK, //200
        CREATED, //201
        BAD_REQUEST, //400
        NOT_FOUND, //404
        TIMEOUT, //408
        TOO_MANY_REQUESTS, //429
        SERVER_ERROR, //500
    }
}
