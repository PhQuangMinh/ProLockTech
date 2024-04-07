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
import main.prolocktech.controller.FileImage;
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
        avatar.setClip(clip);
    }


    public void init(ArrayList<Picture> list, String start, User user){
        this.user = user;
        System.out.println(list.size() + "Anh");
//        try{
            images = list;
            setSave();
            setDelete();
            setGrid();
            if (images.isEmpty()){
                setStatus(false);
                return;
            }
            setStatus(true);
            int begin = Integer.parseInt(start);
            delete.setVisible(list.get(begin).getUser().getIndex().equals(this.user.getIndex()));
            setClipImage();
            setClipAvatar();
            if (begin==0) up.setVisible(false);
            if (begin==list.size()-1) down.setVisible(false);
            index = new AtomicInteger(begin);
            setInformationPicture(list.get(begin));
            imageDisplay.setImage(list.get(begin).getImage());
            imageDisplay.setId(String.valueOf(begin));
            down.setOnAction(event -> makeButtonDown(list));
            up.setOnAction(event -> makeButtonUp(list));
//        } catch (Exception e){
//            System.out.println("No image");
//        }
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

    public void deleteImage(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confimation");
        alert.setHeaderText("Are you sure you want to delete this image?");

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get().getButtonData() == ButtonBar.ButtonData.YES) {
            File file = images.get(index.get()).getFile();
            file.delete();
            if (index.get() < images.size()-1) {
                images.remove(index.get());
                imageDisplay.setImage(images.get(index.get()).getImage());
                for (int i = index.get(); i < images.size(); i++) {
                    images.get(i).setId(String.valueOf(i));
                }
            }
            else{
                images.remove(index.get());
                if (!images.isEmpty()) {
                    index.decrementAndGet();
                    imageDisplay.setImage(images.get(index.get()).getImage());
                }
                else{
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png")));
                    imageDisplay.setImage(image);
                }
            }
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
        FileImage fileImage = new FileImage();
        ArrayList<ImageView> list = fileImage.getImageFromFolder("information/imageuser/user" + picture.getUser().getIndex() + "/avatar");
        avatar.setImage(list.get(0).getImage());
        nameUser.setText(picture.getNameUser());
        ManagerPicture manager = new ManagerPicture();
        timePost.setText(manager.getTime(picture.getFile()));
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
