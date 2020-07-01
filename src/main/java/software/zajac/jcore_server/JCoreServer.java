package software.zajac.jcore_server;
import software.zajac.jcore_server.http.server.ServerListenerThread;
import software.zajac.jcore_server.routing.Router;

import java.io.IOException;

/**
 * JCoreServer Main
 */
public class JCoreServer
{
    private ServerListenerThread serverListenerThread;
    public JCoreServer(int port, Router router) throws IOException {
        serverListenerThread = new ServerListenerThread(port, router);
        System.out.println("JCoreServer created");
    }

    public void start(){
        serverListenerThread.start();
    }

    public void kill(){
        System.out.println("JCoreServer kill invoked");
        serverListenerThread.kill();
    }
}
