package main.prolocktech.view.mainui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class FullImage {
    @FXML
    public ImageView back;
    @FXML
    private AnchorPane pane;
    @FXML
    private FlowPane flowPane;
    @FXML
    private ScrollPane scrollPane;

    private User user;



    private ArrayList<Picture> images;


    public void init(ArrayList<Picture> list, ImageView originalImage, User user) {
        this.user = user;
        images = list;
        setListImage(list);
        flowPane.setVgap(0.5);
        flowPane.getChildren().addAll(list);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        try {
            scrollPane.setVvalue((double) (Integer.parseInt(originalImage.getId()) + 1) / list.size());
        }
        catch (Exception e) {
            System.out.println("No image");
        }
        setBack(originalImage);
    }

    public void setListImage(ArrayList<Picture> list) {
        for (int i=0;i<list.size();i++) {
            ImageView imageView = list.get(i);
            imageView.setFitWidth(178.5);
            imageView.setFitHeight(178.5);
            imageView.setId(String.valueOf(i));
            imageView.setOnMouseClicked(event -> zoomInEvent(imageView.getId()));
        }
    }

    public void setBack(ImageView originalImage){
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("house.png")));
        back.setImage(image);
        back.setOnMouseClicked(event -> zoomInEvent(originalImage.getId()));
    }

    public void zoomInEvent(String index){
        System.out.println(index);
        try {
            FXMLLoader loader = new FXMLLoader(DisplayImage.class.getResource("DisplayImage.fxml"));
            Parent root = loader.load();
            pane.getChildren().clear();
            pane.getChildren().setAll(root);
            DisplayImage displayImage = loader.getController();
            System.out.println(images.size() + " images loaded");
            displayImage.init(images, index, user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
