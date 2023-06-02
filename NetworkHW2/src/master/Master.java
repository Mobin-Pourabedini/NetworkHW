package master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Master {
    public static void main(String[] args) throws IOException {
        System.out.println("Initializing Master...");
        ServerSocket serverSocket = new ServerSocket(8080);
        while(true) {
            Socket socket = serverSocket.accept();
            Connection connection = new Connection(socket);
            if (connection.getClientType() == ClientType.CLIENT) {
                MasterMenu menu = new MasterMenu(connection);
                menu.start();
            }
        }
    }
}
