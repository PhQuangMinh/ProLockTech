package main.prolocktech.model;

import com.google.cloud.storage.Blob;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Picture extends ImageView {
    private String nameUser;
    private File file;

    private User user;
    private Image image;
    private Blob blob;

    public Picture(String nameUser, File file, User user) {
        this.nameUser = nameUser;
        this.file = file;
        this.user = user;
    }

    public Picture(Image image, User user, Blob blob) {
        this.image = image;
        this.user = user;
        this.blob = blob;
    }

    public Blob getBlob() {
        return blob;
    }

    public void setBlob(Blob blob) {
        this.blob = blob;
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
    public long getCreationTime() {
        return blob.getCreateTime();
    }
}
