package com.javafirebasetest.dao;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.MedicalRecord;
import com.javafirebasetest.entity.Patient;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.javafirebasetest.entity.HashPassword.getSHA;
import static com.javafirebasetest.entity.HashPassword.toHexString;

public class PatientDAO {
    private static final DBManager dbManager = DBManager.getInstance();
    static String idPrefix = "PA";
    //CRUD

    //CREATE METHODS
    public static String addPatient(Patient patient) {
        String hexId = null;
        String newId = patient.getPatientId();

        if (getPatientById(newId) == null){
            try {
                hexId = toHexString(getSHA(LocalDateTime.now().toLocalTime().toString()));
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e);
            }

            newId = idPrefix + hexId.substring(hexId.length() - (DBManager.idHashLength));
        }

        dbManager.updateDocument(DBManager.CollectionPath.PATIENT, newId, patient.toMap());

        return newId;
//        if (patient.getPatientId() == null) {
//            return dbManager.addDocument(DBManager.CollectionPath.PATIENT, patient.toMap());
//        } else {
//            dbManager.updateDocument(DBManager.CollectionPath.PATIENT, patient.getPatientId(), patient.toMap());
//            return patient.getPatientId();
//        }
    }

    public String getHashPassword(String password) throws NoSuchAlgorithmException {
        return toHexString(getSHA(password));
    }

    //READ METHODS
    public static Patient getPatientById(String patientID){
        Map<String, Object> patientData;

        try {
            patientData = dbManager.getDocumentById(DBManager.CollectionPath.PATIENT, patientID).getData();
        }
        catch (Exception err){
            return null;
        }

        if (patientData == null) return null;
        return new Patient(patientID, patientData);
    }

    public static List<Patient> getPatientsByName(String name) {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.PATIENT,
                Filter.greaterThanOrEqualTo("name", name),
                Filter.lessThanOrEqualTo("name", name + "\uf7ff")
        );

        List<Patient> patientList = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            patientList.add(new Patient(qds.getId(), qds.getData()));
        }
        return patientList;
    }

    public static List<Patient> getPatientsByPhone(String phoneNumber) {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.PATIENT,
                Filter.equalTo("phoneNumber", phoneNumber)
        );

        List<Patient> patientData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            patientData.add(new Patient(qds.getId(), qds.getData()));
        }
        return patientData;
    }

    public static List<Patient> getPatientsByBloodGroup(Patient.BloodGroup bloodGroup) {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.PATIENT,
                Filter.equalTo("bloodGroup", bloodGroup.getValue())
        );

        List<Patient> patientData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            patientData.add(new Patient(qds.getId(), qds.getData()));
        }

        return patientData;
    }

    public static Patient getPatientByHIN(String healthInsurance) {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.PATIENT,
                Filter.equalTo("healthInsuranceNumber", healthInsurance)
        );

        List<Patient> patientData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            patientData.add(new Patient(qds.getId(), qds.getData()));
        }

        return patientData.getFirst();
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
    public static void deletePatient(String patientID) {
        List<MedicalRecord> medrecList = MedRecDAO.getMedRecByPatientId(patientID);
        for (MedicalRecord medrec : medrecList){
            MedRecDAO.deleteMedRec(medrec.getMedRecId());
        }

        dbManager.deleteDocument(DBManager.CollectionPath.PATIENT, patientID);
    }
}
