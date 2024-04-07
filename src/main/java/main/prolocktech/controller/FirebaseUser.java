package main.prolocktech.controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import javafx.fxml.Initializable;
import main.prolocktech.model.DateOfBirth;
import main.prolocktech.model.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FirebaseUser{
    private DatabaseReference dataRef;

    public User getUser(DataSnapshot dataSnapshot){
        String index = dataSnapshot.child("index").getValue(String.class);
        String firstName = dataSnapshot.child("firstName").getValue(String.class);
        String lastName = dataSnapshot.child("lastName").getValue(String.class);
        String email = dataSnapshot.child("email").getValue(String.class);
        String password = dataSnapshot.child("password").getValue(String.class);
        DateOfBirth date = dataSnapshot.child("date").getValue(DateOfBirth.class);
        int numberOfImages = dataSnapshot.child("numberOfImages").getValue(int.class);
        return new User(index, firstName, lastName, email, password, date, numberOfImages);
    }
    public void readUser(){
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println(getUser(snapshot));
                    users.add(getUser(snapshot));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void addUser(User user){
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println(getUser(snapshot));
                    users.add(getUser(snapshot));
                }
                user.setIndex(String.valueOf(users.size()));
                dataRef.child("user" + user.getIndex()).setValueAsync(user);
                System.out.println("Successfully added");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void updateUser(User user){
        dataRef.child("user" + user.getIndex()).setValueAsync(user);
    }


}
