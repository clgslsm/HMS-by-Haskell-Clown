package com.javafirebasetest.entity;
import java.util.HashMap;
import java.util.Map;

public class Machine {

    private String machineId;
    private String machineName;
    private Long avaiUse;
    private Long useCount;
    public Machine() {}
    public Machine(String machineId, String machineName, Long avaiUse, Long useCount) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.avaiUse = avaiUse;
        this.useCount = useCount;
    }
    public Machine(String machineId, Map<String, Object> machine) {
        super();
        this.machineId = machineId;
        this.machineName = (String) machine.get("machineName");
        this.avaiUse = ((Long) machine.get("avaiUse"));
        this.useCount = ((Long) machine.get("useCount"));
    }

    public String getMachineId() {return machineId;}
    public String getMachineName() {return machineName;}
    public Long getAvaiUse() {return avaiUse;}
    public Long getUseCount() {return useCount;}
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
