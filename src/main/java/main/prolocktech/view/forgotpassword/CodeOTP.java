package main.prolocktech.view.forgotpassword;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.prolocktech.InitApp;
import main.prolocktech.controller.FirebaseImage;
import main.prolocktech.controller.FirebaseUser;
import main.prolocktech.controller.SendMail;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;
import main.prolocktech.view.mainui.MainUI;
import main.prolocktech.view.register.Registration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CodeOTP{
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField yourOTP;
    @FXML
    private Button verify, back;
    @FXML
    private Label confirm, resend;
    private String OTP, emailUser;

    public void init(String emailUser, String work, User user, User newUser, ArrayList<User> listUsers, ArrayList<Picture> pictures, File file){
        this.emailUser = emailUser;
        SendMail sendMail = new SendMail();
        OTP = sendMail.sendMail(emailUser);
        confirm.setVisible(false);
        resend.setOnMouseClicked(event -> resendEvent());
        if (work.equals("password")){
            verify.setOnAction(event -> setVerifyPassword(listUsers));
            back.setOnAction(event -> backPasswordEvent());
        }
        else
            if (work.equals("register")){
                verify.setOnAction(event -> setVerifyRegister(user, listUsers));
                back.setOnAction(event -> backRegisterEvent());
            }
            else{
                verify.setOnAction(event -> setVerifyProfile(newUser, listUsers, pictures, file));
                back.setOnAction(event -> backProfileEvent(user, listUsers, pictures));
            }
    }

    private void setVerifyProfile(User newUser, ArrayList<User> listUsers, ArrayList<Picture> pictures, File file){
        if (yourOTP.getText()!=null && !yourOTP.getText().isEmpty() && OTP.equals(yourOTP.getText())){
            FirebaseUser firebaseUser = new FirebaseUser();
            firebaseUser.updateUser(newUser);
            if (file!=null){
                FirebaseImage firebaseImage = new FirebaseImage();
                firebaseImage.setAvatar(newUser, file.toURI().toString(), file);
            }
            listUsers.remove(Integer.parseInt(newUser.getIndex()));
            listUsers.add(Integer.parseInt(newUser.getIndex()), newUser);
            try {
                FXMLLoader loader = new FXMLLoader(MainUI.class.getResource("MainUI.fxml"));
                Parent root = loader.load();
                MainUI mainUI = loader.getController();
                pane.getChildren().removeAll();
                pane.getChildren().setAll(root);
                pane.setLayoutX(-150);
                mainUI.setUser(newUser);
                mainUI.returnProfile(listUsers, pictures);
            } catch (Exception e) {
                System.out.println("Error:" + e.getMessage());
            }
        }
        else{
            setConfirm();
        }
    }

    private void backProfileEvent(User user, ArrayList<User> listUsers, ArrayList<Picture> pictures) {
        FXMLLoader loader = new FXMLLoader(MainUI.class.getResource("MainUI.fxml"));
        try {
            Stage stage = (Stage)pane.getScene().getWindow();
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            MainUI main = loader.getController();
            main.setUser(user);
            main.returnProfile(listUsers, pictures);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void resendEvent(){
        SendMail sendMail = new SendMail();
        OTP = sendMail.sendMail(emailUser);
        confirm.setVisible(true);
        confirm.setTextFill(Color.GREEN);
        confirm.setText("The otp has been sent to you.");
    }

    private void setConfirm(){
        confirm.setTextFill(Color.RED);
        confirm.setText("Your otp is wrong");
        confirm.setVisible(true);
    }

    public void setVerifyRegister(User user, ArrayList<User> listUsers){
        if (yourOTP.getText()!=null && !yourOTP.getText().isEmpty() && OTP.equals(yourOTP.getText())){
            FirebaseUser firebaseUser = new FirebaseUser();
            firebaseUser.addUser(user, listUsers);
            try {
                FXMLLoader loader = new FXMLLoader(Success.class.getResource("Success.fxml"));
                Parent root = loader.load();
                Success success = loader.getController();
                pane.getChildren().removeAll();
                pane.getChildren().setAll(root);
                success.init("Successful Registration", "Thanks! You have sucessfully registered!", listUsers);
            } catch (Exception e) {
                System.out.println("Error:" + e.getMessage());
            }
        }
        else{
            setConfirm();
        }
    }
    public void setVerifyPassword(ArrayList<User> listUsers){
        if (yourOTP.getText()==null) return;
        if (OTP.equals(yourOTP.getText())){
            try {
                FXMLLoader loader = new FXMLLoader(CodeOTP.class.getResource("CreatePassword.fxml"));
                Parent root = loader.load();
                CreatePassword createPassword = loader.getController();
                pane.getChildren().removeAll();
                pane.getChildren().setAll(root);
                createPassword.init(emailUser, listUsers);
            } catch (Exception e) {
                System.out.println("Error:" + e.getMessage());
            }
        }
        else{
            setConfirm();
        }
    }
    private void backRegisterEvent(){
        try {
            FXMLLoader loader = new FXMLLoader(Registration.class.getResource("Registration.fxml"));
            Parent root = loader.load();
            pane.getChildren().removeAll();
            pane.getChildren().setAll(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void backPasswordEvent(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ForgotPassword.fxml"));
            Parent root = loader.load();
            pane.getChildren().removeAll();
            pane.getChildren().setAll(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
