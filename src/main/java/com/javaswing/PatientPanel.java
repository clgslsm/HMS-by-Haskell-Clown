package com.javaswing;
import com.javafirebasetest.dao.DoctorDAO;
import com.javafirebasetest.dao.MedRecDAO;
import com.javafirebasetest.dao.PatientDAO;
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
    ArrayList<Patient> data = new ArrayList<>();
    PatientDefaultPage defaultPage;
    ViewPatientInfoPage viewPatientInfoPage;
    PatientPanel() {
        CardLayout currentPage = new CardLayout();
        this.setLayout(currentPage);
        this.setBackground(Color.white);

        defaultPage = new PatientDefaultPage();

        // When we click "Add patient" => change to Patient Registration Page
        defaultPage.addPatientBtn.addActionListener(_ -> {
            // Create Patient Registration Page
            AddNewPatientPage addPatientPage = new AddNewPatientPage();
            this.add(addPatientPage, "add-patient-page");

            // Get back to default page
            addPatientPage.backButton.addActionListener(_ ->{
                currentPage.removeLayoutComponent(addPatientPage);
                currentPage.show(this,"default-page");
            });

            addPatientPage.saveButton.addActionListener(_ -> {
                if (addPatientPage.name.getText().isEmpty() || addPatientPage.address.getText().isEmpty() || addPatientPage.phone.getText().isEmpty() ||
                        addPatientPage.gender.getSelectedItem() == null || addPatientPage.bloodGroup.getSelectedItem() == null || addPatientPage.DOB.mergeDate().isEmpty()) {
                    addPatientPage.message.setText("The information can not be left blank!");
                    addPatientPage.message.setVisible(true);
                } else {
                    addPatientPage.message.setVisible(false);
                    System.out.println(addPatientPage.name.getText());
                    System.out.println(addPatientPage.address.getText());
                    System.out.println(addPatientPage.phone.getText());
                    System.out.println(Objects.requireNonNull(addPatientPage.gender.getSelectedItem()).toString());
                    System.out.println(Objects.requireNonNull(addPatientPage.bloodGroup.getSelectedItem()).toString());
                    System.out.println(addPatientPage.DOB.mergeDate());

                    Map<String, Object> patientInfo = new HashMap<>();
                    patientInfo.put("name", addPatientPage.name.getText());
                    patientInfo.put("gender", Objects.requireNonNull(addPatientPage.gender.getSelectedItem()).toString());
                    patientInfo.put("phoneNumber", addPatientPage.phone.getText());
                    patientInfo.put("address", addPatientPage.address.getText());
                    patientInfo.put("bloodGroup", Objects.requireNonNull(addPatientPage.bloodGroup.getSelectedItem()).toString());
                    patientInfo.put("birthDate", addPatientPage.DOB.mergeDate());

                    Patient newPatient = new Patient(null, patientInfo);
                    newPatient.setPatientId(PatientDAO.addPatient(newPatient));
                    System.out.println(newPatient.getPatientId());
                    PatientDAO.addPatient(newPatient);

                    currentPage.removeLayoutComponent(addPatientPage);
                    currentPage.show(this,"default-page");
                    defaultPage.updateTableUI();
                }

//                PatientDAO.updatePatient(patientID,
//                    "name", name.getText(),
//                    "birthDate", DOB.mergeDate(),
//                    "gender", Objects.requireNonNull(gender.getSelectedItem()).toString(),
//                    "address", address.getText(),
//                    "phoneNumber", phone.getText(),
//                    "bloodGroup", Objects.requireNonNull(bloodGroup.getSelectedItem()).toString());
//                System.out.println(patientID);
            });
            currentPage.show(this, "add-patient-page");
        });

        // View, Modify Patient's Information and Delete Patient
        PatientPanel parentPanel = this;
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
                            viewPatientInfoPage = defaultPage.viewPage(row);

                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        parentPanel.add(viewPatientInfoPage, "view-page");
                        currentPage.show(parentPanel, "view-page");
                        viewPatientInfoPage.form.saveButton.addActionListener(_-> {
                            viewPatientInfoPage.form.updateInfo();
                            defaultPage.updateTableUI();
                        });

                        viewPatientInfoPage.backButton.addActionListener(_ ->{
                            currentPage.removeLayoutComponent(viewPatientInfoPage);
                            currentPage.show(parentPanel,"default-page");
                            //defaultPage.updateTableUI();
                        });
                    }

                    // Delete patient
                    if (value instanceof JButton && column == 7) {
                        // Instead of simulating button click, print to terminal
                        System.out.println(STR."Button clicked for row: \{row}");
                        String ID = defaultPage.patientList.getValueAt(row,0).toString();
                        defaultPage.deletePatient(row, defaultPage.model);
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
            addPatientToTable(p, model);
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

        patientList.getColumn("Edit").setCellRenderer(new ButtonRenderer());
        patientList.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox()));
        patientList.getColumn("Delete").setCellRenderer(new DeleteButtonRenderer());
        patientList.getColumn("Delete").setCellEditor(new DeleteButtonEditor(new JCheckBox()));

        patientList.getColumn("Edit").setPreferredWidth(10);
        patientList.getColumn("Delete").setPreferredWidth(10);
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
    void addPatientToTable (Patient patient, CustomTableModel model){
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        DeleteButtonRenderer deleteButtonEditor = new DeleteButtonRenderer();
        Object[] rowData = new Object[]{patient.getPatientId(), patient.getName(), patient.getAge(), patient.getGender(), patient.getBloodGroup().getValue(), patient.getPhoneNumber(), buttonRenderer, deleteButtonEditor};
        model.addRow(rowData);
    }
    void deletePatient (int row, CustomTableModel model){
        model.deleteRow(row);
    }
    public ViewPatientInfoPage viewPage(int row) throws ExecutionException, InterruptedException {
        // call patient ID
        Patient patient = PatientDAO.getPatientById(patientList.getValueAt(row,0).toString());
        ViewPatientInfoPage viewPage = new ViewPatientInfoPage(patient.getPatientId());
        viewPage.title.setText(STR."Information of \{patient.getName()}");
        viewPage.title.setFont(new Font("Courier",Font.BOLD,25));
        viewPage.title.setForeground(Color.gray);
        return viewPage;
    }
    public void showSearchResult(String ID) throws ExecutionException, InterruptedException {
        if (!ID.trim().isEmpty() && !ID.trim().equals("Search by patient ID")){
            try{
            Patient res = PatientDAO.getPatientById(ID);
            model.clearData();
            addPatientToTable(res, model);}
            catch (Exception e) {
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
            addPatientToTable(p, model);
        }
        System.out.println("Update");
    }
    static class CustomTableModel extends AbstractTableModel {
        // Data for each column
        private Object[][] data = {};

        // Column names
        private final String[] columnNames = {"ID","Name","Age","Gender","Blood Type","Phone Number","Edit","Delete"};

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
            setForeground(new Color(0x3497F9));
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
            button.setBackground(new Color(0x126DA6));
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
            field.setText("Search by patient id");
            field.addMouseListener(new CustomMouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (field.getText().equals("Search by patient ID") || field.getText().equals("No patient found")) {
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
                        field.setText("Search by patient ID");
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
class AddNewPatientPage extends JPanel {
    JLabel message = new JLabel("");
    JButton backButton = new RoundedButton(" Return ");
    JTextField name;
    JTextField phone;
    JComboBox<String> gender;
    CustomDatePicker DOB;
    JTextArea address;
    JComboBox<String> bloodGroup;
    JButton addAppointment;
    RoundedButton saveButton;
    AddNewPatientPage() {
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 40));
        //setLayout(new GridLayout(1,2));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Patient Registration Form");
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
        nameLabel.setBounds(100,50,100,20);
        name = new RoundedTextField(1, 20);
        name.setBounds(200,50,200,20);
//        name.setText(patient.getName());

        //  Patient's phone number
        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setFont(new Font("Courier",Font.PLAIN,16));
        phoneLabel.setBounds(100,90,100,20);
        phone = new RoundedTextField(1, 20);
        phone.setBounds(200,90,200,20);
//        phone.setText(patient.getPhoneNumber());

        // Patient's gender
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("Courier",Font.PLAIN,16));
        genderLabel.setBounds(100,130,100,20);
        String[] sex = {"Male", "Female", "Other"};
        gender = new JComboBox<>(sex);
        gender.setFont(new Font("Courier",Font.PLAIN,16));
        gender.setBackground(Color.white);
        gender.setBorder(BorderFactory.createEmptyBorder());
        gender.setBounds(200,130,100,20);
//        gender.setSelectedItem(patient.getGender());

        // Date of birth (DOB)
        JLabel DOBLabel = new JLabel("Date of birth");
        DOBLabel.setFont(new Font("Courier",Font.PLAIN,16));
        DOBLabel.setBounds(100,170,100,20);
        String[] d = {"01" ,"April", "2024"};
        DOB = new CustomDatePicker(d);
        DOB.setBounds(200, 170, 300, 25);

        // Address
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setFont(new Font("Courier",Font.PLAIN,16));
        addressLabel.setBounds(100,210,100,20);
        address = new RoundedTextArea(1, 1,20, Color.gray);
        address.setBounds(200, 210, 200, 100);
        address.setLineWrap(true);
//        address.setText(patient.getAddress());

        // Patient's blood group
        JLabel bloodGroupLabel = new JLabel("Blood type");
        bloodGroupLabel.setFont(new Font("Courier",Font.PLAIN,16));
        bloodGroupLabel.setBounds(100,330,100,20);
        String[] bloodType = {"A+", "A-",
                "B+", "B-",
                "AB+", "AB-",
                "O+", "O-"};
        bloodGroup = new JComboBox<>(bloodType);
        bloodGroup.setFont(new Font("Courier",Font.PLAIN,16));
        bloodGroup.setBackground(Color.WHITE);
        bloodGroup.setBorder(BorderFactory.createEmptyBorder());
        bloodGroup.setBounds(200,330,100,20);
//        bloodGroup.setSelectedItem(patient.getBloodGroup());

        message.setFont(new Font("Courier",Font.PLAIN,16));
        message.setForeground(Color.red);
        message.setBounds(100,370,400,20);

        // Save Button
        saveButton = new RoundedButton(" Save");
        saveButton.setBounds(150, 410, 80, 25);

        setBackground(Color.white);
        add(pageHeader);

        JPanel form = new JPanel();
        form.setVisible(true);

        form.setLayout(null);
        form.setBounds(500, 80, 800, 800);
        form.setBackground(Color.white);

        //add(new Box.Filler(new Dimension(100,15), new Dimension(100,15), new Dimension(100,15)));
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

        add(form);
    }
}
class ViewPatientInfoPage extends JPanel {
    JButton backButton = new RoundedButton(" Return ");
    JLabel title = new JLabel("#MedicalID");
    ViewMode form;
    ViewPatientInfoPage(String PatientID) throws ExecutionException, InterruptedException {
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
        this.add(pageHeader);
        this.add(new Box.Filler(new Dimension(100,15), new Dimension(100,15), new Dimension(100,15)));
        form = new ViewMode(PatientID);
        this.add(form); // Registration form
    }

    static class ViewMode extends JPanel {
        String patientID;
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
        MedicalRecordTableModel model = new MedicalRecordTableModel();
        JTable table = new JTable(model);
        ViewMode(String PatientID) throws ExecutionException, InterruptedException {
            setLayout(new GridLayout(1,3));
            PatientInfoForm = Form(PatientID);
            add(PatientInfoForm);
            medicalRecord = MedicalRecord(PatientID);
            add(medicalRecord);
        }
        JPanel Form(String id) throws ExecutionException, InterruptedException {
            Patient patient = PatientDAO.getPatientById(id);
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

            // Cancel Button
            cancelButton = new RoundedButton(" Cancel");
            cancelButton.setBounds(250, 380, 80, 25);
            cancelButton.addActionListener(_ -> {
                name.setText(patient.getName());
                phone.setText(patient.getPhoneNumber());
                gender.setSelectedItem(patient.getGender());
                address.setText(patient.getAddress());
                bloodGroup.setSelectedItem(patient.getBloodGroup());
            });

            // Save Button
            saveButton = new RoundedButton(" Save");
            saveButton.setBounds(150, 380, 80, 25);
            saveButton.addActionListener(_ -> {

                System.out.println(name.getText());
                System.out.println(address.getText());
                System.out.println(phone.getText());
                System.out.println(Objects.requireNonNull(gender.getSelectedItem()).toString());
                System.out.println(Objects.requireNonNull(bloodGroup.getSelectedItem()).toString());
                System.out.println(DOB.mergeDate());

//                PatientDAO.updatePatient(patientID,
//                    "name", name.getText(),
//                    "birthDate", DOB.mergeDate(),
//                    "gender", Objects.requireNonNull(gender.getSelectedItem()).toString(),
//                    "address", address.getText(),
//                    "phoneNumber", phone.getText(),
//                    "bloodGroup", Objects.requireNonNull(bloodGroup.getSelectedItem()).toString());
//                System.out.println(patientID);
            });

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
            form.add(saveButton);
            form.add(cancelButton);
            return form;
        }
        JPanel MedicalRecord(String PatientID) {
            JPanel medicalRecord = new JPanel();
            medicalRecord.setLayout(null);
            medicalRecord.setBackground(Color.white);

            JPanel header = new JPanel();
            header.setLayout(new BorderLayout());
            header.setBackground(Color.white);
            header.setBounds(25, 10, 475, 30);

            JLabel headerLabel = new JLabel("Medical Record");
            headerLabel.setFont(new Font("Courier",Font.BOLD,25));
            headerLabel.setForeground(Color.gray);
            headerLabel.setBounds(450, 0, 400, 50);

            addAppointment = AddAppointmentButton();
            header.add(headerLabel, BorderLayout.WEST);

            addAppointment.addActionListener(_->{
                AddAppointmentPopup popup = new AddAppointmentPopup(medicalRecord);
                if (popup.choice == 0){
                    Doctor chosenDoctor = getDoctorWithMinPatientCountByDepartment(DeptType.fromValue((String) popup.dep.getSelectedItem()));
                    if (chosenDoctor != null) {
                        try {
                            MedicalRecord newAppointment = MedRecDAO.addMedRecByDoctorAndPatient(chosenDoctor, PatientDAO.getPatientById(PatientID));
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        updateAppointmentTable();
                    }
                    else {
                        JOptionPane.showOptionDialog(medicalRecord,"No available doctor","",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,null,0);
                    }
                }
            });
            assert addAppointment != null;
            header.add(addAppointment, BorderLayout.EAST);

            List<MedicalRecord> medicalRecordList = MedRecDAO.getMedRecByPatientId(PatientID);
            if (!medicalRecordList.isEmpty()) {
                for (MedicalRecord medRecord : medicalRecordList){
                Object[] rowData = new Object[]{medRecord.getDepartment(), DoctorDAO.getDoctorById(medRecord.getDoctorId()).getName(), medRecord.getCheckIn(), medRecord.getCheckOut(), medRecord.getObservation(), medRecord.getStatus(), medRecord.getServiceReview()};
                model.addRow(rowData);
                }
            }

            table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 40));
            table.getTableHeader().setFont(new Font("Courier", Font.BOLD, 12));
            table.getTableHeader().setOpaque(false);
            table.getTableHeader().setBackground(new Color(32, 136, 203));
            table.getTableHeader().setForeground(new Color(255,255,255));

            table.setFocusable(false);
            table.setIntercellSpacing(new java.awt.Dimension(0, 0));
            table.setSelectionBackground(new Color(0x9ACEF5));
            table.setShowVerticalLines(false);
            table.getTableHeader().setReorderingAllowed(false);
            table.setFont(new Font("Courier",Font.PLAIN,12));

            table.setRowHeight(60);

            assert table != null;
            //table.setPreferredScrollableViewportSize(new Dimension(400,500));
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(0,60, 550,1000);

            medicalRecord.add(header);
            medicalRecord.add(scrollPane);
            return medicalRecord;
        }
        public void updateInfo(){
//            PatientDAO.updatePatient(patientID,
//                    "name", name.getText(),
//                    "address", address.getText(),
//                    "phoneNumber", phone.getText(),
//                    "gender", Objects.requireNonNull(gender.getSelectedItem()).toString(),
//                    "bloodGroup", Objects.requireNonNull(bloodGroup.getSelectedItem()).toString(),
//                    "birthDate", PatientForm.reformatDate(DOB.getText().replace('/','-')));
//            System.out.println(patientID);
        }
        public void updateAppointmentTable(){
            model.clearData();
            List<MedicalRecord> medicalRecords = MedRecDAO.getMedRecByPatientId(patientID);
            for (MedicalRecord medicalRecord : medicalRecords) {
                Object[] rowData = new Object[]{medicalRecord.getDepartment(), DoctorDAO.getDoctorById(medicalRecord.getDoctorId()).getName(), medicalRecord.getCheckIn(), medicalRecord.getCheckOut(), medicalRecord.getObservation(), medicalRecord.getStatus(), medicalRecord.getServiceReview()};
                model.addRow(rowData);
            }
            System.out.println("Update");
        }
        static class MedicalRecordTableModel extends AbstractTableModel {
            // Data for each column
            private Object[][] data = {};

            // Column names
            private final String[] columnNames = {"Department", "Dr", "Check in", "Status", "View"};

            // Data types for each column
            @SuppressWarnings("rawtypes")
            private final Class[] columnTypes = {String.class,String.class,String.class,String.class, JButton.class};

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
    }
}
class PatientForm extends JPanel{
    JButton createBtn;
    JTextField nameInput ;
    JTextField phoneInput ;
    JComboBox<String> genderInput;
    JFormattedTextField DOBInput;
    JTextArea addressInput;
    JLabel alertBlank;
    JComboBox<String> bloodGroupInput;
    PatientForm() {
        JPanel form = Form();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK,1,true));
        setSize(1000,1000);
        add(form);
        setVisible(true);
    }

    public JPanel Form (){
        // Patient's name
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(300,50,95,20);
        nameInput = new JTextField();
        nameInput.setBounds(385,50,200,20);
        nameInput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nameInput.setBackground(Color.white);
                nameInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        });

        //  Patient's phone number
        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setBounds(300,80+ 20,95,20);
        phoneInput = new JTextField();
        phoneInput.setBounds(385,80+ 20,200,20);
        phoneInput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                phoneInput.setBackground(Color.white);
                phoneInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        });

        // Patient's gender
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setBounds(300,110+ 20,95,20);
        String[] sex = {"Male", "Female", "Other"};
        genderInput = new JComboBox<>(sex);
        genderInput.setBackground(Color.white);
        genderInput.setBorder(BorderFactory.createEmptyBorder());
        genderInput.setBounds(385, 130, 70, 20);

        // Date of birth (DOB)
        JLabel DOBLabel = new JLabel("Date of birth");
        DOBLabel.setBounds(300,140+ 20,100,20);
        DOBInput = new JFormattedTextField(createFormatter());
        DOBInput.setText("01-01-1980");
        DOBInput.setBounds(385, 140+ 20, 70, 20);

        // Address
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(300,170+ 20,100,20);
        addressInput = new JTextArea();
        addressInput.setBounds(385, 170+ 20, 200, 80);
        addressInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addressInput.setLineWrap(true);
        addressInput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addressInput.setBackground(Color.white);
                addressInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        });

        // Patient's blood group
        JLabel bloodGroupLabel = new JLabel("Blood type");
        bloodGroupLabel.setBounds(300,270+ 20,100,20);
        String[] bloodType = {"A+", "A-",
                "B+", "B-",
                "AB+", "AB-",
                "O+", "O-"};
        bloodGroupInput = new JComboBox<>(bloodType);
        bloodGroupInput.setBackground(Color.WHITE);
        bloodGroupInput.setBorder(BorderFactory.createEmptyBorder());
        bloodGroupInput.setBounds(385,270+ 20,70,20);

        // Alert if blank information exists
        alertBlank = new JLabel("------ Information cannot be blank ------", SwingConstants.CENTER);
        alertBlank.setBackground(new Color(0xFEEFEF));
        alertBlank.setForeground(Color.red);
        alertBlank.setBorder(BorderFactory.createLineBorder(Color.red, 2, true));
        alertBlank.setBounds(300, 410, 300, 30);
