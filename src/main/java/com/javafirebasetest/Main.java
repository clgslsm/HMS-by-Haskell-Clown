package com.javafirebasetest;

import com.javafirebasetest.dao.receptionist.GetPatientDAO;
import com.javafirebasetest.entity.Patient;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //GET PATIENTDAO DEMO
//        ArrayList<Map<String, Object>> staffs = ();
//        for (Map<String, Object> staff : staffs) {
//            System.out.print(staff);
//            System.out.println('\n');
//        }
//        ArrayList<Patient> patient = GetPatientDAO.getPatientsByNamePhone("Emily Brown", "+5189190728");
        ArrayList<Patient> patient = GetPatientDAO.getAllPatients();
        System.out.println(patient);
//        DBManager.getInstance().populateData();
    }


}