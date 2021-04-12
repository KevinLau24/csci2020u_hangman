package hangman;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    protected Socket clientSocket = null;
    protected ServerSocket serverSocket = null;
    //protected ClientThread[] threads = null;
    protected int numClients = 0;

    public static int SERVER_PORT = 8080;
    public static int MAX_CLIENTS = 25;

    public Server(){
        try{
            serverSocket = new ServerSocket(SERVER_PORT);
            //threads = new ClientThread[MAX_CLIENTS];
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            while(true){
                clientSocket = serverSocket.accept();
                //threads[numClients] = new ClientThread(clientSocket);
                //threads[numClients].start();
                //numClients++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Server server = new Server();
        server.run();
    }
}
