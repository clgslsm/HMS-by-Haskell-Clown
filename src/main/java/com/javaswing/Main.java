package com.javaswing;

import com.javafirebasetest.LoginManager;
import com.javafirebasetest.dao.FileManager;
import com.javafirebasetest.dao.MedRecDAO;
import com.javafirebasetest.dao.UserDAO;
import com.javafirebasetest.entity.MedicalRecord;
import com.javafirebasetest.entity.User;

import java.io.File;
import java.io.IOException;
import java.lang.ref.Cleaner;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        FileManager fileManager = FileManager.getInstance();
        File localFile = new File("C:\\Users\\ACER\\Desktop\\Screenshot 2024-04-17 131533.png");

        FileManager.uploadFile(localFile.getPath());

        FileManager.downloadFile("MedicalRecords/Screenshot 2024-04-17 131533.png");

        FileManager.openFileWithDefaultApp("MedicalRecords/Screenshot 2024-04-17 131533.png");

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileManager.deleteFile("MedicalRecords/Screenshot 2024-04-17 131533.png");

        FileManager.cleanUp();
    }
}


