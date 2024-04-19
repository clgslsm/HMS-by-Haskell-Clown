package com.javafirebasetest.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.javafirebasetest.entity.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class _DBPopulator {
    public static void clearDB() throws IOException, ExecutionException, InterruptedException {
        Firestore firestore = DBManager.getInstance().db;

        // List all collections
        Iterable<CollectionReference> collections = firestore.listCollections();

        // Delete each collection
        for (CollectionReference collection : collections) {
            deleteCollection(collection);
        }

        System.out.println("All collections removed successfully.");
    }

    private static void deleteCollection(CollectionReference collection) throws InterruptedException, ExecutionException {
        // Recursively delete all documents in the collection
        for (com.google.cloud.firestore.DocumentReference document : collection.listDocuments()) {
            document.delete();
        }
    }

    static void populateDept() throws ExecutionException, InterruptedException {
        Firestore db = DBManager.getInstance().db;
        CollectionReference colref = db.collection("Departments");
        if (!colref.get().get().getDocuments().isEmpty()) return;

        for (DeptType deptType : DeptType.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", deptType.getValue());

            ApiFuture<DocumentReference> docRef = db.collection("Departments").add(map);
            try {
                System.out.println(docRef.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Creates 17 * 3 doctors of every Dept.
     */
    static void populateDoctor() throws ExecutionException, InterruptedException {
        Firestore db = DBManager.getInstance().db;
        CollectionReference colref = db.collection("Staffs");
        if (!colref.whereEqualTo("userMode", User.Mode.DOCTOR.getValue()).get().get().isEmpty()) return;
        Random random = new Random();

        // Example: Create and add random doctors
        for (DeptType deptType : DeptType.values()) {
            for (int i = 0; i < 3; ++i) {
                String fullName = generateRandomName();
                DeptType department = deptType;

                Doctor doctor = new Doctor(null, fullName, department);
                String docId = DoctorDAO.addDoctor(doctor);

                System.out.println("Doctor added at id " + docId);
            }
        }

        System.out.println("Doctors added successfully.");
    }

    static void populateStaff() throws ExecutionException, InterruptedException {
        Firestore db = DBManager.getInstance().db;
        CollectionReference colref = db.collection("Staffs");
        if (!colref.whereEqualTo("userMode", User.Mode.RECEPTIONIST.getValue()).get().get().isEmpty()) return;
        Random random = new Random();

        // Example: Create and add random doctors
        for (User.Mode userMode : User.Mode.values()) {
            if (userMode == User.Mode.DOCTOR) continue;
            for (int i = 0; i < 3; ++i) {
                String fullName = generateRandomName();

                String id = StaffDAO.addStaff(new Staff(
                        null,
                        fullName,
                        userMode
                ));

                System.out.println("Staff of " + userMode + " added at id " + id);
            }
        }


        System.out.println("Staffs added successfully.");
    }

    //1 user for each staff
    static void populateUser() throws ExecutionException, InterruptedException {
        Firestore db = DBManager.getInstance().db;
        CollectionReference colref = db.collection("Users");
        if (!colref.get().get().isEmpty()) return;

        Random random = new Random();

        List<Staff> staffList = StaffDAO.getAllStaff();

        for (int i = 0; i < staffList.size(); ++i) {
            Staff staff = staffList.get(i);
            String username = staff.getUserMode().getValue() + i;
            String password = username;

            String id = UserDAO.addUser(new User(
                    null,
                    username,
                    password,
                    staff.getUserMode(),
                    staff.getStaffId()
            ));

            System.out.println("User added at id " + id);

        }


        System.out.println("User added successfully.");
    }

    ////////PATIENT
    private static Patient generateRandomPatient() {
        Random random = new Random();
        String name = generateRandomName();
        LocalDate birthDate = generateRandomBirthDate();
        Patient.Gender gender = random.nextBoolean() ? Patient.Gender.MALE : Patient.Gender.FEMALE;
        String address = "Random Address " + random.nextInt(1000);
        String phoneNumber = generateRandomPhoneNumber();
        Patient.BloodGroup bloodGroup = Patient.BloodGroup.values()[random.nextInt(Patient.BloodGroup.values().length)];
        String healthInsuranceNumber = "HIN" + random.nextInt(1000000);

        return new Patient(null, name, birthDate, gender, address, phoneNumber, bloodGroup, healthInsuranceNumber);
    }

    private static String generateRandomName() {
        Random random = new Random();
        String[] FIRST_NAMES = {"John", "Jane", "David", "Emily", "Michael", "Emma", "Robert", "Olivia", "James", "Mary", "William", "Anna", "Daniel", "Sophia", "Joseph", "Isabella", "Christopher", "Jennifer", "Matthew", "Jessica", "Andrew", "Elizabeth", "Joshua", "Amanda"};
        String[] LAST_NAMES = {"Doe", "Smith", "Johnson", "Brown", "Williams", "Jones", "Miller", "Davis", "Anderson", "Martinez", "Taylor", "Thomas", "Wilson", "Moore", "Jackson", "White", "Harris", "Martin", "Clark", "Lewis", "Lee", "Walker", "Hall", "Allen"};

        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];

        return firstName + ' ' + lastName;
    }

    private static LocalDate generateRandomBirthDate() {
        // Generating birth date between 1950 and 2000
        Random random = new Random();
        int year = 1950 + random.nextInt(51); // Random year between 1950 and 2000
        int month = random.nextInt(12) + 1; // Random month between 1 and 12
        int day = random.nextInt(28) + 1; // Random day between 1 and 28
        return LocalDate.of(year, month, day);
    }

    private static String generateRandomPhoneNumber() {
        // Generating random phone number
        Random random = new Random();
        StringBuilder phoneNumber = new StringBuilder("+");
        for (int i = 0; i < 10; i++) {
            phoneNumber.append(random.nextInt(10));
        }
        return phoneNumber.toString();
    }

    public static void populatePatient() throws ExecutionException, InterruptedException {
        CollectionReference colref = DBManager.getInstance().db.collection("Patients");
        if (!colref.get().get().isEmpty()) return;
        for (int i = 0; i < 20; i++) {
            Patient patient = generateRandomPatient();
            String patientId = PatientDAO.addPatient(patient);
            System.out.println("Added patient with ID: " + patientId);
        }
    }

    ///////////////
    private static Machine generateRandomMachine() {
        Random random = new Random();
        String machineId = null; // Set to null as it will be generated by Firestore
        String machineName = generateRandomMachineName();
        Long avaiUse = (long) (random.nextInt(50) + 50); // Random number between 50 and 100
        Long useCount = (long) random.nextInt(Math.toIntExact(avaiUse)); // Random number less than avaiUse

        if (random.nextInt(100) < 30) useCount = avaiUse - 1;

        return new Machine(machineId, machineName, avaiUse, useCount);
    }

    private static String generateRandomMachineName() {
        String[] machineNames = {
                "Ultrasound Scanner", "MRI Machine", "X-Ray Machine", "Blood Pressure Monitor",
                "ECG Machine", "Infusion Pump", "Defibrillator", "Ventilator",
                "Anesthesia Machine", "Dialysis Machine", "Laser Therapy Equipment", "Oxygen Concentrator",
                "Endoscope", "Surgical Microscope", "Autoclave"
        };
        Random random = new Random();
        return machineNames[random.nextInt(machineNames.length)];
    }

    public static void populateMachine() throws ExecutionException, InterruptedException {
        CollectionReference colref = DBManager.getInstance().db.collection(DBManager.CollectionPath.MACHINE.getValue());
        if (!colref.get().get().isEmpty()) return;
        MachineDAO machineDAO = new MachineDAO();

        for (int i = 0; i < 20; i++) {
            Machine machine = generateRandomMachine();
            machineDAO.addMachine(machine);
            System.out.println("Added machine: " + machine);
        }
    }

    private static Medicine generateRandomMedicine() {
        Random random = new Random();
        String medicineId = null; // Set to null as it will be generated by Firestore
        String medicineName = generateRandomMedicineName();
        Long amount = (long) (random.nextInt(500) + 1); // Random number between 1 and 500

        // Set import date to LocalDate.now()
        LocalDate importDate = LocalDate.now();

        // Set expiry date 2 years away from the import date
        LocalDate expiryDate = importDate.plusYears(2);

        // Set unit to "hộp"
        String unit = "Viên";

        String description = "Viên sủi An thần được quảng cáo có tác dụng làm giảm chứng rối loạn tiền đình, đau đầu, giúp máu được lưu thông bình thường, tăng cường trí nhớ và tốt cho hệ tim mạch và não bộ; nâng cao đề kháng, kích thích ăn ngon, ngủ ngon.";

        return new Medicine(medicineId, medicineName, importDate, expiryDate, amount, unit, description);
    }

    private static String generateRandomMedicineName() {
        String[] medicineNames = {
                "Paracetamol", "Ibuprofen", "Amoxicillin", "Ciprofloxacin", "Metformin",
                "Atorvastatin", "Omeprazole", "Lisinopril", "Losartan", "Amlodipine",
                "Levothyroxine", "Azithromycin", "Aspirin", "Prednisone", "Albuterol"
        };
        Random random = new Random();
        return medicineNames[random.nextInt(medicineNames.length)];
    }

    public static void populateMedicine() throws ExecutionException, InterruptedException {
        CollectionReference colref = DBManager.getInstance().db.collection(DBManager.CollectionPath.MEDICINE.getValue());
        if (!colref.get().get().isEmpty()) return;

        for (int i = 0; i < 20; i++) {
            Medicine medicine = generateRandomMedicine();
            MedicineDAO.addMedicine(medicine);
            System.out.println("Added medicine: " + medicine);
        }
    }

    public static void populateMedRec() throws ExecutionException, InterruptedException {
        CollectionReference colref = DBManager.getInstance().db.collection(DBManager.CollectionPath.MEDICAL_RECORD.getValue());
        if (!colref.get().get().isEmpty()) return;

        String analysisFilePath = "C:\\Users\\ACER\\Desktop\\smileyFace.png";

        Random rand = new Random();

        List<Patient> patientList = PatientDAO.getAllPatients();
        List<Staff> receptList = StaffDAO.getStaffByUserMode(User.Mode.RECEPTIONIST);
        for (Patient patient : patientList) {
            for (int i = 0; i < rand.nextInt(2) + 1; ++i) {
                Staff recep = receptList.get(rand.nextInt(receptList.size()));
                Doctor doc = DoctorDAO.getMatchFromDepartment(DeptType.values()[rand.nextInt(17)]);

                MedicalRecord medrec = new MedicalRecord(
                        null,
                        patient.getPatientId(),
                        doc.getStaffId(),
                        recep.getStaffId(),
                        null,
                        null,
                        MedicalRecord.Status.PENDING,
                        0L,
                        null
                );

                String id = MedRecDAO.addMedRec(medrec);
                System.out.print("Patient id: " + patient.getPatientId());
                System.out.println("| MedRec id: " + id);
            }


        }

    }

    public static void populate(boolean clearDBFlag) {
        try {
            if (clearDBFlag)
                clearDB();
            //UNUSED
            populateDoctor();
            populateStaff();
            populateUser();
            populatePatient();
            populateMachine();
            populateMedicine();

            populateMedRec();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
