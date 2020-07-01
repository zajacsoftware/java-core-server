package software.zajac.examples.cuboid_api;

import software.zajac.jcore_server.JCoreServer;
import software.zajac.jcore_server.http.client.RequestExecutor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main( String[] args )  {
        try {
            JCoreServer server = new JCoreServer(8080, CuboidAPI.instance.router);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
