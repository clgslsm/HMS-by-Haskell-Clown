package com.javafirebasetest.dao;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.DeptType;
import com.javafirebasetest.entity.Doctor;
import com.javafirebasetest.entity.MedicalRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorDAO {
    private static final DBManager dbManager = DBManager.getInstance();
    //CRUD

    public static String addDoctor(Doctor doctor) {
        if (doctor.getStaffId() == null) {
            return dbManager.addDocument(DBManager.CollectionPath.STAFF, doctor.toMap());
        } else {
            dbManager.updateDocument(DBManager.CollectionPath.STAFF, doctor.getStaffId(), doctor.toMap());
            return doctor.getStaffId();
        }
    }

    //READ METHODS
    public static Doctor getDoctorById(String doctorId) {
        Map<String, Object> doctorData = dbManager.getDocumentById(DBManager.CollectionPath.STAFF, doctorId).getData();
        assert doctorData != null;
        return new Doctor(doctorId, doctorData);
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
}
