package main.prolocktech.view.forgotpassword;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;
import main.prolocktech.view.login.Login;

import java.io.IOException;
import java.util.ArrayList;

public class ForgotPassword{
    @FXML
    public Button back, sendCode;
    @FXML
    private TextField emailUser;
    @FXML
    private Label confirm;
    @FXML
    private AnchorPane pane;

    public void init(AnchorPane pane, ArrayList<User> listUsers){
        this.pane = pane;
        confirm.setVisible(false);
        sendCode.setOnAction(event -> sendCodeEvent(listUsers));
        back.setOnAction(event -> backEvent(listUsers));
    }

    private User checkEmail(ArrayList<User> list){
        User findUser = null;
        for (User user : list) {
            if (user.getEmail().equals(emailUser.getText())) {
                findUser = user;
            }
        }
        return findUser;
    }

    public void sendCodeEvent(ArrayList<User> listUsers){
        User user = checkEmail(listUsers);
        if (user!=null){
            try {
                FXMLLoader loader = new FXMLLoader(ForgotPassword.class.getResource("CodeOTP.fxml"));
                Parent root = loader.load();
                CodeOTP codeOTP = loader.getController();
                pane.getChildren().removeAll();
                pane.getChildren().setAll(root);
                codeOTP.init(emailUser.getText(), "password", null, null, listUsers, null, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            confirm.setTextFill(Color.RED);
            confirm.setVisible(true);
        }
    }
    private void backEvent(ArrayList<User> listUsers){
        try{
            FXMLLoader loader = new FXMLLoader(Login.class.getResource("Login.fxml"));
            Parent root = loader.load();
            Login login = loader.getController();
            pane.getChildren().removeAll();
            pane.getChildren().setAll(root);
            login.init(listUsers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
