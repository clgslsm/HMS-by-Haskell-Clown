package com.javafirebasetest.dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.DeptType;
import com.javafirebasetest.entity.Doctor;
import com.javafirebasetest.entity.MedicalRecord;
import com.javafirebasetest.entity.User;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.javafirebasetest.entity.HashPassword.getSHA;
import static com.javafirebasetest.entity.HashPassword.toHexString;

public class DoctorDAO {
    private static final DBManager dbManager = DBManager.getInstance();
    static String getHashPassword(String enteredPassword) throws NoSuchAlgorithmException {
        return toHexString(getSHA(enteredPassword));
    }
    // CREATE METHODS
    public static void createDoctor(String username, String password, String name, DeptType department) throws NoSuchAlgorithmException {
        String hashedPassword = getHashPassword(password);
        Map<String, Object> doctorData = new HashMap<>();
        doctorData.put("name", name);
        doctorData.put("department", department.getValue());
        try {
            String ID = dbManager.addDocumentAndGetId(DBManager.CollectionPath.STAFF, doctorData);
            UserDAO.createUser(username, password, User.Mode.DOCTOR, ID);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error creating doctor: " + e.getMessage());
        }
    }
    //READ METHODS
    public static Doctor getDoctorById(String doctorId) {
        Map<String, Object> doctorData = null;
        try {
            DocumentSnapshot documentSnapshot = dbManager.getDocumentById(DBManager.CollectionPath.STAFF, doctorId);

            if (documentSnapshot.exists()) { // Check if the document exists
                doctorData = documentSnapshot.getData();
            } else {
                throw new RuntimeException("Doctor with ID " + doctorId + " does not exist");
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Error retrieving doctor data: " + e.getMessage());
        }

        if (doctorData != null) {
            return new Doctor(doctorId, doctorData);
        } else {
            throw new RuntimeException("Doctor data is null for ID: " + doctorId);
        }
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
                    Filter.equalTo("job", "Doctor")
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
    }

}
