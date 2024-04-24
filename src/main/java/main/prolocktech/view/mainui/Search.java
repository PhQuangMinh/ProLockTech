package main.prolocktech.view.mainui;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.prolocktech.controller.FirebaseImage;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Search {
    @FXML
    private ImageView avatar;
    @FXML
    private JFXListView<User> listView;
    @FXML
    private TextField searchBar;
    private void filterUser(ObservableList<User> data, ListView<User> listView, String search){
        ObservableList<User> filteredData = FXCollections.observableArrayList();
        for (User user : data) {
            String fullName = user.getFirstName() + " " + user.getLastName();
            if (fullName.toLowerCase().contains(search.toLowerCase())) {
                filteredData.add(user);
            }
        }
        listView.setItems(filteredData);
    }
    public void makeSearch(Stage stage, User user, ArrayList<User> listUsers, ArrayList<Picture> pictures){
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png")));
        avatar.setImage(image);
        ObservableList<User> data = FXCollections.observableArrayList(listUsers);
        searchBar.setPromptText("Search...");
        listView.setItems(data);;
        searchBar.textProperty().addListener(((observable, oldValue, newValue) -> {
            filterUser(data, listView, newValue);
        }));
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            FirebaseImage firebaseImage = new FirebaseImage();
            ArrayList<Picture> listImage = firebaseImage.getPictureUser(newValue);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml"));
                Parent root = loader.load();
                MainUI mainUI = loader.getController();
                mainUI.setUser(user);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                mainUI.init(listUsers, pictures);
                mainUI.makeDisPlayImage(listImage, false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
