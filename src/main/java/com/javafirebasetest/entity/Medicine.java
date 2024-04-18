package com.javafirebasetest.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Medicine {

    private String medicineId;
    private String medicineName;
    private LocalDate importDate;
    private LocalDate expiryDate;
    private int amount;
    private String unit;
    public Medicine() {}
    public Medicine(String medicineId, String medicineName, LocalDate importDate, LocalDate expiry,
                    int amount, String unit) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.importDate = importDate;
        this.expiryDate = expiry;
        this.amount = amount;
        this.unit = unit;
    }

    public Medicine(String medicineId, Map<String, Object> medicine) {
        this.medicineId = medicineId;
        this.medicineName = (String) medicine.get("medicineName");
        this.importDate = LocalDate.parse((String) medicine.get("importDate"));
        this.expiryDate = LocalDate.parse((String) medicine.get("expiryDate"));
        this.amount = ((Long) medicine.get("amount")).intValue();
        this.unit = (String) medicine.get("unit");
    }

    public String getMedicineId() {return medicineId;}
    public void setMedicineId(String medicineId) {this.medicineId = medicineId;}
    public String getMedicineName() {return medicineName;}
    public void setMedicineName(String medicineName) {this.medicineName = medicineName;}
    public LocalDate getImportDate() {return importDate;}
    public void setImportDate(LocalDate importDate) {this.importDate = importDate;}
    public LocalDate getExpiryDate() {return expiryDate;}
    public void setExpiryDate(LocalDate expiryDate) {this.expiryDate = expiryDate;}
    public int getAmount() {return amount;}
    public void setAmount(int amount) {this.amount = amount;}
    public String getUnit() {return unit;}
    public void setUnit(String unit) {this.unit = unit;}
    public String getformattedImportDate() { // Hiển thị ngày tháng theo định dạng "dd/mm/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return importDate.format(formatter);
    }

    public String getformattedExpiryDate() { // Hiển thị ngày tháng theo định dạng "dd/mm/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return expiryDate.format(formatter);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("medicineName", medicineName);
        map.put("importDate", importDate.toString());
        map.put("expiryDate", expiryDate.toString());
        map.put("amount", amount);
        map.put("unit", unit);
        return map;
    }

    @Override
    public String toString() {
        return "medicine [medicineId=" + medicineId + ", medicineName=" + medicineName + ", importDate=" +
                getformattedImportDate() + ", expiryDate=" + getformattedExpiryDate() +
                ", amount=" + amount + ", unit=" + unit + "]";
    }
}
