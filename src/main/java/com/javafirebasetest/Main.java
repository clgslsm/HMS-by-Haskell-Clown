package com.javafirebasetest;

import com.javafirebasetest.dao.receptionist.PatientDAO;
import com.javafirebasetest.entity.Patient;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        //GET PATIENTDAO DEMO
//        ArrayList<Map<String, Object>> staffs = ();
//        for (Map<String, Object> staff : staffs) {
//            System.out.print(staff);
//            System.out.println('\n');
//        }
//        List<Patient> patient = GetPatientDAO.getPatientsByName("Emily Brown");
//        System.out.println(patient);

//        Patient patient = GetPatientDAO.getPatientByID("4oBtE8CC0HDeTL5wtjgG");
//        System.out.println(patient);

//        DBManager.getInstance().populateData();

        Patient patient = new Patient(
                null,
                "Tai",
                LocalDate.of(2004, 5, 15),
                Patient.Gender.MALE,
                "VN",
                "123456",
                Patient.BloodGroup.O_POSITIVE
        );

        PatientDAO.addPatient(patient);
    }


}