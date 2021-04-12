package hangman;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    protected Socket clientSocket = null;
    protected ServerSocket serverSocket = null;
    protected ClientThread thread = null;
    protected ArrayList<ClientThread> threads = null;
    protected ArrayList<Game> games = null;
    protected int numClients = 0;

    public static int MAX_CLIENTS = 50;
    public static int SERVER_PORT = 6868;
    public static String SERVER_ADDRESS = null;

    public Server() {
        try {
            threads = new ArrayList<>();
            games = new ArrayList<>();
            InetAddress localhost = InetAddress.getLocalHost();
            SERVER_ADDRESS = localhost.getHostAddress();
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("---------------------------------------------");
            System.out.println("Hangman Game Server is running");
            System.out.println("---------------------------------------------");
            System.out.println("Server address: " + SERVER_ADDRESS);
            System.out.println("Listening to port: " + SERVER_PORT);
            System.out.println("---------------------------------------------");
            while (true) {
                if (numClients < MAX_CLIENTS) {
                    clientSocket = serverSocket.accept();
                    thread = new ClientThread(clientSocket, games);
                    threads.add(thread);
                    thread.start();
                    numClients++;
                }
                for (ClientThread thread : threads) {
                    if (!thread.isAlive()) {
                        threads.remove(thread);
                        numClients--;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
