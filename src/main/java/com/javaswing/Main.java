package com.javaswing;


import com.javafirebasetest.dao._BackendTest;
import com.javafirebasetest.dao._DBPopulator;


import com.google.cloud.Timestamp;
import com.javafirebasetest.LoginManager;
import com.javafirebasetest.dao.DoctorDAO;
import com.javafirebasetest.dao.MedRecDAO;
import com.javafirebasetest.dao.PatientDAO;
import com.javafirebasetest.dao.UserDAO;
import com.javafirebasetest.entity.*;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {
        //SwingUtilities.invokeLater((Runnable) new MainPage("Doctor"));
        SwingUtilities.invokeLater((Runnable) new LoginPage());
//        System.out.println(DoctorDAO.getAllDoctorID());
//          Doctor doctor = DoctorDAO.getDoctorWithMinPatientCountByDepartment(DeptType.PLASTIC_SURGERY);
//          Patient patient = PatientDAO.getPatientById("A216");
//          if (doctor != null) {
//              System.out.println(doctor.toString());
//              MedicalRecord medRec = MedRecDAO.addMedRecByDoctorAndPatient(doctor, patient);
//              System.out.println(medRec);
//          }
//          else {
//              System.out.println("We can not find any doctor");
//          }
//        SwingUtilities.invokeLater((Runnable) new MainPage("Doctor"));
//        MedicalRecord medicalRecord = new MedicalRecord("medrec21", "A216",
//        DeptType.EYE, "0e8Z7qsDKt4DJEcWy7KN", Timestamp.now(), Timestamp.now(), "ABC", MedicalRecord.Status.PENDING, "Good", "Good");
//        MedRecDAO.addMedRec(medicalRecord);
//        DoctorDAO.createDoctor("doctor1", "doctor"
//        ,"nanh", DeptType.EYE);
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
//        MainPage mainPage = new MainPage("Doctor");
//        Thread thread = new Thread(String.valueOf(mainPage));
//        thread.start();

//        _BackendTest.MedicineExportTest();
//        _DBPopulator.populate(true);
        _BackendTest.MedrecTest("C:\\Users\\ACER\\Desktop\\smileyFace.png");
    }
}


