package sample;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Controller {

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        File logo = new File("logo.png");
        Image image = new Image(logo.toURI().toString());
        imageView.setImage(image);
    }

    @FXML
    public void play(){

    }

    @FXML
    public void exit(){
        System.exit(0);
    }
}

