package com.javaswing;
import javax.swing.*;
import com.javafirebasetest.dao.receptionist.PatientDAO;
import com.javafirebasetest.entity.Patient;
import com.javafirebasetest.entity.Receptionist;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Patient newPatient = new Patient(
                "CO1027",
                "ktlt",
                LocalDate.now(),
                Patient.Gender.FEMALE,
                "CT",
                "123456",
                Patient.BloodGroup.O_POSITIVE
        );

        PatientDAO.addPatient(newPatient);

        System.out.println("ADDED");

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(ReceptionistUI::new);
    }
}


