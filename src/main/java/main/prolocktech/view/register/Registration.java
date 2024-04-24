package main.prolocktech.view.register;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import main.prolocktech.model.DateOfBirth;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;
import javafx.collections.FXCollections;
import main.prolocktech.view.forgotpassword.CodeOTP;
import main.prolocktech.view.login.Login;

import java.io.IOException;
import java.util.ArrayList;

public class Registration{
    @FXML
    private TextField firstName, lastName, email, password, confirmPassword;
    @FXML
    private ComboBox<String> day, month, year;
    @FXML
    private Label inform, signIn;
    @FXML
    private AnchorPane pane;
    @FXML
    private Button register;
    private ObservableList<String> observableList;
    private void setDay(){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            list.add(String.valueOf(i));
        }
        observableList = FXCollections.observableArrayList(list);
        day.setItems(observableList);
        day.setPromptText("Day");
    }

    private void setMonth(){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            list.add(String.valueOf(i));
        }
        observableList = FXCollections.observableArrayList(list);
        month.setItems(observableList);
        month.setPromptText("Month");
    }

    private void setYear(){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 2023; i >= 1900; i--) {
            list.add(String.valueOf(i));
        }
        observableList = FXCollections.observableArrayList(list);
        year.setItems(observableList);
        year.setPromptText(("Year"));
    }

    public void init(ArrayList<User> listUsers) {
        setDay();
        setMonth();
        setYear();
        signIn.setOnMouseClicked(event -> cancel(listUsers));
        register.setOnAction(event -> register(listUsers));

    }
    public void cancel(ArrayList<User> listUsers) {
        FXMLLoader loader = new FXMLLoader(Login.class.getResource("Login.fxml"));
        try {
            Parent root = loader.load();
            Login login = loader.getController();
            pane.getChildren().removeAll();
            pane.getChildren().setAll(root);
            login.init(listUsers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkText(){
        return firstName.getText().isEmpty() || lastName.getText().isEmpty() || email.getText().isEmpty()
                || password.getText().isEmpty() || confirmPassword.getText().isEmpty();
    }

    private boolean checkCombo(){
        return day.getValue() == null || month.getValue() == null || year.getValue() == null;
    }
    private boolean checkPassword(){
        if (!password.getText().equals(confirmPassword.getText())) {
            return false;
        }
        return password.getText().length() >= 8;
    }

    private boolean checkEmail(ArrayList<User> list){
        for (User user : list){
            if (user.getEmail().equals(email.getText())) return false;
        }
        return true;
    }

    private int checkValid(ArrayList<User> listUsers){
        if (checkCombo() || checkText() || !checkPassword() || !checkEmail(listUsers)){
            if (checkCombo() || checkText()) return 1;
            if (!checkPassword()) return 2;
            if (!checkEmail(listUsers)) return 3;
        }
        return 0;
    }
    private User getUser(){
        DateOfBirth date = new DateOfBirth(day.getValue(), month.getValue(), year.getValue());
        return new User(firstName.getText(), lastName.getText(), email.getText(), password.getText(), date);
    }

    private void interfaceOTP(ArrayList<User> listUsers){
        inform.setVisible(false);
        User userNow = getUser();
        try {
            FXMLLoader loader = new FXMLLoader(CodeOTP.class.getResource("CodeOTP.fxml"));
            Parent root = loader.load();
            CodeOTP codeOTP = loader.getController();
            pane.getChildren().removeAll();
            pane.getChildren().setAll(root);
            codeOTP.init(email.getText(), "register", userNow, null, listUsers, null, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void register(ArrayList<User> listUsers){
        int valueCheck = checkValid(listUsers);
        if (valueCheck==0) {
            interfaceOTP(listUsers);
            return;
        }
        inform.setVisible(true);
        inform.setTextFill(Color.RED);
        if (valueCheck==1) inform.setText("Please provide complete information.");
        else
            if (valueCheck==2) inform.setText("Invalid password!");
            else inform.setText("Email already exists!");
    }

}
