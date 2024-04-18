package com.javafirebasetest.entity;
import java.util.HashMap;
import java.util.Map;

public class Machine {

    private String machineId;
    private String machineName;
    private int avaiUse;
    private int useCount;
    public Machine() {}
    public Machine(String machineId, String machineName, int avaiUse, int useCount) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.avaiUse = avaiUse;
        this.useCount = useCount;
    }
    public Machine(String machineId, Map<String, Object> machine) {
        super();
        this.machineId = machineId;
        this.machineName = (String) machine.get("machineName");
        this.avaiUse = ((int) machine.get("avaiUse"));
        this.useCount = ((int) machine.get("useCount"));
    }

    public String getMachineId() {return machineId;}
    public String getMachineName() {return machineName;}
    public int getAvaiUse() {return avaiUse;}
    public int getUseCount() {return useCount;}
    public Map<String, Object> toMap() {
        Map<String, Object> machineData = new HashMap<>();
        machineData.put("machineName", machineName);
        machineData.put("avaiUse", avaiUse);
        machineData.put("useCount", useCount);
        return machineData;
    }

    public boolean isUsable(){
        return avaiUse > useCount;
    }

    public String toString() {
        return "Machine[machineId='" + machineId + ", machineName='" + machineName +
                ", avaiUse=" + avaiUse + ", usedCount=" + useCount + ']';
    }
}
