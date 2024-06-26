package com.javafirebasetest.dao;

import com.javafirebasetest.entity.*;
import io.netty.util.Timer;

import javax.crypto.Mac;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class _BackendTest {
    private static final int SLEEP_TIME = 1000;
    public static void FileManagerTest(){
        FileManager fileManager = FileManager.getInstance();
        File localFile = new File("C:/Users/ACER/Desktop/Screenshot 2024-04-17 131533.png");

        String storagePath = FileManager.uploadFile(localFile.getPath());

        System.out.println("Storage path: " + storagePath);

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

            System.out.println("Original patient count: " + chosenDoc.getPatientCount());

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


            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");
            System.out.println("Changed patient count: " + DoctorDAO.getDoctorById(chosenDoc.getStaffId()).getPatientCount());;
            System.out.println("MedRec added, press enter to advance.");
            System.in.read();

            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");
            System.out.print("Enter TestType: ");
            String testType = scanner.nextLine();

            MedRecDAO.updateTestResult(medrecId, new TestResult(
                    testType,
                    null,
                    null,
                    null
            ));


            System.out.println("MedRec test result updated, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            MedRecDAO.send(medrecId, true);
//
//            System.out.println("MedRec sent to Tech, now with status:");
//            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");
//
//            System.out.println("Press enter to advance.");
//            System.in.read();
//
//            //may ban frontend lam 1 cai cho de lay file nha, xong truyen path vo thoi
//            MedRecDAO.updateTestResult(medrecId, new TestResult(
//                    null,
//                    localFilePath,
//                    null,
//                    null
//            ));
//            Thread.sleep(SLEEP_TIME * 2);
//            System.out.println("MedRec AnalysisFilePath updated, now with status:");
//            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");
//
//            MedRecDAO.send(medrecId);
//
//            System.out.println("MedRec sent back to Doctor, now with status:");
//            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");
//
//            System.out.println("Press enter to advance.");
//            System.in.read();
//
//            System.out.println("Opening analysis file...");
//
//            MedRecDAO.getMedRecById(medrecId).openAnalysisFile();

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
            System.out.println("MedRec test result updated, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            MedRecDAO.send(medrecId);
            System.out.println("MedRec sent to Recept, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            System.out.println("Press enter to advance.");
            System.in.read();


            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");
            MedRecDAO.send(medrecId);
            System.out.println("MedRec sent the last time, checked out, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");

            System.out.println("Press enter to advance.");
            System.in.read();

            MedRecDAO.updateServiceRating(medrecId, 5);
            System.out.println("MedRec serviceRating updated, checked out, now with status:");
            System.out.println(MedRecDAO.getMedRecById(medrecId) + "\n");
            System.out.println("Press enter to advance.");
            System.in.read();

            MedRecDAO.deleteMedRec(medrecId);

            FileManager.cleanUp();

            System.out.println("MedRec deleted, analysis file deleted along, test successful.");
            System.out.println("Afterward patient count: " + chosenDoc.getPatientCount());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void MachineUseCountTest() throws InterruptedException {
        List<Machine> failedTests = MachineDAO.getMachineByName("May mat xa");
        for (Machine machine : failedTests){
            System.out.println("Failed test with id " + machine.getMachineId() + " deleted");
            MachineDAO.deleteMachine(machine.getMachineId());
        }

        String machineId = MachineDAO.addMachine(new Machine(
                null,
                "May mat xa",
                1L,
                0L
        ));

        List<Machine> machineList = MachineDAO.getUsableMachine();
        List<Machine> deadMachineList = MachineDAO.getUnusableMachine();

        System.out.println("Usable count: " + machineList.size());
        System.out.println("Unusable count: " + deadMachineList.size());

        Thread.sleep(SLEEP_TIME);
        System.out.println("isUsable before: " + MachineDAO.getMachineByID(machineId).isUsable());

        MachineDAO.useMachine(machineId);
        System.out.println("Machine used");

        Thread.sleep(SLEEP_TIME);
        System.out.println("isUsable after: " + MachineDAO.getMachineByID(machineId).isUsable());

        machineList = MachineDAO.getUsableMachine();
        deadMachineList = MachineDAO.getUnusableMachine();

        System.out.println("Usable count: " + machineList.size());
        System.out.println("Unusable count: " + deadMachineList.size());

        MachineDAO.maintainMachine(machineId);

        Thread.sleep(SLEEP_TIME);
        System.out.println("isUsable after maintain: " + MachineDAO.getMachineByID(machineId).isUsable());

        machineList = MachineDAO.getUsableMachine();
        deadMachineList = MachineDAO.getUnusableMachine();

        System.out.println("Usable count: " + machineList.size());
        System.out.println("Unusable count: " + deadMachineList.size());

        MachineDAO.deleteMachine(machineId);

        System.out.println("Machine deleted, test successful");
    }

    public static void MedicineExportTest() throws InterruptedException {
        List<Medicine> failedTests = MedicineDAO.getMedicineByName("Keo");
        for (Medicine medicine : failedTests){
            System.out.println("Failed test with id " + medicine.getMedicineId() + " deleted");
            MachineDAO.deleteMachine(medicine.getMedicineId());
        }

        String medId = MedicineDAO.addMedicine(new Medicine(
                null,
                "Keo",
                LocalDate.now(),
                LocalDate.now().plusYears(2),
                30L,
                "Vien",
                "Viên sủi An thần được quảng cáo có tác dụng làm giảm chứng rối loạn tiền đình, đau đầu, giúp máu được lưu thông bình thường, tăng cường trí nhớ và tốt cho hệ tim mạch và não bộ; nâng cao đề kháng, kích thích ăn ngon, ngủ ngon.\n" +
                        "\n"
        ));
        Thread.sleep(SLEEP_TIME);

        List<Medicine> medList = MedicineDAO.getOutOfStock();
        System.out.println("Number of out-of-stock medicines: " + medList.size());

        System.out.println("Can export 50 from 30 medicine?: " + MedicineDAO.export(medId, 50L));
        Thread.sleep(SLEEP_TIME);

        System.out.println("Amount left after a fail export: " + MedicineDAO.getMedicineById(medId).getAmount());

        System.out.println("Try exporting 30: " + MedicineDAO.export(medId, 30L));
        Thread.sleep(SLEEP_TIME);

        System.out.println("Amount left after a successful export: " + MedicineDAO.getMedicineById(medId).getAmount());

        medList = MedicineDAO.getOutOfStock();
        System.out.println("Number of out-of-stock medicines: " + medList.size());

        medList = MedicineDAO.getOutOfDate();
        System.out.println("Number of out-of-date medicines: " + medList.size());

        System.out.println("Sửa láo by changing the expiryDate to 2 years ago...");
        MedicineDAO.updateMedicine(medId, "expiryDate", LocalDate.now().plusYears(-2).toString());
        Thread.sleep(SLEEP_TIME);

        medList = MedicineDAO.getOutOfDate();
        System.out.println("Number of out-of-date medicines: " + medList.size());

        MedicineDAO.deleteMedicine(medId);
        System.out.println("Medicine deleted, test succesful");
    }
}
