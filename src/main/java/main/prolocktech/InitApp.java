package main.prolocktech;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;
import main.prolocktech.view.login.Login;

import java.io.IOException;
import java.util.ArrayList;

public class InitApp {
    @FXML
    AnchorPane pane;

    public void init(ArrayList<User> listUsers){
        try {
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
}
