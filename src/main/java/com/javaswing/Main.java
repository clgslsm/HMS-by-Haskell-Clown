package com.javaswing;


import com.javafirebasetest.dao._DBPopulator;


import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {
//        MainPage mainPage = new MainPage("Doctor");
//        Thread thread = new Thread(String.valueOf(mainPage));
//        thread.start();

//        _BackendTest.MedicineExportTest();
        _DBPopulator.populate(true);
//        _BackendTest.MedrecTest("C:\\Users\\ACER\\Desktop\\smileyFace.png");
    }
}


