package com.javafirebasetest.dao;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.Doctor;
import com.javafirebasetest.entity.MedicalRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DoctorDAO {
    private static final DBManager dbManager = DBManager.getInstance();

    //CRUD

    //READ METHODS
    public static Doctor getDoctorById(String doctorId){

        Map<String, Object> doctorData = null;
        try {
            doctorData = dbManager.getDocumentById(DBManager.CollectionPath.STAFF, doctorId).getData();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Doctor Id does not exist"  + e.toString() );
        }

        assert doctorData != null;
        return new Doctor(doctorId, doctorData);
    }
    public static List<MedicalRecord> getMedRecByPatientId(String patientId) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.MEDICAL_RECORD,
                    Filter.equalTo("patientId", patientId)
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<MedicalRecord> medRecList = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            medRecList.add(new MedicalRecord(qds.getId(), qds.getData()));
        }
        return medRecList;
    }

    public static List<MedicalRecord> getMedRecByDoctorId(String doctorId) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.MEDICAL_RECORD,
                    Filter.equalTo("doctorId", doctorId)
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<MedicalRecord> medRecList = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            medRecList.add(new MedicalRecord(qds.getId(), qds.getData()));
        }
        return medRecList;
    }

    public static List<MedicalRecord> getAllMedRec() {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getAllDocuments(DBManager.CollectionPath.MEDICAL_RECORD);

        List<MedicalRecord> medRecData = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            medRecData.add(new MedicalRecord(qds.getId(), qds.getData()));
        }

        return medRecData;
    }
    //UPDATE METHODS
    public static void updateMedRec(String medRecID, Object... fieldsAndValues) {
        Map<String, Object> newData = new HashMap<>();
        for (int i = 0; i < fieldsAndValues.length; i += 2) {
            newData.put((String) fieldsAndValues[i], fieldsAndValues[i + 1]);
        }
        dbManager.updateDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRecID, newData);
    }

}
