package com.javafirebasetest.dao;

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

    //CRUD

    //CREATE METHODS
    public static void addPatient(Patient patient) {
        if (patient.getPatientId() == null) {
            dbManager.addDocument(DBManager.CollectionPath.PATIENT, patient.toMap());
        } else {
            dbManager.updateDocument(DBManager.CollectionPath.PATIENT, patient.getPatientId(), patient.toMap());
        }

    }

    //READ METHODS
    public static Patient getPatientById(String patientID) throws ExecutionException, InterruptedException {
        Map<String, Object> patientData = dbManager.getDocumentById(DBManager.CollectionPath.PATIENT, patientID).getData();

        assert patientData != null;
        return new Patient(patientID, patientData);
    }
    public static List<Patient> getPatientsByName(String name) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.PATIENT,
                    Filter.and(Filter.greaterThanOrEqualTo("name", name),
                            Filter.lessThanOrEqualTo("name", name + "uf7ff"))
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Patient> patientList = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            patientList.add(new Patient(qds.getId(), qds.getData()));
        }

        return patientList;
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
        querySnapshot = dbManager.getAllDocuments(DBManager.CollectionPath.PATIENT);

        List<Patient> patientData = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            patientData.add(new Patient(qds.getId(), qds.getData()));
        }

        return patientData;
    }
    //UPDATE METHODS
    public static void updatePatient(String patientID, Object... fieldsAndValues) {
        Map<String, Object> newData = new HashMap<>();
        for (int i = 0; i < fieldsAndValues.length; i += 2) {
            newData.put((String) fieldsAndValues[i], fieldsAndValues[i + 1]);
        }
        dbManager.updateDocument(DBManager.CollectionPath.PATIENT, patientID, newData);
    }
    //DELETE METHODS
    public  static void deletePatient(String patientID){
        try {
            dbManager.deleteDocument(DBManager.CollectionPath.PATIENT, patientID);
        } catch (Exception e) {
            throw new RuntimeException("Delete failed: Patient doesnt exist/" + e.toString());
        }
    }
}
