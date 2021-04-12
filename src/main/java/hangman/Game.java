package hangman;

import com.github.dhiraj072.randomwordgenerator.RandomWordGenerator;

import java.util.ArrayList;

public class Game {
    private ArrayList<ClientThread> players = null;
    private int MAX_PLAYERS = 0;
    private boolean isStart = false;

    public Game(int maxPlayers) {
        MAX_PLAYERS = maxPlayers;
        players = new ArrayList<>();
    }

    public void start() {
        isStart = true;
        String randomWord = RandomWordGenerator.getRandomWord();
    }

    public void addPlayer(ClientThread newPlayer) {
        players.add(newPlayer);
    }
}
