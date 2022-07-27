import network.MySocketHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    static ServerSocket serverSocket;
    public static final int SERVER_PORT = 1993;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        System.out.println("Listening on port "+SERVER_PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New connection made");
            MySocketHandler handler = new MySocketHandler(socket);
            handler.start();
        }
    }

}
