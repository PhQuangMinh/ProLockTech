package main.prolocktech;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.prolocktech.view.login.Login;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class InitApp {
//    public void initFirebase() {
//        String linkUrl = "https://proptitlocktech-default-rtdb.firebaseio.com/";
//        String serviceAccountFile = "proptitlocktech-firebase-adminsdk-xp853-e2fde6529e.json";
//        try {
//            FileInputStream serviceAccount = new FileInputStream(serviceAccountFile);
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl(linkUrl)
//                    .build();
//            FirebaseApp.initializeApp(options);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    @FXML
    AnchorPane pane;

    public void init(){
//        initFirebase();
        try {
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
}
