package com.javafirebasetest.dao;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.Medicine;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.javafirebasetest.entity.HashPassword.getSHA;
import static com.javafirebasetest.entity.HashPassword.toHexString;

public class MedicineDAO {
    private static final DBManager dbManager = DBManager.getInstance();
    static String idPrefix = "ME_";

    public static Medicine getMedicineById(String medicineId) {
        Map<String, Object> medicineData = dbManager.getDocumentById(DBManager.CollectionPath.MEDICINE, medicineId).getData();
        assert medicineData != null;
        return new Medicine(medicineId, medicineData);
    }

    public static List<Medicine> getMedicineByName(String medicineName) {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.MEDICINE,
                Filter.greaterThanOrEqualTo("medicineName", medicineName),
                Filter.lessThanOrEqualTo("medicineName", medicineName + "\uf7ff")
        );

        List<Medicine> medicineData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            medicineData.add(new Medicine(qds.getId(), qds.getData()));
        }
        return medicineData;
    }

    public static List<Medicine> getMedicineByUnit(String unit) {
        List<QueryDocumentSnapshot> querySnapshot;

        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.MEDICINE,
                Filter.equalTo("unit", unit)
        );

        List<Medicine> medicineData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            medicineData.add(new Medicine(qds.getId(), qds.getData()));
        }
        return medicineData;
    }

    public static List<Medicine> getAllMedicine() {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getAllDocuments(DBManager.CollectionPath.MEDICINE);
        List<Medicine> medicineData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            medicineData.add(new Medicine(qds.getId(), qds.getData()));
        }
        return medicineData;
    }

    public static String addMedicine(Medicine medicine) {
        String hexId = null;
        String newId = null;

        try {
            hexId = toHexString(getSHA(LocalDateTime.now().toLocalTime().toString()));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }

        newId = idPrefix + hexId.substring(hexId.length() - (DBManager.idHashLength));

        dbManager.updateDocument(DBManager.CollectionPath.MEDICINE, newId, medicine.toMap());

        return newId;
//        if (medicine.getMedicineId() == null) {
//            return dbManager.addDocument(DBManager.CollectionPath.MEDICINE, medicine.toMap());
//        } else {
//            dbManager.updateDocument(DBManager.CollectionPath.MEDICINE, medicine.getMedicineId(), medicine.toMap());
//            return medicine.getMedicineId();
//        }
    }

    public static void updateMedicine(String medicineId, Object... fieldsAndValues) {
        Map<String, Object> newData = new HashMap<>();
        for (int i = 0; i < fieldsAndValues.length; i += 2) {
            newData.put((String) fieldsAndValues[i], fieldsAndValues[i + 1]);
        }
        dbManager.updateDocument(DBManager.CollectionPath.MEDICINE, medicineId, newData);
    }

    //DELETE METHODS
    public static void deleteMedicine(String medicineId) {
        dbManager.deleteDocument(DBManager.CollectionPath.MEDICINE, medicineId);
    }

    //FRONTEND HELPER FUNCTIONS

    /**
    * Returns false when the amount in storage is not enough.
    * Returns true when exported successfully
    * */
    public static boolean export(String medicineId, Long amount){
        Medicine med = getMedicineById(medicineId);

        if (med.getAmount() < amount) return false;

        updateMedicine(medicineId, "amount", med.getAmount() - amount);
        return true;
    }

    /**
     * Returns list of medicines with amount = 0.
     *
     *
     */
    public static List<Medicine> getOutOfStock() {
        List<QueryDocumentSnapshot> querySnapshot;

        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.MEDICINE,
                Filter.equalTo("amount", 0)
        );

        List<Medicine> medicineData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            medicineData.add(new Medicine(qds.getId(), qds.getData()));
        }
        return medicineData;
    }

    /**
     * Returns list of medicines with expiryDate earlier than current date.
     */
    public static List<Medicine> getOutOfDate() {
        List<QueryDocumentSnapshot> querySnapshot;

        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.MEDICINE,
                Filter.lessThan("expiryDate", LocalDate.now().toString())
        );

        List<Medicine> medicineData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            medicineData.add(new Medicine(qds.getId(), qds.getData()));
        }
        return medicineData;
    }
}
