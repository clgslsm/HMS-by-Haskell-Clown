package com.javafirebasetest.dao;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.MedicalRecord;
import com.javafirebasetest.entity.TestResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedRecDAO {
    private static final DBManager dbManager = DBManager.getInstance();

    //CRUD

    //CREATE METHODS
    public static String addMedRec(MedicalRecord medRec) {
        if (medRec.getMedRecId() == null) {
            return dbManager.addDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRec.toMap());
        } else {
            dbManager.updateDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRec.getMedRecId(), medRec.toMap());
            return medRec.getMedRecId();
        }
    }

    //READ METHODS
    public static MedicalRecord getMedRecById(String medRecId) {
        Map<String, Object> medRecData = dbManager.getDocumentById(DBManager.CollectionPath.MEDICAL_RECORD, medRecId).getData();
        assert medRecData != null;
        return new MedicalRecord(medRecId, medRecData);
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

    public static List<MedicalRecord> getMedRecBy_doctorId_status(String doctorId, MedicalRecord.Status status) {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.MEDICAL_RECORD,
                Filter.equalTo("doctorId", doctorId),
                Filter.equalTo("status", status.toString())
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
    public static void updateMedRec(String medRecId, Object... fieldsAndValues) {
        Map<String, Object> newData = new HashMap<>();
        for (int i = 0; i < fieldsAndValues.length; i += 2) {
            newData.put((String) fieldsAndValues[i], fieldsAndValues[i + 1]);
        }
        dbManager.updateDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRecId, newData);
    }

    //DELETE METHODS
    public static void deleteMedRec(String medRecId) {
        MedicalRecord medrec = getMedRecById(medRecId);
        FileManager.deleteFile(medrec.getTestResult().getAnalysisFilePath());
        dbManager.deleteDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRecId);
    }

    //FRONTEND HELPER FUNCTIONS
    public void send(String medRecId){
        MedicalRecord medrec = getMedRecById(medRecId);
        medrec.advanceStatus();

        addMedRec(medrec);
    }

    /**
     * Put a new TestResult object in. Only the fields that are not null in the input object will be updated.
     * CANNOT CHANGE analysisFilePath, please use updateTestResult_AnalysisFilePath().
     * @param medRecId ID of the MedRec to update
     * @param newTestresult The TestResult object with new values for the fields. Use null for values not requiring changes.
     *
     */
    public void updateTestResult(String medRecId, TestResult newTestresult){
        MedicalRecord medrec = getMedRecById(medRecId);
        newTestresult.setAnalysisFilePath(null);
        medrec.mergeTestResult(newTestresult);

        addMedRec(medrec);
    }

    public void updateTestResult_AnalysisFilePath(String medRecId, String analysisFilePath){
        MedicalRecord medrec = getMedRecById(medRecId);

        if (medrec.getTestResult().getAnalysisFilePath() != null){
            FileManager.deleteFile(medrec.getTestResult().getAnalysisFilePath());
        }

        String storagePath = FileManager.uploadFile(analysisFilePath);
        medrec.mergeTestResult(new TestResult(
                null,
                storagePath,
                null,
                null
        ));

        addMedRec(medrec);
    }

    public void viewAnalysisFile(String medRecId){
        getMedRecById(medRecId).openAnalysisFile();
    }

    public void performCheckout(String medRecId){
        MedicalRecord medrec = getMedRecById(medRecId);

        if (medrec.getStatus() != MedicalRecord.Status.CHECKED_OUT)
            throw new RuntimeException("Medrec with id " + medRecId + " is not ready to checkout!");
        updateMedRec(medRecId, "checkOut", Timestamp.now());
    }
}