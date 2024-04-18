package com.javafirebasetest.dao;

import com.javafirebasetest.entity.*;
import io.netty.util.Timer;

import java.io.IOException;
import java.util.Scanner;

public class _BackendTest {
    public static void MedrecTest(){
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

            Thread.sleep(1000);

            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");
            System.out.println("MedRec added, press enter to advance.");
            System.in.read();

            MedRecDAO.send(medrecId);
            Thread.sleep(1000);
            System.out.println("MedRec sent, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");
            System.out.print("Enter TestType: ");
            String testType = scanner.nextLine();

            MedRecDAO.updateTestResult(medrecId, new TestResult(
                    testType,
                    null,
                    null,
                    null
            ));

            Thread.sleep(1000);

            System.out.println("MedRec test result updated, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            MedRecDAO.send(medrecId);
            Thread.sleep(1000);
            System.out.println("MedRec sent, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            System.out.println("Press enter to advance.");
            System.in.read();

            //may ban frontend lam 1 cai cho de lay file nha, xong truyen path vo thoi
            MedRecDAO.updateTestResult_AnalysisFilePath(medrecId, "C:\\Users\\ACER\\Desktop\\smileyFace.png");
            Thread.sleep(1000);
            System.out.println("MedRec AnalysisFilePath updated, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            MedRecDAO.send(medrecId);
            Thread.sleep(1000);
            System.out.println("MedRec sent, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            System.out.println("Press enter to advance.");
            System.in.read();

            System.out.print("Enter diagnois: ");
            String diagnois = scanner.nextLine();
            System.out.print("\nEnter prescription: ");
            String prescription = scanner.nextLine();

            MedRecDAO.updateTestResult(medrecId, new TestResult(
                    null,
                    null,
                    diagnois,
                    prescription
            ));
            Thread.sleep(1000);
            System.out.println("MedRec test result updated, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            MedRecDAO.send(medrecId);
            Thread.sleep(1000);
            System.out.println("MedRec sent, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            System.out.println("Press enter to advance.");
            System.in.read();

            MedRecDAO.deleteMedRec(medrecId);

            System.out.println("MedRec deleted, test successful.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
