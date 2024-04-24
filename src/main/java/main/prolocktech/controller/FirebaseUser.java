package main.prolocktech.controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.prolocktech.Main;
import main.prolocktech.model.DateOfBirth;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class FirebaseUser{
    public void initFirebase() {
        String linkUrl = "https://proptitlocktech-default-rtdb.firebaseio.com/";
        String serviceAccountFile = "proptitlocktech-firebase-adminsdk-xp853-e2fde6529e.json";
        try {
            FileInputStream serviceAccount = new FileInputStream(serviceAccountFile);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(linkUrl)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void readUser(Stage stage){
        initFirebase();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users");
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    users.add(data.getValue(User.class));
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Main main = new Main();
                        main.init(stage, users);
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void addUser(User user, ArrayList<User> listUsers){
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users");
        user.setIndex(String.valueOf(listUsers.size()));
        listUsers.add(user);
        dataRef.child("user" + user.getIndex()).setValueAsync(user);
        FirebaseImage firebaseImage = new FirebaseImage();
        File file = new File("src/main/resources/main/prolocktech/controller/Avatar.jpg");
        firebaseImage.setAvatar(user, "src/main/resources/main/prolocktech/controller/Avatar.jpg", file);
    }

    public void updateUser(User user){
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users");
        dataRef.child("user" + user.getIndex()).setValueAsync(user);
    }

}
