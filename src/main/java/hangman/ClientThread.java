package hangman;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread {
    protected Socket socket = null;
    protected ArrayList<Game> games = null;
    protected Game currentGame = null;

    public ClientThread(Socket socket, ArrayList<Game> games) {
        super();
        this.socket = socket;
        this.games = new ArrayList<>(games);
    }

    public void run() {
        try {
            boolean endOfSession = false;
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
        return true;
    }
}
