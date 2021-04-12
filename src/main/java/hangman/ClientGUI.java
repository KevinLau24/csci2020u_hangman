package hangman;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class ClientGUI extends Application {
    private double W_HEIGHT = 375, W_WIDTH = 400;
    private double GAME_HEIGHT = 575, GAME_WIDTH = 600;
    private Scene introScene, menuScene, gameScene;
    private Image logo = new Image("/images/logo.png");

    private Socket socket = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Intro scene
        Label ipAddressLb = new Label("Server's IP Address:");
        Label portLb = new Label("Server's Port:");
        Label connectStatusLb = new Label();

        TextField ipAddressTf = new TextField();
        TextField portTf = new TextField();

        Button connectBtn = new Button("Connect");

        VBox ipAddressVBox = new VBox(ipAddressLb, ipAddressTf);
        ipAddressVBox.setSpacing(5);
        VBox portVBox = new VBox(portLb, portTf);
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
            try {
                // socket = new Socket(ipAddressTf.getText(), Integer.parseInt(portTf.getText()));
                socket = new Socket("localhost", 6868);
                primaryStage.setScene(menuScene);
            } catch (IOException | NumberFormatException e) {
                connectStatusLb.setText("Can not connect to server");
                connectStatusLb.setTextFill(Color.RED);
                e.printStackTrace();
            }
        });


        // Menu scene
        ImageView logoImgV = new ImageView(logo);
        HBox logoHBox = new HBox(logoImgV);
        logoHBox.setAlignment(Pos.CENTER);

        Button playBtn = new Button("Play");
        playBtn.setPrefWidth(100);
        Button exitBtn = new Button("Exit");
        exitBtn.setPrefWidth(100);
        exitBtn.setOnAction(e -> {
            try {
                socket.close();
                Platform.exit();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

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


        // Game scene



        primaryStage.setTitle("Hangman");
        primaryStage.getIcons().add(logo);
        primaryStage.show();
    }


    public static void main(String[] args) { launch(args); }
}
