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
    private String description;
    private Long amount;
    private String unit;
    private Long price;

    public Medicine() {
    }

    public Medicine(String medicineId, String medicineName, LocalDate importDate, LocalDate expiry, String description,
                    Long amount, String unit, Long price) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.importDate = importDate;
        this.expiryDate = expiry;
        this.description = description;
        this.amount = amount;
        this.unit = unit;
        this.price = price;
    }

    public Medicine(String medicineId, Map<String, Object> medicine) {
        this.medicineId = medicineId;
        this.medicineName = (String) medicine.get("medicineName");
        this.importDate = LocalDate.parse((String) medicine.get("importDate"));
        this.expiryDate = LocalDate.parse((String) medicine.get("expiryDate"));
        this.description = (String) medicine.get("description");
        this.amount = ((Long) medicine.get("amount"));
        this.unit = (String) medicine.get("unit");
        this.price = ((Long) medicine.get("price"));
    }

    public String getMedicineId() {
        return medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public Long getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public Long getPrice() {
        return price;
    }

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
        map.put("description", description);
        map.put("amount", amount);
        map.put("unit", unit);
        map.put("price", price);
        return map;
    }

    @Override
    public String toString() {
        return "medicine [medicineId=" + medicineId + ", medicineName=" + medicineName + ", importDate=" +
                getformattedImportDate() + ", expiryDate=" + getformattedExpiryDate() + ", description=" + description +
                ", amount=" + amount + ", unit=" + unit + ", price=" + price + "]";
    }
}
