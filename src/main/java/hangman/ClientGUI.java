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

import java.io.IOException;
import java.util.ArrayList;

public class ClientGUI extends Application {
    private double W_HEIGHT = 375, W_WIDTH = 400;
    private Scene introScene, menuScene, gameScene;
    private Image logo = new Image("/images/logo.png");

    private Label wordLb;
    private TextArea usedLettersTa;
    private Canvas hangmanCanvas;

    private Client client;
    private int numGuesses = 0;
    private ArrayList<String> guessedChar;

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
            client = new Client("localhost", 6868);

            // Game scene
            guessedChar = new ArrayList<>(client.getGuessedChar());
            numGuesses = client.getNumGuesses();

            wordLb = new Label();
            wordLb.setFont(Font.font("Arial", FontWeight.BOLD,15));
            Label usedLettersLb = new Label("Guessed Letters:");
            Label statusLb = new Label();

            TextField guessTf = new TextField();

            usedLettersTa = new TextArea();

            Button guessBtn = new Button("Guess");
            guessBtn.prefWidth(100);

            hangmanCanvas = new Canvas(200,200);

            HBox usedLettersHBox = new HBox(usedLettersLb);
            VBox usedLettersVBox = new VBox(usedLettersHBox, usedLettersTa);
            HBox hangmanHBox = new HBox(hangmanCanvas, usedLettersVBox);
            HBox guessesHBox = new HBox(guessTf, guessBtn);
            HBox wordHBox = new HBox(wordLb);
            HBox statusHBox = new HBox(statusLb);

            usedLettersTa.setEditable(false);
            usedLettersTa.setPrefWidth(175);

            usedLettersHBox.setAlignment(Pos.CENTER);
            usedLettersVBox.setSpacing(10);
            hangmanHBox.setAlignment(Pos.CENTER);
            hangmanHBox.setSpacing(10);
            guessesHBox.setAlignment(Pos.CENTER);
            guessesHBox.setSpacing(10);
            wordHBox.setAlignment(Pos.CENTER);
            statusHBox.setAlignment(Pos.CENTER);

            GridPane gameGrid = new GridPane();
            gameGrid.setAlignment(Pos.CENTER);
            gameGrid.setVgap(10);
            gameGrid.add(wordHBox,0,0);
            gameGrid.add(hangmanHBox, 0, 2);
            gameGrid.add(guessesHBox,0,4);
            gameGrid.add(statusHBox,0,5);

            gameScene = new Scene(gameGrid, W_WIDTH, W_HEIGHT);

            Thread update = new Thread(new Runnable() {
                @Override
                public void run() {
                    Runnable updater = new Runnable() {
                        @Override
                        public void run() {
                            wordLb.setText(client.getCurrentWord());
                            guessedChar = new ArrayList<>(client.getGuessedChar());
                            String usedLetters = "";
                            for (String s : guessedChar) {
                                usedLetters += s + "\n";
                            }
                            usedLettersTa.setText(usedLetters);
                            numGuesses = client.getNumGuesses();
                            drawHangman();
                        }
                    };
                    while (client.isWin().equalsIgnoreCase("CONTINUE")) {
                        Platform.runLater(updater);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                        }
                    }
                    end(client.isWin());
                }
            });
            update.start();

            guessBtn.setOnAction(e -> {
                try {
                    update.sleep(500);
                } catch (InterruptedException ex) {
                }
                String message = "";
                String guessWord = guessTf.getText();
                guessTf.clear();
                if (!isStringOnlyAlphabet(guessWord)) {
                    statusLb.setText("Invalid input! Try again");
                    statusLb.setTextFill(Color.RED);
                } else if (guessedChar.contains(guessWord)) {
                    statusLb.setText("Letter is already guessed! Try again");
                    statusLb.setTextFill(Color.RED);
                } else {
                    message = client.sendGuess(guessWord);
                    if (message.equalsIgnoreCase("DONE")) {
                        update.stop();
                        message = client.isWin();
                        end(message);
                        primaryStage.setScene(menuScene);
                    } else {
                        update();
                        if (message.equalsIgnoreCase("CORRECT!")) {
                            statusLb.setTextFill(Color.GREEN);
                        } else {
                            statusLb.setTextFill(Color.RED);
                        }
                        statusLb.setText(message);
                    }
                }
                update.interrupt();
            });


            primaryStage.setScene(gameScene);
        });


        primaryStage.setTitle("Hangman");
        primaryStage.getIcons().add(logo);
        primaryStage.show();
    }

    public static boolean isStringOnlyAlphabet(String str) {
        return ((str != null)
                && (!str.equals(""))
                && (str.matches("^[a-zA-Z]*$")));
    }

    private void update() {
        wordLb.setText(client.getCurrentWord());
        guessedChar = new ArrayList<>(client.getGuessedChar());
        String usedLetters = "";
        for (String s : guessedChar) {
            usedLetters += s + "\n";
        }
        usedLettersTa.setText(usedLetters);
        numGuesses = client.getNumGuesses();
        drawHangman();
    }

    private void end(String message) {
        update();
        wordLb.setText(client.getTargetWord());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Result");
        alert.setHeaderText(null);
        alert.setContentText(message + "\n" + "The word is " + client.getTargetWord());
        alert.showAndWait();
        client.closeClient();
    }

    //draws the hangman
    private void drawHangman() {
        GraphicsContext gc = hangmanCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.clearRect(0, 0, 200, 200);

        // Hangman Stand
        gc.setFill(Color.BLACK);
        gc.strokeLine(0,200,100,200);
        gc.strokeLine(50,0,50,200);
        gc.strokeLine(50,0,150,0);
        gc.strokeLine(150,0,150,20);

        int hangmanCount = 0;
        while (hangmanCount < numGuesses) {
            if (hangmanCount == 0) {
                gc.strokeOval(135,20,30,30); //head
            }
            else if (hangmanCount == 1) {
                gc.strokeLine(150,50,150,120); //body
            }
            else if (hangmanCount == 2) {
                gc.strokeLine(150,80,100,30); // left arm
            }
            else if (hangmanCount == 3) {
                gc.strokeLine(150, 80, 200, 30); // right arm
            }
            else if (hangmanCount == 4) {
                gc.strokeLine(150,120,100,175); //left leg
            }
            else {
                gc.strokeLine(150,120,200,175); //right leg
            }
            hangmanCount++;
        }
    }


    public static void main(String[] args) { launch(args); }
}
