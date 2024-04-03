package com.javaswing;

import com.javafirebasetest.dao.receptionist.PatientDAO;
import com.javafirebasetest.entity.Doctor;
import com.javafirebasetest.entity.Patient;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

class DoctorPanel extends JPanel {
//    ArrayList<Doctor> data = new ArrayList<>();
//    DefaultPage defaultPage;
    //ViewDoctorInfoPage viewDoctorInfoPage;
    DoctorPanel() {
//        CardLayout currentPage = new CardLayout();
//        this.setLayout(currentPage);
//        this.setBackground(Color.white);
//
//        defaultPage = new DefaultPage();
//
//        defaultPage.addDoctorBtn.addActionListener(_ -> {
//            // Create Patient Registration Page
//            AddNewPatientPage addPatientPage = new AddNewPatientPage();
//            this.add(addPatientPage, "add-patient-page");
//
//            // Get back to default page
//            addPatientPage.backButton.addActionListener(_ ->{
//                currentPage.removeLayoutComponent(addPatientPage);
//                currentPage.show(this,"default-page");
//            });
//
//            // Fill in the form and store the information of the new patient
//            addPatientPage.form.createBtn.addActionListener(_ ->{
//                String ID = addPatientPage.form.IDInput.getText();
//                String name = addPatientPage.form.nameInput.getText();
//                String gender;
//                if (addPatientPage.form.male.isSelected())
//                    gender = "Male";
//                else if (addPatientPage.form.female.isSelected())
//                    gender = "Female";
//                else gender = "Other";
//                String phone = addPatientPage.form.phoneInput.getText();
//                String address = addPatientPage.form.addressInput.getText();
//                String bloodGroup = addPatientPage.form.bloodGroupInput.getText();
//                String dateOfBirth = addPatientPage.form.DOBInput.getText();
//                System.out.println(PatientForm.reformatDate(dateOfBirth));
//
//                // Creating the map
//                Map<String, Object> patientInfo = new HashMap<>();
//                patientInfo.put("name", name);
//                patientInfo.put("gender", gender);
//                patientInfo.put("phoneNumber", phone);
//                patientInfo.put("address", address);
//                patientInfo.put("bloodGroup", bloodGroup);
//                patientInfo.put("birthDate", PatientForm.reformatDate(dateOfBirth));
//                Patient newPatient = new Patient(ID, patientInfo);
//                data.add(newPatient);
//                try {
//                    PatientDAO.addPatient(newPatient);
//                } catch (ExecutionException | InterruptedException ex) {
//                    throw new RuntimeException(ex);
//                }
//                defaultPage.addPatientToTable(newPatient);
////                System.out.println(data);
//
//                currentPage.removeLayoutComponent(addPatientPage);
//                currentPage.show(this,"default-page");
//            });
//
//            currentPage.show(this, "add-patient-page");
//        });
        setBackground(new Color(0xF1F8FF));

    }

}