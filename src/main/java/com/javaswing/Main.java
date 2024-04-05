package com.javaswing;

import com.google.cloud.Timestamp;
import com.javafirebasetest.dao.DoctorDAO;
import com.javafirebasetest.dao.MedRecDAO;
import com.javafirebasetest.entity.DeptType;
import com.javafirebasetest.entity.Doctor;

import java.util.List;

public class Main {
    public static void main(String[] args) {
//        List<Doctor> doctorList = DoctorDAO.getDoctorByDepartment(DeptType.NUCLEAR_MEDICINE);
//        for (Doctor doc : doctorList){
//            System.out.println(doc);
//        }

        Doctor doctor = DoctorDAO.getDoctorById("EQTOqwQ55vGsqfC7YrPS");
        System.out.println(doctor);

//        SwingUtilities.invokeLater(ReceptionistUI::new);
    }
}


