package com.javafirebasetest.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    @Override
    public String toString() {
        return "medicine [medicineId=" + medicineId + ", medicineName=" + medicineName + ", importDate=" +
                getformattedImportDate() + ", expiryDate=" + getformattedExpiryDate() + ", description=" + description +
                ", amount=" + amount + ", unit=" + unit + ", price=" + price + "]";
    }
}
