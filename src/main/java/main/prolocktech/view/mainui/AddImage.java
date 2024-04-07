package main.prolocktech.view.mainui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.prolocktech.controller.FileImage;
import main.prolocktech.model.User;

import java.io.File;
import java.util.Objects;

public class AddImage{
    private User user;
    @FXML
    private Button button;
    @FXML
    private ImageView imageView;
    @FXML
    private Label inform;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void process(Stage stage) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png")));
        imageView.setImage(image);
        button.setOnAction(event -> {
            makeImageAdd(stage);
        });
    }

    public void setClip(){
        Rectangle clip = new Rectangle();
        clip.setHeight(400);
        clip.setWidth(400);
        clip.setArcHeight(40);
        clip.setArcWidth(40);
        imageView.setClip(clip);
    }
    private void makeImageAdd(Stage stage) {
        setClip();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(stage);
        if (file==null) {
            inform.setTextFill(Color.RED);
            inform.setText("Failed to add image");
            return;
        }
        inform.setTextFill(Color.GREEN);
        inform.setText("Successfully added image");
        Image image = new Image(file.toURI().toString(), 300, 300, false, true);
        imageView.setPreserveRatio(true);
        imageView.setImage(image);
        FileImage fileImage = new FileImage();
        fileImage.writeImage(file.toURI().toString(), image, user);
    }
}
