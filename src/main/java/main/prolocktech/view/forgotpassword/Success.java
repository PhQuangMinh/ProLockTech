package main.prolocktech.view.forgotpassword;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.prolocktech.view.login.Login;

import java.io.IOException;

public class Success {
    @FXML
    public Label title, inform;
    @FXML
    private Button backToLogin;
    @FXML
    private AnchorPane pane;


    public void backtoEvent(){
        try{
            FXMLLoader loader = new FXMLLoader(Login.class.getResource("Login.fxml"));
            Parent root = loader.load();
            pane.getChildren().removeAll();
            pane.getChildren().setAll(root);
            Login login = loader.getController();
            login.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void init(String textTitle, String textInform) {
        backToLogin.setOnAction(event -> backtoEvent());
        title.setText(textTitle);
        inform.setText(textInform);
    }

}
