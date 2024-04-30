package com.javafirebasetest.dao;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.Machine;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.javafirebasetest.entity.HashPassword.getSHA;
import static com.javafirebasetest.entity.HashPassword.toHexString;

public class MachineDAO {
    private static final DBManager dbManager = DBManager.getInstance();
    static String idPrefix = "MA";

    public static String addMachine(Machine machine) {
        String hexId = null;
        String newId = machine.getMachineId();

        if (getMachineByID(newId) == null){
            try {
                hexId = toHexString(getSHA(LocalDateTime.now().toLocalTime().toString()));
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e);
            }

            newId = idPrefix + hexId.substring(hexId.length() - (DBManager.idHashLength));
        }

        dbManager.updateDocument(DBManager.CollectionPath.MACHINE, newId, machine.toMap());

        return newId;
//        if (machine.getMachineId() == null) {
//            return dbManager.addDocument(DBManager.CollectionPath.MACHINE, machine.toMap());
//        } else {
//            dbManager.updateDocument(DBManager.CollectionPath.MACHINE, machine.getMachineId(), machine.toMap());
//            return machine.getMachineId();
//        }
    }

    public static Machine getMachineByID(String machineId) {
        try {
            Map<String, Object> machineData = dbManager.getDocumentById(DBManager.CollectionPath.MACHINE, machineId).getData();
            if (machineData == null) return null;
            return new Machine(machineId, machineData);
        }
        catch (Exception err){
            return null;
        }
    }

    public static List<Machine> getMachineByName(String machineName) {
        List<QueryDocumentSnapshot> querySnapshot;

        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.MACHINE,
                Filter.greaterThanOrEqualTo("machineName", machineName),
                Filter.lessThanOrEqualTo("machineName", machineName + "\uf7ff")
        );

        List<Machine> machineData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            machineData.add(new Machine(qds.getId(), qds.getData()));
        }
        return machineData;
    }

    public static List<Machine> getUsableMachine() {
        List<Machine> machineData = getAllMachines();
        List<Machine> resultMachineData = new ArrayList<>();

        for (Machine machine : machineData){
            if (machine.isUsable())
                resultMachineData.add(machine);
        }

        return resultMachineData;
    }

    public static List<Machine> getUnusableMachine() {

        List<Machine> machineData = getAllMachines();
        List<Machine> resultMachineData = new ArrayList<>();

        for (Machine machine : machineData){
            if (!machine.isUsable())
                resultMachineData.add(machine);
        }

        return resultMachineData;
    }


    public static List<Machine> getAllMachines() {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getAllDocuments(DBManager.CollectionPath.MACHINE);

        List<Machine> machineData = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            machineData.add(new Machine(qds.getId(), qds.getData()));
        }

        return machineData;
    }

    public static void updateMachine(String machineId, Object... fieldsAndValues) {
        Map<String, Object> newData = new HashMap<>();
        for (int i = 0; i < fieldsAndValues.length; i += 2) {
            newData.put((String) fieldsAndValues[i], fieldsAndValues[i + 1]);
        }
        dbManager.updateDocument(DBManager.CollectionPath.MACHINE, machineId, newData);
    }

    public static void deleteMachine(String machineId) {
        dbManager.deleteDocument(DBManager.CollectionPath.MACHINE, machineId);
    }

    //FRONTEND HELPER FUNCTIONS
    public static boolean useMachine(String machineId){
        Machine machine = getMachineByID(machineId);

        if (machine.isUsable()){
            updateMachine(machineId, "useCount", machine.getUseCount() + 1);
            return true;
        }
        else{
            return false;
        }
    }

    public static void maintainMachine(String machineId){
        Machine machine = getMachineByID(machineId);
        updateMachine(machineId, "useCount", 0);
    }
}
