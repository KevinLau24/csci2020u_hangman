package hangman;

import com.github.dhiraj072.randomwordgenerator.RandomWordGenerator;

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
    protected int numClient = 0;
    public static int numGuesses = 0;
    public static String targetWord = "chicken";
    public static String currentWord = "_______";
    public static ArrayList<String> guessedChar = null;

    public static int MAX_CLIENTS = 2;
    protected int MAX_GUESSES = 6;
    public static int SERVER_PORT = 6868;
    public static String SERVER_ADDRESS = null;

    public Server() {
        try {
            threads = new ArrayList<>();
            guessedChar = new ArrayList<>();
            //generateWord();
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
                if (numClient < MAX_CLIENTS) {
                    clientSocket = serverSocket.accept();
                    thread = new ClientThread(clientSocket, targetWord, currentWord, numGuesses, guessedChar);
                    threads.add(thread);
                    thread.start();
                    numClient++;
                }
                for (ClientThread thread : threads) {
                    if (!thread.isAlive()) {
                        numClient--;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateWord() {
        //targetWord = RandomWordGenerator.getRandomWord();
        targetWord = "chicken";
        currentWord = targetWord.replaceAll(".", "_");
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
