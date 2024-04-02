package com.javafirebasetest.dao;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.dao.DBManager;
import com.javafirebasetest.entity.Medicine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MedicineDAO {
    private static final DBManager dbManager = DBManager.getInstance();
    public static Medicine getMedicineId(String medicineId) throws ExecutionException, InterruptedException {
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
    public static void addMedicine(Medicine medicine) {
        dbManager.addDocument(DBManager.CollectionPath.MEDICINE, medicine.toMap());
    }
}
