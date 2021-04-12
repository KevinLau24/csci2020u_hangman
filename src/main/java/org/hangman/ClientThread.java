package hangman;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientThread extends Thread {
    private Socket socket;
    private SocketAddress socketAddress;

    public ClientThread(Socket socket){
        this.socket = socket;
        socketAddress = this.socket.getLocalSocketAddress();
    }

    public void run(){
        System.out.println("Connected to server");

        try{
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
