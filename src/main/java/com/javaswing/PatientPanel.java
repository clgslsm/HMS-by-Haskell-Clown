package com.javaswing;
import com.javafirebasetest.dao.DoctorDAO;
import com.javafirebasetest.dao.MedRecDAO;
import com.javafirebasetest.dao.PatientDAO;
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
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutionException;

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

            // Fill in the form and store the information of the new patient
            addPatientPage.form.createBtn.addActionListener(_ ->{
                String ID = addPatientPage.form.IDInput.getText();
                String name = addPatientPage.form.nameInput.getText();
                String gender = Objects.requireNonNull(addPatientPage.form.genderInput.getSelectedItem()).toString();
                String phone = addPatientPage.form.phoneInput.getText();
                String address = addPatientPage.form.addressInput.getText();
                String bloodGroup = Objects.requireNonNull(addPatientPage.form.bloodGroupInput.getSelectedItem()).toString();
                String dateOfBirth = addPatientPage.form.DOBInput.getText();
                System.out.println(PatientForm.reformatDate(dateOfBirth));

                // Creating the map
                if (!ID.trim().isEmpty() && !name.trim().isEmpty() && !phone.trim().isEmpty() && !address.trim().isEmpty()) {
                    Map<String, Object> patientInfo = new HashMap<>();
                    patientInfo.put("name", name);
                    patientInfo.put("gender", gender);
                    patientInfo.put("phoneNumber", phone);
                    patientInfo.put("address", address);
                    patientInfo.put("bloodGroup", bloodGroup);
                    patientInfo.put("birthDate", PatientForm.reformatDate(dateOfBirth));
                    Patient newPatient = new Patient(ID, patientInfo);
                    data.add(newPatient);
                    PatientDAO.addPatient(newPatient);
                    defaultPage.updateTableUI();

                    currentPage.removeLayoutComponent(addPatientPage);
                    currentPage.show(this, "default-page");
                }
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
                            viewPatientInfoPage.form.setViewMode();
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
    CustomTableModel model = new CustomTableModel();
    JTable patientList = new JTable(model);
    PatientDefaultPage() {
        this.setMaximumSize(new Dimension(1300,600));
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 75));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Header container
        JPanel header = new JPanel();
        JLabel title = new JLabel("Patient Information");
        title.setFont(title.getFont().deriveFont(25F));
        title.setForeground(new Color(0x3497F9));
        header.setBackground(Color.white);
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

        updateTableUI();
        // UI for patient list
        patientList.getTableHeader().setPreferredSize(new Dimension(patientList.getTableHeader().getWidth(), 40));
        patientList.getTableHeader().setFont(new Font("Courier", Font.BOLD, 13));
        patientList.getTableHeader().setOpaque(false);
        patientList.getTableHeader().setBackground(new Color(32, 136, 203));
        patientList.getTableHeader().setForeground(new Color(255,255,255));
        patientList.setFocusable(false);
        patientList.setIntercellSpacing(new java.awt.Dimension(0, 0));
        patientList.setSelectionBackground(new java.awt.Color(232, 57, 95));
        patientList.setShowVerticalLines(false);
        patientList.getTableHeader().setReorderingAllowed(false);
        patientList.setFont(new Font("Courier",Font.PLAIN,13));
        patientList.getColumn("View").setCellRenderer(new ButtonRenderer());
        patientList.getColumn("View").setCellEditor(new ButtonEditor(new JCheckBox()));
        patientList.getColumn("Del").setCellRenderer(new DeleteButtonRenderer());
        patientList.getColumn("Del").setCellEditor(new DeleteButtonEditor(new JCheckBox()));
        patientList.setRowHeight(40);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(patientList);
        body.add(scrollPane);

        this.add(header);
        JPanel space = new JPanel();
        space.setBackground(Color.white);
        space.setSize(new Dimension(40, 40));
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
    public ViewPatientInfoPage viewPage(int row) throws ExecutionException, InterruptedException {
        // call patient ID
        Patient patient = PatientDAO.getPatientById(patientList.getValueAt(row,0).toString());
        ViewPatientInfoPage viewPage = new ViewPatientInfoPage(patient.getPatientId());
        viewPage.form.patientID = patient.getPatientId();
        viewPage.title.setText(STR."#\{patient.getPatientId()}");
        viewPage.form.name.setText(patient.getName());
        viewPage.form.phone.setText(patient.getPhoneNumber());
        viewPage.form.bloodGroup.setSelectedItem(patient.getBloodGroup().getValue());
        viewPage.form.address.setText(patient.getAddress());
        viewPage.form.DOB.setText(patient.getformattedDate());
        viewPage.form.gender.setSelectedItem(patient.getGender().getValue());

        return viewPage;
    }
    public void showSearchResult(String ID) throws ExecutionException, InterruptedException {
        if (!ID.trim().isEmpty() && !ID.trim().equals("Search by patient ID")){
            Patient res = PatientDAO.getPatientById(ID);
            model.clearData();
            addPatientToTable(res);
        }
        else updateTableUI();
    }
    public void updateTableUI(){
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
        private final String[] columnNames = {"ID","Name","Age","Gender","Blood Type","Phone Number","View","Del"};

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
            setBackground(Color.white);
            setForeground(Color.BLUE);
            setIcon(new ImageIcon(new ImageIcon("src/main/java/com/javaswing/img/view-icon.png").getImage().getScaledInstance(15,15*143/256, Image.SCALE_SMOOTH)));
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
            button.setBackground(Color.white);
            button.setForeground(Color.BLUE);
            button.setIcon(new ImageIcon(new ImageIcon("src/main/java/com/javaswing/img/view-icon.png").getImage().getScaledInstance(15,15*143/256, Image.SCALE_SMOOTH)));
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
            setBackground(Color.red);
            setForeground(Color.white);
            setText("Delete");
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
            button.setBackground(Color.red);
            button.setForeground(Color.white);
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
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBackground(Color.white);
            setMaximumSize(new Dimension(350,28));
            add(searchInput);
            add(searchButton);
        }
        JTextField SearchBox(){
            JTextField field = new JTextField();
            field.setPreferredSize(new Dimension(200,30));
            field.setBackground(Color.white);
            field.setForeground(Color.GRAY);
            field.setFocusable(false);
            field.revalidate();
            field.setText("Search by patient ID");
            field.addMouseListener(new CustomMouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (field.getText().equals("Search by patient ID")) {
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
            JButton button = new JButton("Search");
            button.setBackground(Color.gray);
            button.setForeground(Color.white);
            button.setPreferredSize(new Dimension(80,36));
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
    BackButton backButton = new BackButton();
    PatientForm form = new PatientForm();
    AddNewPatientPage() {
        JLabel title = new JLabel("Patient Registration Form");
        title.setFont(title.getFont().deriveFont(20.0F));

        this.setBackground(Color.white);
        this.setMaximumSize(new Dimension(1300,600));
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 75));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel pageHeader = new JPanel();
        pageHeader.setBackground(Color.white);
        pageHeader.setLayout(new BoxLayout(pageHeader, BoxLayout.X_AXIS));
        pageHeader.add(backButton);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.setAlignmentY(0);
        pageHeader.add(Box.createHorizontalGlue());
        pageHeader.add(title);
        title.setAlignmentX(Component.RIGHT_ALIGNMENT);
        title.setAlignmentY(Component.TOP_ALIGNMENT);

        this.add(pageHeader);
        this.add(new Box.Filler(new Dimension(100,30), new Dimension(100,30), new Dimension(100,30)));
        this.add(form); // Registration form
    }
}
class ViewPatientInfoPage extends JPanel {
    BackButton backButton = new BackButton();
    JLabel title = new JLabel("#MedicalID");
    ViewMode form;

    ViewPatientInfoPage(String PatientID){
        title.setFont(title.getFont().deriveFont(18.0F));
        this.setBackground(Color.white);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 20));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel pageHeader = new JPanel();
        pageHeader.setBackground(Color.white);
        pageHeader.setLayout(new BoxLayout(pageHeader, BoxLayout.X_AXIS));
        pageHeader.add(backButton);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        pageHeader.add(Box.createHorizontalGlue());
        pageHeader.add(title);
        title.setAlignmentX(Component.RIGHT_ALIGNMENT);
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
        JTextField DOB;
        JTextArea address;
        JComboBox<String> bloodGroup;
        JButton addAppointment;
        JButton EditButton = EditInfoButton();
        JButton saveButton = SaveButton();
        JButton cancelButton = CancelButton();
        JPanel PatientInfoForm = Form();
        JPanel medicalRecord;
        MedicalRecordTableModel model = new MedicalRecordTableModel();
        JTable table = new JTable(model);
        ViewMode(String PatientID){
            setLayout(new GridLayout(1,2));
            add(PatientInfoForm);
            medicalRecord = MedicalRecord(PatientID);
            add(medicalRecord);
            setViewMode();
        }
        JPanel Form() {
            JLabel title = new JLabel("Personal Information");
            title.setFont(new Font("Arial", Font.BOLD,18));
            title.setForeground(Color.gray);
            title.setBounds(50, 10, 200, 50);

            // Patient's name
            JLabel nameLabel = new JLabel("Name");
            nameLabel.setBounds(300-250,50+ 20,95,20);
            name = new ViewModeTextField();
            name.setBounds(385-250,50+ 20,200,20);

            //  Patient's phone number
            JLabel phoneLabel = new JLabel("Phone");
            phoneLabel.setBounds(300-250,80+ 20,95,20);
            phone = new ViewModeTextField();
            phone.setBounds(385-250,80+ 20,200,20);

            // Patient's gender
            JLabel genderLabel = new JLabel("Gender");
            genderLabel.setBounds(300-250,110+ 20,95,20);
            String[] sex = {"Male", "Female", "Other"};
            gender = new JComboBox<>(sex);
            gender.setBackground(Color.white);
            gender.setBorder(BorderFactory.createEmptyBorder());
            gender.setBounds(385-250,130,70,20);

            // Date of birth (DOB)
            JLabel DOBLabel = new JLabel("Date of birth");
            DOBLabel.setBounds(300-250,140+ 20,100,20);
            DOB = new ViewModeTextField();
            DOB.setBounds(385-250, 140+ 20, 70, 20);

            // Address
            JLabel addressLabel = new JLabel("Address");
            addressLabel.setBounds(300-250,170+ 20,100,20);
            address = new JTextArea();
            address.setBounds(385-250, 170+ 20, 200, 80);
            address.setLineWrap(true);

            // Patient's blood group
            JLabel bloodGroupLabel = new JLabel("Blood type");
            bloodGroupLabel.setBounds(300-250,270+ 20,100,20);
            String[] bloodType = {"A+", "A-",
                    "B+", "B-",
                    "AB+", "AB-",
                    "O+", "O-"};
            bloodGroup = new JComboBox<>(bloodType);
            bloodGroup.setBackground(Color.WHITE);
            bloodGroup.setBorder(BorderFactory.createEmptyBorder());
            bloodGroup.setBounds(385-250,270+ 20,70,20);

            // Edit Button
            EditButton.setBounds(50, 525, 100, 25);

            // Cancel Button
            cancelButton.setBounds(50, 525, 80, 25);

            // Save Button
            saveButton.setBounds(150, 525, 80, 25);

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
            form.add(EditButton);
            form.add(cancelButton);
            form.add(saveButton);
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

            JLabel headerLabel = new JLabel("Medical Records");
            headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            headerLabel.setForeground(Color.gray);


            addAppointment = AddAppointmentButton();
            header.add(headerLabel, BorderLayout.WEST);
            assert addAppointment != null;
            header.add(addAppointment, BorderLayout.EAST);

            List<MedicalRecord> medicalRecordList = MedRecDAO.getMedRecByPatientId(PatientID);
            if (!medicalRecordList.isEmpty()) {
                MedicalRecord medicalRecord1 = medicalRecordList.getFirst();
                Object[] rowData = new Object[]{medicalRecord1.getDepartment(), DoctorDAO.getDoctorById(medicalRecord1.getDoctorId()).getName(), medicalRecord1.getCheckIn(), medicalRecord1.getCheckOut(), medicalRecord1.getObservation(), medicalRecord1.getStatus(), medicalRecord1.getServiceReview()};
                model.addRow(rowData);
            }
//            System.out.println(patientID);
//            medicalRecordList = MedRecDAO.getMedRecByPatientId(patientID);
//            if (!medicalRecordList.isEmpty()) {
//                MedicalRecord medicalRecord1 = medicalRecordList.getFirst();
//                Object[] rowData = new Object[]{medicalRecord1.getDepartment(), DoctorDAO.getDoctorById(medicalRecord1.getDoctorId()).getName(), medicalRecord1.getCheckIn(), medicalRecord1.getCheckOut(), medicalRecord1.getObservation(), medicalRecord1.getStatus(), medicalRecord1.getServiceReview()};
//                model.addRow(rowData);
//            }

            assert table != null;
            table.setPreferredScrollableViewportSize(new Dimension(400,500));
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(0,60, 515,500);

            medicalRecord.add(header);
            medicalRecord.add(scrollPane);
            return medicalRecord;
        }
        public void setViewMode(){
            EditButton.setVisible(true);
            saveButton.setVisible(false);
            cancelButton.setVisible(false);
            name.setBorder(BorderFactory.createEmptyBorder());
            name.setEditable(false);
            phone.setEditable(false);
            phone.setBorder(BorderFactory.createEmptyBorder());
            gender.setEditable(true);
            gender.setEnabled(false);
            gender.setBorder(BorderFactory.createEmptyBorder());
            DOB.setEditable(false);
            DOB.setBorder(BorderFactory.createEmptyBorder());
            address.setEditable(false);
            address.setBorder(BorderFactory.createEmptyBorder());
            bloodGroup.setEditable(true);
            bloodGroup.setEnabled(false);
            bloodGroup.setBorder(BorderFactory.createEmptyBorder());
        }
        private void setModifyMode(){
            EditButton.setVisible(false);
            saveButton.setVisible(true);
            cancelButton.setVisible(true);
            name.setEditable(true);
            name.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            phone.setEditable(true);
            phone.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            gender.setEnabled(true);
            DOB.setEditable(true);
            DOB.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            address.setEditable(true);
            address.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            bloodGroup.setEnabled(true);
        }
        public void updateInfo(){
            PatientDAO.updatePatient(patientID,
                    "name", name.getText(),
                    "address", address.getText(),
                    "phoneNumber", phone.getText(),
                    "gender", Objects.requireNonNull(gender.getSelectedItem()).toString(),
                    "bloodGroup", Objects.requireNonNull(bloodGroup.getSelectedItem()).toString(),
                    "birthDate", PatientForm.reformatDate(DOB.getText().replace('/','-')));
            System.out.println(patientID);
        }
        static class ViewModeTextField extends JTextField {
            ViewModeTextField(){
                super();
                setBorder(BorderFactory.createEmptyBorder());
                setBackground(Color.white);
            }
        }
        static class MedicalRecordTableModel extends AbstractTableModel {
            // Data for each column
            private Object[][] data = {};

            // Column names
            private final String[] columnNames = {"Tên khoa", "Tên bác sĩ", "Thời gian vào", "Thời gian ra", "Chẩn đoán", "Trạng thái", "Đánh giá dịch vụ"};

            // Data types for each column
            @SuppressWarnings("rawtypes")
            private final Class[] columnTypes = {String.class,String.class,String.class,String.class,String.class,String.class,String.class};

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
        }
        public JButton AddAppointmentButton(){
            JButton addAppointmentButton = new JButton("  + Add appointment  ");
            addAppointmentButton.setForeground(Color.white);
            addAppointmentButton.setBackground(new Color(0x3497F9));
            addAppointmentButton.setMaximumSize(new Dimension(125,30));
            addAppointmentButton.setBorder(BorderFactory.createEmptyBorder());
            return addAppointmentButton;
        }
        public JButton EditInfoButton(){
            JButton EditButton = new JButton("  Modify  ");
            EditButton.setForeground(Color.white);
            EditButton.setBackground(new Color(0x3497F9));
            EditButton.setMaximumSize(new Dimension(100,30));
            EditButton.setBorder(BorderFactory.createEmptyBorder());
            EditButton.addActionListener(_ -> setModifyMode());
            return EditButton;
        }
        public JButton SaveButton(){
            JButton Button = new JButton("  Save  ");
            Button.setForeground(Color.black);
            Button.setBackground(Color.green);
            Button.setMaximumSize(new Dimension(80,30));
            Button.setBorder(BorderFactory.createEmptyBorder());
            return Button;
        }
        public JButton CancelButton(){
            JButton Button = new JButton("  Cancel  ");
            Button.setForeground(Color.white);
            Button.setBackground(Color.gray);
            Button.setMaximumSize(new Dimension(80,30));
            Button.setBorder(BorderFactory.createEmptyBorder());
            Button.addActionListener(_ -> setViewMode());
            return Button;
        }
    }
}
class PatientForm extends JPanel{
    JButton createBtn;
    JTextField IDInput;
    JTextField nameInput ;
    JTextField phoneInput ;
    JComboBox<String> genderInput;
    JFormattedTextField DOBInput;
    JTextArea addressInput;
    JComboBox<String> bloodGroupInput;
    PatientForm() {
        JPanel form = Form();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK,1,true));
        setSize(700,400);
        add(form);
        setVisible(true);
    }

    public JPanel Form (){
        // Patient's ID
        JLabel IDLabel = new JLabel("Medical ID");
        IDLabel.setBounds(300,20 + 20,95,20);
        IDInput = new JTextField();
        IDInput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                IDInput.setBackground(Color.white);
                IDInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        });
        IDInput.setBounds(385,20 + 20,100,20);

        // Patient's name
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(300,50+ 20,95,20);
        nameInput = new JTextField();
        nameInput.setBounds(385,50+ 20,200,20);
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

        // Create button
        createBtn = new JButton("CREATE");
        createBtn.setBackground(new Color(0x3497F9));
        createBtn.setForeground(Color.white);
        createBtn.setBounds(400,380-10,100,30);
        createBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (IDInput.getText().trim().isEmpty()){
                    alertBlank(IDInput);
                }
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
        form.add(IDLabel);
        form.add(IDInput);
        form.add(bloodGroupLabel);
        form.add(bloodGroupInput);
        form.add(createBtn);

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