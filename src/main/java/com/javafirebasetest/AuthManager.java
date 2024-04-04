package com.javafirebasetest;

import com.javafirebasetest.entity.Doctor;
import com.javafirebasetest.entity.User;
import com.javafirebasetest.entity.User.Mode;
import com.javafirebasetest.dao.UserDAO;

import java.util.concurrent.ExecutionException;

public class AuthManager {
    private  UserDAO userDAO;
    public User login(String username, String enterPassword) throws ExecutionException, InterruptedException {
        User user = UserDAO.getUserByUsername(username);
        if (user != null && UserDAO.authenticateUser(user, enterPassword)) {
            return user;
        }
        return null;
    }

    public void printDoctorInformation(User user) {
        if (user.getUserMode() == Mode.DOCTOR) {
            System.out.println(user.getID());
        } else {
            System.out.println("User is not a doctor.");
        }
    }
}
