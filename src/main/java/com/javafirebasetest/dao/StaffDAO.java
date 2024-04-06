package com.javafirebasetest.dao;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.Staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class StaffDAO {
    private static final DBManager dbManager = DBManager.getInstance();

    //CRUD

    //CREATE METHODS
    public static void addStaff(Staff staff) {
        if (staff.getStaffId() == null) {
            dbManager.addDocument(DBManager.CollectionPath.STAFF, staff.toMap());
        } else {
            dbManager.updateDocument(DBManager.CollectionPath.STAFF, staff.getStaffId(), staff.toMap());
        }
    }

    //READ METHODS
    public static Staff getStaffById(String staffId){

        Map<String, Object> staffData = null;
        try {
            staffData = dbManager.getDocumentById(DBManager.CollectionPath.STAFF, staffId).getData();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Staff ID does not exist"  + e.toString() );
        }

        assert staffData != null;
        return new Staff(staffId, staffData);
    }
    public static List<Staff> getStaffByUserMode(User.Mode userMode) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.STAFF,
                    Filter.equalTo("userMode", userMode)
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Staff> staffList = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            staffList.add(new Staff(qds.getId(), qds.getData()));
        }
        return staffList;
    }

    public static List<Staff> getAllStaff() {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getAllDocuments(DBManager.CollectionPath.STAFF);

        List<Staff> staffData = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            staffData.add(new Staff(qds.getId(), qds.getData()));
        }

        return staffData;
    }
    //UPDATE METHODS
    public static void updateStaff(String staffId, Object... fieldsAndValues) {
        Map<String, Object> newData = new HashMap<>();
        for (int i = 0; i < fieldsAndValues.length; i += 2) {
            newData.put((String) fieldsAndValues[i], fieldsAndValues[i + 1]);
        }
        dbManager.updateDocument(DBManager.CollectionPath.STAFF, staffId, newData);
    }
    //DELETE METHODS
    public  static void deleteStaffById(String staffId){
        try {
            dbManager.deleteDocument(DBManager.CollectionPath.STAFF, staffId);
        } catch (Exception e) {
            throw new RuntimeException("Delete failed: Staff does not exist/" + e.toString());
        }
    }
}
