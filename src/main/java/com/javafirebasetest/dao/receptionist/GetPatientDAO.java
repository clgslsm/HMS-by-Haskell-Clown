package com.javafirebasetest.dao.receptionist;

import com.google.cloud.firestore.Filter;
import com.javafirebasetest.dao.DBManager;
import com.javafirebasetest.entity.Patient;

import javax.annotation.processing.Filer;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class GetPatientDAO {
    private static final DBManager dbManager = DBManager.getInstance();
    public static Patient getPatientByID(String patientID) throws ExecutionException, InterruptedException {
        Map<String, Object> patientData = dbManager.getDocumentById("Patients", patientID);

        Patient patient = new Patient(patientData);
        return patient;
    }

    public static ArrayList<Patient> getPatientByNamePhone(String name, String phoneNumber) {
        ArrayList<Patient> patientData;
        try{
            patientData = dbManager.getDocumentsByConditions(
                    "Patients",
                    DBManager.makeFilter(
                            Filter.equalTo("name", name),
                            Filter.equalTo("phoneNumber", phoneNumber)
                    )
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return patientData;
    }
}
