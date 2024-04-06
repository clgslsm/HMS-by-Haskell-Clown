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
    private int amount;
    private String unit;
    private int price;
    public Medicine() {}
    public Medicine(String medicineId, String medicineName, LocalDate importDate, LocalDate expiry, String description,
                    int amount, String unit, int price) {
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
        this.amount = ((Long) medicine.get("amount")).intValue();
        this.unit = (String) medicine.get("unit");
        this.price = ((Long) medicine.get("price")).intValue();
    }

    public String getMedicineId() {return medicineId;}
    public void setMedicineId(String medicineId) {this.medicineId = medicineId;}
    public String getMedicineName() {return medicineName;}
    public void setMedicineName(String medicineName) {this.medicineName = medicineName;}
    public LocalDate getImportDate() {return importDate;}
    public void setImportDate(LocalDate importDate) {this.importDate = importDate;}
    public LocalDate getExpiryDate() {return expiryDate;}
    public void setExpiryDate(LocalDate expiryDate) {this.expiryDate = expiryDate;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public int getAmount() {return amount;}
    public void setAmount(int amount) {this.amount = amount;}
    public String getUnit() {return unit;}
    public void setUnit(String unit) {this.unit = unit;}
    public int getPrice() {return price;}
    public void setPrice(int price) {this.price = price;}
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
