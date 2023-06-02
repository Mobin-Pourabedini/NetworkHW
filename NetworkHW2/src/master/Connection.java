package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Connection{
    private static List<Connection> workerConnections = new ArrayList<>();
    private ClientType clientType;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private int workerCapacity;
    private int workerId;
    private static int workerCount = 0;

    public Connection(Socket socket) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
        String data = in.readLine();
        if (data.equals("Worker")) {
            System.out.println("Worker connected!");
            this.clientType = ClientType.WORKER;
            this.workerId = workerCount + 1000;
            out.println(workerId);
            workerCount++;
            out.println("get capacity");
            workerCapacity = Integer.parseInt(in.readLine());
            workerConnections.add(this);
        } else if (data.equals("Client")) {
            System.out.println("Client connected!");
            this.clientType = ClientType.CLIENT;
        } else {
            System.out.println("Unknown connection!");
        }
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void send(String data) {
        out.println(data);
    }

    public String receive() throws IOException {
        return in.readLine();
    }

    public int getWorkerCapacity() {
        return workerCapacity;
    }

    public int getWorkerId() {
        return workerId;
    }

    public static Connection getWorkerById(String id) {
        for (Connection workerConnection : workerConnections) {
            if (String.valueOf(workerConnection.getWorkerId()).equals(id)) {
                return workerConnection;
            }
        }
        return null;
    }

    public static List<Connection> getWorkerConnections() {
        return workerConnections;
    }

    public void setWorkerCapacity(int workerCapacity) {
        this.workerCapacity = workerCapacity;
    }
}
