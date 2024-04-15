package com.javaswing;

import com.javafirebasetest.dao.MedRecDAO;
import com.javafirebasetest.dao.PatientDAO;
import com.javafirebasetest.entity.MedicalRecord;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class ViewMedicalRecordPanel extends JPanel {
    JLabel message = new JLabel("");
    JButton backButton = new RoundedButton(" Return ");
    JTextArea observation;
    JToggleButton medicine = new JToggleButton("Prescriptions");
    JComboBox<String> medicineButton;
    JTextField quantity;
    JToggleButton test = new JToggleButton("Test");
    JTextArea testDecription;
    JTextArea testResult;
    RoundedButton saveButton;
    MedicalRecord mr;
    ViewMedicalRecordPanel(String id) throws ExecutionException, InterruptedException {
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 40));
        //setLayout(new GridLayout(1,2));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        mr = MedRecDAO.getMedRecById(id);

        JLabel title = new JLabel("Medical record form");
        title.setFont(new Font("Courier",Font.BOLD,25));
        title.setForeground(Color.gray);
        title.setBounds(50, 0, 400, 50);

        JPanel pageHeader = new JPanel();
        pageHeader.setBackground(Color.white);
        pageHeader.setLayout(new BoxLayout(pageHeader, BoxLayout.X_AXIS));
        pageHeader.add(backButton);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        pageHeader.add(Box.createHorizontalGlue());
        pageHeader.add(title);
        title.setAlignmentX(Component.RIGHT_ALIGNMENT);
        pageHeader.add(Box.createHorizontalGlue());

        // Patient's name
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(new Font("Courier",Font.PLAIN,16));
        nameLabel.setBounds(100,60,100,20);
        JLabel name = new JLabel(PatientDAO.getPatientById(mr.getPatientId()).getName().toString());
        nameLabel.setFont(new Font("Courier",Font.PLAIN,16));
        nameLabel.setBounds(200,60,200,20);

        //  Patient's phone number
        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setFont(new Font("Courier",Font.PLAIN,16));
        phoneLabel.setBounds(100,100,100,20);
        JLabel phone = new JLabel(PatientDAO.getPatientById(mr.getPatientId()).getPhoneNumber());
        phone.setBounds(200,100,200,20);

        // Patient's gender
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("Courier",Font.PLAIN,16));
        genderLabel.setBounds(100,140,100,20);
        JLabel gender = new JLabel(PatientDAO.getPatientById(mr.getPatientId()).getGender().getValue());
        gender.setFont(new Font("Courier",Font.PLAIN,16));
        gender.setBounds(200,140,100,20);

        // Date of birth (DOB)
        JLabel DOBLabel = new JLabel("Date of birth");
        DOBLabel.setFont(new Font("Courier",Font.PLAIN,16));
        DOBLabel.setBounds(100,180,100,20);
        JLabel DOB = new JLabel(PatientDAO.getPatientById(mr.getPatientId()).getBirthDate().toString());
        DOB.setBounds(200, 180, 300, 25);

        // Address
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setFont(new Font("Courier",Font.PLAIN,16));
        addressLabel.setBounds(100,220,100,20);
        JLabel address = new JLabel(PatientDAO.getPatientById(mr.getPatientId()).getAddress());
        address.setBounds(200, 220, 200, 100);

        // Patient's blood group
        JLabel bloodGroupLabel = new JLabel("Blood type");
        bloodGroupLabel.setFont(new Font("Courier",Font.PLAIN,16));
        bloodGroupLabel.setBounds(100,340,100,20);
        JLabel bloodGroup = new JLabel(PatientDAO.getPatientById(mr.getPatientId()).getBloodGroup().getValue());
        bloodGroup.setFont(new Font("Courier",Font.PLAIN,16));
        bloodGroup.setBounds(200,340,100,20);

        message.setFont(new Font("Courier",Font.PLAIN,16));
        message.setForeground(Color.red);
        message.setBounds(200, 380, 300, 25);

        // Save Button
        saveButton = new RoundedButton(" Save");
        saveButton.setBounds(170, 420, 80, 25);

        JPanel form = new JPanel();
        form.setBackground(Color.white);
        form.add(title);
        form.setLayout(null);

        form.add(nameLabel);
        form.add(name);
        form.add(phoneLabel);
        form.add(phone);
        form.add(genderLabel);
        form.add(gender);
        form.add(DOBLabel);
        form.add(DOB);
        form.add(addressLabel);
        form.add(address);
        form.add(bloodGroupLabel);
        form.add(bloodGroup);
        form.add(message);
        form.add(saveButton);

        add(pageHeader);
        add(form);
    }
}