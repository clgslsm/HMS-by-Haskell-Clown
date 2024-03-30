package com.javafirebasetest.dao.receptionist;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.dao.DBManager;
import com.javafirebasetest.entity.Patient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PatientDAO {
    private static final DBManager dbManager = DBManager.getInstance();

    //GET METHODS
    public static Patient getPatientByID(String patientID) throws ExecutionException, InterruptedException {
        Map<String, Object> patientData = dbManager.getDocumentById(DBManager.CollectionPath.PATIENT, patientID).getData();

        assert patientData != null;
        return new Patient(patientID, patientData);
    }
    public static List<Patient> getPatientsByName(String name) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.PATIENT,
                    Filter.equalTo("name", name)
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Patient> patientData = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            patientData.add(new Patient(qds.getId(), qds.getData()));
        }

        return patientData;
    }
    public static List<Patient> getPatientsByPhone(String phoneNumber) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.PATIENT,
                    Filter.equalTo("phoneNumber", phoneNumber)
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Patient> patientData = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            patientData.add(new Patient(qds.getId(), qds.getData()));
        }

        return patientData;
    }
    public static List<Patient> getPatientsByBloodGroup(Patient.BloodGroup bloodGroup) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.PATIENT,
                    Filter.equalTo("bloodGroup", bloodGroup.getValue())
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Patient> patientData = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            patientData.add(new Patient(qds.getId(), qds.getData()));
        }

        return patientData;
    }
    public static List<Patient> getAllPatients() {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getAllDocuments(DBManager.CollectionPath.PATIENT);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Patient> patientData = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            patientData.add(new Patient(qds.getId(), qds.getData()));
        }

        return patientData;
    }

    //ADD METHODS
    public static void addPatient(Patient patient) throws ExecutionException, InterruptedException {

        dbManager.addDocument(DBManager.CollectionPath.PATIENT, patient.toMap());
    }
}
