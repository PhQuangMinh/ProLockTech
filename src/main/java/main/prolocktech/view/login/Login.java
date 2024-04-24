package main.prolocktech.view.login;

import com.google.firebase.database.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.prolocktech.controller.FirebaseImage;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;
import main.prolocktech.view.forgotpassword.ForgotPassword;
import main.prolocktech.view.mainui.MainUI;
import main.prolocktech.view.register.Registration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Login{
    @FXML
    private TextField emailText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private AnchorPane pane;
    @FXML
    private Label inform, forgetPassword, signUp;
    @FXML
    private Button signIn;
    public void init(ArrayList<User> listUsers){
        inform.setVisible(false);
        signIn.setOnAction(event -> confirmUser(listUsers));
        signUp.setOnMouseClicked(event -> openRegistration(listUsers));
        forgetPassword.setOnMouseClicked(event -> openForgetPassword(listUsers));
    }

    public void openRegistration(ArrayList<User> listUsers){
        try {
            FXMLLoader loader = new FXMLLoader(Registration.class.getResource("Registration.fxml"));
            Parent root = loader.load();
            pane.getChildren().removeAll();
            pane.getChildren().setAll(root);
            Registration registration = loader.getController();
            registration.init(listUsers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openForgetPassword(ArrayList<User> listUsers) {
        try {
            FXMLLoader loader = new FXMLLoader(ForgotPassword.class.getResource("ForgotPassword.fxml"));
            Parent root = loader.load();
            pane.getChildren().removeAll();
            pane.getChildren().setAll(root);
            ForgotPassword forgotPassword = loader.getController();
            forgotPassword.init(pane, listUsers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void confirmUser(ArrayList<User> listUsers){
        User finalUser = null;
        for (User user : listUsers) {
            if (emailText.getText().equals(user.getEmail()) && passwordText.getText().equals(user.getPassword())) {
                finalUser = user;
                break;
            }
        }
        if (finalUser == null) {
            inform.setVisible(true);
            inform.setTextFill(Color.RED);
            inform.setText("Email or password is incorrect!");
            return;
        }
        openMainGUI(finalUser, listUsers);

    }
    public void openMainGUI(User user, ArrayList<User> listUsers) {
        if (user == null) {
            inform.setVisible(true);
            inform.setTextFill(Color.RED);
            return;
        }
        try {
            FirebaseImage firebaseImage = new FirebaseImage();
            ArrayList<Picture> pictures = firebaseImage.getPictures(listUsers);
            Stage stage = (Stage) pane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(MainUI.class.getResource("MainUI.fxml"));
            Parent root = loader.load();
            MainUI main = loader.getController();
            pane.getChildren().clear();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            main.setUser(user);
            main.init(listUsers, pictures);
        } catch (IOException ignored) {
        }
    }
}
