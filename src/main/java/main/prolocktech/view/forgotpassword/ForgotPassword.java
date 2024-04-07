package main.prolocktech.view.forgotpassword;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.prolocktech.controller.FileUser;
import main.prolocktech.controller.SendMail;
import main.prolocktech.model.User;
import main.prolocktech.view.login.Login;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ForgotPassword implements Initializable {
    @FXML
    public Button back, sendCode;
    @FXML
    private TextField emailUser;
    @FXML
    private Label confirm;
    @FXML
    private AnchorPane pane;

    public void init(AnchorPane pane){
        this.pane = pane;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirm.setVisible(false);
        sendCode.setOnAction(event -> sendCodeEvent());
        back.setOnAction(event -> backEvent());
    }
    private boolean checkEmail(ArrayList<User> list){
        for (User user : list) {
            if (user.getEmail().equals(emailUser.getText())) {
                return true;
            }
        }
        return false;
    }

    public void sendCodeEvent(){
        FileUser fileUser = new FileUser();
        ArrayList<User> list = fileUser.readUser();
        if (checkEmail(list)){
            try {
                FXMLLoader loader = new FXMLLoader(ForgotPassword.class.getResource("CodeOTP.fxml"));
                Parent root = loader.load();
                CodeOTP codeOTP = loader.getController();
                pane.getChildren().removeAll();
                pane.getChildren().setAll(root);
                codeOTP.init(emailUser.getText(), "password", null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            confirm.setTextFill(Color.RED);
            confirm.setVisible(true);
        }
    }
    private void backEvent(){
        try{
            FXMLLoader loader = new FXMLLoader(Login.class.getResource("Login.fxml"));
            Parent root = loader.load();
            Login login = loader.getController();
            pane.getChildren().removeAll();
            pane.getChildren().setAll(root);
            login.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
