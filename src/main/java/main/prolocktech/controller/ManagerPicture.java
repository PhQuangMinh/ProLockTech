package main.prolocktech.controller;

import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

public class ManagerPicture {
    public String getTime(File file){
        Path path = file.toPath();

        try {
            BasicFileAttributes fatr = Files.readAttributes(path, BasicFileAttributes.class);
            long createTine = fatr.creationTime().toMillis();
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
