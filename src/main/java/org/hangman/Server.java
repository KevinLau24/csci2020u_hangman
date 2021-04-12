package hangman;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private Socket clientSocket = null;
    private ServerSocket serverSocket = null;
    private hangman.ClientThread[] threads = null;
    private int numClients = 0;

    public int serverPort;
    public int maxClients = 4;

    public Server(){
        try{
            serverSocket = new ServerSocket(serverPort);
            threads = new hangman.ClientThread[maxClients];
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            while (true){
                clientSocket = serverSocket.accept();
                threads[numClients] = new hangman.ClientThread(clientSocket);
                threads[numClients].start();
                numClients++;
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
