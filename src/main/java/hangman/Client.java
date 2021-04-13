package hangman;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
    private Socket socket = null;
    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;

    private static String SERVER_ADDRESS = null;
    private static int SERVER_PORT;

    public Client (String serverAddress, int port) {
        try {
            SERVER_ADDRESS = serverAddress;
            SERVER_PORT = port;
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + SERVER_ADDRESS);
        } catch (IOException ex) {
            System.err.println("IOException while connecting to server: " + SERVER_ADDRESS);
        }
        if (socket == null) {
            System.err.println("Socket is null");
        }
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("IOException while opening a read/write connection");
        }
    }

    public String getWord() {
        String word = "";
        try {
            dataOutputStream.writeUTF("GET WORD");
            word = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return word;
        }
    }

    public int getNumGuesses() {
        int numGuesses = 0;
        try {
            dataOutputStream.writeUTF("GET GUESSED-NUM");
            numGuesses = Integer.parseInt(dataInputStream.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return numGuesses;
        }
    }

    public ArrayList<String> getGuessedChar() {
        ArrayList<String> temp = new ArrayList<>();
        try {
            dataOutputStream.writeUTF("GET GUESSED-CHAR");
            String message;
            while (true) {
                if (dataInputStream.available() > 0) {
                    message = dataInputStream.readUTF();
                    if (message.equalsIgnoreCase("end()")) {
                        break;
                    }
                    temp.add(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return temp;
        }
    }

    public String sendGuess(String guess) {
        String message = "";
        try {
            dataOutputStream.writeUTF("SEND GUESS");
            dataOutputStream.writeUTF(guess);
            message = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return message;
        }
    }

    public void closeClient() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
