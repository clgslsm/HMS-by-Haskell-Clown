package com.javaswing;

import com.javafirebasetest.dao.DoctorDAO;
import com.javafirebasetest.entity.DeptType;
import com.javafirebasetest.entity.Doctor;

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
    }

}


