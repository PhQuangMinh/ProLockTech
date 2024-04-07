package main.prolocktech.controller;

import javafx.scene.image.Image;
import main.prolocktech.model.User;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class FileUser {
    public ArrayList<User> readUser(){
        ArrayList<User> list = new ArrayList<>();
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("src/main/data/user.in"));
            list = (ArrayList<User>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("EmptyUser");
            return list;
        }
        return list;
    }

    public void addUser(User user){
        ArrayList<User> list = readUser();
        makeInitFolder();
        makeFolderUser(String.valueOf(list.size()));
        user.setIndex(String.valueOf(list.size()));
        FileImage fileImage = new FileImage();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("Avatar.jpg")));
        fileImage.saveAvatar("/Avatar.jpg", user, image);
        list.add(user);
        writeUser(list);
    }

    public void writeUser(ArrayList<User> list) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("src/main/data/user.in"));
            objectOutputStream.reset();
            objectOutputStream.writeObject(list);
            objectOutputStream.close();
            System.out.println("Sucessfully User");
        } catch (IOException e) {
            System.out.println("Erorr File User");
        }
    }

    public void updateUser(User user, User newUser){
        ArrayList<User> list = readUser();
        list.remove(Integer.parseInt(user.getIndex()));
        list.add(Integer.parseInt(newUser.getIndex()), newUser);
        writeUser(list);
    }

    public void makeFolder(String path){
        File folder = new File(path);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("Folder created successfully.");
            } else {
                System.out.println("Failed to create folder.");
            }
        } else {
            System.out.println("Folder already exists.");
        }
    }
    private void makeFolderUser(String index){
        String path = "src/main/data/imageuser/user" + index;
        makeFolder(path);
        makeFolder(path + "/avatar");
    }
    private void makeInitFolder(){
        makeFolder("src/main/data");
        makeFolder("src/main/data/imageuser");
        makeFolder("src/main/data/images");
    }
}
