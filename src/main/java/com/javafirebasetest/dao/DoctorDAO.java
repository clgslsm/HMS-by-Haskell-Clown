package com.javafirebasetest.dao;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.DeptType;
import com.javafirebasetest.entity.Doctor;
import com.javafirebasetest.entity.MedicalRecord;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

import static com.javafirebasetest.entity.HashPassword.getSHA;
import static com.javafirebasetest.entity.HashPassword.toHexString;

public class DoctorDAO {
    private static final DBManager dbManager = DBManager.getInstance();
    static String idPrefix = "DO";
    //CRUD

    public static String addDoctor(Doctor doctor) {
        String hexId = null;
        String newId = doctor.getStaffId();

        if (getDoctorById(newId) == null){
            try {
                hexId = toHexString(getSHA(LocalDateTime.now().toLocalTime().toString()));
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e);
            }

            newId = idPrefix + hexId.substring(hexId.length() - (DBManager.idHashLength));
        }
        dbManager.updateDocument(DBManager.CollectionPath.STAFF, newId, doctor.toMap());

        return newId;
//        if (doctor.getStaffId() == null) {
//            return dbManager.addDocument(DBManager.CollectionPath.STAFF, doctor.toMap());
//        } else {
//            dbManager.updateDocument(DBManager.CollectionPath.STAFF, doctor.getStaffId(), doctor.toMap());
//            return doctor.getStaffId();
//        }
    }

    //READ METHODS
    public static Doctor getDoctorById(String doctorId) {
        Map<String, Object> doctorData;
        try {
            doctorData = dbManager.getDocumentById(DBManager.CollectionPath.STAFF, doctorId).getData();
        }
        catch (Exception err){
            return null;
        }
        if (doctorData == null) return null;
        return new Doctor(doctorId, doctorData);
    }
    public static List<Doctor> getDoctorByName(String doctorName) {
        List<QueryDocumentSnapshot> querySnapshot;

        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.STAFF,
                Filter.greaterThanOrEqualTo("name", doctorName),
                Filter.lessThanOrEqualTo("name", doctorName + "\uf7ff")
        );

        List<Doctor> doctorList = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            doctorList.add(new Doctor(qds.getId(), qds.getData()));
        }
        return doctorList;
    }
    public static List<Doctor> getDoctorByDepartment(DeptType deptType) {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.STAFF,
                Filter.equalTo("department", deptType.getValue())
        );

        List<Doctor> doctorList = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            doctorList.add(new Doctor(qds.getId(), qds.getData()));
        }
        return doctorList;
    }
    public static Doctor getDoctorWithMinPatientCountByDepartment(DeptType deptType) {
        List<Doctor> doctorList = getDoctorByDepartment(deptType);
        int minPatientCount = Integer.MAX_VALUE;
        Doctor doctorWithMinPatientCount = null;
        for (Doctor doctor : doctorList) {
            List<MedicalRecord> medRecList = MedRecDAO.getMedRecByDoctorId(doctor.getStaffId());
            int patientCount = medRecList.size();
            if (patientCount < minPatientCount) {
                minPatientCount = patientCount;
                doctorWithMinPatientCount = doctor;
            }
        }
        return doctorWithMinPatientCount;
    }

    public static List<Doctor> getAllDoctor() {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.STAFF,
                Filter.equalTo("userMode", "Doctor")
        );

        List<Doctor> doctorList = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            doctorList.add(new Doctor(qds.getId(), qds.getData()));
        }
        return doctorList;
    }

    public static List<String> getAllDoctorID() {
        List<QueryDocumentSnapshot> querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.STAFF,
                Filter.equalTo("userMode", "Doctor")
        );
        List<String> doctorIDList = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            doctorIDList.add(qds.getId());
        }
        return doctorIDList;
    }
    public static void updateDoctorPatientCount(String doctorId) {
        List<MedicalRecord> medRecList = MedRecDAO.getMedRecByDoctorId(doctorId);
        int patientCount = medRecList.size();
        updateDoctor(doctorId, "patientCount", patientCount);
    }
    public static void updateAllDoctorPatientCount() {
        List<String> doctorIDList = getAllDoctorID();
        for (String doctorId: doctorIDList) {
            updateDoctorPatientCount(doctorId);
        }
    }
    //UPDATE METHODS
    public static void updateDoctor(String doctorId, Object... fieldsAndValues) {
        Map<String, Object> newData = new HashMap<>();
        for (int i = 0; i < fieldsAndValues.length; i += 2) {
            newData.put((String) fieldsAndValues[i], fieldsAndValues[i + 1]);
        }
        dbManager.updateDocument(DBManager.CollectionPath.STAFF, doctorId, newData);
    }

    //DELETE METHODS
    public static void deleteDoctorById(String doctorId) {
        dbManager.deleteDocument(DBManager.CollectionPath.STAFF, doctorId);
    }


    //FRONTEND HELPER FUNCTIONS

    public static Doctor getMatchFromDepartment(DeptType deptType) {
        List<QueryDocumentSnapshot> querySnapshot;

        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.STAFF,
                Filter.equalTo("department", deptType.getValue())
        );

        List<Doctor> doctorList = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            doctorList.add(new Doctor(qds.getId(), qds.getData()));
        }
        doctorList.sort(new Comparator<Doctor>() {
            @Override
            public int compare(Doctor o1, Doctor o2) {
                return Long.compare(o1.getPatientCount(), o2.getPatientCount());
            }
        });
        if (doctorList.getFirst().getPatientCount() >= Doctor.PATIENT_LIMIT) return null;
        return doctorList.getFirst();
    }

    public static void updatePatientCount(String doctorId, int incr) {
        Doctor doc = getDoctorById(doctorId);
        if (doc == null) return;
        Long newCount = doc.getPatientCount() + incr;

        if (newCount < 0) newCount = 0L;

        DoctorDAO.updateDoctor(doctorId, "patientCount", newCount);
    }

}
