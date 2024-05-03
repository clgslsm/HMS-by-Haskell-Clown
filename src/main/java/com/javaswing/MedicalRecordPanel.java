package com.javaswing;

import com.javafirebasetest.dao.*;
import com.javafirebasetest.entity.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MedicalRecordPanel extends JPanel {
    User user;
    MedicalRecordDefaultPage defaultPage;
    CardLayout currentPage = new CardLayout();
    public MedicalRecordPanel(User user) throws ExecutionException, InterruptedException {
        this.user = user;
        this.setLayout(currentPage);
        this.setBackground(Color.white);

        defaultPage = new MedicalRecordDefaultPage(this);

        // Always show default page
        this.add(defaultPage, "default-page");
        currentPage.show(this, "default-page");
    }
}
class MedicalRecordDefaultPage extends JPanel {
    MedicalRecordPanel parentPanel;
    static JLabel title = new JLabel("List of Medical Records");
    static CustomTableModel model;
    JTable medicalRecordList;
    public MedicalRecordDefaultPage(MedicalRecordPanel parentPanel) throws ExecutionException, InterruptedException {
        this.parentPanel = parentPanel;
        this.setMaximumSize(new Dimension(1300, 600));
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 35));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.add(headerPanel());
        JPanel space = new JPanel();
        space.setBackground(Color.white);
        space.setSize(new Dimension(40, 40));
        this.add(space);
        this.add(bodyContainer());
    }
    JPanel headerPanel(){
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));

        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new GridLayout(2,1));
        titleContainer.setBackground(Color.white);
        title.setFont(title.getFont().deriveFont(28F));
        title.setForeground(new Color(0x3497F9));
        JLabel subTitle = new JLabel("Use appropriate machine to test patients");
        subTitle.setFont(new Font("Arial",Font.BOLD,15));
        titleContainer.add(title);
        titleContainer.add(subTitle);
        header.setBackground(Color.white);
        header.add(titleContainer);
        header.add(Box.createHorizontalGlue());
        return header;
    }
    JPanel bodyContainer() throws ExecutionException, InterruptedException {
        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        body.setBackground(Color.white);

        model = new CustomTableModel();
        medicalRecordList = new JTable(model);
        medicalRecordList.getTableHeader().setPreferredSize(new Dimension(medicalRecordList.getTableHeader().getWidth(), 40));
        medicalRecordList.getTableHeader().setFont(new Font("Courier", Font.BOLD, 13));
        medicalRecordList.getTableHeader().setOpaque(false);
        medicalRecordList.getTableHeader().setBackground(new Color(32, 136, 203));
        medicalRecordList.getTableHeader().setForeground(new Color(255,255,255));
        hideColumn(medicalRecordList);
        medicalRecordList.setFocusable(false);
        medicalRecordList.setIntercellSpacing(new java.awt.Dimension(0, 0));
        medicalRecordList.setSelectionBackground(new Color(0x126DA6));
        medicalRecordList.setSelectionForeground(Color.white);
        medicalRecordList.setShowVerticalLines(false);
        medicalRecordList.getTableHeader().setReorderingAllowed(false);
        medicalRecordList.setFont(new Font("Courier",Font.PLAIN,13));
        medicalRecordList.getColumn("").setCellRenderer(new ViewButtonRenderer());
        medicalRecordList.getColumn("").setCellEditor(new ViewButtonEditor(new JCheckBox()));

        // Create a custom cell renderer
        TableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Set the font color for the specific cell
                if (column == 4) {
                    String cellText = value.toString();
                    label.setFont(new Font("Courier",Font.BOLD,13));
                    if (cellText.equals("TESTING")) {
                        label.setForeground(new Color(0x3497F9));
                    } else if (cellText.equals("TESTED")) {
                        label.setForeground(new Color(0x87A922));
                    }
                } else {
                    // Reset the font color for other columns
                    label.setForeground(table.getForeground());
                }
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        };
        medicalRecordList.getColumnModel().getColumn(4).setCellRenderer(renderer);


        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < 4; i++) {
            medicalRecordList.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        medicalRecordList.setRowHeight(36);
        refreshMedicalRecordTable();

        medicalRecordList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = medicalRecordList.getColumnModel().getColumnIndexAtX(evt.getX());
                int row = evt.getY() / medicalRecordList.getRowHeight();

                if (row < medicalRecordList.getRowCount() && row >= 0 && column < medicalRecordList.getColumnCount() && column >= 0) {
                    Object value = medicalRecordList.getValueAt(row, column);
                    if (value instanceof JButton) {
                        // Instead of simulating button click, print to terminal
                        ViewMedicalRecordPanel viewMedicalRecordPage;
                        System.out.println(STR."Button clicked for row: \{row}");
                        try {
                            viewMedicalRecordPage = viewPage(row);
                            viewMedicalRecordPage.backButton.addActionListener(_->{
                                parentPanel.currentPage.show(parentPanel, "view-page");
                                parentPanel.currentPage.removeLayoutComponent(viewMedicalRecordPage);
                                try {
                                    refreshMedicalRecordTable();
                                } catch (ExecutionException e) {
                                    throw new RuntimeException(e);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        parentPanel.add(viewMedicalRecordPage, "view-page");
                        parentPanel.currentPage.show(parentPanel, "view-page");
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(medicalRecordList);
        body.add(scrollPane);

        return body;
    }
    static void addMedicineRecordToTable(MedicalRecord medrec) throws ExecutionException, InterruptedException {
        Doctor doc = DoctorDAO.getDoctorById(medrec.getDoctorId());
        ViewButtonRenderer viewButtonRenderer = new ViewButtonRenderer();
        Object[] rowData = new Object[]{
                medrec.getMedRecId()
                , PatientDAO.getPatientById(medrec.getPatientId()).getName()
                , doc.getDepartment()
                , doc.getName()
                , medrec.getStatus()
                , viewButtonRenderer
                };
        model.addRow(rowData);
    }
    static void refreshMedicalRecordTable() throws ExecutionException, InterruptedException {
        model.clearData();
        List<MedicalRecord> allMedrecTesting = MedRecDAO.getMedRecByCondition("status","TESTING");
        for (MedicalRecord medrec : allMedrecTesting) {
            addMedicineRecordToTable(medrec);
        }
        List<MedicalRecord> allMedrecTested = MedRecDAO.getMedRecByCondition("status","TESTED");
        for (MedicalRecord medrec : allMedrecTested) {
            addMedicineRecordToTable(medrec);
        }
        title.setText("List of Medical Records (%d)".formatted(allMedrecTesting.size()));
        System.out.println("Refresh Medical Record Table");
    }
    private void hideColumn(JTable table){
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.getColumnModel().getColumn(0).setResizable(false);
    }
    private ViewMedicalRecordPanel viewPage(int row) throws Exception {
        System.out.println(medicalRecordList.getModel().getValueAt(row,0).toString());
        System.out.println(parentPanel.user.getStaffId());
        // call medicine ID
        return new ViewMedicalRecordPanel(
                medicalRecordList.getModel().getValueAt(row,0).toString(),
                parentPanel.user.getStaffId()
                );
    }
    static class CustomTableModel extends AbstractTableModel {
        // Data for each column
        private Object[][] data = {};
        // Column names
        private final String[] columnNames = {"MedRecID","Patient Name","Department","Doctor Name","Status",""};
        // Data types for each column
        @SuppressWarnings("rawtypes")
        private final Class[] columnTypes = {String.class,String.class,String.class,String.class,String.class,JButton.class};
        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            // Make all cells non-editable
            return false;
        }

        // Method to add a new row to the table
        public void addRow(Object[] rowData) {
            Object[][] newData = new Object[data.length + 1][getColumnCount()];
            System.arraycopy(data, 0, newData, 0, data.length);
            newData[data.length] = rowData;
            data = newData;
            fireTableRowsInserted(data.length - 1, data.length - 1); // Notify the table that rows have been inserted
        }

        // Method to delete a row from the table
        public void deleteRow(int rowIndex) {
            if (rowIndex >= 0 && rowIndex < data.length) {
                Object[][] newData = new Object[data.length - 1][getColumnCount()];
                int dstIndex = 0;
                for (int srcIndex = 0; srcIndex < data.length; srcIndex++) {
                    if (srcIndex != rowIndex) {
                        newData[dstIndex++] = data[srcIndex];
                    }
                }
                data = newData;
                fireTableRowsDeleted(rowIndex, rowIndex); // Notify the table that rows have been deleted
            }
        }

        // Method to clear all data from the table
        public void clearData() {
            int rowCount = getRowCount();
            data = new Object[0][0];
            if (rowCount > 0) fireTableRowsDeleted(0, rowCount - 1); // Notify the table that rows have been deleted
        }
    }
    static class ViewButtonRenderer extends JButton implements TableCellRenderer {

        public ViewButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(Color.WHITE);
            setText("View Info");
            setForeground(Color.DARK_GRAY);
            setMaximumSize(new Dimension(70,18));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder());
            return this;
        }
    }
    static class ViewButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ViewButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(_ -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            button.setBackground(Color.white);
            button.setText("View Info");
            button.setSize(25,25);
            button.setForeground(Color.DARK_GRAY);
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            });
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
class ViewMedicalRecordPage extends JPanel {
    MedicalRecordPanel parentPanel;
    JLabel message = new JLabel("");
    JButton backButton = new RoundedButton(" Return ");
    JTextArea diagnosis;
    JTextArea testDecription;
    RoundedButton testResult;
    RoundedButton saveButton;
    String pre = "";
    String filePath = "";
    MedicalRecord mr;
    ViewMedicalRecordPage(MedicalRecordPanel parentPanel,String id, String userId) throws ExecutionException, InterruptedException {
        this.parentPanel = parentPanel;
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 40));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        mr = MedRecDAO.getMedRecById(id);

        JLabel title = new JLabel("Medical Record");
        title.setFont(new Font("Courier",Font.BOLD,30));
        title.setForeground(Color.gray);
        title.setBounds(50, 0, 400, 50);

        JPanel pageHeader = new JPanel();
        pageHeader.setBackground(Color.white);
        pageHeader.setLayout(new BoxLayout(pageHeader, BoxLayout.X_AXIS));
        pageHeader.add(backButton);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.addActionListener(_->{
            parentPanel.currentPage.removeLayoutComponent(this);
            parentPanel.currentPage.show(parentPanel,"default-page");
            try {
                MedicalRecordDefaultPage.refreshMedicalRecordTable();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
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
        JLabel DOB = new JLabel(PatientDAO.getPatientById(mr.getPatientId()).getformattedDate());
        DOB.setFont(new Font("Courier",Font.PLAIN,16));
        DOB.setBounds(200, 150, 300, 25);

        // Patient's blood group
        JLabel bloodGroupLabel = new JLabel("Blood type");
        bloodGroupLabel.setFont(new Font("Courier",Font.PLAIN,16));
        bloodGroupLabel.setBounds(100,190,100,20);
        JLabel bloodGroup = new JLabel(PatientDAO.getPatientById(mr.getPatientId()).getBloodGroup().getValue());
        bloodGroup.setFont(new Font("Courier",Font.PLAIN,16));
        bloodGroup.setBounds(200,190,100,20);

        JLabel diagnosisLabel = new JLabel("Diagnosis");
        diagnosisLabel.setFont(new Font("Courier",Font.PLAIN,16));
        diagnosisLabel.setBounds(100,270-30,100,20);
        diagnosis = new RoundedTextArea(1, 1,20, Color.gray);
        diagnosis.setBounds(200, 270-30, 300, 200);
        diagnosis.setLineWrap(true);
        diagnosis.setEditable(false);

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
        form.add(diagnosisLabel);
        form.add(diagnosis);
        form.add(message);
        form.add(saveButton);

        return form;
    }
    JPanel Exam(MedicalRecord mr, String userId) {
        JPanel exam = new JPanel();
        exam.setLayout(null);
        exam.setBackground(Color.white);

        JLabel title2 = new JLabel("Test");
        title2.setFont(new Font("Courier",Font.BOLD,20));
        title2.setForeground(Color.gray);
        title2.setBounds(100, 370-340, 400, 25);

        JLabel desLabel = new JLabel("Decription");
        desLabel.setFont(new Font("Courier",Font.PLAIN,16));
        desLabel.setBounds(100,410-340,100,20);
        testDecription = new RoundedTextArea(1, 1,20, Color.gray);
        testDecription.setBounds(200, 410-340, 300, 100);
        testDecription.setLineWrap(true);

        JLabel resLabel = new JLabel("Result");
        resLabel.setFont(new Font("Courier",Font.PLAIN,16));
        resLabel.setBounds(100,520-340,100,20);
        testResult = new RoundedButton("Choose result file");
        testResult.setBounds(200, 520-340, 200, 100);
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

        diagnosis.setEditable(false);
        testDecription.setEnabled(false);
        testResult.setEnabled(false);
        testDecription.setBackground(Color.gray);
        saveButton.setEnabled(false);


        if (mr.getStatus().getValue().equals("Testing") && StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Technician")) {
            testResult.setEnabled(true);
            saveButton.setEnabled(true);
        }
        // Trạng thái xét nghiệm xong, chỉ bác sĩ được sửa đơn thuốc
        else if (mr.getStatus().getValue().equals("Tested") && StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Technician")) {
            testResult.setEnabled(false);
            saveButton.setVisible(false);
        }

        if (mr.getTestResult().getDiagnosis() != null) {
            diagnosis.setText(mr.getTestResult().getDiagnosis());
        }
        if (mr.getTestResult().getTestType() != null) {
            testDecription.setText(mr.getTestResult().getTestType());
        }

        saveButton.addActionListener(_->{
            TestResult t = null;
            if (StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Technician")) {
                t  = new TestResult(null, filePath,null, null);
                assert t != null;
                MedRecDAO.updateTestResult(mr.getMedRecId(), t);
            }
            MedRecDAO.send(mr.getMedRecId());
            System.out.println(MedRecDAO.getMedRecById(mr.getMedRecId()).getStatus().getValue());
            message.setText("Medical record has been update");
            message.setVisible(true);
        });

        exam.add(title2);
        exam.add(desLabel);
        exam.add(testDecription);
        exam.add(resLabel);
        exam.add(testResult);
        return exam;
    }
}
