package lb; 

import java.io.*;        //input/output streams 
import java.net.*;       //netwrk clses (socket,serversocket)
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancer{
    private static final List<InetSocketAddress> servers=List.of(new InetSocketAddress("localhost",8080),
    new InetSocketAddress("localhost",8081),new InetSocketAddress("localhost",8082));

    private static final AtomicInteger nextServerIndex=new AtomicInteger(0);
    private static final Map<InetSocketAddress,Boolean> serverHealth=new ConcurrentHashMap<>();
    private static final int HEALTH_CHECK_INTERVAL_MS=5000;
    public static void main(String[] args) throws IOException{               //bcoz networking can thrw exceptions
        int port=8088; //load balancer port(client send http requests to this port)
        ServerSocket serverSocket=new ServerSocket(port);       //opn srvr socket i.e. waits for incoming client connections
        System.out.println("Load Balancer running on port"+port);

        for(InetSocketAddress server:servers){
            serverHealth.put(server, true);
        }
        new Thread(()->{
            while(true){
                for(InetSocketAddress server:servers){
                    try(Socket socket=new Socket()){
                        socket.connect(server,100);
                        serverHealth.put(server, true);
                    }catch(IOException e){
                        serverHealth.put(server, false);
                    }
                }
                try {
                    Thread.sleep(HEALTH_CHECK_INTERVAL_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while(true){
            Socket clientSocket=serverSocket.accept();
            InetSocketAddress backend=getNextHealthyServer();
            if(backend!=null){
                new Thread(()->handleRequest(clientSocket, backend)).start();
            }else{
                PrintWriter out=new PrintWriter(clientSocket.getOutputStream(),true);
            }
        }
    }

    private static void handleRequest(Socket clientSocket, InetSocketAddress backend) {
        try (
            Socket backendSocket = new Socket(backend.getHostName(),backend.getPort());       //connects to backend server
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
