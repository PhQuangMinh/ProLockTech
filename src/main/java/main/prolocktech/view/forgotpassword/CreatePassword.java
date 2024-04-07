package main.prolocktech.view.forgotpassword;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import main.prolocktech.controller.FileUser;
import main.prolocktech.model.User;

import java.io.IOException;
import java.util.ArrayList;

public class CreatePassword {
    @FXML
    private AnchorPane pane;
    @FXML
    private Button resetButton;
    @FXML
    private Label confirm;
    @FXML
    private PasswordField newPassword, confirmPassword;
    private String emailUser;

    public void init(String emailUser){
        this.emailUser = emailUser;
        resetButton.setOnAction(event -> resetEvent());
        confirm.setVisible(false);
    }

    private boolean checkPassword(){
        if (newPassword.getText().isEmpty() || confirmPassword.getText().isEmpty()) return false;
        if (!newPassword.getText().equals(confirmPassword.getText())) return false;
        return newPassword.getText().length() >= 8;
    }

    private User getUser(){
        User user = null;
        FileUser file = new FileUser();
        ArrayList<User> list = file.readUser();
        for (User user1 : list) {
            if (user1.getEmail().equals(emailUser)) {
                user = user1;
            }
        }
        return user;
    }
    private void updateUser(){
        User user = getUser();
        user.setPassword(newPassword.getText());
        FileUser managerFile = new FileUser();
        managerFile.updateUser(user, user);
    }

    private void resetEvent(){
        if (checkPassword()){
            updateUser();
            try {
                FXMLLoader loader = new FXMLLoader(CreatePassword.class.getResource("Success.fxml"));
                Parent root = loader.load();
                Success resetSuccess = loader.getController();
                pane.getChildren().removeAll();
                pane.getChildren().setAll(root);
                resetSuccess.init("Password changed", "Your password has been changed successfully.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            confirm.setTextFill(Color.RED);
            confirm.setVisible(true);
        }
    }
}
