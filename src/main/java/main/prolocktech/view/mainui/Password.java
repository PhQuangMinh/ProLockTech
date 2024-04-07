package main.prolocktech.view.mainui;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import main.prolocktech.controller.FileUser;
import main.prolocktech.model.User;

import java.util.Objects;

public class Password {
    @FXML
    private ImageView avatar;
    @FXML
    private PasswordField currentPassword, newPassword, confirmPassword;
    @FXML
    private JFXButton cancel, update, confirm;
    @FXML
    private Label alert;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void process(User user){
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png")));
        avatar.setImage(image);
        setUser(user);
        cancel.setVisible(false);
        update.setVisible(true);
        confirm.setVisible(false);
        currentPassword.setEditable(false);
        newPassword.setEditable(false);
        confirmPassword.setEditable(false);
        alert.setVisible(false);
        cancel.setOnAction(event -> buttonCancel());
        update.setOnAction(event -> buttonUpdate());
        confirm.setOnAction(event -> buttonConfirm());
    }
    
    public void buttonUpdate(){
        cancel.setVisible(true);
        update.setVisible(false);
        confirm.setVisible(true);
        currentPassword.setEditable(true);
        newPassword.setEditable(true);
        confirmPassword.setEditable(true);
    }
    
    public void buttonCancel(){
        cancel.setVisible(false);
        update.setVisible(true);
        confirm.setVisible(false);
        currentPassword.setEditable(false);
        newPassword.setEditable(false);
        confirmPassword.setEditable(false);
        currentPassword.clear();
        newPassword.clear();
        confirmPassword.clear();
    }

    private boolean checkPassword(){
        if (currentPassword.getText().isEmpty() || newPassword.getText().isEmpty() || confirmPassword.getText().isEmpty()) return false;
        if (!currentPassword.getText().equals(user.getPassword())) return false;
        if (!newPassword.getText().equals(confirmPassword.getText())) return false;
//        if (newPassword.getText().length()<8) return false;
        return true;
    }

    public void buttonConfirm(){
        alert.setVisible(true);
        if (checkPassword()){
            User newUser = user;
            newUser.setPassword(newPassword.getText());
            FileUser managerFile = new FileUser();
            managerFile.updateUser(user, newUser);
            alert.setTextFill(Color.GREEN);
            alert.setText("Update password successfully");
        }
        else{
            alert.setTextFill(Color.RED);
            alert.setText("Update password failed");
        }
        buttonCancel();
    }
}
