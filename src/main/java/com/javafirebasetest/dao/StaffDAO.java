package com.javafirebasetest.dao;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.Staff;
import com.javafirebasetest.entity.User;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.javafirebasetest.entity.HashPassword.getSHA;
import static com.javafirebasetest.entity.HashPassword.toHexString;

public class StaffDAO {
    private static final DBManager dbManager = DBManager.getInstance();

    static Map<User.Mode, String> idPrefixMap = new HashMap<User.Mode, String>(){{
        put(User.Mode.DOCTOR, "DO");
        put(User.Mode.RECEPTIONIST, "RC");
        put(User.Mode.PHARMACIST, "PH");
        put(User.Mode.TECHNICIAN, "TE");
        put(User.Mode.ADMIN, "AD");
    }};
    //CRUD

    //CREATE METHODS
    public static String addStaff(Staff staff) {
        String hexId = null;
        String newId = staff.getStaffId();

        if (getStaffById(newId) == null){
            try {
                hexId = toHexString(getSHA(LocalDateTime.now().toLocalTime().toString()));
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e);
            }

            newId = idPrefixMap.get(staff.getUserMode()) + hexId.substring(hexId.length() - (DBManager.idHashLength));
        }
        dbManager.updateDocument(DBManager.CollectionPath.STAFF, newId, staff.toMap());

        return newId;

//        if (staff.getStaffId() == null) {
//            return dbManager.addDocument(DBManager.CollectionPath.STAFF, staff.toMap());
//        } else {
//            dbManager.updateDocument(DBManager.CollectionPath.STAFF, staff.getStaffId(), staff.toMap());
//            return staff.getStaffId();
//        }
    }

    //READ METHODS
    public static Staff getStaffById(String staffId) {

        Map<String, Object> staffData;

        try {
            staffData = dbManager.getDocumentById(DBManager.CollectionPath.STAFF, staffId).getData();
        }
        catch (Exception err){
            return null;
        }

        if (staffData == null) return null;
        return new Staff(staffId, staffData);
    }
    public static List<Staff> getStaffByName(String name) {
        List<QueryDocumentSnapshot> querySnapshot;

        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.STAFF,
                Filter.greaterThanOrEqualTo("name", name),
                Filter.lessThanOrEqualTo("name", name + "\uf7ff")
        );

        List<Staff> staffList = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            staffList.add(new Staff(qds.getId(), qds.getData()));
        }
        return staffList;
    }
    public static List<Staff> getStaffByUserMode(User.Mode userMode) {
        List<QueryDocumentSnapshot> querySnapshot;

        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.STAFF,
                Filter.equalTo("userMode", userMode.getValue())
        );

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
    public static void deleteStaffById(String staffId) {
        try {
            dbManager.deleteDocument(DBManager.CollectionPath.STAFF, staffId);
        } catch (Exception e) {
            throw new RuntimeException("Delete failed: Staff does not exist/" + e);
        }
    }
}
