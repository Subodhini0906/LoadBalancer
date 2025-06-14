package lb;

import java.io.*;
import java.net.*;

public class LoadBalancer {
    public static void main(String[] args) throws IOException {
        int port = 8088; // Load Balancer port
        String backendHost = "localhost";
        int backendPort = 8080; // Your backend server port

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Load Balancer running on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleRequest(clientSocket, backendHost, backendPort)).start();
        }
    }

    private static void handleRequest(Socket clientSocket, String backendHost, int backendPort) {
        try (
            Socket backendSocket = new Socket(backendHost, backendPort);
            InputStream clientIn = clientSocket.getInputStream();
            OutputStream clientOut = clientSocket.getOutputStream();
            InputStream backendIn = backendSocket.getInputStream();
            OutputStream backendOut = backendSocket.getOutputStream()
        ) {
            clientIn.transferTo(backendOut);
            backendOut.flush();
            backendIn.transferTo(clientOut);
            clientOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }
}
