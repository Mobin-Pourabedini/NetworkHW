package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) throws IOException {
        System.out.println("Starting Client service...");
        Socket socket = new Socket("localhost", 8080);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
        System.out.println("Client connected to the Master :)");
        out.println("Client");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        while (!command.equals("exit")) {
            out.println(command);
            System.out.println(in.readLine());
            command = scanner.nextLine();
        }
        out.close();
        in.close();
        socket.close();
    }
}
