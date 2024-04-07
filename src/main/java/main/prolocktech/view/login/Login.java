package main.prolocktech.view.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.prolocktech.controller.FileUser;
import main.prolocktech.model.User;
import main.prolocktech.view.forgotpassword.ForgotPassword;
import main.prolocktech.view.mainui.MainUI;
import main.prolocktech.view.register.Registration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Login{
    @FXML
    private TextField emailText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private AnchorPane pane;
    @FXML
    private Label inform;
    public void init(){
        inform.setVisible(false);
    }

    @FXML
    public void openRegistration() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Registration.class.getResource("Registration.fxml")));
        pane.getChildren().removeAll();
        pane.getChildren().setAll(root);
    }

    public void openForgetPassword() {
        FXMLLoader loader = new FXMLLoader(ForgotPassword.class.getResource("ForgotPassword.fxml"));
        try {
            Parent root = loader.load();
            pane.getChildren().removeAll();
            pane.getChildren().setAll(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private User findUser(){
        FileUser file = new FileUser();
        ArrayList<User> users = file.readUser();
        for (User user : users) {
            if (user.getEmail().equals(emailText.getText()) && user.getPassword().equals(passwordText.getText())){
                return user;
            }
        }
        return null;
    }
    public void openMainGUI(ActionEvent actionEvent) {
        User user = findUser();
        if (user == null) {
            inform.setVisible(true);
            inform.setTextFill(Color.RED);
            return;
        }
        try {
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(MainUI.class.getResource("MainUI.fxml"));
            Parent root = loader.load();
            MainUI main = loader.getController();
            pane.getChildren().clear();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            main.setUser(user);
            main.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
