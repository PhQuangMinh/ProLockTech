package main.prolocktech.controller;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class FirebaseImage implements Initializable {
    private final String nameBucket = "proptitlocktech.appspot.com";
    private Storage storage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String locationFile = "proptitlocktech-firebase-adminsdk-xp853-e2fde6529e.json";
        try {
            FileInputStream serviceAccountFile = new FileInputStream(locationFile);
            storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountFile))
                    .build()
                    .getService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getExtension(String path) {
        int index = path.lastIndexOf('.');
        if (index<0) return ".png";
        return path.substring(index);
    }

    public void writeImageTo(User user, String pathImage, String pathFirebase, int option){
        user.setNumberOfImages(user.getNumberOfImages()+1);
        if (option==1) pathFirebase += getExtension(pathImage);
        else pathFirebase+= ".png";
        try {
            FileInputStream file = new FileInputStream(pathImage);
            Blob blob = storage.create(Blob.newBuilder(nameBucket, pathFirebase).build(), file);
            System.out.println("File uploaded to: " + blob.getBlobId());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeImageUpload(User user, String pathImage){
        String path = "user" + user.getIndex() + "/image" + user.getNumberOfImages();
        writeImageTo(user, pathImage, path, 1);
    }
    public void writeAvatar(User user, String pathImage){
        String path = "user" + user.getIndex() + "/avatar/avatar.png";
        writeImageTo(user, pathImage, path, 2);
    }

    private boolean isImage(String name){
        int index = name.lastIndexOf('.');
        if (index<0) return false;
        String extension = name.substring(index + 1);
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif") || extension.equals("bmp");
    }

    private List<Blob> getListBlob(User user){
        String folderName = "user" + user.getIndex() + "/";
        Page<Blob> blobs = storage.list(nameBucket, Storage.BlobListOption.prefix(folderName));
        List<Blob> blobList = new ArrayList<>();
        for (Blob blob : blobs.iterateAll()) {
            if (isImage(blob.getName())) {
                blobList.add(blob);
            }
        }
        return blobList;
    }

    private Image getImage(Blob blob) {
        try {
            byte[] blobContent = blob.getContent();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(blobContent);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int cmp(Picture a, Picture b) {
        if (a.getCreationTime()<b.getCreationTime()) return 1;
        return -1;
    }

    private ArrayList<Picture> convertBlob(List<Blob> list, User user){
        ArrayList<Picture> listPicture = new ArrayList<>();
        for(Blob blob : list) {
            Image image = getImage(blob);
            Picture picture = new Picture(image, user, blob);
            listPicture.add(picture);
        }
        listPicture.sort(this::cmp);
        return listPicture;
    }
    public ArrayList<Picture> readPictureUser(User user){
        List<Blob> listBlob = getListBlob(user);
        return convertBlob(listBlob, user);
    }

    public ArrayList<Picture> readPictures(){
        ArrayList<Picture> list = new ArrayList<>();
        List<Blob> listBlob = new ArrayList<>();
        FirebaseUser firebaseUser = new FirebaseUser();
//        ArrayList<User> users = firebaseUser.readUser();
//        for (User user : users) {
//            listBlob.addAll(getListBlob(user));
//            list.addAll(convertBlob(listBlob, user));
//        }
//        list.sort(this::cmp);
        return list;
    }

    public Image getAvatar(User user){
        String path = "user" + user.getIndex() + "/avatar/avatar.png";
        Blob blob = storage.get(Blob.newBuilder(nameBucket, path).build().getBlobId());
        return getImage(blob);
    }
}
