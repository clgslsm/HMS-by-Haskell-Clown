package com.javaswing;

import com.javafirebasetest.dao.MedRecDAO;
import com.javafirebasetest.dao.MedicineDAO;
import com.javafirebasetest.dao.PatientDAO;
import com.javafirebasetest.dao.StaffDAO;
import com.javafirebasetest.entity.DeptType;
import com.javafirebasetest.entity.MedicalRecord;
import com.javafirebasetest.entity.Medicine;
import com.javafirebasetest.entity.TestResult;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ViewMedicalRecordPanel extends JPanel {
    JLabel message = new JLabel("");
    JButton backButton = new RoundedButton(" Return ");
    JTextArea diagnosis;
    JTextField rating;
    JRadioButton Prescriptions = new JRadioButton("Prescriptions");
    JRadioButton Test = new JRadioButton("Test");
    JComboBox<String> medicineButton;
    JTextField quantity;
    JTextArea testDecription;
    RoundedButton testResult;
    RoundedButton saveButton;
    String pre = "";
    String filePath = "";
    MedicalRecord mr;
    ViewMedicalRecordPanel(String id, String userId) throws ExecutionException, InterruptedException {
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 40));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        mr = MedRecDAO.getMedRecById(id);

        JLabel title = new JLabel("Medical record form");
        title.setFont(new Font("Courier",Font.BOLD,30));
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

        JPanel body = Body(mr, userId);
        add(pageHeader);
        add(body);
    }

    JPanel Body(MedicalRecord mr, String userId) throws ExecutionException, InterruptedException {
        JPanel body = new JPanel();
        body.setLayout(new GridLayout(1,2));
        JPanel inform = Inform(mr, userId);
        JPanel exam = Exam(mr, userId);
        body.add(inform);
        body.add(exam);
        return body;
    }

    JPanel Inform(MedicalRecord mr, String userId) throws ExecutionException, InterruptedException {
        // Patient's name
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(new Font("Courier",Font.PLAIN,16));
        nameLabel.setBounds(100,30,100,20);
        JLabel name = new JLabel(PatientDAO.getPatientById(mr.getPatientId()).getName());
        name.setFont(new Font("Courier",Font.PLAIN,16));
        name.setBounds(200,30,200,20);

        //  Patient's phone number
        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setFont(new Font("Courier",Font.PLAIN,16));
        phoneLabel.setBounds(100,70,100,20);
        JLabel phone = new JLabel(PatientDAO.getPatientById(mr.getPatientId()).getPhoneNumber());
        phone.setFont(new Font("Courier",Font.PLAIN,16));
        phone.setBounds(200,70,200,20);

        // Patient's gender
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("Courier",Font.PLAIN,16));
        genderLabel.setBounds(100,110,100,20);
        JLabel gender = new JLabel(PatientDAO.getPatientById(mr.getPatientId()).getGender().getValue());
        gender.setFont(new Font("Courier",Font.PLAIN,16));
        gender.setBounds(200,110,100,20);

        // Date of birth (DOB)
        JLabel DOBLabel = new JLabel("Date of birth");
        DOBLabel.setFont(new Font("Courier",Font.PLAIN,16));
        DOBLabel.setBounds(100,150,100,20);
        JLabel DOB = new JLabel(PatientDAO.getPatientById(mr.getPatientId()).getBirthDate().toString());
        DOB.setFont(new Font("Courier",Font.PLAIN,16));
        DOB.setBounds(200, 150, 300, 25);

        // Patient's blood group
        JLabel bloodGroupLabel = new JLabel("Blood type");
        bloodGroupLabel.setFont(new Font("Courier",Font.PLAIN,16));
        bloodGroupLabel.setBounds(100,190,100,20);
        JLabel bloodGroup = new JLabel(PatientDAO.getPatientById(mr.getPatientId()).getBloodGroup().getValue());
        bloodGroup.setFont(new Font("Courier",Font.PLAIN,16));
        bloodGroup.setBounds(200,190,100,20);

        JLabel ratingLabel = new JLabel("Service rating");
        ratingLabel.setFont(new Font("Courier",Font.PLAIN,16));
        ratingLabel.setBounds(100,230,100,20);
        rating = new RoundedTextField(1, 20);
        rating.setBounds(200, 230, 230, 20);

        JLabel diagnosisLabel = new JLabel("Diagnosis");
        diagnosisLabel.setFont(new Font("Courier",Font.PLAIN,16));
        diagnosisLabel.setBounds(100,270,100,20);
        diagnosis = new RoundedTextArea(1, 1,20, Color.gray);
        diagnosis.setBounds(200, 270, 300, 200);
        diagnosis.setLineWrap(true);

        diagnosis.setEditable(false);
        Prescriptions.setEnabled(false);
        Test.setEnabled(false);

        Prescriptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Prescriptions.isSelected()) {
                    Test.setSelected(false);
                }
            }
        });
        Test.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Test.isSelected()) {
                    Prescriptions.setSelected(false);
                }
            }
        });

        Prescriptions.setBounds(100, 480, 150, 25);
        Prescriptions.setBackground(Color.white);
        Prescriptions.setFocusable(false);
        Prescriptions.setFocusPainted(false);
        Prescriptions.setFont(new Font("Courier",Font.PLAIN,16));
        Test.setBounds(350, 480, 150, 25);
        Test.setBackground(Color.white);
        Test.setFocusable(true);
        Test.setFocusPainted(false);
        Test.setFont(new Font("Courier",Font.PLAIN,16));

        message.setFont(new Font("Courier",Font.PLAIN,16));
        message.setForeground(Color.red);
        message.setBounds(200, 520, 300, 25);

        // Save Button
        saveButton = new RoundedButton(" Save ");
        saveButton.setBounds(200, 550, 80, 25);

        JPanel form = new JPanel();
        form.setBackground(Color.white);
        form.setLayout(null);

        form.add(nameLabel);
        form.add(name);
        form.add(phoneLabel);
        form.add(phone);
        form.add(genderLabel);
        form.add(gender);
        form.add(DOBLabel);
        form.add(DOB);
        form.add(bloodGroupLabel);
        form.add(bloodGroup);
        form.add(ratingLabel);
        form.add(rating);
        form.add(diagnosisLabel);
        form.add(diagnosis);
        form.add(Prescriptions);
        form.add(Test);
        form.add(message);
        form.add(saveButton);

        return form;
    }
    JPanel Exam(MedicalRecord mr, String userId) {
        JPanel exam = new JPanel();
        exam.setLayout(null);
        exam.setBackground(Color.white);

        JLabel title1 = new JLabel("Prescriptions");
        title1.setFont(new Font("Courier",Font.BOLD,20));
        title1.setForeground(Color.gray);
        title1.setBounds(100, 30, 400, 25);

        JLabel medicineLabel = new JLabel("Medicine");
        medicineLabel.setFont(new Font("Courier",Font.PLAIN,16));
        medicineLabel.setBounds(100,70,100,20);

        int i = 0;
        List<Medicine> medicinesList = MedicineDAO.getAllMedicine();
        String[] m = new String[medicinesList.size()];
        for (Medicine me : medicinesList) {
            m[i] = me.getMedicineName();
            i++;
        }
        medicineButton = new JComboBox<>(m);
        medicineButton.setBackground(Color.white);
        medicineButton.setBorder(BorderFactory.createEmptyBorder());
        medicineButton.setBounds(200, 70, 200, 20);

        JLabel quantityLabel = new JLabel("Quantity");
        quantityLabel.setFont(new Font("Courier",Font.PLAIN,14));
        quantityLabel.setBounds(100,110,100,20);
        quantity = new RoundedTextField(1, 20);
        quantity.setBounds(200, 110, 200, 20);

        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new Object[]{"Name", "Quantity"});

        // Tạo JTable với DefaultTableModel
        JTable table = new JTable(model);
        table.getTableHeader().setFont(new Font("Courier", Font.BOLD, 12));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(32, 136, 203));
        table.getTableHeader().setForeground(new Color(255,255,255));

        table.setFocusable(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(0x126DA6));
        table.setSelectionForeground(Color.white);
        table.setShowVerticalLines(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(new Font("Courier",Font.PLAIN,13));
        table.setBackground(Color.white);
        table.setRowHeight(30);

        // Thiết lập kích thước cho table
        table.setPreferredScrollableViewportSize(new Dimension(200, 200));

        // Tạo JScrollPane và thêm table vào đó
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(100, 150, 400, 200);

        JButton addMedicineToPrecription = new RoundedButton(" Add ");
        addMedicineToPrecription.addActionListener(_->{
            if (medicineButton.getSelectedItem() != null && !quantity.getText().isEmpty()) {
                Object[] newOb = {medicineButton.getSelectedItem(), quantity.getText()};
                model.addRow(newOb);
            }
        });
        addMedicineToPrecription.setBounds(450, 100, 80, 25);

        //EDIT
        table.setEditingColumn(1);
        table.getDefaultEditor(Object.class).addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                saveData(table);
            }
            @Override
            public void editingCanceled(ChangeEvent e) {
                // Do nothing when editing is canceled
            }
        });
        // DELETE
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) { // Only proceed for right mouse button clicks
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        int response = JOptionPane.showConfirmDialog(null, "Do you want to delete this row?",
                                "Confirm", JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.YES_OPTION) {
                            // Delete the row
                            DefaultTableModel model = (DefaultTableModel) table.getModel();
                            model.removeRow(row);
                        }
                    }
                }
            }
        });

        JLabel title2 = new JLabel("Test");
        title2.setFont(new Font("Courier",Font.BOLD,20));
        title2.setForeground(Color.gray);
        title2.setBounds(100, 370, 400, 25);

        JLabel desLabel = new JLabel("Decription");
        desLabel.setFont(new Font("Courier",Font.PLAIN,16));
        desLabel.setBounds(100,410,100,20);
        testDecription = new RoundedTextArea(1, 1,20, Color.gray);
        testDecription.setBounds(200, 410, 300, 100);
        testDecription.setLineWrap(true);

        JLabel resLabel = new JLabel("Result");
        resLabel.setFont(new Font("Courier",Font.PLAIN,16));
        resLabel.setBounds(100,520,100,20);
        testResult = new RoundedButton("Choose result file");
        testResult.setBounds(200, 520, 150, 80);
        if (mr.getStatus().getValue().equals("Tested")) {
            testResult.setText("View the result");
        }

        testResult.addActionListener(_->{
            if (mr.getStatus().getValue().equals("Testing")) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePath= selectedFile.getAbsolutePath();
                }
            }
            else if (mr.getStatus().getValue().equals("Tested")) {
                mr.openAnalysisFile();
            }
        });

        Prescriptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Prescriptions.isSelected()) {
                    Test.setSelected(false);
                    // Khi chọn "Prescriptions", cho phép chỉnh sửa Medicine và Quantity
                    medicineButton.setEnabled(true);
                    quantity.setEditable(true);
                    // Vô hiệu hóa các trường liên quan đến Test
                    testDecription.setEnabled(false);
                    testResult.setEnabled(false);
                    addMedicineToPrecription.setEnabled(true);
                }
            }
        });

        Test.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Test.isSelected()) {
                    Prescriptions.setSelected(false);
                    // Khi chọn "Test", vô hiệu hóa Medicine và Quantity
                    medicineButton.setEnabled(false);
                    quantity.setEditable(false);
                    // Cho phép chỉnh sửa các trường liên quan đến Test
                    testDecription.setEnabled(true);
                    addMedicineToPrecription.setEnabled(false);
                    testDecription.setBackground(Color.white);

                    if (StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Doctor")) {
                        testResult.setEnabled(false);
                    }
                    else if (StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Technician")) {
                        testResult.setEnabled(true);
                    }
                }
            }
        });

        diagnosis.setEditable(false);
        Prescriptions.setEnabled(false);
        Test.setEnabled(false);
        rating.setEditable(false);

        medicineButton.setEnabled(false);
        quantity.setEditable(false);
        testDecription.setEnabled(false);
        testResult.setEnabled(false);
        addMedicineToPrecription.setEnabled(false);

        saveButton.setEnabled(false);
        
        // Trạng thái chờ, receptionist được xem, không được chỉnh sửa và lưu
        //                 doctor được chỉnh sửa và lưu 
        if (mr.getStatus().getValue().equals("Pending") && StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Receptionist")) {
            // chỉ xem
        }
        if (mr.getStatus().getValue().equals("Pending") && StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Doctor")) {
            diagnosis.setEditable(true);
            Prescriptions.setEnabled(true);
            Test.setEnabled(true);
            
            saveButton.setEnabled(true);
        }
        // Trạng thái chờ xét nghiệm, chỉ tech mới được sửa test result
        else if (mr.getStatus().getValue().equals("Testing") && StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Technician")) {
            testResult.setEnabled(true);

            saveButton.setEnabled(true);
            Test.setSelected(true);
            Prescriptions.setSelected(false);
        }
        // Trạng thái xét nghiệm xong, chỉ bác sĩ được sửa đơn thuốc
        else if (mr.getStatus().getValue().equals("Tested") && StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Doctor")) {
            medicineButton.setEnabled(true);
            quantity.setEditable(true);
            addMedicineToPrecription.setEnabled(true);
            testResult.setEnabled(true);
            diagnosis.setEditable(true);
            
            saveButton.setEnabled(true);
            Prescriptions.setSelected(true);
            Test.setSelected(false);
        }
        // Trạng thái chẩn bệnh xong, chỉ recep chỉnh rating
        else if (mr.getStatus().getValue().equals("Diagnosed") && StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Receptionist")) {
            rating.setEditable(true);
            
            saveButton.setEnabled(true);
        }

        if (mr.getTestResult().getDiagnosis() != null) {
            diagnosis.setText(mr.getTestResult().getDiagnosis());
        }
        if (mr.getTestResult().getTestType() != null) {
            testDecription.setText(mr.getTestResult().getTestType());
        }
        if (mr.getTestResult().getPrescription() != null) {
            Object[][] ob = parseStringToObjectArray(mr.getTestResult().getPrescription());
            for (Object[] objects : ob) {
                model.addRow(objects);
                System.out.println(objects[1]);
            }
        }
        if (mr.getServiceRating() != null) {
            rating.setText(mr.getServiceRating().toString());
        }

        saveButton.addActionListener(_->{
            saveButton.setEnabled(false);
            saveData(table);
            TestResult t = null;
            if (StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Doctor") && Prescriptions.isSelected()) {
                t = new TestResult(null, null, diagnosis.getText(), pre);
                assert t != null;
                MedRecDAO.updateTestResult(mr.getMedRecId(), t);
               if (mr.getStatus().equals("Pending")) {
                   MedRecDAO.send(mr.getMedRecId(), true);
               }
            }
            else if (StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Doctor")&& Test.isSelected()) {
                t  = new TestResult(testDecription.getText(), null, diagnosis.getText(), null);
                assert t != null;
                MedRecDAO.updateTestResult(mr.getMedRecId(), t);
            }
            else if (StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Technician")) {
                t  = new TestResult(null, filePath,null, null);
                assert t != null;
                MedRecDAO.updateTestResult(mr.getMedRecId(), t);
            }
            else if (StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Receptionist")) {
                MedRecDAO.updateServiceRating(mr.getMedRecId(), Integer.parseInt(rating.getText()));
            }

            MedRecDAO.send(mr.getMedRecId());
            System.out.println(MedRecDAO.getMedRecById(mr.getMedRecId()).getStatus().getValue());
            message.setText("Medical record has been update");
            message.setVisible(true);
        });

        exam.add(title1);
        exam.add(medicineLabel);
        exam.add(medicineButton);
        exam.add(quantityLabel);
        exam.add(quantity);
        exam.add(addMedicineToPrecription);
        exam.add(scrollPane);
        exam.add(title2);
        exam.add(desLabel);
        exam.add(testDecription);
        exam.add(resLabel);
        exam.add(testResult);
        return exam;
    }
    private void saveData(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();
        int colCount = model.getColumnCount();
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                Object value = model.getValueAt(row, col);
                pre = pre + value + "\t";
            }
            pre = pre + "\n";
        }
    }
    public Object[][] parseStringToObjectArray(String input) {
        String[] rows = input.trim().split("\n"); // Tách các hàng dựa trên ký tự xuống dòng
        Object[][] result = new Object[rows.length][2]; // Mảng kết quả với 2 cột

        for (int i = 0; i < rows.length; i++) {
            String row = rows[i].trim();
            int firstSpaceIndex = row.indexOf(" ");
            if (firstSpaceIndex != -1) { // Check if a space is found in the row
                result[i][0] = row.substring(0, firstSpaceIndex - 1); // First column
                result[i][1] = row.substring(firstSpaceIndex - 1); // Second column
            } else {
                // If no space is found, handle the situation as needed
                System.err.println("No space found in input at row " + (i + 1));
                // You might want to set the second column to null or an empty string
                result[i][0] = row; // Set the entire row as the first column
                result[i][1] = ""; // Set the second column as an empty string
            }
        }
        return result;
    }
}

