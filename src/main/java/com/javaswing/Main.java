package com.javaswing;

import com.google.cloud.Timestamp;
import com.javafirebasetest.dao.receptionist.MedRecDAO;
import com.javafirebasetest.entity.MedicalRecord;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        MedRecDAO.updateMedRec("medrec0", "checkOut", Timestamp.now(), "prescription", "hetcuu");


//        SwingUtilities.invokeLater(ReceptionistUI::new);
    }
}


