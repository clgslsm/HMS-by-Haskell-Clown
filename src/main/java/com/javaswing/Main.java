package com.javaswing;
import javax.swing.*;

import com.javafirebasetest.dao.UserDAO;
import com.javafirebasetest.dao.receptionist.PatientDAO;
import com.javafirebasetest.entity.Patient;
import com.javafirebasetest.entity.Receptionist;
import com.javafirebasetest.entity.User;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.javafirebasetest.entity.User.Mode.ADMIN;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {

//        SwingUtilities.invokeLater(ReceptionistUI::new
        UserDAO.createUser("admin", "admin", ADMIN);
    }
}


