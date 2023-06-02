package worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.regex.Matcher;

public class Worker {
    private static int capacity = 8;
    private static Socket socket;
    private static String id;
    private static PrintWriter out;
    private static BufferedReader in;
    private static List<String> taskIds = new java.util.ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Initializing worker...");
        socket = new Socket("localhost", 8080);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
        System.out.println("Worker started!");
        out.println("Worker");
        id = in.readLine();
        try {
            while (true) {
                String data = in.readLine();
                if (Commands.ADD_TASK.getMatcher(data).matches()) {
                    Matcher matcher = Commands.ADD_TASK.getMatcher(data);
                    matcher.matches();
                    String taskId = matcher.group("id");
                    taskIds.add(taskId);
                    System.out.println("Task " + taskId + " added!");
                }
                if (Commands.REMOVE_TASK.getMatcher(data).matches()) {
                    Matcher matcher = Commands.REMOVE_TASK.getMatcher(data);
                    matcher.matches();
                    String taskId = matcher.group("id");
                    taskIds.remove(taskId);
                    System.out.println("Task " + taskId + " removed!");
                }
                if (Commands.GET_CAP.getMatcher(data).matches()) {
                    out.println(capacity);
                }
            }
        } catch (IOException e) {
            System.out.println("Connection lost!");
        }
    }
}
