package com.javaswing;

import com.formdev.flatlaf.FlatLightLaf;
import com.javafirebasetest.dao._BackendTest;
import com.javafirebasetest.dao._DBPopulator;


import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {
        //SwingUtilities.invokeLater((Runnable) new MainPage("Doctor"));
        FlatLightLaf.setup();
        UIManager.put("TextComponent.arc",10);
        UIManager.put("Button.arc",20);
        System.setProperty("flatlaf.useWindowDecorations","true");
        SwingUtilities.invokeLater(LoginPage::new);
//        _DBPopulator.populate(true);

    }
}


