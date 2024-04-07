package main.prolocktech.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.prolocktech.model.Picture;
import main.prolocktech.model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class FileImage {
    private List<File> imageFiles;
    private ArrayList<Picture> pictureFiles;
    private void writeFileImage(User user, String originalPath, Image image, String path, int option){
        int index = originalPath.lastIndexOf('/');
        String fileName =  originalPath.substring(index);
        index = fileName.lastIndexOf('.');
        String extension = fileName.substring(index + 1);
        String pathSave = null;
        if (option==1) pathSave = path + fileName;
        else
        if (option==2) pathSave = path + user.getIndex() + fileName;
        else pathSave = path + user.getIndex() + "/avatar/" + fileName;

        File file = new File(pathSave);

        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, extension, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeImage(String path, Image image, User user){
        writeFileImage(user, path, image, "src/main/data/images", 1);
        writeFileImage(user, path, image, "src/main/data/imageuser/user", 2);
    }

    public void saveAvatar(String path, User user, Image image){
        File file = new File("src/main/data/imageuser/user" + user.getIndex() + "/avatar");
        if (file.exists() && file.isDirectory()){
            File[] files = file.listFiles();
            assert files != null;
            for (File f : files) {
                f.delete();
            }
        }
        writeFileImage(user, path, image, "src/main/data/imageuser/user", 3);
    }


    private boolean checkFileImage(File file){
        String path = file.getAbsolutePath();
        String extension = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
        return !extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png") && !extension.equals("gif");
    }

    private void takeFile(String path){
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            assert files != null;
            if (files.length > 0) {
                imageFiles = List.of(files);
            }
        }
    }

    private ArrayList<ImageView> convertToImageView(){
        ArrayList<ImageView> images = new ArrayList<>();
        for (int i=0;i<imageFiles.size();i++) {
            File file = imageFiles.get(i);
            if (checkFileImage(file)) continue;
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setId(String.valueOf(i));
            images.add(imageView);
        }
        return images;
    }

    public ArrayList<ImageView> getImageFromFolder(String path){
        takeFile(path);
        return convertToImageView();
    }


    private void convertToPicture(User user){
        ArrayList<Picture> images = new ArrayList<>();
        for (int i=0;i<imageFiles.size();i++) {
            File file = imageFiles.get(i);
            if (checkFileImage(file)) continue;
            Image image = new Image(file.toURI().toString());
            String fullName = user.getFirstName() + " " + user.getLastName();
            Picture picture = new Picture(fullName, file, user);
            picture.setImage(image);
            picture.setId(String.valueOf(imageFiles.size()-i));
            images.add(picture);
        }
        pictureFiles.addAll(images);
    }

    private void sortFilePicture(){
        pictureFiles.sort((f1, f2) -> {
            try {
                long creationTime1 = Files.readAttributes(f1.getFile().toPath(), BasicFileAttributes.class).creationTime().toMillis();
                long creationTime2 = Files.readAttributes(f2.getFile().toPath(), BasicFileAttributes.class).creationTime().toMillis();
                return Long.compare(creationTime2, creationTime1);
            } catch (IOException e) {
                System.out.println("loi roi");
                return 0;
            }
        });
    }

    public ArrayList<Picture> takePictureUser(User user){
        imageFiles = new ArrayList<>();
        String path = "src/main/data/imageuser/user" + user.getIndex();
        takeFile(path);
        pictureFiles = new ArrayList<>();
        convertToPicture(user);
        sortFilePicture();
        return pictureFiles;
    }

    public ArrayList<Picture> takePictures(){
        imageFiles = new ArrayList<>();
        pictureFiles = new ArrayList<>();
        FileUser fileUser = new FileUser();
        ArrayList<User> list = fileUser.readUser();
        for (User user : list){
            String path = "isrc/main/data/imageuser/user" + user.getIndex();
            takeFile(path);
            convertToPicture(user);
        }
        sortFilePicture();
        return pictureFiles;
    }
}
