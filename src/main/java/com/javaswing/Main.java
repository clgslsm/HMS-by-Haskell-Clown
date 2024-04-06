package com.javaswing;

import com.javafirebasetest.LoginManager;
import com.javafirebasetest.dao.UserDAO;
import com.javafirebasetest.entity.User;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        boolean res = LoginManager.login("Doctor19", "Doctor19");

        System.out.println(res);

        System.out.println(LoginManager.getUserInstance());

        LoginManager.logout();

        System.out.println(LoginManager.getUserInstance());
    }
}


