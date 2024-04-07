package main.prolocktech;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.prolocktech.controller.FirebaseImage;
import main.prolocktech.controller.FirebaseUser;
import main.prolocktech.model.User;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("view/logo.png")));
        stage.setTitle("ProLockTech");
        stage.setResizable(true);
        try {
            FXMLLoader loader = new FXMLLoader(InitApp.class.getResource("InitApp.fxml"));
            Parent root = loader.load();
            InitApp initApp = loader.getController();
            initApp.init();
            Scene scene = new Scene(root, 600, 600);
            stage.getIcons().add(image);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}