package main.prolocktech.view.mainui;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.prolocktech.InitApp;
import main.prolocktech.controller.FirebaseImage;
import main.prolocktech.controller.FirebaseUser;
import main.prolocktech.controller.ManagerPicture;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
public class MainUI {
    @FXML
    private ImageView avatar, menu;
    @FXML
    private AnchorPane paneBlur, main, mainWork;
    @FXML
    private VBox slideBar;
    @FXML
    private Label nameAvatar;
    @FXML
    private JFXButton buttonHome, buttonAdd, buttonSearch, buttonProfile, buttonLogout, buttonMyImage, buttonPassword;
    private FXMLLoader loader;
    private User user;

    private ArrayList<Picture> pictures;
    private ArrayList<User> listUsers;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MainUI() {
    }

    public void setClip(){
        Rectangle clip = new Rectangle();
        clip.setHeight(110);
        clip.setWidth(110);
        clip.setArcHeight(110);
        clip.setArcWidth(110);
        avatar.setClip(clip);
    }

    private void makeSlideBar(){
        setClip();
        buttonHome.setOnMouseClicked(event -> {
            menu.setDisable(false);
            FirebaseImage firebaseImage = new FirebaseImage();
            ArrayList<Picture> pictures = firebaseImage.getPictures(listUsers);
            makeDisPlayImage(pictures, true);
        });
        buttonMyImage.setOnMouseClicked(event ->{
            menu.setDisable(false);
            FirebaseImage firebaseImage = new FirebaseImage();
            ArrayList<Picture> pictures = firebaseImage.getPictureUser(user);
            makeDisPlayImage(pictures, true);
        });
        buttonAdd.setOnMouseClicked(event ->{
            menu.setDisable(false);
            makeAdd();
        });
        buttonSearch.setOnMouseClicked(event ->{
            menu.setDisable(false);
            makeSearch(pictures);
        });
        buttonProfile.setOnMouseClicked(event ->{
            menu.setDisable(false);
            makeProfile(true, pictures);
        });
        buttonPassword.setOnMouseClicked(event ->{
            menu.setDisable(false);
            makePassword();
        });
        buttonLogout.setOnMouseClicked(event ->{
            menu.setDisable(false);
            makeLogout();
        });
    }

    private void init(Double fromValue, Double toValue, Double setX, Boolean check){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.05), paneBlur);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.play();
        if (check){
            fadeTransition.setOnFinished(event -> {
                paneBlur.setVisible(false);
            });
        }
        TranslateTransition translate = new TranslateTransition(Duration.seconds(0.05), slideBar);
        translate.setByX(setX);
        translate.play();
    }
    private void makePaneBlur(){
        paneBlur.setOnMouseClicked(event ->{
            menu.setDisable(false);
            init(0.5, 0.0, -500.0, true);
        });
    }
    public void setAvatar() {
        FirebaseImage firebaseImage = new FirebaseImage();
        avatar.setImage(firebaseImage.getAvatar(user));
        avatar.setPreserveRatio(false);
        String fullName = user.getFirstName() + " " + user.getLastName();
        nameAvatar.setStyle("-fx-font-weight: bold;");
        nameAvatar.setStyle("-fx-font-size: 15px;");
        nameAvatar.setText(fullName);
    }
    private void makeMenu(){
        menu.setOnMouseClicked(event ->{
            setAvatar();
            menu.setDisable(true);
            paneBlur.setVisible(true);
            init(0.0, 0.5, 500.0, false);
        });
    }

    public void init(ArrayList<User> listUsers, ArrayList<Picture> pictures){
        this.pictures = pictures;
        this.listUsers = listUsers;
        paneBlur.setVisible(false);
        makeMenu();
        makePaneBlur();
        makeSlideBar();
//        FirebaseImage firebaseImage = new FirebaseImage();
        makeDisPlayImage(pictures, false);
    }

    public void returnProfile(ArrayList<User> listUsers, ArrayList<Picture> pictures){
        init(listUsers, pictures);
        makeProfile(false, pictures);
    }

    private void setRoot(String pathFXML){
        try {
            loader = new FXMLLoader(getClass().getResource(pathFXML));
            Parent root = loader.load();
            mainWork.getChildren().clear();
            mainWork.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void makeDisPlayImage(ArrayList<Picture> list, boolean option) {
        if (option) init(0.5, 0.0, -500.0, true);
        setRoot("DisplayImage.fxml");
        DisplayImage displayImage = loader.getController();
        displayImage.setUser(user);
        ManagerPicture managerPicture = new ManagerPicture();
        list = managerPicture.getPictures(list);
        displayImage.init(list, "0", user);
        user = displayImage.getUser();
    }

    public void makeAdd() {
        init(0.5, 0.0, -500.0, true);
        setRoot("AddImage.fxml");
        AddImage addImage = loader.getController();
        addImage.setUser(user);
        addImage.process((Stage)mainWork.getScene().getWindow());
        user = addImage.getUser();
    }

    public void makeSearch(ArrayList<Picture> pictures) {
        init(0.5, 0.0, -500.0, true);
        setRoot("Search.fxml");
        Search search = loader.getController();
        Stage stage = (Stage)mainWork.getScene().getWindow();
        search.makeSearch(stage, user, listUsers, pictures);
    }

    public void makeProfile(boolean option, ArrayList<Picture> pictures){
        if (option) init(0.5, 0.0, -500.0, true);
        setRoot("Profile.fxml");
        Profile profile = loader.getController();
        profile.setUser(user);
        profile.process(user, listUsers, pictures);
        user = profile.getUser();
    }

    public void makePassword(){
        init(0.5, 0.0, -500.0, true);
        setRoot("Password.fxml");
        Password password = loader.getController();
        password.setUser(user);
        password.process(user);
        user = password.getUser();
        System.out.println(user.getEmail());
    }

    public void makeLogout(){
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(InitApp.class.getResource("InitApp.fxml")));
            Parent root = loader.load();
            main.getChildren().clear();
            main.getChildren().add(root);
            InitApp initApp = loader.getController();
            initApp.init(listUsers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
