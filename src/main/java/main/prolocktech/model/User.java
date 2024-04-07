package main.prolocktech.model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String index;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private DateOfBirth date;
    private int numberOfImages;
    public User(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String firstName, String lastName, String email, String password, DateOfBirth date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.date = date;
        this.numberOfImages = 0;
    }

    public User(String index, String firstName, String lastName, String email, String password, DateOfBirth date, int numberOfImages) {
        this.index = index;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.date = date;
        this.numberOfImages = numberOfImages;
    }


    public int getNumberOfImages() {
        return numberOfImages;
    }

    public void setNumberOfImages(int numberOfImages) {
        this.numberOfImages = numberOfImages;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DateOfBirth getDate() {
        return date;
    }

    public void setDate(DateOfBirth date) {
        this.date = date;
    }

    @Override
    public String toString(){
        return firstName + " " + lastName;
    }

}
