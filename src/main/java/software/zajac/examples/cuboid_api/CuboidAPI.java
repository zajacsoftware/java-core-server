package software.zajac.examples.cuboid_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import software.zajac.jcore_server.HeaderConstants;
import software.zajac.jcore_server.http.server.Response;
import software.zajac.examples.cuboid_api.io.DBAbstractionLayer;
import software.zajac.jcore_server.http.server.RequestMethod;
import software.zajac.examples.cuboid_api.io.DBCallException;
import software.zajac.jcore_server.routing.Route;
import software.zajac.jcore_server.routing.RouteHandler;
import software.zajac.jcore_server.routing.Router;
import software.zajac.utils.Json;

import java.io.IOException;

import java.util.Map;

public class CuboidAPI {

    //API call handler. Validates request geom, adds new cuboid to database, generates http response
    private final RouteHandler addShape = (Map<String, String> routeParam, String requestBody) -> {
        DBAbstractionLayer db = DBAbstractionLayer.GetInstance();
        try {
            final JsonNode node = Json.parse(requestBody);

            if(!node.has("id")) return Response.CreateResponse(HeaderConstants.STATUS.BAD_REQUEST, "Missing field: \"id\"");
            if(!node.has("vertices")) return Response.CreateResponse(HeaderConstants.STATUS.BAD_REQUEST, "Missing field \"vertices\"");
            String cubeId = node.get("id").asText();
            if(db.containsKey(cubeId)) return Response.CreateResponse(HeaderConstants.STATUS.BAD_REQUEST, "Id of \""+cubeId+"\" already exists");

            try{
                int[] vertices = Json.fromJson(node.get("vertices"), int[].class);
                if(vertices.length!=24) return Response.CreateResponse(HeaderConstants.STATUS.BAD_REQUEST, "Vertices array should contain exactly 24 integers.");

                int[] edges = Json.fromJson(node.get("edges"), int[].class);
                //TODO: if required, use edges in order to validate if the vertex geom describes cuboid;

                if(db.overlapTest(vertices)) return Response.CreateResponse(HeaderConstants.STATUS.BAD_REQUEST, "The object collides with another object(s) already stored.");

                int[] faces = Json.fromJson(node.get("faces"), int[].class);
                try {
                    // NOTE: holds this thread until  I/O operation is finished.
                    db.addCuboid(cubeId, vertices, edges, faces );

                    return Response.CreateResponse(HeaderConstants.STATUS.CREATED, "Object added.");
                }catch (DBCallException ex){
                    return Response.CreateResponse(HeaderConstants.STATUS.SERVER_ERROR, ex.getMessage());
                }

            }catch ( JsonProcessingException e) {
                return Response.CreateResponse(HeaderConstants.STATUS.BAD_REQUEST, "Invalid JSON: "+e.getMessage());
            }
        } catch (IOException e) {
            return Response.CreateResponse(HeaderConstants.STATUS.BAD_REQUEST, "Invalid JSON");
        }
    };

    // API call handler.
    private final RouteHandler getAllShapes = (Map<String, String> routeParam, String requestBody)->{
        DBAbstractionLayer db = DBAbstractionLayer.GetInstance();
        try{
            String data = db.getAll();
            return Response.CreateResponse(HeaderConstants.STATUS.OK, data);
        } catch (DBCallException ex){
            return Response.CreateResponse(HeaderConstants.STATUS.SERVER_ERROR, ex.getMessage());
        }
    };

     //API call handler. Get shape by id
    private final RouteHandler getShape = (Map<String, String> routeParam, String requestBody)->{
        return Response.CreateResponse(HeaderConstants.STATUS.OK, ""+Math.random()); // testing
    };

    // Setting up routing
    private final Route[] routes = new Route[]{
            new Route(RequestMethod.POST, "/api/:v/shapes/", addShape),
            new Route(RequestMethod.GET, "/api/:v/shapes/:id/", getShape),
            new Route(RequestMethod.GET, "/api/:v/shapes/", getAllShapes)
    };

    public final Router router = Router.CreateRouter(routes);

    public static final CuboidAPI instance = new CuboidAPI();

    private CuboidAPI(){}

}
