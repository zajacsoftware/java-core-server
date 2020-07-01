package software.zajac.jcore_server.http.server;

import software.zajac.jcore_server.HeaderConstants;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Creates HTTP/1.1 compliant response
 */
public class Response {


    /**
     * The default 500 response. Used in case of an unknown server error.
     */
    public static final Response DefaultServerError = new Response("500","Internal Server Error", new byte[0], HeaderConstants.TYPE_TEXT );

    /**
     * Creates an instance of Response. Defaults contentType to "text/plain"
     * @param status Response.STATUS
     * @param content data to be returned with the response
     * @return new instance of Response
     * @throws CreateResponseException
     */
    public static Response CreateResponse(HeaderConstants.STATUS status, String content) throws CreateResponseException {
        return CreateResponse(status, content, HeaderConstants.TYPE_TEXT);
    }

    /**
     * Creates an instance of Response.
     * @param status Response.STATUS
     * @param content data to be returned with the response
     * @param contentType defines content type tag in response header
     * @return new instance of Response
     * @throws CreateResponseException
     */
    private static Response CreateResponse(HeaderConstants.STATUS status, String content, String contentType) throws CreateResponseException {

        byte[] contentBytes;
        try {
            contentBytes = content.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new CreateResponseException(e);
        }

        switch (status) {
            case OK:
                return new Response("200","OK", contentBytes, contentType );
            case CREATED:
                return new Response("201","Created", contentBytes, contentType );
            case BAD_REQUEST:
                return new Response("400","Bad Request", contentBytes, contentType );
            case NOT_FOUND:
                return new Response("404","Not Found", contentBytes, contentType );
            case TIMEOUT:
                return new Response("408","Request Timeout", contentBytes, contentType );
            case TOO_MANY_REQUESTS:
                return new Response("429","Too Many Requests", contentBytes, contentType );
            case SERVER_ERROR:
                return new Response("500","Internal Server Error", contentBytes, contentType );
        }
        throw new CreateResponseException("Error creating Response");
    }

    /**
     * HTTP response data as byte array
     */
   private byte[] bytes;

   private Response(String statusCode, String reason, byte[] contentBytes, String contentType) {

       int contentLength = contentBytes.length;

       byte[] header = new StringBuilder()
               .append(HeaderConstants.PROTOCOL).append(HeaderConstants.CL).append(HeaderConstants.SP).append(statusCode).append(HeaderConstants.SP).append(reason)
               .append(HeaderConstants.CRLF)
               .append(HeaderConstants.CONTENT_TYPE).append(HeaderConstants.CL).append(HeaderConstants.SP).append(contentType)
               .append(HeaderConstants.CRLF)
               .append(HeaderConstants.DATE).append(HeaderConstants.CL).append(HeaderConstants.SP).append(new Date())
               .append(HeaderConstants.CRLF)
               .append(HeaderConstants.CONTENT_LENGTH).append(HeaderConstants.CL).append(contentLength)
               .append(HeaderConstants.CRLF)
               .append(HeaderConstants.CRLF)
               .toString().getBytes();
       int headerLength = header.length;

       this.bytes = new byte[ headerLength + contentLength];
       System.arraycopy(header, 0, this.bytes, 0, headerLength);
       System.arraycopy(contentBytes, 0, this.bytes , headerLength, contentLength);
   }

   public byte[] getBytes() {
       return this.bytes;
   }

}
