package com.javaswing;

import com.google.cloud.Timestamp;
import com.javafirebasetest.dao.MedRecDAO;

public class Main {
    public static void main(String[] args) {
        MedRecDAO.updateMedRec("medrec0", "checkOut", Timestamp.now(), "prescription", "hetcuu");


//        SwingUtilities.invokeLater(ReceptionistUI::new);
    }
}


