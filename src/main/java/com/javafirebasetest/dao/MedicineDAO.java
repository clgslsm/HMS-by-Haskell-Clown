package com.javafirebasetest.dao;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.Medicine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicineDAO {
    private static final DBManager dbManager = DBManager.getInstance();

    public static Medicine getMedicineById(String medicineId) {
        Map<String, Object> medicineData = dbManager.getDocumentById(DBManager.CollectionPath.MEDICINE, medicineId).getData();
        assert medicineData != null;
        return new Medicine(medicineId, medicineData);
    }

    public static List<Medicine> getMedicineByName(String medicineName) {
        List<QueryDocumentSnapshot> querySnapshot;

        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.MEDICINE,
                Filter.equalTo("medicineName", medicineName)
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
        if (medicine.getMedicineId() == null) {
            return dbManager.addDocument(DBManager.CollectionPath.MEDICINE, medicine.toMap());
        } else {
            dbManager.updateDocument(DBManager.CollectionPath.MEDICINE, medicine.getMedicineId(), medicine.toMap());
            return medicine.getMedicineId();
        }
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
}
