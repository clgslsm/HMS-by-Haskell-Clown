package com.javafirebasetest.dao;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.MedicalRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedRecDAO {
    private static final DBManager dbManager = DBManager.getInstance();

    //CRUD

    //CREATE METHODS
    public static String addMedRec(MedicalRecord medRec) {
        if (medRec.getmedicalRecordId() == null) {
            return dbManager.addDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRec.toMap());
        } else {
            dbManager.updateDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRec.getmedicalRecordId(), medRec.toMap());
            return medRec.getmedicalRecordId();
        }
    }

    //READ METHODS
    public static MedicalRecord getMedRecById(String medRecID) {
        Map<String, Object> medRecData = null;
        medRecData = dbManager.getDocumentById(DBManager.CollectionPath.MEDICAL_RECORD, medRecID).getData();
        assert medRecData != null;
        return new MedicalRecord(medRecID, medRecData);
    }

    public static List<MedicalRecord> getMedRecByPatientId(String patientId) {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.MEDICAL_RECORD,
                Filter.equalTo("patientId", patientId)
        );

        List<MedicalRecord> medRecList = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            medRecList.add(new MedicalRecord(qds.getId(), qds.getData()));
        }
        return medRecList;
    }

    public static List<MedicalRecord> getMedRecByDoctorId(String doctorId) {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.MEDICAL_RECORD,
                Filter.equalTo("doctorId", doctorId)
        );

        List<MedicalRecord> medRecList = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            medRecList.add(new MedicalRecord(qds.getId(), qds.getData()));
        }
        return medRecList;
    }

    public static List<MedicalRecord> getMedRecByCondition(Object... fieldsAndValues) {
        Filter[] filters = new Filter[fieldsAndValues.length/2];

        for (int i = 0; i < fieldsAndValues.length; i += 2){
            filters[i/2] = Filter.equalTo((String) fieldsAndValues[i], fieldsAndValues[i+1]);
        }

        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.MEDICAL_RECORD,
                filters
        );

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

    //DELETE METHODS
    public static void deleteMedRec(String medRecID) {
        dbManager.deleteDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRecID);
    }
}
