package com.javaswing;

import com.javafirebasetest.LoginManager;
import com.javafirebasetest.dao.UserDAO;
import com.javafirebasetest.entity.User;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {
        SwingUtilities.invokeLater(MainPage::new);
//        DoctorDAO.createDoctor("doctor1", "doctor","nanh", DeptType.EYE);
//        DoctorDAO.createDoctor("doctor2", "doctor","ngoc anh", DeptType.EYE);
//          UserDAO.createUser("admin", "admin", User.Mode.ADMIN);
//=======
//
//    public static void main(String[] args) {
//        boolean res = LoginManager.login("Doctor19", "Doctor19");
//
//        System.out.println(res);
//
//        System.out.println(LoginManager.getUserInstance());
//
//        LoginManager.logout();
//
//        System.out.println(LoginManager.getUserInstance());
//>>>>>>> 7689022763cca0e69785073b79840111630456de
    }
}


