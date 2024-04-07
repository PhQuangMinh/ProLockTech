module main.prolocktech {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires com.google.auth;
    requires google.cloud.storage;
    requires google.cloud.core;
    requires google.cloud.firestore;
    requires gax;
    requires javafx.swing;
    requires java.mail;
    requires com.jfoenix;

    opens main.prolocktech.model to javafx.fxml;
    opens main.prolocktech to javafx.fxml;
    opens main.prolocktech.view to javafx.fxml;
    opens main.prolocktech.controller to javafx.fxml;
    opens main.prolocktech.view.forgotpassword to javafx.fxml;
    opens main.prolocktech.view.mainui to javafx.fxml;
    opens main.prolocktech.view.login to javafx.fxml;
    opens main.prolocktech.view.register to javafx.fxml;

    exports main.prolocktech;
    exports main.prolocktech.model;
    exports main.prolocktech.controller;
    exports main.prolocktech.view.forgotpassword;
    exports main.prolocktech.view.mainui;
    exports main.prolocktech.view.login;
    exports main.prolocktech.view.register;

}