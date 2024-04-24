package main.prolocktech.model;

import javafx.scene.image.ImageView;

import java.io.File;

public class Picture extends ImageView {
    private String nameUser, nameFile;
    private File file;

    private User user;

    private long timeCreated;

    public Picture(User user, long timeCreated, String nameFile) {
        this.user = user;
        this.timeCreated = timeCreated;
        this.nameFile = nameFile;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
