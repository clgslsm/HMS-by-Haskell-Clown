package com.javaswing;
import javax.swing.*;

import com.javafirebasetest.dao.DoctorDAO;
import com.javafirebasetest.dao.PatientDAO;
import com.javafirebasetest.dao.UserDAO;
import com.javafirebasetest.entity.DeptType;
import com.javafirebasetest.entity.Patient;
import com.javafirebasetest.entity.Receptionist;
import com.javafirebasetest.entity.User;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {
        SwingUtilities.invokeLater(MainPage::new);
//        DoctorDAO.createDoctor("doctor1", "doctor","nanh", DeptType.EYE);
//        DoctorDAO.createDoctor("doctor2", "doctor","ngoc anh", DeptType.EYE);
//          UserDAO.createUser("admin", "admin", User.Mode.ADMIN);
    }
}