//        alertBlank.setHorizontalTextPosition(SwingConstants.CENTER);
        alertBlank.setVisible(false);

        // Create button
        createBtn = new JButton("CREATE");
        createBtn.setBackground(new Color(0x3497F9));
        createBtn.setForeground(Color.white);
        createBtn.setBounds(400,380-10,100,30);
        createBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (nameInput.getText().trim().isEmpty()){
                    alertBlank(nameInput);
                }
                if (phoneInput.getText().trim().isEmpty()){
                    alertBlank(phoneInput);
                }
                if (addressInput.getText().trim().isEmpty()){
                    alertBlank(addressInput);
                }
            }
        });

        JPanel form = new JPanel();
        form.setBackground(Color.white);
        form.setLayout(null);
        form.add(nameLabel);
        form.add(nameInput);
        form.add(phoneLabel);
        form.add(phoneInput);
        form.add(genderLabel);
        form.add(genderInput);
        form.add(DOBLabel);
        form.add(DOBInput);
        form.add(addressLabel);
        form.add(addressInput);
//        form.add(IDLabel);
//        form.add(IDInput);
        form.add(bloodGroupLabel);
        form.add(bloodGroupInput);
        form.add(createBtn);
        form.add(alertBlank);

        return form;
    }

    protected MaskFormatter createFormatter() {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter("##-##-####");
        } catch (java.text.ParseException exc) {
            System.err.println(STR."formatter is bad: \{exc.getMessage()}");
            System.exit(-1);
        }
        return formatter;
    }

    public static String reformatDate(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void alertBlank(Component textfield){
        textfield.setBackground(new Color(0xfeefef));
        if (textfield instanceof JTextField || textfield instanceof JTextArea){
            ((JTextComponent) textfield).setBorder(BorderFactory.createLineBorder(Color.red));
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