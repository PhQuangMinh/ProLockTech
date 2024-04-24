package main.prolocktech.controller;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import javafx.scene.image.Image;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;

import java.io.*;
import java.util.*;

public class FirebaseImage{
    private final String nameBucket = "proptitlocktech.appspot.com";
    private Storage storage;

    public void init() {
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

    public void writeImageTo(String pathImage, String pathFirebase, int option, File file){
        init();
        if (option==1){
            pathFirebase += getExtension(pathImage);
        }
        else pathFirebase+= ".png";
        try {
            FileInputStream fileInput = new FileInputStream(file);
            Blob blob = storage.create(Blob.newBuilder(nameBucket, pathFirebase).build(), fileInput);
            System.out.println("File uploaded to: " + blob.getBlobId());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeImageUpload(User user, String pathImage, File file){
        String pathFirebase = "user" + user.getIndex() + "/image" + user.getNumberOfImages();
        user.setNumberOfImages(user.getNumberOfImages()+1);
        writeImageTo(pathImage, pathFirebase, 1, file);
    }
    public void setAvatar(User user, String pathImage, File file){
        init();
        String path = "user" + user.getIndex() + "/avatar/avatar";
        writeImageTo(pathImage, path, 2, file);
    }

    private boolean isImage(String name){
        int index = name.lastIndexOf('.');
        if (index<0) return false;
        String extension = name.substring(index + 1);
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif") || extension.equals("bmp");
    }

    private List<Blob> getListBlob(User user){
        init();
        String folderName = "user" + user.getIndex() + "/image";
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
        if (blob != null) {
            byte[] blobData = blob.getContent();
            return new Image(new ByteArrayInputStream(blobData));
        }
        return null;
    }

    private ArrayList<Picture> convertBlob(List<Blob> list, User user){
        ArrayList<Picture> listPicture = new ArrayList<>();
        for(Blob blob : list) {
            Image image = getImage(blob);
            long creationTime = blob.getCreateTime();
            Picture picture = new Picture(user, creationTime, blob.getName());
            picture.setFitHeight(400);
            picture.setFitWidth(400);
            picture.setImage(image);
            picture.setPreserveRatio(false);
            listPicture.add(picture);
        }
        return listPicture;
    }
    public ArrayList<Picture> getPictureUser(User user){
        List<Blob> listBlob = getListBlob(user);
        return convertBlob(listBlob, user);
    }


    public ArrayList<Picture> getPictures(ArrayList<User> listUsers){
        ArrayList<Picture> listPicture = new ArrayList<>();
        for (User user: listUsers) {
            listPicture.addAll(convertBlob(getListBlob(user), user));
        }
        return listPicture;
    }

    public Image getAvatar(User user){
        init();
        String path = "user" + user.getIndex() + "/avatar/avatar.png";
        Blob blob = storage.get(Blob.newBuilder(nameBucket, path).build().getBlobId());
        return getImage(blob);
    }

    public void deleteImage(String pathFile){
        init();
        storage.delete(Blob.newBuilder(nameBucket, pathFile).build().getBlobId());
    }
}
