package com.javaswing;


import com.javafirebasetest.dao._DBPopulator;


import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {
        //SwingUtilities.invokeLater((Runnable) new MainPage("Doctor"));
        SwingUtilities.invokeLater(LoginPage::new);
//        _DBPopulator.populate(true);

    }
}


