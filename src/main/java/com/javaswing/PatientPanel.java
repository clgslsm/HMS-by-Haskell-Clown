package com.javaswing;
import com.javafirebasetest.entity.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.javafirebasetest.dao.receptionist.PatientDAO;
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
                String gender;
                if (addPatientPage.form.male.isSelected())
                    gender = "Male";
                else if (addPatientPage.form.female.isSelected())
                    gender = "Female";
                else gender = "Other";
                String phone = addPatientPage.form.phoneInput.getText();
                String address = addPatientPage.form.addressInput.getText();
                String bloodGroup = addPatientPage.form.bloodGroupInput.getText();
                String dateOfBirth = addPatientPage.form.DOBInput.getText();
                System.out.println(PatientForm.reformatDate(dateOfBirth));

                // Creating the map
                Map<String, Object> patientInfo = new HashMap<>();
                patientInfo.put("name", name);
                patientInfo.put("gender", gender);
                patientInfo.put("phoneNumber", phone);
                patientInfo.put("address", address);
                patientInfo.put("bloodGroup", bloodGroup);
                patientInfo.put("birthDate", PatientForm.reformatDate(dateOfBirth));
                Patient newPatient = new Patient(ID, patientInfo);
                data.add(newPatient);
                try {
                    PatientDAO.addPatient(newPatient);
                } catch (ExecutionException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                defaultPage.addPatientToTable(newPatient);
//                System.out.println(data);

                currentPage.removeLayoutComponent(addPatientPage);
                currentPage.show(this,"default-page");
            });

            currentPage.show(this, "add-patient-page");
        });

        // See full information and medical records of a specific patient
        PatientPanel parentPanel = this;
        defaultPage.patientList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = defaultPage.patientList.getColumnModel().getColumnIndexAtX(evt.getX());
                int row = evt.getY() / defaultPage.patientList.getRowHeight();

                if (row < defaultPage.patientList.getRowCount() && row >= 0 && column < defaultPage.patientList.getColumnCount() && column >= 0) {
                    Object value = defaultPage.patientList.getValueAt(row, column);
                    if (value instanceof JButton) {
                        // Instead of simulating button click, print to terminal
                        System.out.println(STR."Button clicked for row: \{row}");
                        try {
                            viewPatientInfoPage = defaultPage.viewPage(row);
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        parentPanel.add(viewPatientInfoPage, "view-page");
                        currentPage.show(parentPanel, "view-page");

                        viewPatientInfoPage.backButton.addActionListener(_ ->{
                            currentPage.removeLayoutComponent(viewPatientInfoPage);
                            currentPage.show(parentPanel,"default-page");
                        });
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
    JButton addPatientBtn = AddPatientButton();
    CustomTableModel model;
    JTable patientList;
    PatientDefaultPage() {
        this.setMaximumSize(new Dimension(1300,600));
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 75));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Header container
        JPanel header = new JPanel();
        JLabel title = new JLabel("Patient Info");
        title.setFont(title.getFont().deriveFont(20F));
        header.setBackground(Color.white);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.add(addPatientBtn);
        header.add(Box.createHorizontalGlue());
        header.add(title);

        //Table
        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        body.setBackground(Color.white);

        model = new CustomTableModel();
        List<Patient> allPatients = PatientDAO.getAllPatients();
        for (Patient p : allPatients) {
            addPatientToTable(p);
        }
        patientList = new JTable(model); // UI for patient list
        patientList.setRowHeight(30);
        patientList.setGridColor(Color.gray);
        patientList.setSelectionBackground(new Color(0xfdf7e7));
        patientList.setFont(new Font("Courier",Font.PLAIN,13));
        patientList.setPreferredScrollableViewportSize(new Dimension(850,500));
        patientList.getColumn("User Action").setCellRenderer(new ButtonRenderer());
        patientList.getColumn("User Action").setCellEditor(new ButtonEditor(new JCheckBox()));
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(patientList);
        body.add(scrollPane);

        this.add(header);
        this.add(new Box.Filler(new Dimension(100,30), new Dimension(100,30), new Dimension(100,30)));
        this.add(body);
        this.add(new Box.Filler(new Dimension(100,30), new Dimension(100,30), new Dimension(100,30)));
    }
    void addPatientToTable (Patient patient){
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        Object[] rowData = new Object[]{patient.getPatientID(), patient.getName(), patient.getAge(), patient.getGender(), patient.getBloodGroup().getValue(), patient.getPhoneNumber(), buttonRenderer};
        model.addRow(rowData);
    }
    public ViewPatientInfoPage viewPage(int row) throws ExecutionException, InterruptedException {
        ViewPatientInfoPage viewPage = new ViewPatientInfoPage();
        // call patient ID
        Patient patient = PatientDAO.getPatientByID(patientList.getValueAt(row,0).toString());
        viewPage.title.setText(STR."#\{patient.getPatientId()}");
        viewPage.form.name.setText(patient.getName());
        viewPage.form.phone.setText(patient.getPhoneNumber());
        viewPage.form.bloodGroup.setText(patient.getBloodGroup().getValue());
        viewPage.form.address.setText(patient.getAddress());
        viewPage.form.DOB.setText(patient.getformattedDate());
        viewPage.form.gender.setText(patient.getGender().getValue());
        return viewPage;
    }

    static class CustomTableModel extends AbstractTableModel {
        // Data for each column
        private Object[][] data = {};

        // Column names
        private final String[] columnNames = {"ID","Name","Age","Gender","Blood Type","Phone Number","User Action"};

        // Data types for each column
        @SuppressWarnings("rawtypes")
        private final Class[] columnTypes = {String.class,String.class,String.class,String.class,String.class,String.class,JButton.class};

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
            return columnIndex == 6;
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

    static class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(Color.green);
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
            button.setBackground(Color.green);
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

    public JButton AddPatientButton(){
        JButton addPatientButton = new JButton("  + Add patient  ");
        addPatientButton.setForeground(Color.white);
        addPatientButton.setBackground(new Color(0x3497F9));
        addPatientButton.setMaximumSize(new Dimension(125,30));
        addPatientButton.setBorder(BorderFactory.createEmptyBorder());
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
    ViewMode form = new ViewMode();
    JLabel title = new JLabel("#MedicalID");

    ViewPatientInfoPage(){
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
        this.add(form); // Registration form
    }

    static class ViewMode extends JPanel {
        JTextField name;
        JTextField phone;
        JTextField gender;
        JTextField DOB;
        JTextArea address;
        JTextField bloodGroup;
        ViewMode(){
            JPanel form = Form();
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.BLACK,1,true));
            setSize(700,400);
            add(form);
            setVisible(true);
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
            gender = new ViewModeTextField();
            gender.setBounds(385-250, 130, 50,20);

            // Date of birth (DOB)
            JLabel DOBLabel = new JLabel("Date of birth");
            DOBLabel.setBounds(300-250,140+ 20,100,20);
            DOB = new ViewModeTextField();
            DOB.setBounds(385-250, 140+ 20, 70, 20);

            // Address
            JLabel addressLabel = new JLabel("Address");
            addressLabel.setBounds(300-250,170+ 20,100,20);
            address = new JTextArea();
            address.setEditable(false);
            address.setBounds(385-250, 170+ 20, 200, 80);
            address.setLineWrap(true);

            // Patient's blood group
            JLabel bloodGroupLabel = new JLabel("Blood type");
            bloodGroupLabel.setBounds(300-250,270+ 20,100,20);
            bloodGroup = new ViewModeTextField();
            bloodGroup.setBounds(385-250,270+ 20,70,20);

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

            return form;
        }

        static class ViewModeTextField extends JTextField {
            ViewModeTextField(){
                super();
                setEditable(false);
                setBorder(BorderFactory.createEmptyBorder());
                setBackground(Color.white);
            }
        }
    }
}
class PatientForm extends JPanel{
    JButton createBtn;
    JTextField IDInput;
    JTextField nameInput ;
    JTextField phoneInput ;
    JRadioButton male;
    JRadioButton female;
    JRadioButton otherGender;
    ButtonGroup gender;
    JFormattedTextField DOBInput;
    JTextArea addressInput;
    JTextField bloodGroupInput;
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
        IDInput.setBounds(385,20 + 20,100,20);

