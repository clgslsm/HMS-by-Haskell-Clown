package com.javaswing;

import com.javafirebasetest.dao.DoctorDAO;
import com.javafirebasetest.dao.StaffDAO;
import com.javafirebasetest.entity.DeptType;
import com.javafirebasetest.entity.Doctor;
import com.javafirebasetest.entity.Staff;
import org.checkerframework.checker.units.qual.Time;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleConsumer;

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


