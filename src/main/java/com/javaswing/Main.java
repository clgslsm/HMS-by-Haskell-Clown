package com.javaswing;

import com.javafirebasetest.dao.MedicineDAO;
import com.javafirebasetest.entity.Medicine;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Medicine newMedicine = new Medicine(
                "lmao",
                "Thuoc an than",
                LocalDate.now(),
                LocalDate.now(),
                null,
                64L,
                "viên",
                100L
        );

        MedicineDAO.addMedicine(newMedicine);

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Medicine medicine;
        medicine = MedicineDAO.getMedicineById("lmao");


        System.out.println(medicine);

        MedicineDAO.updateMedicine("lmao", "description", "Bla bla bla");

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


