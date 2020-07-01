package software.zajac.jcore_server.routing;
import software.zajac.jcore_server.HeaderConstants;
import software.zajac.jcore_server.http.server.CreateResponseException;
import software.zajac.jcore_server.http.server.Response;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Handles HTTP requests
 */
public class Router {

    private static Router instance;
    public static Router CreateRouter(Route[] routes){
        if(instance == null) instance = new Router(routes);
        return instance;
    }
    /**
     * Group Routes by HTTP method
     */
    private final Map<String, Route[]> routingMap;

    private Router(Route[] routes)  {
        Map<String, List<Route>> tempMap = new HashMap<>();
        List<String> tempList = new ArrayList<String>();
        for (Route route : routes) {
           //  Checking if this route already exists
            String testString = route.method+" "+route.pattern.toString();
            if(tempList.contains(testString)) {
                try {
                    throw new Exception("Route duplicate: "+testString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tempList.add(testString);

            // Grouping routes by the HTTP method
            String key = route.method.toString();
            if (!tempMap.containsKey(key)) {
                ArrayList<Route> list = new ArrayList<Route>();
                list.add(route);
                tempMap.put(key, list);
            } else {
                tempMap.get(key).add(route);
            }
        }

        routingMap = new HashMap<String, Route[]>() {{
            tempMap.forEach((methodName,routeList) -> {put(methodName, routeList.toArray(new Route[routeList.size()]));
            });
        }};
    }

    /**
     * Extracts data from request and passes it to Api
     * @param socket  socket connection.
     */
    public void handleRequest(Socket socket) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            InputStreamReader isReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isReader);

            String headerLine = br.readLine();

            if(headerLine==null) throw new IOException("Invalid request: empty header");

            String protocol = headerLine.substring(headerLine.lastIndexOf(" "));
            //TODO: add protocol validation here;

            String method = headerLine.substring(0, headerLine.indexOf(" "));

            String url = headerLine.substring(headerLine.indexOf(" ") + 1, headerLine.lastIndexOf(" "));
            int queryStart = url.indexOf("?");
            String path = queryStart>=0?url.substring(0,queryStart):url; // TODO: if required, handle query string here

            try {
                if (routingMap.containsKey(method)) {
                    Route[] rs = routingMap.get(method);
                    Optional<Route> matchedRoute = Arrays.stream(rs).filter(route ->  route.testPath(path)).findFirst();

                    if (matchedRoute.isPresent()) {
                        // Skipping rest of the header;
                        // TODO: validate content type and encoding here.
                        while ((headerLine = br.readLine()).length() != 0) {
                            // System.out.println(headerLine);
                        }

                        //Reading request body;
                        StringBuilder payload = new StringBuilder();
                        while (br.ready()) {
                            payload.append((char) br.read());
                        }
                        // Executing matched route with request body.
                        Response response = matchedRoute.get().execute(payload.toString());
                        outputStream.write(response.getBytes());

                    } else {
                        Response response = Response.CreateResponse(HeaderConstants.STATUS.NOT_FOUND, "Route not found");
                        outputStream.write(response.getBytes());
                    }
                }
            }catch (CreateResponseException e) {
                // Unknown error, responding with generic 500;
                outputStream.write( Response.DefaultServerError.getBytes());
            }
        }  catch ( IOException  e) {
            // Unable to respond.
             // System.out.println(e.toString());
        } finally {
            if(outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
            try {
                socket.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }
}
