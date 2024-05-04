package com.javafirebasetest.dao;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.Doctor;
import com.javafirebasetest.entity.MedicalRecord;
import com.javafirebasetest.entity.Patient;

import com.javafirebasetest.entity.TestResult;


import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.javafirebasetest.entity.HashPassword.getSHA;
import static com.javafirebasetest.entity.HashPassword.toHexString;


public class MedRecDAO {
    private static final DBManager dbManager = DBManager.getInstance();
    static String idPrefix = "MR";

    //CRUD

    //CREATE METHODS
    public static String addMedRec(MedicalRecord medRec) {
        String hexId = null;
        String newId = medRec.getMedRecId();

        if (MedRecDAO.getMedRecById(newId) == null){
            try {
                hexId = toHexString(getSHA(LocalDateTime.now().toLocalTime().toString()));
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e);
            }

            newId = idPrefix + hexId.substring(hexId.length() - (DBManager.idHashLength));
            DoctorDAO.updatePatientCount(medRec.getDoctorId(), 1);
        }

        dbManager.updateDocument(DBManager.CollectionPath.MEDICAL_RECORD, newId, medRec.toMap());

        return newId;


//        String output = medRec.getMedRecId();
//
//        if (MedRecDAO.getMedRecById(output) == null){
//            DoctorDAO.updatePatientCount(medRec.getDoctorId(), 1);
//        }
//
//        if (medRec.getMedRecId() == null) {
//            output = dbManager.addDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRec.toMap());
//        } else {
//            dbManager.updateDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRec.getMedRecId(), medRec.toMap());
//        }
//        return output;
    }

    //READ METHODS
    public static MedicalRecord getMedRecById(String medRecId) {
        try {
            Map<String, Object> medRecData = dbManager.getDocumentById(DBManager.CollectionPath.MEDICAL_RECORD, medRecId).getData();
            if (medRecData == null) return null;
            return new MedicalRecord(medRecId, medRecData);
        } catch (Exception e) {
            return null;
        }
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

        String relatedDocId = medrec.getDoctorId();

        if (medrec.getTestResult() != null){
            if (medrec.getTestResult().getAnalysisFilePath() != null){
                FileManager.deleteFile(medrec.getTestResult().getAnalysisFilePath());
            }
        }

        dbManager.deleteDocument(DBManager.CollectionPath.MEDICAL_RECORD, medRecId);

        if (medrec.getStatus() != MedicalRecord.Status.CHECKED_OUT)
            DoctorDAO.updatePatientCount(relatedDocId, -1);
    }

    //FRONTEND HELPER FUNCTIONS
    public static void send(String medRecId){
        MedicalRecord medrec = getMedRecById(medRecId);

        if (medrec.getStatus() == MedicalRecord.Status.TESTED){
            DoctorDAO.updatePatientCount(medrec.getDoctorId(), -1);
        }

        if (medrec.getStatus() == MedicalRecord.Status.DIAGNOSED){
            medrec.setCheckOut(Timestamp.now());
        }

        medrec.advanceStatus();

        addMedRec(medrec);
    }

    private static void send(String medRecId, int count){
        MedicalRecord medrec = getMedRecById(medRecId);

        if (medrec.getStatus() == MedicalRecord.Status.DIAGNOSED){
            medrec.setCheckOut(Timestamp.now());
        }

        for (int i = 0; i < count; ++i){
            medrec.advanceStatus();
        }

        addMedRec(medrec);
    }

    public static void send(String medRecId, boolean noTest){
        if (noTest) send(medRecId, 2);
        else send(medRecId);
    }

    private static void updateTestResult_AnalysisFilePath(String medRecId, String analysisFilePath) {
        MedicalRecord medrec = getMedRecById(medRecId);

        if (medrec.getTestResult().getAnalysisFilePath() != null){
            FileManager.deleteFile(medrec.getTestResult().getAnalysisFilePath());
        }

        String storagePath = FileManager.uploadFile(analysisFilePath, medRecId);
        TestResult newTestresult = new TestResult(
                null,
                storagePath,
                null,
                null
        );
        medrec.mergeTestResult(newTestresult);

        addMedRec(medrec);
    }

    /**
     * Put a new TestResult object in. Only the fields that are not null in the input object will be updated.
     * @param medRecId ID of the MedRec to update
     * @param newTestresult The TestResult object with new values for the fields. Use null for values not requiring changes.
     *
     */
    public static void updateTestResult(String medRecId, TestResult newTestresult){
        if (newTestresult.getAnalysisFilePath() != null){
            updateTestResult_AnalysisFilePath(medRecId, newTestresult.getAnalysisFilePath());
            return;
        }

        MedicalRecord medrec = getMedRecById(medRecId);
        medrec.mergeTestResult(newTestresult);

        addMedRec(medrec);
    }

    public static void viewAnalysisFile(String medRecId){
        getMedRecById(medRecId).openAnalysisFile();
    }

    public static void updateServiceRating(String medrecId, int reviewStars){
        updateMedRec(medrecId, "serviceRating", reviewStars);
    }

}