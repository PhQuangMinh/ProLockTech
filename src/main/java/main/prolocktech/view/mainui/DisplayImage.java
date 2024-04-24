package main.prolocktech.view.mainui;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.prolocktech.controller.FirebaseImage;
import main.prolocktech.controller.ManagerPicture;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class DisplayImage {
    @FXML
    private Label nameUser, timePost;
    @FXML
    private ImageView delete, save, grid, avatar, imageDisplay;
    @FXML
    private AnchorPane pane;
    private User user;
    @FXML
    private Button up, down;
    private AtomicInteger index;
    private ArrayList<Picture> images;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void setClipImage(){
        Rectangle clip = new Rectangle();
        clip.setHeight(400);
        clip.setWidth(400);
        clip.setArcHeight(40);
        clip.setArcWidth(40);
        imageDisplay.setClip(clip);
    }

    private void setClipAvatar(){
        Rectangle clip = new Rectangle();
        clip.setHeight(40);
        clip.setWidth(40);
        clip.setArcHeight(40);
        clip.setArcWidth(40);
        avatar.setPreserveRatio(false);
        avatar.setClip(clip);
    }


    public void init(ArrayList<Picture> list, String start, User user){
        this.user = user;
        images = list;
        setSave();
        setDelete();
        setGrid();
        if (list.isEmpty()){
            setStatus(false);
            return;
        }
        setImageDisplay(start);
    }

    private void setImageDisplay(String start){
        setStatus(true);
        int begin = Integer.parseInt(start);
        delete.setVisible(images.get(begin).getUser().getIndex().equals(this.user.getIndex()));
        setClipImage();
        setClipAvatar();
        if (begin==0) up.setVisible(false);
        if (begin==images.size()-1) down.setVisible(false);
        index = new AtomicInteger(begin);
        setInformationPicture(images.get(begin));
        imageDisplay.setPreserveRatio(false);
        imageDisplay.setImage(images.get(begin).getImage());
        imageDisplay.setId(String.valueOf(begin));
        down.setOnAction(event -> makeButtonDown(images));
        up.setOnAction(event -> makeButtonUp(images));
    }
    private void setGrid(){
        grid.setOnMouseClicked(event -> makeGrid());
        Image house = new Image(Objects.requireNonNull(getClass().getResourceAsStream("house.png")));
        grid.setImage(house);
    }

    private void setStatus(boolean status){
        up.setVisible(status);
        down.setVisible(status);
        delete.setVisible(status);
        save.setVisible(status);
        grid.setVisible(status);
        avatar.setVisible(status);
        nameUser.setVisible(status);
        timePost.setVisible(status);
    }
    public void setDelete(){
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("delete.png")));
        delete.setImage(image);
        delete.setOnMouseClicked(event -> deleteImage());
    }

    public void setSave(){
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("save.png")));
        save.setImage(image);
        save.setOnMouseClicked(event -> saveImage());
    }

    private boolean getAlert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confimation");
        alert.setHeaderText("Are you sure you want to delete this image?");

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get().getButtonData() == ButtonBar.ButtonData.YES;
    }

    public void deleteImage(){
        if (getAlert()) {
            String pathFile = images.get(index.get()).getNameFile();
            FirebaseImage firebaseImage = new FirebaseImage();
            firebaseImage.deleteImage(pathFile);
            if (images.size()==1) {
                images.clear();
                setStatus(false);
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png")));
                imageDisplay.setImage(image);
                return;
            }
            if (index.get() < images.size()-1) {
                images.remove(index.get());
                for (int i = index.get(); i < images.size(); i++) {
                    images.get(i).setId(String.valueOf(i));
                }
            }
            else{
                images. remove(index.get());
                index.decrementAndGet();
            }
            setImageDisplay(String.valueOf(index.get()));
        }

    }
    public void saveImage(){
        Image image = imageDisplay.getImage();
        if (image != null){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image (*.png, *.jpg, *.jpeg, *.bmp, *.gif)", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"));
            Stage stage = (Stage) pane.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);
            if (file!=null){
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), getFileExtension(file), file);
                } catch (IOException e) {
                    System.out.println("Failed to write");
                }
            }
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "png";
    }
    public void setInformationPicture(Picture picture) {
        FirebaseImage firebaseImage = new FirebaseImage();
        avatar.setImage(firebaseImage.getAvatar(user));
        nameUser.setText(picture.getUser().getFirstName() + " " + picture.getUser().getLastName());
        ManagerPicture manager = new ManagerPicture();
        timePost.setText(manager.getTime(picture));
    }
    public void makeButtonDown(ArrayList<Picture> list) {
        up.setVisible(true);
        imageDisplay.setImage(list.get(index.incrementAndGet()).getImage());
        imageDisplay.setId(String.valueOf(index.get()));
        setInformationPicture(list.get(index.get()));
        delete.setVisible(list.get(index.get()).getUser().getIndex().equals(user.getIndex()));
        down.setVisible(index.get() < list.size() - 1);
    }
    public void makeButtonUp(ArrayList<Picture> list){
        down.setVisible(true);
        imageDisplay.setImage(list.get(index.decrementAndGet()).getImage());
        imageDisplay.setId(String.valueOf(index.get()));
        setInformationPicture(list.get(index.get()));
        delete.setVisible(list.get(index.get()).getUser().getIndex().equals(user.getIndex()));
        up.setVisible(index.get() > 0);
    }

    public void makeGrid(){
        FXMLLoader loader = new FXMLLoader(FullImage.class.getResource("FullImage.fxml"));
        try {
            Parent root = loader.load();
            FullImage fullImage = loader.getController();
            pane.getChildren().clear();
            pane.getChildren().add(root);
            fullImage.init(images, imageDisplay, user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
