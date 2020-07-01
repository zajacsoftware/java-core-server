package software.zajac.jcore_server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import software.zajac.jcore_server.http.client.RequestExecutor;
import software.zajac.jcore_server.http.server.RequestMethod;
import software.zajac.jcore_server.http.server.Response;
import software.zajac.jcore_server.routing.Route;
import software.zajac.jcore_server.routing.RouteHandler;
import software.zajac.jcore_server.routing.Router;

import java.io.Console;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

public class JCoreServerTest {

    // API call handler.
    private final RouteHandler testApiCallHandler = (Map<String, String> routeParam, String requestBody)->{
        return Response.CreateResponse(HeaderConstants.STATUS.OK, "works");
    };

    @Test
    public void testServer() {
        Route[] routes = new Route[]{
                new Route(RequestMethod.POST, "/", testApiCallHandler),
        };
        JCoreServer server = null;
        try {
            server = new JCoreServer(8080, Router.CreateRouter(routes));
            server.start();
            server.kill();
        } catch (IOException e) {
            fail("Expected occured: "+e.getMessage());
        }
    }
}