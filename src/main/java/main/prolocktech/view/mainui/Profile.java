package main.prolocktech.view.mainui;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.prolocktech.controller.FirebaseImage;
import main.prolocktech.controller.FirebaseUser;
import main.prolocktech.model.DateOfBirth;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;
import main.prolocktech.view.forgotpassword.CodeOTP;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Profile{
    @FXML
    private TextField firstName, lastName, email;

    @FXML
    private ImageView imageView;

    @FXML
    private JFXButton editProfile, updateProfile, cancel;

    @FXML
    private ComboBox<String> day, month, year;

    @FXML
    private Label inform;

    @FXML
    private AnchorPane pane;
    private File file;
    private User user;

    private ArrayList<User> listUsers;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setClip(){
        Rectangle clip = new Rectangle();
        clip.setHeight(150);
        clip.setWidth(150);
        clip.setArcHeight(150);
        clip.setArcWidth(150);
        imageView.setClip(clip);
    }
    public void process(User user, ArrayList<User> listUsers, ArrayList<Picture> pictures){
        this.listUsers = listUsers;
        this.user = user;
        setClip();
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        setInformation(false);
        setComboBox();
        setButton(false);
        setAvatar();
        inform.setVisible(false);
        editProfile.setOnAction(event -> editProfileEvent());
        updateProfile.setOnAction(event -> updateProfileEvent(pictures));
        cancel.setOnAction(event -> cancelEvent());
    }


    private void setButton(boolean option){
        editProfile.setVisible(!option);
        updateProfile.setVisible(option);
        cancel.setVisible(option);
        imageView.setDisable(!option);
        day.setDisable(!option);
        month.setDisable(!option);
        year.setDisable(!option);
    }

    private void comboBox(int begin, int end, ComboBox<String> comboBox, String text){
        ArrayList<String> list = new ArrayList<>();
        if (text.equals("Year")){
            for (int i = end; i >= begin; i--) {
                list.add(String.valueOf(i));
            }
        }
        else {
            for (int i = begin; i <= end; i++) {
                list.add(String.valueOf(i));
            }
        }
        ObservableList<String> observableList = FXCollections.observableArrayList(list);
        comboBox.setItems(observableList);
    }
    private void setComboBox(){
        comboBox(1, 31, day, "Day");
        day.setPromptText(user.getDate().getDay());
        comboBox(1, 12, month, "Month");
        month.setPromptText(user.getDate().getMonth());
        comboBox(1900, 2023, year, "Year");
        year.setPromptText(user.getDate().getYear());
    }

    private void setInformation(boolean option){
        firstName.setEditable(option);
        lastName.setEditable(option);
        email.setEditable(option);
    }

    private void setAvatar(){
        FirebaseImage firebaseImage = new FirebaseImage();
        imageView.setImage(firebaseImage.getAvatar(user));
        imageView.setPreserveRatio(false);
    }

    public void editImageEvent(){
        Stage stage = (Stage)pane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File Chooser");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(stage);
        if (file!=null) {
            Image image = new Image(file.toURI().toString(), 300, 300, false, true);
            imageView.setImage(image);
            this.file = file;
            System.out.println("Luu avatar thanh cong");
        }
    }
    @FXML
    private void editProfileEvent() {
        setButton(true);
        setInformation(true);
    }

    private boolean checkText(){
        return firstName.getText().isEmpty() || lastName.getText().isEmpty() || email.getText().isEmpty();
    }

    private boolean checkEmail(){
        for (User user : listUsers) {
            if (user.getIndex().equals(this.user.getIndex())) continue;
            if (user.getEmail().equals(email.getText())) {
                return false;
            }
        }
        return true;
    }
    private int checkValid(){
        if (checkText()) return 1;
        if (!checkEmail()) return 2;
        return 0;
    }

    private void updateEmail(ArrayList<Picture> pictures, User newUser){
        try {
            FXMLLoader loader = new FXMLLoader(CodeOTP.class.getResource("CodeOTP.fxml"));
            Parent root = loader.load();
            CodeOTP codeOTP = loader.getController();
            pane.getChildren().clear();
            AnchorPane anchorPane = new AnchorPane(root);
            anchorPane.setLayoutX(100);
            pane.getChildren().add(anchorPane);
            codeOTP.init(email.getText(), user.getIndex(), user, newUser, listUsers, pictures, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private User getNewUser(){
        User newUser = user;
        newUser.setFirstName(firstName.getText());
        newUser.setLastName(lastName.getText());
        newUser.setEmail(email.getText());
        newUser.setDate(new DateOfBirth(day.getPromptText(), month.getPromptText(), year.getPromptText()));
        return newUser;
    }

    private void inform(int check){
        inform.setVisible(true);
        inform.setTextFill(Color.RED);
        if (check==1) inform.setText("Please provide complete information.");
        else inform.setText("Email already exists!");
    }


    public void updateProfileEvent(ArrayList<Picture> pictures) {
        int check = checkValid();
        if (check==0){
            setInformation(false);
            setButton(false);
            User newUser = getNewUser();
            updateEmail(pictures, newUser);
            return;
        }
        inform(check);
    }
    private void cancelEvent(){
        setAvatar();
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        day.setPromptText(user.getDate().getDay());
        month.setPromptText(user.getDate().getMonth());
        year.setPromptText(user.getDate().getYear());
        setButton(false);
        setInformation(false);
    }
}
