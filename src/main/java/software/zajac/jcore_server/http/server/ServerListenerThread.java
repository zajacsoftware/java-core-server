package software.zajac.jcore_server.http.server;
import software.zajac.jcore_server.routing.Router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread {

    private int port;
    private ServerSocket serverSocket;
    private Router router;
    private boolean isAlive;
    public ServerListenerThread(int port, Router router) throws IOException {
        this.port = port;
        this.router = router;

        System.out.println( "ServerListenerThread ");
        serverSocket = new ServerSocket(this.port);
    }
    @Override
    public void run() {
        isAlive = true;
        try {
            while (isAlive && serverSocket.isBound() && !serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                RequestWorkerThread worker = new RequestWorkerThread(socket, router);
                worker.start();
            }
            System.out.println( "server stopped.");
        } catch (IOException e) {
            System.out.println( e.toString());
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
            // do nothing
          }
         }
    }

    public void kill(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isAlive = false;
    }
}
