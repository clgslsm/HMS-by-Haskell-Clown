package com.javafirebasetest.dao.receptionist;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.javafirebasetest.dao.DBManager;
import com.javafirebasetest.entity.Patient;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class GetPatientDAO {
    private static final DBManager dbManager = DBManager.getInstance();

    public static Patient getPatientByID(String patientID) throws ExecutionException, InterruptedException {
        Map<String, Object> patientData = dbManager.getDocumentById("Patients", patientID);

        return new Patient(patientID, patientData);
    }

    public static ArrayList<Patient> getPatientsByNamePhone(String name, String phoneNumber) {
        QuerySnapshot querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    "Patients",
                    Filter.equalTo("name", name),
                    Filter.equalTo("phoneNumber", phoneNumber)
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Patient> patientData = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot){
            patientData.add(new Patient(qds.getId(), qds.getData()));
        }

        return patientData;
    }

    public static ArrayList<Patient> getAllPatients() {
        QuerySnapshot querySnapshot;
        try {
            querySnapshot = dbManager.getAllDocuments("Patients");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Patient> patientData = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot){
            patientData.add(new Patient(qds.getId(), qds.getData()));
        }

        return patientData;
    }
}
