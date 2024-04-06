package com.javafirebasetest.dao;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.Medicine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MedicineDAO {
    private static final DBManager dbManager = DBManager.getInstance();

    public static Medicine getMedicineById(String medicineId) throws ExecutionException, InterruptedException {
        Map<String, Object> medicineData = dbManager.getDocumentById(DBManager.CollectionPath.MEDICINE, medicineId).getData();
        assert medicineData != null;
        return new Medicine(medicineId, medicineData);
    }

    public static List<Medicine> getMedicineByName(String medicineName) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.MEDICINE,
                    Filter.equalTo("medicineName", medicineName)
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Medicine> medicineData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            medicineData.add(new Medicine(qds.getId(), qds.getData()));
        }
        return medicineData;
    }

    public static List<Medicine> getMedicineByUnit(String unit) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.MEDICINE,
                    Filter.equalTo("unit", unit)
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
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

    public static void addMedicine(Medicine medicine){
        if (medicine.getMedicineId() == null) {
            dbManager.addDocument(DBManager.CollectionPath.MEDICINE, medicine.toMap());
        } else {
            dbManager.updateDocument(DBManager.CollectionPath.MEDICINE, medicine.getMedicineId(), medicine.toMap());
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
        try {
            dbManager.deleteDocument(DBManager.CollectionPath.MEDICINE, medicineId);
        } catch (Exception e) {
            throw new RuntimeException("Delete failed: Medicine ID doesnt exist/" + e.toString());
        }
    }
}
