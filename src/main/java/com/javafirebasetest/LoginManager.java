package com.javafirebasetest;

import com.javafirebasetest.dao.UserDAO;
import com.javafirebasetest.entity.User;

public class LoginManager {
    private static User userInstance = null;

    public static boolean login(String username, String password) {
        if (userInstance != null) {
            throw new RuntimeException("Already logged in");
        }
        userInstance = UserDAO.getUserByUsernamePassword(username, password);
        return userInstance != null;
    }

    public static void logout() {
        if (userInstance == null) {
            throw new RuntimeException("Not logged in");
        }
        userInstance = null;
    }

    public static User getUserInstance() {
        if (userInstance == null) {
            throw new RuntimeException("Not logged in");
        }
        return userInstance;
    }
}
