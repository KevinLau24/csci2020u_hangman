package hangman;

import javafx.scene.paint.Color;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

public class ClientThread extends Thread {
    protected Socket socket = null;
    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;
    public static int numGuesses = 0;
    public static String targetWord = "";
    public static String currentWord = "";
    public static ArrayList<String> guessedChar = null;

    protected int MAX_GUESSES = 6;

    public ClientThread(Socket socket, String targetWord, String currentWord, int numGuesses, ArrayList<String> guessedChar) {
        super();
        this.socket = socket;
        this.targetWord = targetWord;
        this.currentWord = currentWord;
        this.numGuesses = numGuesses;
        this.guessedChar = new ArrayList<>(guessedChar);
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("IOException while opening a read/write connection");
        }
    }

    public void run() {
        boolean endOfSession = false;
        try {
            while (!endOfSession) {
                 endOfSession = processCommand();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean processCommand() throws IOException {
        synchronized (this) {
            String command = null;
            try {
                command = dataInputStream.readUTF();
            } catch (IOException e) {
                System.err.println("Error reading command from socket");
                return true;
            }

            if (command.equalsIgnoreCase("GET GUESSED-CHAR")) {
                for (String s : guessedChar) {
                    dataOutputStream.writeUTF(s);
                }
                dataOutputStream.writeUTF("end()");
                return false;
            }

            if (command.equalsIgnoreCase("GET GUESSED-NUM")) {
                dataOutputStream.writeUTF(String.valueOf(numGuesses));
                return false;
            }

            if (command.equalsIgnoreCase("GET WORD")) {
                dataOutputStream.writeUTF(currentWord);
                return false;
            }

            if (command.equalsIgnoreCase("SEND GUESS")) {
                String guessedChar = dataInputStream.readUTF();
                if (targetWord.equalsIgnoreCase(currentWord)) {
                    dataOutputStream.writeUTF("CONGRATULATION!");
                    return false;
                }
                if (targetWord.equalsIgnoreCase(guessedChar)) {
                    currentWord = targetWord;
                    dataOutputStream.writeUTF("CONGRATULATION!");
                    return false;
                }
                this.guessedChar.add(guessedChar);
                if (targetWord.toLowerCase().contains(guessedChar)) {
                    String temp[] = targetWord.split("");
                    for (int i = 0; i < temp.length; i++) {
                        if (temp[i].equalsIgnoreCase(guessedChar)) {
                            System.out.println(i);
                            currentWord = currentWord.substring(0, i) + guessedChar + currentWord.substring(i+1);
                        }
                    }
                    dataOutputStream.writeUTF("CORRECT!");
                    return false;
                }
                numGuesses++;
                if (numGuesses >= MAX_GUESSES) {
                    dataOutputStream.writeUTF("OUT OF GUESSES!");
                    return false;
                }
                dataOutputStream.writeUTF("WRONG! Try again");
                return false;
            }

            if (command.equalsIgnoreCase("GET TARGET-WORD")) {
                dataOutputStream.writeUTF(targetWord);
                return true;
            }
        }
        return true;
    }
}
