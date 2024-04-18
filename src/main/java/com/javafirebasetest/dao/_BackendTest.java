package com.javafirebasetest.dao;

import com.javafirebasetest.entity.*;
import io.netty.util.Timer;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class _BackendTest {
    private static final int SLEEP_TIME = 500;
    public static void FileManagerTest(){
        FileManager fileManager = FileManager.getInstance();
        File localFile = new File("C:/Users/ACER/Desktop/Screenshot 2024-04-17 131533.png");

        String storagePath = FileManager.uploadFile(localFile.getPath());

        System.out.println("Storage path: " + storagePath);

        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FileManager.downloadFile("MedicalRecords/Screenshot 2024-04-17 131533.png");

        FileManager.openFileWithDefaultApp("MedicalRecords/Screenshot 2024-04-17 131533.png");

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileManager.deleteFile("MedicalRecords/Screenshot 2024-04-17 131533.png");

        FileManager.cleanUp();
    }
    public static void MedrecTest(String localFilePath){
        try {
            MedicalRecord dummy = MedRecDAO.getMedRecById("testId");
            if (dummy != null) {
                System.out.println("MedRec exists, deleting...");
                MedRecDAO.deleteMedRec("testId");
            }

            Scanner scanner = new Scanner(System.in);
            Doctor chosenDoc = DoctorDAO.getMatchFromDepartment(DeptType.DENTAL);

            //All docs in department have PATIENT_LIMIT patients
            if (chosenDoc == null) return;

            String medrecId = MedRecDAO.addMedRec(new MedicalRecord(
                    "testId",
                    "r3ngmqsLeoOIq9I8oD0S",
                    chosenDoc.getStaffId(),
                    StaffDAO.getStaffByUserMode(User.Mode.RECEPTIONIST).getFirst().getStaffId(),
                    null,
                    null,
                    MedicalRecord.Status.PENDING,
                    0L,
                    null
            ));

            Thread.sleep(SLEEP_TIME);

            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");
            System.out.println("MedRec added, press enter to advance.");
            System.in.read();

            System.out.println("MedRec sent to Doctor, now with status (status shouldnt change):");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");
            System.out.print("Enter TestType: ");
            String testType = scanner.nextLine();

            MedRecDAO.updateTestResult(medrecId, new TestResult(
                    testType,
                    null,
                    null,
                    null
            ));

            Thread.sleep(SLEEP_TIME);

            System.out.println("MedRec test result updated, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            MedRecDAO.send(medrecId);
            Thread.sleep(SLEEP_TIME);
            System.out.println("MedRec sent to Tech, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            System.out.println("Press enter to advance.");
            System.in.read();

            //may ban frontend lam 1 cai cho de lay file nha, xong truyen path vo thoi
            MedRecDAO.updateTestResult_AnalysisFilePath(medrecId, localFilePath);
            Thread.sleep(SLEEP_TIME * 2);
            System.out.println("MedRec AnalysisFilePath updated, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            MedRecDAO.send(medrecId);
            Thread.sleep(SLEEP_TIME);
            System.out.println("MedRec sent back to Doctor, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            System.out.println("Press enter to advance.");
            System.in.read();

            System.out.print("Enter diagnosis: ");
            String diagnois = scanner.nextLine();
            System.out.print("\nEnter prescription: ");
            String prescription = scanner.nextLine();

            MedRecDAO.updateTestResult(medrecId, new TestResult(
                    null,
                    null,
                    diagnois,
                    prescription
            ));
            Thread.sleep(SLEEP_TIME);
            System.out.println("MedRec test result updated, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            MedRecDAO.send(medrecId);
            Thread.sleep(SLEEP_TIME);
            System.out.println("MedRec sent to Recept, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            System.out.println("Press enter to advance.");
            System.in.read();

            Thread.sleep(SLEEP_TIME);
            MedRecDAO.send(medrecId);
            System.out.println("MedRec sent the last time (doesnt move away from Recept, but still update status to Check_out, now with status:");
            Thread.sleep(SLEEP_TIME);
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");
            Thread.sleep(SLEEP_TIME);
            MedRecDAO.performCheckout(medrecId);
            System.out.println("MedRec checked out, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            System.out.println("Press enter to advance.");
            System.in.read();

            MedRecDAO.deleteMedRec(medrecId);

            System.out.println("MedRec deleted, analysis file deleted along, test successful.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
