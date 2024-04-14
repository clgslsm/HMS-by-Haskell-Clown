package com.javafirebasetest.entity;
import java.util.HashMap;
import java.util.Map;

public class Machine {

    private String machineId;
    private String machineName;
    private int avaiUse;
    private int usedCount;
    public Machine() {}
    public Machine(String machineId, String machineName, int avaiUse, int usedCount) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.avaiUse = avaiUse;
        this.usedCount = usedCount;
    }
    public Machine(String machineId, Map<String, Object> machine) {
        super();
        this.machineId = machineId;
        this.machineName = (String) machine.get("machineName");
        this.avaiUse = ((int) machine.get("avaiUse"));
        this.usedCount = ((int) machine.get("usedCount"));
    }
    public void setMachineId(String machineId) {this.machineId = machineId;}
    public void setName(String machineName) {this.machineName = machineName;}
    public void setAvaiUse(int avaiUse) {this.avaiUse = avaiUse;}
    public void setUsedCount(int usedCount) {this.usedCount = usedCount;}
    public String getMachineId() {return machineId;}
    public String getMachineName() {return machineName;}
    public int getAvaiUse() {return avaiUse;}
    public int getUsedCount() {return usedCount;}
    public Map<String, Object> toMap() {
        Map<String, Object> machineData = new HashMap<>();
        machineData.put("machineName", machineName);
        machineData.put("avaiUse", avaiUse);
        machineData.put("usedCount", usedCount);
        return machineData;
    }
    public String toString() {
        return "Machine[machineId='" + machineId + ", machineName='" + machineName +
                ", avaiUse=" + avaiUse + ", usedCount=" + usedCount + ']';
    }
}
