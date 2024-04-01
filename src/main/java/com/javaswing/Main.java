package com.javaswing;

import com.google.cloud.MetadataConfig;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.dao.DBManager;
import com.javafirebasetest.dao.receptionist.MedRecDAO;
import com.javafirebasetest.dao.receptionist.PatientDAO;
import com.javafirebasetest.entity.DeptType;
import com.javafirebasetest.entity.MedicalRecord;
import com.javafirebasetest.entity.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
        List<MedicalRecord> medRecList = MedRecDAO.getAllMedRec();
        System.out.println(medRecList);


//        SwingUtilities.invokeLater(ReceptionistUI::new);
    }
}


