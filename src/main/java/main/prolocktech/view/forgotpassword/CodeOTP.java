package main.prolocktech.view.forgotpassword;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import main.prolocktech.controller.FileUser;
import main.prolocktech.controller.FirebaseUser;
import main.prolocktech.controller.SendMail;
import main.prolocktech.model.User;
import main.prolocktech.view.register.Registration;

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

    public void init(String emailUser, String work, User user){
        this.emailUser = emailUser;
        SendMail sendMail = new SendMail();
        OTP = sendMail.sendMail(emailUser);
        confirm.setVisible(false);
        resend.setOnMouseClicked(event -> resendEvent());
        if (work.equals("password")){
            verify.setOnAction(event -> setVerifyPassword());
            back.setOnAction(event -> backPasswordEvent());
        }
        else{
            verify.setOnAction(event -> setVerifyRegister(user));
            back.setOnAction(event -> backRegisterEvent());
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

    public void setVerifyRegister(User user){
        if (yourOTP.getText()==null) return;
        if (OTP.equals(yourOTP.getText())){
            FileUser fileUser = new FileUser();
            fileUser.addUser(user);
            try {
                FXMLLoader loader = new FXMLLoader(Success.class.getResource("Success.fxml"));
                Parent root = loader.load();
                Success success = loader.getController();
                pane.getChildren().removeAll();
                pane.getChildren().setAll(root);
                success.init("Successful Registration", "Thanks! You have sucessfully registered!");
            } catch (Exception e) {
                System.out.println("Error:" + e.getMessage());
            }
        }
        else{
            setConfirm();
        }
    }
    public void setVerifyPassword(){
        if (yourOTP.getText()==null) return;
        if (OTP.equals(yourOTP.getText())){
            try {
                FXMLLoader loader = new FXMLLoader(CodeOTP.class.getResource("CreatePassword.fxml"));
                Parent root = loader.load();
                CreatePassword createPassword = loader.getController();
                pane.getChildren().removeAll();
                pane.getChildren().setAll(root);
                createPassword.init(emailUser);
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
