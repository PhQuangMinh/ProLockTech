package main.prolocktech.view.forgotpassword;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;
import main.prolocktech.view.login.Login;

import java.io.IOException;
import java.util.ArrayList;

public class Success {
    @FXML
    public Label title, inform;
    @FXML
    private Button backToLogin;
    @FXML
    private AnchorPane pane;


    public void backtoEvent(ArrayList<User> listUsers){
        try{
            FXMLLoader loader = new FXMLLoader(Login.class.getResource("Login.fxml"));
            Parent root = loader.load();
            pane.getChildren().removeAll();
            pane.getChildren().setAll(root);
            Login login = loader.getController();
            login.init(listUsers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void init(String textTitle, String textInform, ArrayList<User> listUsers) {
        backToLogin.setOnAction(event -> backtoEvent(listUsers));
        title.setText(textTitle);
        inform.setText(textInform);
    }

}
