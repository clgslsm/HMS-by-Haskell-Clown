package com.javaswing;
import com.javafirebasetest.dao.DoctorDAO;
import com.javafirebasetest.dao.MedRecDAO;
import com.javafirebasetest.dao.PatientDAO;
import com.javafirebasetest.dao.StaffDAO;
import com.javafirebasetest.entity.DeptType;
import com.javafirebasetest.entity.Doctor;
import com.javafirebasetest.entity.MedicalRecord;
import com.javafirebasetest.entity.Patient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.javafirebasetest.dao.DoctorDAO.getDoctorWithMinPatientCountByDepartment;
import static com.javaswing.CustomDatePicker.splitDate;

class PatientPanel extends JPanel {
    PatientDefaultPage defaultPage;
    ViewPatientInfoPage viewPatientInfoPage;
    ViewPatientInfoPage addPatientPage;
    ViewMedicalRecordPanel viewMedicalRecordPanel;
    PatientPanel(String userId) {
        CardLayout currentPage = new CardLayout();
        this.setLayout(currentPage);
        this.setBackground(Color.white);

        defaultPage = new PatientDefaultPage();
        PatientPanel parentPanel = this;

        // When we click "Add patient" => change to Patient Registration Page
        defaultPage.addPatientBtn.addActionListener(_ -> {
            // Create Patient Registration Page
            addPatientPage = defaultPage.viewPage(userId);

            this.add(addPatientPage, "add-patient-page");
            addPatientPage.form.saveButton.addActionListener(_->{
                if (addPatientPage.form.name.getText().isEmpty() || addPatientPage.form.address.getText().isEmpty() || addPatientPage.form.phone.getText().isEmpty() ||
                        addPatientPage.form.gender.getSelectedItem() == null || addPatientPage.form.bloodGroup.getSelectedItem() == null || addPatientPage.form.DOB.mergeDate().isEmpty()) {
                    addPatientPage.form.message.setText("The information can not be left blank!");
                    addPatientPage.form.message.setVisible(true);
                }
                else {
                    addPatientPage.form.message.setVisible(false);
                    System.out.println(addPatientPage.form.name.getText());
                    System.out.println(addPatientPage.form.address.getText());
                    System.out.println(addPatientPage.form.phone.getText());
                    System.out.println(Objects.requireNonNull(addPatientPage.form.gender.getSelectedItem()).toString());
                    System.out.println(Objects.requireNonNull(addPatientPage.form.bloodGroup.getSelectedItem()).toString());
                    System.out.println(CustomDatePicker.mergeDate());

                    Map<String, Object> patientInfo = new HashMap<>();
                    patientInfo.put("name", addPatientPage.form.name.getText());
                    patientInfo.put("gender", Objects.requireNonNull(addPatientPage.form.gender.getSelectedItem()).toString());
                    patientInfo.put("phoneNumber", addPatientPage.form.phone.getText());
                    patientInfo.put("address", addPatientPage.form.address.getText());
                    patientInfo.put("bloodGroup", Objects.requireNonNull(addPatientPage.form.bloodGroup.getSelectedItem()).toString());
                    patientInfo.put("birthDate", CustomDatePicker.mergeDate());

                    Patient newPatient = new Patient(null, patientInfo);
                    newPatient.setPatientId(PatientDAO.addPatient(newPatient));
                    System.out.println(newPatient.getPatientId());
                    PatientDAO.addPatient(newPatient);

                    addPatientPage.form.message.setText("New patient was added.");
                    addPatientPage.form.message.setVisible(true);

                    addPatientPage.form.medicalRecord = addPatientPage.form.MedicalRecord(newPatient.getPatientId(), userId);
                    addPatientPage.form.add(addPatientPage.form.medicalRecord);

                    defaultPage.repaint();

                    addPatientPage.form.table.addMouseListener(new java.awt.event.MouseAdapter()
                    {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            int column = addPatientPage.form.table.getColumnModel().getColumnIndexAtX(evt.getX());
                            int row = evt.getY() / addPatientPage.form.table.getRowHeight();

                            if (row < addPatientPage.form.table.getRowCount() && row >= 0 && column < addPatientPage.form.table.getColumnCount() && column >= 0) {

                                if (column == 5) {
                                    // Instead of simulating button click, print to terminal
                                    System.out.println(STR."MR for row: \{row}");

                                    try {
                                        viewMedicalRecordPanel = new ViewMedicalRecordPanel(addPatientPage.form.table.getValueAt(row,0).toString(), userId);
                                    } catch (ExecutionException | InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    //System.out.println(addPatientPage.form.table.getValueAt(row,0).toString());
                                    parentPanel.add(viewMedicalRecordPanel, "view-mr-page");
                                    currentPage.show(parentPanel, "view-mr-page");

                                    addPatientPage.backButton.addActionListener(_->{
                                        currentPage.removeLayoutComponent(viewMedicalRecordPanel);
                                        currentPage.show(parentPanel,"view-page");
                                    });
                                }
                            }
                        }
                    });
                }
            });

            // Get back to default page
            addPatientPage.backButton.addActionListener(_ ->{
                currentPage.removeLayoutComponent(addPatientPage);
                currentPage.show(this,"default-page");
                defaultPage.updateTableUI();
            });
            currentPage.show(this, "add-patient-page");
        });

        // View, Modify Patient's Information and Delete Patient
        defaultPage.patientList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = defaultPage.patientList.getColumnModel().getColumnIndexAtX(evt.getX());
                int row = evt.getY() / defaultPage.patientList.getRowHeight();

                if (row < defaultPage.patientList.getRowCount() && row >= 0 && column < defaultPage.patientList.getColumnCount() && column >= 0) {
                    Object value = defaultPage.patientList.getValueAt(row, column);

                    // View and Modify
                    if (value instanceof JButton && column == 6) {
                        // Instead of simulating button click, print to terminal
                        System.out.println(STR."Button clicked for row: \{row}");
                        try {
                            viewPatientInfoPage = defaultPage.viewPage(row, userId);

                            //if (table != null) {
                                viewPatientInfoPage.form.table.addMouseListener(new java.awt.event.MouseAdapter()
                                {
                                    @Override
                                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                                        int column = viewPatientInfoPage.form.table.getColumnModel().getColumnIndexAtX(evt.getX());
                                        int row = evt.getY() / viewPatientInfoPage.form.table.getRowHeight();

                                        if (row < viewPatientInfoPage.form.table.getRowCount() && row >= 0 && column < viewPatientInfoPage.form.table.getColumnCount() && column >= 0) {

                                            if (column == 5) {
                                                // Instead of simulating button click, print to terminal
                                                System.out.println(STR."MR for row: \{row}");

                                                try {
                                                    viewMedicalRecordPanel = new ViewMedicalRecordPanel(viewPatientInfoPage.form.table.getValueAt(row,0).toString(), userId);
                                                } catch (ExecutionException | InterruptedException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                parentPanel.add(viewMedicalRecordPanel, "view-mr-page");
                                                currentPage.show(parentPanel, "view-mr-page");

                                                viewMedicalRecordPanel.backButton.addActionListener(_->{
                                                    currentPage.removeLayoutComponent(viewMedicalRecordPanel);
                                                    currentPage.show(parentPanel,"view-page");
                                                });
                                            }
                                        }
                                    }
                                });
                            //} else {














                            //}
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        parentPanel.add(viewPatientInfoPage, "view-page");
                        currentPage.show(parentPanel, "view-page");

                        viewPatientInfoPage.form.saveButton.addActionListener(_ -> {
                            Patient patient = null;
                            try {
                                patient = PatientDAO.getPatientById(defaultPage.patientList.getValueAt(row,0).toString());
                            } catch (ExecutionException e) {
                                throw new RuntimeException(e);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println(viewPatientInfoPage.form.name.getText());
                            System.out.println(viewPatientInfoPage.form.address.getText());
                            System.out.println(viewPatientInfoPage.form.phone.getText());
                            System.out.println(Objects.requireNonNull(viewPatientInfoPage.form.gender.getSelectedItem()).toString());
                            System.out.println(Objects.requireNonNull(viewPatientInfoPage.form.bloodGroup.getSelectedItem()).toString());
                            System.out.println(viewPatientInfoPage.form.DOB.mergeDate());

                            PatientDAO.updatePatient(patient.getPatientId(),
                                    "name", viewPatientInfoPage.form.name.getText(),
                                    "birthDate", viewPatientInfoPage.form.DOB.mergeDate(),
                                    "gender", Objects.requireNonNull(viewPatientInfoPage.form.gender.getSelectedItem()).toString(),
                                    "address", viewPatientInfoPage.form.address.getText(),
                                    "phoneNumber", viewPatientInfoPage.form.phone.getText(),
                                    "bloodGroup", Objects.requireNonNull(viewPatientInfoPage.form.bloodGroup.getSelectedItem()).toString());
                            System.out.println(patient.getPatientId());

                            viewPatientInfoPage.form.message.setText("Update on patient has completed.");
                            viewPatientInfoPage.form.message.setVisible(true);

                            defaultPage.updateTableUI();
                        });

                        viewPatientInfoPage.backButton.addActionListener(_ ->{
                            currentPage.removeLayoutComponent(viewPatientInfoPage);
                            currentPage.show(parentPanel,"default-page");
                        });
                    }

                    // Delete patient
                    if (value instanceof JButton && column == 7) {
                        // Instead of simulating button click, print to terminal
                        System.out.println(STR."Button clicked for row: \{row}");
                        String ID = defaultPage.patientList.getValueAt(row,0).toString();
                        defaultPage.deletePatient(row);
                        PatientDAO.deletePatient(ID);
                    }
                }
            }
        });

