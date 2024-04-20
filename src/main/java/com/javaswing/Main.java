package com.javaswing;


import com.javafirebasetest.dao._BackendTest;
import com.javafirebasetest.dao._DBPopulator;


import com.google.cloud.Timestamp;
import com.javafirebasetest.LoginManager;
import com.javafirebasetest.dao.DoctorDAO;
import com.javafirebasetest.dao.MedRecDAO;
import com.javafirebasetest.dao.PatientDAO;
import com.javafirebasetest.dao.UserDAO;
import com.javafirebasetest.entity.*;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {
//        MainPage mainPage = new MainPage("Doctor");
//        Thread thread = new Thread(String.valueOf(mainPage));
//        thread.start();

//        _BackendTest.MedicineExportTest();
//        _DBPopulator.populate(true);
        _BackendTest.MedrecTest("C:\\Users\\ACER\\Desktop\\smileyFace.png");
    }
}


