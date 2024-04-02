package com.javafirebasetest.dao;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.dao.DBManager;
import com.javafirebasetest.entity.Machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
public class MachineDAO {
    private static final DBManager dbManager = DBManager.getInstance();
    public static Machine getMachineByID(String machineId) throws ExecutionException, InterruptedException {
        Map<String, Object> machineData = dbManager.getDocumentById(DBManager.CollectionPath.MACHINE, machineId).getData();
        assert machineData != null;
        return new Machine(machineId, machineData);
    }
    public static List<Machine> getMachineByName(String machineName) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.MACHINE,
                    Filter.equalTo("machineName", machineName)
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Machine> machineData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            machineData.add(new Machine(qds.getId(), qds.getData()));
        }
        return machineData;
    }
    public static List<Machine> getMachineByStatus(String machineStatus) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.MACHINE,
                    Filter.equalTo("machineStatus", machineStatus)
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Machine> machineData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            machineData.add(new Machine(qds.getId(), qds.getData()));
        }
        return machineData;
    }
    public static List<Machine> getMachineByUsageHistory(String machineUsageHistory) {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.MACHINE,
                    Filter.equalTo("usageHistory", machineUsageHistory)
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Machine> machineData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            machineData.add(new Machine(qds.getId(), qds.getData()));
        }
        return machineData;
    }
    public static List<Machine> getAllMachines() {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.MACHINE
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Machine> machineData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            machineData.add(new Machine(qds.getId(), qds.getData()));
        }
        return machineData;
    }
    public static void addMachine(Machine machine) {
        dbManager.addDocument(DBManager.CollectionPath.MACHINE, machine.toMap());
    }
    public static void deleteMachine(String machineId) {
        try {
            dbManager.deleteDocument(DBManager.CollectionPath.MACHINE, machineId);
        } catch (Exception e) {
            throw new RuntimeException("Delete failed: Machine doesn't exist/" + e.toString());
        }
    }
    public static void updateMachine(String machineId, Object... fieldsAndValues) {
        Map<String, Object> newData = new HashMap<>();
        for (int i = 0; i < fieldsAndValues.length; i += 2) {
            newData.put((String) fieldsAndValues[i], fieldsAndValues[i + 1]);
        }
        dbManager.updateDocument(DBManager.CollectionPath.MACHINE, machineId, newData);
    }
}
