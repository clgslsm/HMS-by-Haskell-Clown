package com.javafirebasetest.dao;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.DeptType;
import com.javafirebasetest.entity.Doctor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DoctorDAO {
    private static final DBManager dbManager = DBManager.getInstance();

    //CRUD

    public static void addDoctor(Doctor doctor) {
        if (doctor.getStaffId() == null) {
            dbManager.addDocument(DBManager.CollectionPath.STAFF, doctor.toMap());
        } else {
            dbManager.updateDocument(DBManager.CollectionPath.STAFF, doctor.getStaffId(), doctor.toMap());
        }
    }

    //READ METHODS
    public static Doctor getDoctorById(String doctorId) {

        Map<String, Object> doctorData = null;
        try {
            doctorData = dbManager.getDocumentById(DBManager.CollectionPath.STAFF, doctorId).getData();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Doctor Id does not exist" + e.toString());
        }

        assert doctorData != null;
        return new Doctor(doctorId, doctorData);
    }
    public static List<Doctor> getDoctorByName(String doctorName) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.STAFF,
                    Filter.and(Filter.greaterThanOrEqualTo("name", doctorName),
                            Filter.lessThanOrEqualTo("name", doctorName + "\uf7ff"))
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Doctor> doctorList = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            doctorList.add(new Doctor(qds.getId(), qds.getData()));
        }
        return doctorList;
    }
    public static List<Doctor> getDoctorByDepartment(DeptType deptType) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.STAFF,
                    Filter.equalTo("department", deptType.getValue())
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Doctor> doctorList = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            doctorList.add(new Doctor(qds.getId(), qds.getData()));
        }
        return doctorList;
    }

    public static List<Doctor> getAllDoctor() {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.STAFF,
                    Filter.equalTo("userMode", "Doctor")
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Doctor> doctorList = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            doctorList.add(new Doctor(qds.getId(), qds.getData()));
        }
        return doctorList;
    }

    //UPDATE METHODS
    public static void updateDoctor(String doctorId, Object... fieldsAndValues) {
        Map<String, Object> newData = new HashMap<>();
        for (int i = 0; i < fieldsAndValues.length; i += 2) {
            newData.put((String) fieldsAndValues[i], fieldsAndValues[i + 1]);
        }
        dbManager.updateDocument(DBManager.CollectionPath.STAFF, doctorId, newData);
    }    //DELETE METHODS

    public static void deleteDoctorById(String doctorId) {
        try {
            dbManager.deleteDocument(DBManager.CollectionPath.STAFF, doctorId);
        } catch (Exception e) {
            throw new RuntimeException("Delete failed: Doctor does not exist/" + e.toString());
        }
    }

}
