package software.zajac.jcore_server.routing;
import software.zajac.jcore_server.http.server.CreateResponseException;
import software.zajac.jcore_server.http.server.Response;
import java.util.Map;

/**
 *
 */
public interface RouteHandler {
    /**
     *
     * @param routeParams hash map of route parameter names and values
     * @param requestBody data extracted from request body
     * @return Response object
     * @throws CreateResponseException
     */
    Response execute(Map<String, String> routeParams, String requestBody) throws CreateResponseException;
}
