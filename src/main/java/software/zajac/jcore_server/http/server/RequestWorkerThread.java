package software.zajac.jcore_server.http.server;

import software.zajac.jcore_server.routing.Router;
import java.net.Socket;

public class RequestWorkerThread extends Thread {
    private final Router router;
    private final Socket socket;
    public RequestWorkerThread(Socket socket, Router router) {
        this.socket = socket;
        this.router = router;
    }

    @Override
    public void run() {
        router.handleRequest(this.socket);
    }
}
