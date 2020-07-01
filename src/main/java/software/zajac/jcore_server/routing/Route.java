package software.zajac.jcore_server.routing;

import software.zajac.jcore_server.http.server.CreateResponseException;
import software.zajac.jcore_server.http.server.RequestMethod;
import software.zajac.jcore_server.http.server.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Binds route to route handler.
 */
public class Route {

    private static final Pattern ROUTE_PARAMS_PATTERN = Pattern.compile("(?:/)(:(.*?))(?:/|$)");

    public final RequestMethod method;
    public final Pattern pattern;
    public final String[] routeParamsNames;
    public final RouteHandler handler;
    private Map<String, String> routeParams = null;
    private Map<String, String> requestParams = null;
    public Route(RequestMethod method, String route, RouteHandler handler) {
        this.method = method;
        this.handler = handler;

        Matcher m = ROUTE_PARAMS_PATTERN.matcher(route);
        List<String> paramsList = new ArrayList<String>();
        StringBuilder routePatternBuilder = new StringBuilder("^");
        routePatternBuilder.append(route);
        while (m.find()){
            String paramName = m.group(2);
            String toReplace = m.group(1);
            // Injecting named group for extracting route parameters by name.
            String pattern = "(?<"+paramName+">[^/]+?)";
            int from = routePatternBuilder.indexOf(toReplace);
            routePatternBuilder.replace(from, from+toReplace.length(), pattern);
            paramsList.add(paramName);
        }

        int lastCharIndex =  routePatternBuilder.length()-1;

        // Making trailing slash optional.
        if(routePatternBuilder.lastIndexOf("/") == lastCharIndex)
        {
              routePatternBuilder.append("?");
        }else {
               routePatternBuilder.append("/?");
        }

        routePatternBuilder.append("$");

        this.pattern = Pattern.compile( routePatternBuilder.toString());
        this.routeParamsNames = paramsList.toArray(new String[paramsList.size()]);
    }

    /**
     * Tests request path against route pattern.
     * Extracts the route parameters.
     *
     * @param path  the path value extracted from request header
     */
    public boolean testPath(String path) {
        Matcher matcher = pattern.matcher(path);
        int matches = 0;
        while (matcher.find()) {
            matches++;
            routeParams = new HashMap<>();
            for (String paramName : routeParamsNames){
                routeParams.put(paramName, matcher.group(paramName));
             //   System.out.println(paramName+" : "+matcher.group(paramName));
            }
        }
        return (matches==1);
    }

    /**
     * Invokes route handler
     * @param requestBody data extracted from request's body
     * @return Response
     * @throws CreateResponseException
     */
    public Response execute(String requestBody) throws CreateResponseException {
        return handler.execute(this.routeParams, requestBody);
    }

    public Map<String, String> getRoutParams(){
        return this.routeParams;
    }

    public Map<String, String> getRequestParams(){
        return this.routeParams;
    }

}
