package main.prolocktech.controller;

import main.prolocktech.model.Picture;

import java.util.ArrayList;

public class ManagerPicture {
    public String getTime(Picture picture) {
        long createTine = picture.getTimeCreated();
        long now = System.currentTimeMillis();
        long duration = now - createTine;
        duration/=1000;
        String time;
        if (duration < 60) {
            time = duration + "sec";
        }
        else if (duration < 3600) {
            time = duration / 60 + "min";
        }
        else if (duration < 86400) {
            time = duration / 3600 + "h";
        }
        else {
            time = duration / 86400 + "days";
        }
        return time;
    }
    private int cmp(Picture a, Picture b) {
        if (a.getTimeCreated() < b.getTimeCreated()) return 1;
        return -1;
    }

    public ArrayList<Picture> getPictures(ArrayList<Picture> listPictures){
        listPictures.sort(this::cmp);
        return listPictures;
    }
}
