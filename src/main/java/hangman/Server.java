package hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    protected Socket clientSocket = null;
    protected ServerSocket serverSocket = null;
    protected ClientThread thread = null;
    protected ArrayList<ClientThread> threads = null;
    protected int numClient = 0;
    public static int numGuesses = 0;
    public String targetWord;
    public String currentWord;
    public static ArrayList<String> guessedChar = null;

    public static int MAX_CLIENTS = 2;
    protected int MAX_GUESSES = 6;
    public static int SERVER_PORT = 6868;
    public static String SERVER_ADDRESS = null;

    public Server() {
        try {
            threads = new ArrayList<>();
            guessedChar = new ArrayList<>();
            generateWord();
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
        try {
            URL netURL = new URL("https://random-word-api.herokuapp.com//word?number=1");
            URLConnection conn = netURL.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);
            InputStream inStream = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            String jsonData = buffer.toString();
            Pattern p = Pattern.compile("\"([^\"]*)\"");
            Matcher m = p.matcher(jsonData);
            while (m.find()) {
                targetWord = m.group(1);
            }
            currentWord = targetWord.replaceAll(".", "_");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