        // Patient's name
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(300,50+ 20,95,20);
        nameInput = new JTextField();
        nameInput.setBounds(385,50+ 20,200,20);

        //  Patient's phone number
        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setBounds(300,80+ 20,95,20);
        phoneInput = new JTextField();
        phoneInput.setBounds(385,80+ 20,200,20);

        // Patient's gender
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setBounds(300,110+ 20,95,20);
        male = new JRadioButton("Male");
        male.setBounds(380,110+ 20,60,20);
        male.setBackground(Color.white);
        female = new JRadioButton("Female");
        female.setBounds(440,110+ 20,70,20);
        female.setBackground(Color.white);
        otherGender = new JRadioButton("Other");
        otherGender.setBounds(515,110+ 20,70,20);
        otherGender.setBackground(Color.white);
        gender = new ButtonGroup();
        gender.add(male);
        gender.add(female);
        gender.add(otherGender);

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

        // Patient's blood group
        JLabel bloodGroupLabel = new JLabel("Blood type");
        bloodGroupLabel.setBounds(300,270+ 20,100,20);
        bloodGroupInput = new JTextField();
        bloodGroupInput.setBounds(385,270+ 20,70,20);

        // Create button
        createBtn = new JButton("CREATE");
        createBtn.setBackground(new Color(0x3497F9));
        createBtn.setForeground(Color.white);
        createBtn.setBounds(400,380-10,100,30);

        JPanel form = new JPanel();
        form.setBackground(Color.white);
        form.setLayout(null);
        form.add(nameLabel);
        form.add(nameInput);
        form.add(phoneLabel);
        form.add(phoneInput);
        form.add(genderLabel);
        form.add(male);
        form.add(female);
        form.add(otherGender);
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
}