        // Always show default page
        this.add(defaultPage, "default-page");
        currentPage.show(this, "default-page");
    }
}
class PatientDefaultPage extends JLabel {
    SearchEngine searchEngine = new SearchEngine();
    JButton addPatientBtn = AddPatientButton();
    CustomTableModel model;
    JTable patientList;
    PatientDefaultPage() {
        this.setMaximumSize(new Dimension(1300,600));
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 40));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Header container
        JPanel header = new JPanel();
        JLabel title = new JLabel("Patient Information");
        title.setFont(title.getFont().deriveFont(25F));
        title.setForeground(new Color(0x3497F9));
        header.setBackground(new Color(0xF1F8FF));
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));

        header.add(title);
        header.add(Box.createHorizontalGlue());
        header.add(searchEngine);
        header.add(Box.createHorizontalGlue());
        header.add(addPatientBtn);

        searchEngine.searchButton.addActionListener(_-> {
            try {
                showSearchResult(searchEngine.searchInput.getText());
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        //Table
        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        body.setBackground(Color.white);

        model = new CustomTableModel();
        List<Patient> allPatients = PatientDAO.getAllPatients();
        for (Patient p : allPatients) {
            addPatientToTable(p);
        }

        patientList = new JTable(model);

        // UI for patient list
        patientList.getTableHeader().setPreferredSize(new Dimension(patientList.getTableHeader().getWidth(), 40));
        patientList.getTableHeader().setFont(new Font("Courier", Font.BOLD, 16));
        patientList.getTableHeader().setOpaque(false);
        patientList.getTableHeader().setBackground(new Color(32, 136, 203));
        patientList.getTableHeader().setForeground(new Color(255,255,255));

        patientList.setFocusable(false);
        patientList.setIntercellSpacing(new java.awt.Dimension(0, 0));
        patientList.setSelectionBackground(new Color(0x126DA6));
        patientList.setSelectionForeground(Color.white);
        patientList.setShowVerticalLines(false);
        patientList.getTableHeader().setReorderingAllowed(false);
        patientList.setFont(new Font("Courier",Font.PLAIN,16));

        patientList.getColumn(" ").setCellRenderer(new ButtonRenderer());
        patientList.getColumn(" ").setCellEditor(new ButtonEditor(new JCheckBox()));
        patientList.getColumn("  ").setCellRenderer(new DeleteButtonRenderer());
        patientList.getColumn("  ").setCellEditor(new DeleteButtonEditor(new JCheckBox()));

        patientList.getColumn(" ").setPreferredWidth(10);
        patientList.getColumn("  ").setPreferredWidth(10);
        patientList.setRowHeight(60);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(patientList);
        body.add(scrollPane);

        this.add(header);
        JPanel space = new JPanel();
        space.setBackground(new Color(0xF1F8FF));
        space.setSize(new Dimension(100, 100));
        this.add(space);
        this.add(body);
    }
    void addPatientToTable (Patient patient){
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        DeleteButtonRenderer deleteButtonEditor = new DeleteButtonRenderer();
        Object[] rowData = new Object[]{patient.getPatientId(), patient.getName(), patient.getAge(), patient.getGender(), patient.getBloodGroup().getValue(), patient.getPhoneNumber(), buttonRenderer, deleteButtonEditor};
        model.addRow(rowData);
    }
    void deletePatient (int row){
        model.deleteRow(row);
    }
    public ViewPatientInfoPage viewPage(int row, String userId) throws ExecutionException, InterruptedException {
        // call patient ID
        Patient patient = PatientDAO.getPatientById(patientList.getValueAt(row,0).toString());
        ViewPatientInfoPage viewPage = new ViewPatientInfoPage(patient.getPatientId(), userId);
        viewPage.title.setText(STR."Information of \{patient.getName()}");
        viewPage.title.setFont(new Font("Courier", Font.BOLD,30));
        viewPage.title.setForeground(Color.gray);
        viewPage.form.message.setVisible(false);

        return viewPage;
    }
    public ViewPatientInfoPage viewPage(String userId) {
        // call patient ID
        ViewPatientInfoPage viewPage = new ViewPatientInfoPage(userId);
        viewPage.title.setText(STR."Patient Registration Form");
        viewPage.title.setFont(new Font("Courier",Font.BOLD,25));
        viewPage.title.setForeground(Color.gray);
        viewPage.form.message.setVisible(false);

        return viewPage;
    }
    public void showSearchResult(String name) throws ExecutionException, InterruptedException {
        if (!name.trim().isEmpty() && !name.trim().equals("Search by patient name")){
            try{
                List<Patient> res = PatientDAO.getPatientsByName(name);
                model.clearData();
                for (Patient p : res) {
                    addPatientToTable(p);
                }
            }
            catch (Exception e) {
                updateTableUI();
                searchEngine.searchInput.setText("No patient found");
                searchEngine.searchInput.setForeground(Color.red);
            }
        }
        else updateTableUI();
    }
    public void updateTableUI() {
        model.clearData();
        List<Patient> allPatients = PatientDAO.getAllPatients();
        for (Patient p : allPatients) {
            addPatientToTable(p);
        }
        System.out.println("Update");
    }
    static class CustomTableModel extends AbstractTableModel {
        // Data for each column
        private Object[][] data = {};

        // Column names
        private final String[] columnNames = {"ID","Name","Age","Gender","Blood Type","Phone Number"," ","  "};

        // Data types for each column
        @SuppressWarnings("rawtypes")
        private final Class[] columnTypes = {String.class,String.class,String.class,String.class,String.class,String.class,JButton.class,JButton.class};

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
            return columnIndex == 6 || columnIndex == 7;
        }

        // Method to add a new row to the table
        public void addRow(Object[] rowData) {
            Object[][] newData = new Object[data.length + 1][getColumnCount()];
            System.arraycopy(data, 0, newData, 0, data.length);
            newData[data.length] = rowData;
            data = newData;
            fireTableRowsInserted(data.length-1,data.length-1); // Notify the table that rows have been inserted
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
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setForeground(new Color(0x3497F9));
            setFont(new Font("Courier",Font.BOLD,16));
            setBackground(Color.white);
            setText("Edit");

            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
            setSize(25,25);
            return this;
        }
    }
    static class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(_ -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            button.setBackground(new Color(0x126DA6));
            button.setForeground(Color.WHITE);
            button.setText("Edit");
            button.setFont(new Font("Courier",Font.PLAIN,16));
            button.setFocusable(false);
            button.setSize(25,25);
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
    static class DeleteButtonRenderer extends JButton implements TableCellRenderer {
        public DeleteButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setForeground(new Color(0xdb524b));
            setFont(new Font("Courier",Font.BOLD,16));
            setBackground(Color.white);
            setText("Delete");

            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
            setSize(25,25);
            return this;
        }
    }
    static class DeleteButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public DeleteButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(_ -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            button.setBackground(new Color(0xdb524b));
            button.setForeground(Color.white);
            button.setFont(new Font("Courier",Font.PLAIN,16));
            button.setFocusable(false);
            button.setText("Delete");
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
    static class SearchEngine extends JPanel {
        JTextField searchInput = SearchBox();
        JButton searchButton = SearchButton();
        SearchEngine(){
            setBackground(new Color(0xF1F8FF));
            setMaximumSize(new Dimension(1000, 60));
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(searchInput);
            add(Box.createHorizontalStrut(10));
            add(searchButton);
        }
        JTextField SearchBox(){
            RoundedTextField field = new RoundedTextField(20, 20);
            field.setPreferredSize(new Dimension(1500, 40));
            field.setBackground(Color.white);
            field.setForeground(Color.GRAY);
            field.setFocusable(false);
            field.revalidate();
            field.setFont(new Font("Courier",Font.PLAIN,16));
            field.setText("Search by patient name");
            field.addMouseListener(new CustomMouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (field.getText().equals("Search by patient name") || field.getText().equals("No patient found")) {
                        field.setText("");
                        field.setForeground(Color.BLACK);
                    }
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    field.setFocusable(true);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    field.setFocusable(false);
                    if (field.getText().isEmpty()) {
                        field.setForeground(Color.GRAY);
                        field.setText("Search by patient name");
                    }
                }
            });
            return field;
        }
        JButton SearchButton(){
            JButton button = new RoundedButton("Search");
            button.setFont(new Font("Courier",Font.PLAIN,13));
            button.setFocusable(false);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(0x3497F9));
            button.setBounds(100, 100, 125, 60);
            button.setBorder(new EmptyBorder(10,10,10,10));
            return button;
        }
    }
    public JButton AddPatientButton(){
        JButton addPatientButton = new RoundedButton("  + Add patient  ");
        addPatientButton.setFont(new Font("Courier",Font.PLAIN,13));
        addPatientButton.setFocusable(false);
        addPatientButton.setForeground(Color.WHITE);
        addPatientButton.setBackground(new Color(0x3497F9));
        addPatientButton.setBounds(100, 100, 125, 60);
        addPatientButton.setBorder(new EmptyBorder(10,10,10,10));
        return addPatientButton;
    }
}
class ViewPatientInfoPage extends JPanel {
    JButton backButton = new RoundedButton(" Return ");
    JLabel title = new JLabel("#MedicalID");
    ViewMode form;
    ViewPatientInfoPage(String PatientID, String userid) throws ExecutionException, InterruptedException {
        title.setFont(title.getFont().deriveFont(18.0F));
        this.setBackground(Color.white);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 40));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel pageHeader = new JPanel();
        pageHeader.setBackground(Color.white);
        pageHeader.setLayout(new BoxLayout(pageHeader, BoxLayout.X_AXIS));
        pageHeader.add(backButton);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        pageHeader.add(Box.createHorizontalGlue());
        pageHeader.add(title);
        title.setAlignmentX(Component.RIGHT_ALIGNMENT);
        pageHeader.add(Box.createHorizontalGlue());

        form = new ViewMode(PatientID, userid);

        this.add(pageHeader);
        this.add(new Box.Filler(new Dimension(100,15), new Dimension(100,15), new Dimension(100,15)));
        this.add(form); // Registration form
    }
    ViewPatientInfoPage(String userId) {
        title.setFont(title.getFont().deriveFont(18.0F));
        this.setBackground(Color.white);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 40));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel pageHeader = new JPanel();
        pageHeader.setBackground(Color.white);
        pageHeader.setLayout(new BoxLayout(pageHeader, BoxLayout.X_AXIS));
        pageHeader.add(backButton);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        pageHeader.add(Box.createHorizontalGlue());
        pageHeader.add(title);
        title.setAlignmentX(Component.RIGHT_ALIGNMENT);
        pageHeader.add(Box.createHorizontalGlue());

        form = new ViewMode(userId);

        this.add(pageHeader);
        this.add(new Box.Filler(new Dimension(100,15), new Dimension(100,15), new Dimension(100,15)));
        this.add(form); // Registration form
    }

    static class ViewMode extends JPanel {
        JTextField name;
        JTextField phone;
        JComboBox<String> gender;
        CustomDatePicker DOB;
        JTextArea address;
        JComboBox<String> bloodGroup;
        JButton addAppointment;
        JButton saveButton;
        JButton cancelButton;
        JPanel PatientInfoForm;
        JPanel medicalRecord;
        MedicalRecordTableModel model;
        JTable table;
        JLabel message = new JLabel("");
        ViewMode(String PatientID, String userId) throws ExecutionException, InterruptedException {
            setLayout(new GridLayout(1,3));
            PatientInfoForm = Form(PatientID);
            add(PatientInfoForm);
            medicalRecord = MedicalRecord(PatientID, userId);
            add(medicalRecord);
        }
        ViewMode(String userId) {
            setLayout(new GridLayout(1,3));
            PatientInfoForm = Form();
            add(PatientInfoForm);
        }
        JPanel Form(String id) throws ExecutionException, InterruptedException {
            Patient patient = PatientDAO.getPatientById(id);
            JLabel title = new JLabel("Personal Information");
            title.setFont(new Font("Courier",Font.BOLD,20));
            title.setForeground(Color.gray);
            title.setBounds(50, 0, 400, 50);

            // Patient's name
            JLabel nameLabel = new JLabel("Name");
            nameLabel.setFont(new Font("Courier",Font.PLAIN,16));
            nameLabel.setBounds(100,60,100,20);
            name = new RoundedTextField(1, 20);
            name.setBounds(200,60,200,20);
            name.setText(patient.getName());

            //  Patient's phone number
            JLabel phoneLabel = new JLabel("Phone");
            phoneLabel.setFont(new Font("Courier",Font.PLAIN,16));
            phoneLabel.setBounds(100,100,100,20);
            phone = new RoundedTextField(1, 20);
            phone.setBounds(200,100,200,20);
            phone.setText(patient.getPhoneNumber());

            // Patient's gender
            JLabel genderLabel = new JLabel("Gender");
            genderLabel.setFont(new Font("Courier",Font.PLAIN,16));
            genderLabel.setBounds(100,140,100,20);
            String[] sex = {"Male", "Female", "Other"};
            gender = new JComboBox<>(sex);
            gender.setFont(new Font("Courier",Font.PLAIN,16));
            gender.setBackground(Color.white);
            gender.setBorder(BorderFactory.createEmptyBorder());
            gender.setBounds(200,140,100,20);
            gender.setSelectedItem(patient.getGender());

            // Date of birth (DOB)
            JLabel DOBLabel = new JLabel("Date of birth");
            DOBLabel.setFont(new Font("Courier",Font.PLAIN,16));
            DOBLabel.setBounds(100,180,100,20);
            String[] d = splitDate(patient.getBirthDate().toString());
            DOB = new CustomDatePicker(d);
            DOB.setBounds(200, 180, 300, 25);

            // Address
            JLabel addressLabel = new JLabel("Address");
            addressLabel.setFont(new Font("Courier",Font.PLAIN,16));
            addressLabel.setBounds(100,220,100,20);
            address = new RoundedTextArea(1, 1,20, Color.gray);
            address.setBounds(200, 220, 200, 100);
            address.setLineWrap(true);
            address.setText(patient.getAddress());

            // Patient's blood group
            JLabel bloodGroupLabel = new JLabel("Blood type");
            bloodGroupLabel.setFont(new Font("Courier",Font.PLAIN,16));
            bloodGroupLabel.setBounds(100,340,100,20);
            String[] bloodType = {"A+", "A-",
                    "B+", "B-",
                    "AB+", "AB-",
                    "O+", "O-"};
            bloodGroup = new JComboBox<>(bloodType);
            bloodGroup.setFont(new Font("Courier",Font.PLAIN,16));
            bloodGroup.setBackground(Color.WHITE);
            bloodGroup.setBorder(BorderFactory.createEmptyBorder());
            bloodGroup.setBounds(200,340,100,20);
            bloodGroup.setSelectedItem(patient.getBloodGroup());

            message.setFont(new Font("Courier",Font.PLAIN,16));
            message.setForeground(Color.red);
            message.setBounds(200, 380, 300, 25);

            // Cancel Button
            cancelButton = new RoundedButton(" Cancel");
            cancelButton.setBounds(250, 420, 80, 25);

            // Save Button
            saveButton = new RoundedButton(" Save");
            saveButton.setBounds(150, 420, 80, 25);

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
            form.add(cancelButton);
            return form;
        }
        JPanel Form() {
            JLabel title = new JLabel("Personal Information");
            title.setFont(new Font("Courier",Font.BOLD,25));
            title.setForeground(Color.gray);
            title.setBounds(50, 0, 400, 50);

            // Patient's name
            JLabel nameLabel = new JLabel("Name");
            nameLabel.setFont(new Font("Courier",Font.PLAIN,16));
            nameLabel.setBounds(100,60,100,20);
            name = new RoundedTextField(1, 20);
            name.setBounds(200,60,200,20);

            //  Patient's phone number
            JLabel phoneLabel = new JLabel("Phone");
            phoneLabel.setFont(new Font("Courier",Font.PLAIN,16));
            phoneLabel.setBounds(100,100,100,20);
            phone = new RoundedTextField(1, 20);
            phone.setBounds(200,100,200,20);

            // Patient's gender
            JLabel genderLabel = new JLabel("Gender");
            genderLabel.setFont(new Font("Courier",Font.PLAIN,16));
            genderLabel.setBounds(100,140,100,20);
            String[] sex = {"Male", "Female", "Other"};
            gender = new JComboBox<>(sex);
            gender.setFont(new Font("Courier",Font.PLAIN,16));
            gender.setBackground(Color.white);
            gender.setBorder(BorderFactory.createEmptyBorder());
            gender.setBounds(200,140,100,20);

            // Date of birth (DOB)
            JLabel DOBLabel = new JLabel("Date of birth");
            DOBLabel.setFont(new Font("Courier",Font.PLAIN,16));
            DOBLabel.setBounds(100,180,100,20);
            DOB = new CustomDatePicker(new String[]{"1", "July", "1990"});
            DOB.setBounds(200, 180, 300, 25);

            // Address
            JLabel addressLabel = new JLabel("Address");
            addressLabel.setFont(new Font("Courier",Font.PLAIN,16));
            addressLabel.setBounds(100,220,100,20);
            address = new RoundedTextArea(1, 1,20, Color.gray);
            address.setBounds(200, 220, 200, 100);
            address.setLineWrap(true);

            // Patient's blood group
            JLabel bloodGroupLabel = new JLabel("Blood type");
            bloodGroupLabel.setFont(new Font("Courier",Font.PLAIN,16));
            bloodGroupLabel.setBounds(100,340,100,20);
            String[] bloodType = {"A+", "A-",
                    "B+", "B-",
                    "AB+", "AB-",
                    "O+", "O-"};
            bloodGroup = new JComboBox<>(bloodType);
            bloodGroup.setFont(new Font("Courier",Font.PLAIN,16));
            bloodGroup.setBackground(Color.WHITE);
            bloodGroup.setBorder(BorderFactory.createEmptyBorder());
            bloodGroup.setBounds(200,340,100,20);

            message.setFont(new Font("Courier",Font.PLAIN,16));
            message.setForeground(Color.red);
            message.setBounds(200, 380, 300, 25);

            // Cancel Button
            cancelButton = new RoundedButton(" Cancel");
            cancelButton.setBounds(250, 420, 80, 25);

            // Save Button
            saveButton = new RoundedButton(" Save");
            saveButton.setBounds(150, 420, 80, 25);

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
            form.add(cancelButton);
            return form;
        }
        JPanel MedicalRecord(String PatientID, String userId) {
            JPanel medicalRecord = new JPanel();
            medicalRecord.setLayout(new BorderLayout());
            medicalRecord.setBackground(Color.white);

            JPanel header = new JPanel();
            header.setLayout(new BorderLayout());
            header.setBackground(Color.white);
            header.setBounds(25, 10, 475, 30);

            JLabel headerLabel = new JLabel("Medical Record");
            headerLabel.setFont(new Font("Courier",Font.BOLD,20));
            headerLabel.setForeground(Color.gray);
            headerLabel.setBounds(450, 0, 400, 50);

            addAppointment = AddAppointmentButton();
            header.add(headerLabel, BorderLayout.WEST);

            addAppointment.addActionListener(_->{
                AddAppointmentPopup popup = new AddAppointmentPopup(medicalRecord);
                if (popup.choice == 0){
                        Doctor chosenDoctor = DoctorDAO.getMatchFromDepartment(DeptType.fromValue((String) popup.dep.getSelectedItem()));
                        if (chosenDoctor != null) {
                            MedicalRecord newAppointment = new MedicalRecord(null,
                                                                                    PatientID,
                                                                                    chosenDoctor.getStaffId(),
                                                                                    userId, null, null,
                                                                                    MedicalRecord.Status.PENDING, 0L, null);
                            MedRecDAO.addMedRec(newAppointment);
                            if (newAppointment != null) {
                                ViewButtonRenderer buttonRenderer = new ViewButtonRenderer();
                                Object[] rowData = new Object[]{newAppointment.getMedRecId(), DoctorDAO.getDoctorById(newAppointment.getDoctorId()).getDepartment(), DoctorDAO.getDoctorById(newAppointment.getDoctorId()).getName(), newAppointment.getCheckIn(), newAppointment.getStatus(), buttonRenderer};
                                model.addRow(rowData);
                                int lastRowIndex = model.getRowCount() - 1;
                                model.setValueAt(newAppointment.getMedRecId(), lastRowIndex, 0); // Set ID của cuộc hẹn mới vào dòng vừa thêm
                            }
                        }
                        else {
                            JOptionPane.showOptionDialog(medicalRecord,"No available doctor","",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,null,0);
                        }
                }
            });
            assert addAppointment != null;
            if (StaffDAO.getStaffById(userId).getUserMode().getValue().equals("Receptionist")) {
                header.add(addAppointment, BorderLayout.EAST);
            }
            header.setBorder(new EmptyBorder(0,0,0,10));

            model = new MedicalRecordTableModel();

            List<MedicalRecord> medicalRecordList = MedRecDAO.getMedRecByPatientId(PatientID);
            if (!medicalRecordList.isEmpty()) {
                for (MedicalRecord medRecord : medicalRecordList){
                    ViewButtonRenderer buttonRenderer = new ViewButtonRenderer();
                    Object[] rowData = new Object[]{medRecord.getMedRecId(), DoctorDAO.getDoctorById(medRecord.getDoctorId()).getDepartment(), DoctorDAO.getDoctorById(medRecord.getDoctorId()).getName(), medRecord.getCheckIn(), medRecord.getStatus(), buttonRenderer};
                    model.addRow(rowData);
                }
            }

            table = new JTable(model);

            table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 40));
            table.getTableHeader().setFont(new Font("Courier", Font.BOLD, 12));
            table.getTableHeader().setOpaque(false);
            table.getTableHeader().setBackground(new Color(32, 136, 203));
            table.getTableHeader().setForeground(new Color(255,255,255));

            table.setFocusable(false);
            table.setIntercellSpacing(new java.awt.Dimension(0, 0));
            table.setSelectionBackground(new Color(0x126DA6));
            table.setSelectionForeground(Color.white);
            table.setShowVerticalLines(false);
            table.getTableHeader().setReorderingAllowed(false);
            table.setFont(new Font("Courier",Font.PLAIN,12));
            table.setBackground(Color.white);

            table.getColumn("View").setCellRenderer(new ViewButtonRenderer());
            table.getColumn("View").setCellEditor(new ViewButtonEditor(new JCheckBox()));

            table.setRowHeight(60);

            //assert table != null;
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(table);

            scrollPane.setBounds(0,60, 550,1000);
            scrollPane.setBackground(Color.white);

            medicalRecord.add(header,BorderLayout.NORTH);
            medicalRecord.add(scrollPane);
            return medicalRecord;
        }
        static class MedicalRecordTableModel extends AbstractTableModel {
            // Data for each column
            private Object[][] data = {};

            // Column names
            private final String[] columnNames = {"ID", "Department", "Dr", "Check in", "Status", "View"};

            // Data types for each column
            @SuppressWarnings("rawtypes")
            private final Class[] columnTypes = {String.class, String.class,String.class,String.class,String.class, JButton.class};

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
                return columnIndex == 5;
            }

            // Method to add a new row to the table
            public void addRow(Object[] rowData) {
                Object[][] newData = new Object[data.length + 1][getColumnCount()];
                System.arraycopy(data, 0, newData, 0, data.length);
                newData[data.length] = rowData;
                data = newData;
                fireTableRowsInserted(data.length - 1, data.length - 1); // Notify the table that rows have been inserted
            }
            public void clearData() {
                int rowCount = getRowCount();
                data = new Object[0][0];
                if (rowCount > 0) fireTableRowsDeleted(0, rowCount - 1); // Notify the table that rows have been deleted
            }
        }
        public JButton AddAppointmentButton(){
            JButton addAppointmentButton = new RoundedButton("  + Add appointment  ");
            addAppointmentButton.setForeground(Color.white);
            addAppointmentButton.setBackground(new Color(0x3497F9));
            addAppointmentButton.setMaximumSize(new Dimension(125,30));
            addAppointmentButton.setBorder(BorderFactory.createEmptyBorder());
            return addAppointmentButton;
        }
        static class ViewButtonRenderer extends JButton implements TableCellRenderer {
            public ViewButtonRenderer() {
                setOpaque(true);
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                setForeground(new Color(0x3497F9));
                setFont(new Font("Courier",Font.BOLD,16));
                setBackground(Color.white);
                setText("View");

                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
                setSize(25,25);
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
                button.addActionListener(e -> fireEditingStopped());
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                                                         boolean isSelected, int row, int column) {
                button.setBackground(new Color(0x126DA6));
                button.setForeground(Color.WHITE);
                button.setText("View");
                button.setFont(new Font("Courier",Font.PLAIN,16));
                button.setFocusable(false);
                button.setSize(25,25);
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
}
class CustomMouseListener implements MouseListener {
    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
class AddAppointmentPopup {
    public int choice;
    JComboBox<String> dep;
    AddAppointmentPopup(JPanel patient){
        int i = 0;
        String[] department = new String[DeptType.values().length];
        for (DeptType dt : DeptType.values()) {
            department[i] = dt.getValue();
            i++;
        }
        dep = new JComboBox<>(department);
        dep.setBackground(Color.white);
        dep.setBorder(BorderFactory.createEmptyBorder());

        Object[] message = {"Name of Department:", dep};

        ImageIcon icon = new ImageIcon("src/main/java/com/javaswing/img/schedule.png");
        Image image = icon.getImage(); // transform it
        image = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(image);  // transform it back

        choice = JOptionPane.showConfirmDialog(null, message, "Add appointment", JOptionPane.OK_CANCEL_OPTION);
    }
}