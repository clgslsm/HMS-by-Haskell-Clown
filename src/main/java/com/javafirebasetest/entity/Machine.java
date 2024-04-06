package com.javafirebasetest.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Machine {

    private String machineId;
    private String machineName;
    private LocalDate purchaseDate;
    private Status machineStatus;
    private String usageHistory;

    public enum Status {
        AVAILABLE("Available"), BORROWED("Borrowed"), OUT_OF_ORDER("Out of order"),;
        private final String value;
        Status(String value) {this.value = value;}
        public String getValue() {return value;}
        public static Machine.Status fromValue(String value) {
            for (Machine.Status bg : Machine.Status.values())
                if (bg.value.equalsIgnoreCase(value)) return bg;
            throw new IllegalArgumentException("Invalid blood group: " + value);
        }
    }
    public Machine() {}
    public Machine(String machineId, String machineName, LocalDate purchaseDate, Machine.Status machineStatus, String usageHistory) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.purchaseDate = purchaseDate;
        this.machineStatus = machineStatus;
        this.usageHistory = usageHistory;
    }
    public Machine(String machineId, Map<String, Object> machine) {
        super();
        this.machineId = machineId;
        this.machineName = (String) machine.get("machineName");
        this.purchaseDate = LocalDate.parse((String) machine.get("purchaseDate"), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.machineStatus = Machine.Status.fromValue((String) machine.get("machineStatus"));
        this.usageHistory = (String) machine.get("usageHistory");
        this.machineStatus = Machine.Status.fromValue((String) machine.get("machineStatus"));
        this.usageHistory = (String) machine.get("usageHistory");
    }
    public void setMachineId(String machineId) {this.machineId = machineId;}
    public void setName(String machineName) {this.machineName = machineName;}
    public void setPurchaseDate(LocalDate purchaseDate) {this.purchaseDate = purchaseDate;}
    public void setMachineStatus(Machine.Status machineStatus) {this.machineStatus = machineStatus;}
    public void setUsageHistory(String usageHistory) {this.usageHistory = usageHistory;}
    public String getMachineId() {return machineId;}
    public String getMachineName() {return machineName;}
    public LocalDate getPurchaseDate() {return purchaseDate;}
    public Machine.Status getMachineStatus() {return machineStatus;}
    public String getUsageHistory() {return usageHistory;}
    public String getformattedDate() { // Hiển thị ngày tháng theo định dạng "dd/mm/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return purchaseDate.format(formatter);
    }
    public Map<String, Object> toMap() {
        Map<String, Object> machineData = new HashMap<>();
        machineData.put("machineId", machineId);
        machineData.put("machineName", machineName);
        machineData.put("purchaseDate", purchaseDate.toString());
        machineData.put("machineStatus", machineStatus.getValue());
        machineData.put("usageHistory", usageHistory);
        return machineData;
    }
    @Override
    public String toString() {
        return "machine [machineId=" + machineId + ", machineName=" + machineName + ", purchaseDate=" +
                getformattedDate() + ", machineStatus=" + machineStatus.getValue() + ", usageHistory=" + usageHistory + "]";
    }
}
