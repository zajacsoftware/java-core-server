package software.zajac.examples.cuboid_api.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import software.zajac.examples.cuboid_api.geom.Bounds;
import software.zajac.examples.cuboid_api.geom.Cuboid;
import software.zajac.utils.Json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Abstraction layer for DB I/Os
 */
public class DBAbstractionLayer {
    private static DBAbstractionLayer instance;
    public static DBAbstractionLayer GetInstance() {
        if(instance == null) instance = new DBAbstractionLayer();
        return instance;
    }

    private boolean synced = false;
    private List<Bounds> boundsCache;
    private List<String> keysCache;
    private Map<String, Cuboid> dataCache;


    private DBAbstractionLayer() {
        boundsCache = new ArrayList<>();
        keysCache = new ArrayList<>();

        try {
            System.out.println("Populating Cache from stored data");
            String data =  getAll();
            JsonNode node = Json.parse(data);
            JsonNode[] arr =  Json.fromJson(node, JsonNode[].class);
            Arrays.stream(arr).forEach((c)->{
                    try {
                        boundsCache.add(new Bounds(Json.fromJson(c.get("vertices"),int[].class)));
                        keysCache.add(c.get("id").textValue());
                    } catch (JsonProcessingException e) {
                       // ignore
                    }
                }
            );
            synced = true;
        } catch (IOException | DBCallException e) {
            //TODO: If cache not synced with DB Respond with "500 Out of sync" error for every API call. Add cache syncing re-try.
            synced = false;
        }
    }

    public boolean isSynced() {
        return this.synced;
    }

    /**
     * Allows to test if object with the same id has already been stored
     * @param newObjectId id of an object
     * @return true or false
     */
    public boolean containsKey(String newObjectId){
        return keysCache.contains(newObjectId);
    }

    /**
     * Allows to test if new object would collide with exising ones before storing it
     * @param vertices array of integers describing vertices of a new object
     * @return true or false
     */
    public boolean overlapTest(int[] vertices) {
        return boundsCache.parallelStream().anyMatch((bounds -> bounds.contains(vertices)));
    }

    /**
     * Adds new object to the DB and updates local cache
     * @param id id of a new object
     * @param vertices array of integers describing vertices of a new object
     * @param edges array of integers describing edges
     * @param faces array of integers describing faces
     * @throws DBCallException
     */
    public void addCuboid(String id, int[] vertices, int[] edges, int[] faces) throws DBCallException {
        Cuboid c = new Cuboid(id, vertices, edges, faces );
        AddCuboid op = new AddCuboid(c);
        Thread t = new Thread(op);
        t.start();
        try {
           t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String errorMessage = op.getErrorMessage();

        if(errorMessage != null) {
            throw new DBCallException(errorMessage);
        }
        // no errors, updating local cache;
        keysCache.add(id);
        boundsCache.add(new Bounds(vertices));
    }

    /**
     *
     * @return list of all stored Cuboids as JSON string
     * @throws DBCallException
     */
    public String getAll() throws DBCallException {

        GetAll op = new GetAll();
        Thread t = new Thread(op);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String error = op.getErrorMessage();
        if(error!=null){
            throw new DBCallException("Error fetching data: "+error);
        }

        return op.getResult();
    }
}
