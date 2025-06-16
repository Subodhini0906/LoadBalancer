package lb; 

import java.io.*;        //input/output streams 
import java.net.*;       //netwrk clses (socket,serversocket)

public class LoadBalancer{
    public static void main(String[] args) throws IOException{               //bcoz networking can thrw exceptions
        int port=8088; //load balancer port(client send http requests to this port)
        String backendHost="localhost";
        int backendPort=8080;    //backnd server port

        ServerSocket serverSocket=new ServerSocket(port);       //opn srvr socket i.e. waits for incoming client connections
        System.out.println("Load Balancer running on port"+port);

        while(true){
            Socket clientSocket=serverSocket.accept();
            new Thread(()-> handleRequest(clientSocket,backendHost,backendPort)).start();
        }
    }

    private static void handleRequest(Socket clientSocket, String backendHost, int backendPort) {
        try (
            Socket backendSocket = new Socket(backendHost, backendPort);       //connects to backend server
            InputStream clientIn = clientSocket.getInputStream();              //read cient request
            OutputStream clientOut = clientSocket.getOutputStream();           //send response
            InputStream backendIn = backendSocket.getInputStream();            //read from backend
            OutputStream backendOut = backendSocket.getOutputStream()          //sends to bcknd
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
