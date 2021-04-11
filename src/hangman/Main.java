package hangman;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    double menuWindowHeight = 375, menuWindowWidth = 400;
    double gameWindowHeight = 575, gameWindowWidth = 600;
    Scene menuScene, hostScene, playScene, gameScene;
    Image image = new Image(getClass().getClassLoader().getResource("logo.png").toString());


    @Override
    public void start(Stage primaryStage) throws Exception{
        // Menu scene
        ImageView logo = new ImageView(image);
        HBox hBoxLogo = new HBox(logo);
        hBoxLogo.setAlignment(Pos.CENTER);

        Button hostBtn = new Button("Host Game");
        hostBtn.setPrefWidth(100);
        hostBtn.setOnAction(e -> primaryStage.setScene(hostScene));
        Button findBtn = new Button("Find Game");
        findBtn.setPrefWidth(100);
        findBtn.setOnAction(e -> primaryStage.setScene(playScene));
        Button exitBtn = new Button("Exit Game");
        exitBtn.setPrefWidth(100);
        exitBtn.setOnAction(e -> Platform.exit());

        VBox vBoxBtn = new VBox();
        vBoxBtn.getChildren().addAll(hostBtn, findBtn, exitBtn);
        vBoxBtn.setAlignment(Pos.CENTER);
        vBoxBtn.setSpacing(10);

        GridPane menuGrid = new GridPane();
        menuGrid.setAlignment(Pos.TOP_CENTER);
        menuGrid.setVgap(10);
        menuGrid.add(hBoxLogo,0,0);
        menuGrid.add(vBoxBtn,0,1);

        menuScene = new Scene(menuGrid,menuWindowWidth,menuWindowHeight);
        primaryStage.setScene(menuScene);


        // Host scene
        GridPane hostGrid = new GridPane();
        hostGrid.setAlignment(Pos.TOP_CENTER);
        hostGrid.setVgap(10);
        hostGrid.setPadding(new Insets(10,10,10,10));

        Label ipLb1 = new Label("IP Address: ");
        Label portLb1 = new Label("Port: ");
        Label statusLb1 = new Label();

        TextField ipTf1 = new TextField();
        ipTf1.setPromptText("IP Address");
        TextField portTf1 = new TextField();
        portTf1.setPromptText("Numbers only");

        Button backBtn1 = new Button("Back");
        backBtn1.setPrefWidth(50);
        Button startBtn = new Button("Start");
        startBtn.setPrefWidth(50);
        Button stopBtn = new Button("Stop");
        stopBtn.setPrefWidth(50);
        stopBtn.setDisable(true);

        HBox hBoxServerBtn = new HBox(startBtn, stopBtn);
        hBoxServerBtn.setAlignment(Pos.CENTER);
        hBoxServerBtn.setSpacing(10);
        HBox hBoxBackBtn1 = new HBox(backBtn1);
        hBoxBackBtn1.setPrefWidth(menuWindowWidth);
        HBox hBoxStatusLb1 = new HBox(statusLb1);
        hBoxStatusLb1.setAlignment(Pos.CENTER);

        GridPane innerGrid1 = new GridPane();
        innerGrid1.setAlignment(Pos.TOP_CENTER);
        innerGrid1.setHgap(10);
        innerGrid1.setVgap(10);
        innerGrid1.add(ipLb1,0,0);
        innerGrid1.add(ipTf1,1,0);
        innerGrid1.add(portLb1,0,1);
        innerGrid1.add(portTf1, 1, 1);
        innerGrid1.add(hBoxServerBtn,0,2,2,1);
        innerGrid1.add(hBoxStatusLb1,0,3,2,1);

        hostGrid.add(hBoxBackBtn1,0,0);
        hostGrid.add(innerGrid1,0,1);

        hostScene = new Scene(hostGrid, menuWindowWidth, menuWindowHeight);

        startBtn.setOnAction(e -> {
            statusLb1.setText("Server is running");
            backBtn1.setDisable(true);
            startBtn.setDisable(true);
            stopBtn.setDisable(false);
            ipTf1.setEditable(false);
            portTf1.setEditable(false);

            stopBtn.setOnAction(e1 -> {
                statusLb1.setText("Server stopped");
                backBtn1.setDisable(false);
                startBtn.setDisable(false);
                stopBtn.setDisable(true);
            });
        });

        backBtn1.setOnAction(e -> {
            ipTf1.clear();
            portTf1.clear();
            statusLb1.setText("");
            primaryStage.setScene(menuScene);
        });


        // Play Scene
        GridPane playGrid = new GridPane();
        playGrid.setAlignment(Pos.TOP_CENTER);
        playGrid.setVgap(10);
        playGrid.setPadding(new Insets(10,10,10,10));

        Label ipLb2 = new Label("IP Address: ");
        Label portLb2 = new Label("Port: ");
        Label statusLb2 = new Label();

        TextField ipTf2 = new TextField();
        ipTf2.setPromptText("IP Address");
        TextField portTf2 = new TextField();
        portTf2.setPromptText("Numbers only");

        Button backBtn2 = new Button("Back");
        backBtn2.setPrefWidth(50);
        Button playBtn = new Button("Play");
        startBtn.setPrefWidth(50);

        HBox hBoxPlayBtn = new HBox(playBtn);
        hBoxPlayBtn.setAlignment(Pos.CENTER);
        HBox hBoxBackBtn2 = new HBox(backBtn2);
        hBoxBackBtn2.setPrefWidth(menuWindowWidth);
        HBox hBoxStatusLb2 = new HBox(statusLb2);
        hBoxStatusLb2.setAlignment(Pos.CENTER);

        GridPane innerGrid2 = new GridPane();
        innerGrid2.setAlignment(Pos.TOP_CENTER);
        innerGrid2.setHgap(10);
        innerGrid2.setVgap(10);
        innerGrid2.add(ipLb2,0,0);
        innerGrid2.add(ipTf2,1,0);
        innerGrid2.add(portLb2,0,1);
        innerGrid2.add(portTf2, 1, 1);
        innerGrid2.add(hBoxPlayBtn,0,2,2,1);
        innerGrid2.add(hBoxStatusLb2,0,3,2,1);

        playGrid.add(hBoxBackBtn2,0,0);
        playGrid.add(innerGrid2,0,1);

        playScene = new Scene(playGrid, menuWindowWidth, menuWindowHeight);

        backBtn2.setOnAction(e -> {
            ipTf2.clear();
            portTf2.clear();
            statusLb2.setText("");
            primaryStage.setScene(menuScene);
        });


        primaryStage.setTitle("Hangman");
        primaryStage.getIcons().add(image);
        primaryStage.show();
    }


    public static void main(String[] args) { launch(args); }
}
