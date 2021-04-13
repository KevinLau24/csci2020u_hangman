package hangman;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientGUI extends Application {
    private double W_HEIGHT = 375, W_WIDTH = 400;
    private Scene introScene, menuScene, gameScene;
    private Image logo = new Image("/images/logo.png");

    private static String SERVER_ADDRESS = null;
    private static int SERVER_PORT;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Intro scene
        Label ipAddressLb = new Label("Server's IP Address:");
        Label portLb = new Label("Server's Port:");
        Label connectStatusLb = new Label();

        TextField ipAddressTf = new TextField();
        TextField portTf = new TextField();

        Button connectBtn = new Button("Connect");

        HBox ipAddressHBox = new HBox(ipAddressLb);
        ipAddressHBox.setAlignment(Pos.CENTER);
        HBox portHBox = new HBox(portLb);
        portHBox.setAlignment(Pos.CENTER);
        VBox ipAddressVBox = new VBox(ipAddressHBox, ipAddressTf);
        ipAddressVBox.setSpacing(5);
        VBox portVBox = new VBox(portHBox, portTf);
        portVBox.setSpacing(5);
        HBox connectBtnHBox = new HBox(connectBtn);
        connectBtnHBox.setAlignment(Pos.CENTER);
        HBox connectSttLbHBox = new HBox(connectStatusLb);
        connectSttLbHBox.setAlignment(Pos.CENTER);

        GridPane introGrid = new GridPane();
        introGrid.setAlignment(Pos.CENTER);
        introGrid.setVgap(10);
        introGrid.add(ipAddressVBox,0,0);
        introGrid.add(portVBox,0,1);
        introGrid.add(connectBtnHBox,0,3);
        introGrid.add(connectSttLbHBox,0,2);

        introScene = new Scene(introGrid, W_WIDTH, W_HEIGHT);
        primaryStage.setScene(introScene);

        connectBtn.setOnAction(event -> {
            primaryStage.setScene(menuScene);
        });


        // Menu scene
        ImageView logoImgV = new ImageView(logo);
        HBox logoHBox = new HBox(logoImgV);
        logoHBox.setAlignment(Pos.CENTER);

        Button playBtn = new Button("Play");
        playBtn.setPrefWidth(100);

        Button exitBtn = new Button("Exit");
        exitBtn.setPrefWidth(100);
        exitBtn.setOnAction(e -> Platform.exit());

        VBox menuBtnVBox = new VBox();
        menuBtnVBox.getChildren().addAll(playBtn, exitBtn);
        menuBtnVBox.setAlignment(Pos.CENTER);
        menuBtnVBox.setSpacing(10);

        GridPane menuGrid = new GridPane();
        menuGrid.setAlignment(Pos.CENTER);
        menuGrid.setVgap(10);
        menuGrid.add(logoHBox,0,0);
        menuGrid.add(menuBtnVBox,0,1);

        menuScene = new Scene(menuGrid, W_WIDTH, W_HEIGHT);

        playBtn.setOnAction(event -> {
            // SERVER_ADDRESS = ipAddressTf.getText();
            // SERVER_PORT = Integer.parseInt(portTf.getText());
            Client client = new Client("localhost", 6868);

            // Game scene
            String currentWord = client.getWord();
            ArrayList<String> guessedChar = new ArrayList<>(client.getGuessedChar());
            int numGuesses = client.getNumGuesses();
            int MAX_GUESSES = 6;

            Label wordLbl = new Label(currentWord);
            wordLbl.setFont(Font.font("Arial", FontWeight.BOLD,15));
            Label usedLettersLb = new Label("Guessed Letters:");

            TextField guessTf = new TextField();

            TextArea usedLettersTa = new TextArea();

            Button guessBtn = new Button("Guess");
            guessBtn.prefWidth(100);

            Canvas hangmanCanvas = new Canvas(200,200);
            GraphicsContext gc = hangmanCanvas.getGraphicsContext2D();

            HBox usedLettersHBox = new HBox(usedLettersLb);
            VBox usedLettersVBox = new VBox(usedLettersHBox, usedLettersTa);
            HBox hangmanHBox = new HBox(hangmanCanvas, usedLettersVBox);
            HBox guessesHBox = new HBox(guessTf, guessBtn);
            HBox wordHBox = new HBox(wordLbl);

            usedLettersTa.setEditable(false);
            usedLettersTa.setPrefWidth(175);

            usedLettersHBox.setAlignment(Pos.CENTER);
            usedLettersVBox.setSpacing(10);
            hangmanHBox.setAlignment(Pos.CENTER);
            hangmanHBox.setSpacing(10);
            guessesHBox.setAlignment(Pos.CENTER);
            guessesHBox.setSpacing(10);
            wordHBox.setAlignment(Pos.CENTER);

            // Hangman Stand
            gc.setFill(Color.BLACK);
            gc.strokeLine(0,200,100,200);
            gc.strokeLine(50,0,50,200);
            gc.strokeLine(50,0,150,0);
            gc.strokeLine(150,0,150,20);

            // Drawing human test
            gc.setFill(Color.BLACK);
            gc.strokeOval(135,20,30,30); //head
            gc.strokeLine(150,50,150,120); //body
            gc.strokeLine(150,80,100,30); // left arm
            gc.strokeLine(150,80,200,30); // right arm
            gc.strokeLine(150,120,100,175); //left leg
            gc.strokeLine(150,120,200,175); //right leg

            GridPane gameGrid = new GridPane();
            gameGrid.setAlignment(Pos.CENTER);
            gameGrid.setVgap(10);
            gameGrid.add(wordHBox,0,0);
            gameGrid.add(hangmanHBox, 0, 2);
            gameGrid.add(guessesHBox,0,4);

            gameScene = new Scene(gameGrid, W_WIDTH, W_HEIGHT);

            guessBtn.setOnAction(e -> {
                String message = client.sendGuess(guessTf.getText());
                usedLettersTa.appendText(guessTf.getText() + '\n');


                guessTf.clear();


                if (message.equalsIgnoreCase("CONGRATULATION") | message.equalsIgnoreCase("OUT OF GUESSES")) {
                    System.out.println(message);
                    client.closeClient();
                    primaryStage.setScene(menuScene);
                }
                if (message.equalsIgnoreCase("CONTINUE")) {
                    wordLbl.setText(client.getWord());
                    System.out.println(client.getGuessedChar());
                    System.out.println(client.getNumGuesses());
                }
            });

            primaryStage.setScene(gameScene);
        });




        primaryStage.setTitle("Hangman");
        primaryStage.getIcons().add(logo);
        primaryStage.show();
    }


    public static void main(String[] args) { launch(args); }
}
