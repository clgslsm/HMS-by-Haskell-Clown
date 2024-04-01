package com.javafirebasetest.dao.receptionist;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.dao.DBManager;
import com.javafirebasetest.entity.MedicalRecord;
import com.javafirebasetest.entity.Patient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MedRecDAO {
    private static final DBManager dbManager = DBManager.getInstance();

    //CRUD

    //CREATE METHODS
    public static void addMedRec(MedicalRecord medRec) {
        if (medRec.getmedicalRecordId() == null) {
            dbManager.addDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRec.toMap());
        } else {
            dbManager.updateDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRec.getmedicalRecordId(), medRec.toMap());
        }
    }

    //READ METHODS
    public static MedicalRecord getMedRecById(String medRecID){

        Map<String, Object> medRecData = null;
        try {
            medRecData = dbManager.getDocumentById(DBManager.CollectionPath.MEDICAL_RECORD, medRecID).getData();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Medical Record Id does not exist"  + e.toString() );
        }

        assert medRecData != null;
        return new MedicalRecord(medRecID, medRecData);
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
    //DELETE METHODS
    public  static void deleteMedRec(String medRecID){
        try {
            dbManager.deleteDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRecID);
        } catch (Exception e) {
            throw new RuntimeException("Delete failed: Medical Record does not exist/" + e.toString());
        }
    }
}
