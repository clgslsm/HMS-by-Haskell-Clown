package com.javafirebasetest;

import com.javafirebasetest.dao.receptionist.PatientDAO;
import com.javafirebasetest.entity.Patient;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

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

//        Patient newPatient = new Patient(
//                null,
//                "Tai",
//                LocalDate.now(),
//                Patient.Gender.MALE,
//                "CT",
//                "123456",
//                Patient.BloodGroup.O_POSITIVE
//                );
//        PatientDAO.addPatient(newPatient);
//        System.out.println("ADDED");

//        List<Patient> patients = PatientDAO.getPatientsByName("Hiep Tai");
//        if (patients.size() <= 0){
//            throw new RuntimeException("No patient with such name");
//        }
//        Patient patient = patients.get(0);
//
//        PatientDAO.updatePatient(patient.getPatientId(), "name", "Hiep Tai", "phoneNumber", "456789");
//        System.out.println("updated");

//        Patient newPatient = new Patient(
//                "CO1027",
//                "ktlt",
//                LocalDate.now(),
//                Patient.Gender.FEMALE,
//                "CT",
//                "123456",
//                Patient.BloodGroup.O_POSITIVE
//        );
//
//        PatientDAO.addPatient(newPatient);
//
//        System.out.println("ADDED");
//
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        PatientDAO.deletePatient("CO1027");
//        System.out.println("Deleted");
    }
