package com.javaswing;
<<<<<<< HEAD

=======
import javax.swing.*;
import com.javafirebasetest.dao.DoctorDAO;
import com.javafirebasetest.dao.PatientDAO;
import com.javafirebasetest.dao.UserDAO;
import com.javafirebasetest.entity.DeptType;
import com.javafirebasetest.entity.Patient;
import com.javafirebasetest.entity.Receptionist;
import com.javafirebasetest.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.javafirebasetest.dao.DBManager;
>>>>>>> ad772331ba671cff71e9ca796461cba8af6f37ae
import com.javafirebasetest.dao.DoctorDAO;
import com.javafirebasetest.entity.DeptType;
import com.javafirebasetest.entity.Doctor;

<<<<<<< HEAD
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) {
        Doctor newDoctor = new Doctor(
          "lmao",
          "Strange",
          DeptType.PSYCHIATRY
        );

        DoctorDAO.addDoctor(newDoctor);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        DoctorDAO.updateDoctor("lmao", "department", DeptType.DENTAL.getValue());

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        DoctorDAO.deleteDoctorById("lmao");
//        SwingUtilities.invokeLater(ReceptionistUI::new);
=======
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {
//        SwingUtilities.invokeLater(MainPage::new);
//        DoctorDAO.createDoctor("doctor1", "doctor","nanh", DeptType.EYE);
//        DoctorDAO.createDoctor("doctor2", "doctor","ngoc anh", DeptType.EYE);
//          UserDAO.createUser("admin", "admin", User.Mode.ADMIN);
>>>>>>> ad772331ba671cff71e9ca796461cba8af6f37ae
    }

}


