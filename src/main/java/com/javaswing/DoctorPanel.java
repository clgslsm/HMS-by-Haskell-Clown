//package com.javaswing;
//import com.javafirebasetest.dao.PatientDAO;
//import com.javafirebasetest.entity.*;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.table.AbstractTableModel;
//import javax.swing.table.TableCellRenderer;
//import javax.swing.text.MaskFormatter;
//import java.awt.*;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//import com.javafirebasetest.dao.DoctorDAO;
//import com.javafirebasetest.dao.MedRecDAO;
//
//class DoctorPanel extends JPanel {
//    ArrayList<Doctor> data = new ArrayList<>();
//    DoctorDefaultPage defaultPage;
//    ViewDoctorInfoPage viewDoctorInfoPage;
//    DoctorPanel() {
//        CardLayout currentPage = new CardLayout();
//        this.setLayout(currentPage);
//        this.setBackground(Color.white);
//
//        defaultPage = new DoctorDefaultPage();
//
//        // When we click "Add Doctor" => change to Doctor Registration Page
//        defaultPage.addDoctorBtn.addActionListener(_ -> {
//            String[] department = new String[DeptType.values().length];
//            int i = 0;
//            for (DeptType dt : DeptType.values()) {
//                department[i] = dt.getValue();
//                i++;
//            }
//            JComboBox<String> dep = new JComboBox<>(department);
//            dep.setBackground(Color.white);
//            dep.setBorder(BorderFactory.createEmptyBorder());
//            dep.setBounds(385-250,130,70,20);
//            JTextField nameField = new JTextField(30);
//
//            Object[] message = {
//                    "Name of Department:", dep,
//                    "Name of Dr:", nameField
//            };
//
//            int option = JOptionPane.showConfirmDialog(null, message, "", JOptionPane.OK_CANCEL_OPTION);
//
//            if (option == JOptionPane.OK_OPTION) {
//                String d = Objects.requireNonNull(dep.getSelectedItem()).toString();
//                String name = nameField.getText();
//
//                // Kiểm tra xem có ô nào bị bỏ trống không
//                if (d.isEmpty() || name.isEmpty()) {
//                    JOptionPane.showMessageDialog(null, "The input box cannot be left blank!", "Error", JOptionPane.ERROR_MESSAGE);
//                } else {
//                    JOptionPane.showMessageDialog(null, "Department: " + d + "\nName: " + name, "Information", JOptionPane.INFORMATION_MESSAGE);
//                    //Doctor newDoctor = new Doctor("12", name, );
//////                data.add(newDoctor);
//////                try {
//////                    DoctorDAO.addDoctor(newDoctor);
//////                } catch (ExecutionException | InterruptedException ex) {
//////                    throw new RuntimeException(ex);
//////                }
//////                defaultPage.addDoctorToTable(newDoctor);
//////                System.out.println(data);
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "Cancel", "Notification", JOptionPane.INFORMATION_MESSAGE);
//            }
//        });
//        // See full information and medical records of a specific patient
//        DoctorPanel parentPanel = this;
//        defaultPage.doctorList.addMouseListener(new java.awt.event.MouseAdapter()
//        {
//            @Override
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                int column = defaultPage.doctorList.getColumnModel().getColumnIndexAtX(evt.getX());
//                int row = evt.getY() / defaultPage.doctorList.getRowHeight();
//
//                if (row < defaultPage.doctorList.getRowCount() && row >= 0 && column < defaultPage.doctorList.getColumnCount() && column >= 0) {
//                    Object value = defaultPage.doctorList.getValueAt(row, column);
//                    if (value instanceof JButton) {
//                        // Instead of simulating button click, print to terminal
//                        System.out.println(STR."Button clicked for row: \{row}");
//                        try {
//                            viewDoctorInfoPage = defaultPage.viewPage(row);
//                        } catch (ExecutionException | InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                        parentPanel.add(viewDoctorInfoPage, "view-page");
//                        currentPage.show(parentPanel, "view-page");
//                        viewDoctorInfoPage.backButton.addActionListener(_ ->{
//                            currentPage.removeLayoutComponent(viewDoctorInfoPage);
//                            currentPage.show(parentPanel,"default-page");
//                        });
//                    }
//                }
//            }
//        });
//
//        // Always show default page
//        this.add(defaultPage, "default-page");
//        currentPage.show(this, "default-page");
//    }
//}
//class DoctorDefaultPage extends JLabel {
//    JButton addDoctorBtn = AddDoctorButton();
//    CustomTableModel model;
//    JTable doctorList;
//    DoctorDefaultPage() {
//        this.setMaximumSize(new Dimension(1300,600));
//        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 75));
//        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//
//        // Header container
//        JPanel header = new JPanel();
//        JLabel title = new JLabel("Doctor Info");
//        title.setFont(title.getFont().deriveFont(25F));
//        title.setForeground(new Color(0x3497F9));
//        header.setBackground(new Color(0xF1F8FF));
//        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
//
//
//        header.add(title);
//        header.add(Box.createHorizontalGlue());
//        header.add(addDoctorBtn);
//
//        //Table
//        JPanel body = new JPanel();
//        body.setLayout(new BorderLayout());
//        body.setBackground(Color.white);
//
//        model = new CustomTableModel();
//        List<Doctor> allDoctors = DoctorDAO.getAllDoctor();
//        for (Doctor p : allDoctors) {
//            addDoctorToTable(p);
//        }
//        doctorList = new JTable(model); // UI for patient list
//
//        doctorList.getTableHeader().setPreferredSize(new Dimension(doctorList.getTableHeader().getWidth(), 40));
//        doctorList.getTableHeader().setFont(new Font("Courier", Font.BOLD, 13));
//        doctorList.getTableHeader().setOpaque(false);
//        doctorList.getTableHeader().setBackground(new Color(32, 136, 203));
//        doctorList.getTableHeader().setForeground(new Color(255,255,255));
//
//        doctorList.setFocusable(false);
//        doctorList.setIntercellSpacing(new java.awt.Dimension(0, 0));
//        doctorList.setSelectionBackground(new java.awt.Color(232, 57, 95));
//        doctorList.setShowVerticalLines(false);
//        doctorList.getTableHeader().setReorderingAllowed(false);
//        doctorList.setFont(new Font("Courier",Font.PLAIN,13));
//        doctorList.getColumn("View").setCellRenderer(new PatientDefaultPage.ButtonRenderer());
//        doctorList.getColumn("View").setCellEditor(new PatientDefaultPage.ButtonEditor(new JCheckBox()));
//        doctorList.setRowHeight(40);
//
//        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setViewportView(doctorList);
//        body.add(scrollPane);
//
//        this.add(header);
//        JPanel space = new JPanel();
//        space.setBackground(new Color(0xF1F8FF));
//        space.setSize(new Dimension(40, 40));
//        this.add(space);
//        this.add(body);
//        //this.add(new Box.Filler(new Dimension(100,30), new Dimension(100,30), new Dimension(100,30)));
//    }
//    void addDoctorToTable (Doctor doctor){
//        ButtonRenderer buttonRenderer = new ButtonRenderer();
//        Object[] rowData = new Object[]{doctor.getStaffId(), doctor.getDepartment().getValue(), doctor.getName(), doctor.getPatientCount(), buttonRenderer};
//        model.addRow(rowData);
//    }
//    public ViewDoctorInfoPage viewPage(int row) throws ExecutionException, InterruptedException {
//        ViewDoctorInfoPage viewPage = new ViewDoctorInfoPage();
//        // call patient ID
//        List<MedicalRecord> medicalRecords = MedRecDAO.getMedRecByDoctorId(doctorList.getValueAt(row, 0).toString());
//        MedicalRecord medicalRecord = medicalRecords.getFirst();
//        viewPage.title.setText(STR."#\{medicalRecord.getPatientId()}");
//        viewPage.form.name.setText(medicalRecord.getDoctorId());
//        return viewPage;
//    }
//
//    static class CustomTableModel extends AbstractTableModel {
//        // Data for each column
//        private Object[][] data = {};
//
//        // Column names
//        private final String[] columnNames = {"ID","Department","Name","Number of Patient", "View"};
//
//        // Data types for each column
//        @SuppressWarnings("rawtypes")
//        private final Class[] columnTypes = {String.class,String.class,String.class,String.class,JButton.class};
//
//        @Override
//        public int getRowCount() {
//            return data.length;
//        }
//
//        @Override
//        public int getColumnCount() {
//            return columnNames.length;
//        }
//
//        @Override
//        public Object getValueAt(int rowIndex, int columnIndex) {
//            return data[rowIndex][columnIndex];
//        }
//
//        @Override
//        public String getColumnName(int column) {
//            return columnNames[column];
//        }
//
//        @Override
//        public Class<?> getColumnClass(int columnIndex) {
//            return columnTypes[columnIndex];
//        }
//
//        @Override
//        public boolean isCellEditable(int rowIndex, int columnIndex) {
//            // Make all cells non-editable
//            return columnIndex == 3;
//        }
//
//        // Method to add a new row to the table
//        public void addRow(Object[] rowData) {
//            Object[][] newData = new Object[data.length + 1][getColumnCount()];
//            System.arraycopy(data, 0, newData, 0, data.length);
//            newData[data.length] = rowData;
//            data = newData;
//            fireTableRowsInserted(data.length - 1, data.length - 1); // Notify the table that rows have been inserted
//        }
//    }
//
//    static class ButtonRenderer extends JButton implements TableCellRenderer {
//
//        public ButtonRenderer() {
//            setOpaque(true);
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value,
//                                                       boolean isSelected, boolean hasFocus, int row, int column) {
//            setBackground(Color.green);
//            setIcon(new ImageIcon(new ImageIcon("src/main/java/com/javaswing/img/view-icon.png").getImage().getScaledInstance(15,15*143/256, Image.SCALE_SMOOTH)));
//            setSize(25,25);
//            return this;
//        }
//    }
//    static class ButtonEditor extends DefaultCellEditor {
//
//        protected JButton button;
//        private String label;
//        private boolean isPushed;
//
//        public ButtonEditor(JCheckBox checkBox) {
//            super(checkBox);
//            button = new JButton();
//            button.setOpaque(true);
//            button.addActionListener(_ -> fireEditingStopped());
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value,
//                                                     boolean isSelected, int row, int column) {
//            button.setBackground(Color.green);
//            button.setIcon(new ImageIcon(new ImageIcon("src/main/java/com/javaswing/img/view-icon.png").getImage().getScaledInstance(15,15*143/256, Image.SCALE_SMOOTH)));
//            button.setSize(25,25);
//            isPushed = true;
//            return button;
//        }
//
//        @Override
//        public Object getCellEditorValue() {
//            isPushed = false;
//            return label;
//        }
//
//        @Override
//        public boolean stopCellEditing() {
//            isPushed = false;
//            return super.stopCellEditing();
//        }
//    }
//
//    public JButton AddDoctorButton(){
//        JButton addDoctorButton = new RoundedButton("  + Add doctor  ");
//        addDoctorButton.setFont(new Font("Courier",Font.PLAIN,13));
//        addDoctorButton.setFocusable(false);
//        addDoctorButton.setForeground(Color.WHITE);
//        addDoctorButton.setBackground(new Color(0x3497F9));
//        addDoctorButton.setBounds(100, 100, 125, 60);
//        addDoctorButton.setBorder(new EmptyBorder(10,10,10,10));
//
//        return addDoctorButton;
//    }
//}
//class AddNewDoctorPage extends JPanel {
//    JButton backButton = new RoundedButton(" Return ");
//    DoctorForm form = new DoctorForm();
//    AddNewDoctorPage() {
//        JLabel title = new JLabel("Doctor Registration Form");
//        title.setFont(title.getFont().deriveFont(25.0F));
//
//        this.setBackground(Color.white);
//        this.setMaximumSize(new Dimension(1300,600));
//        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 75));
//        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//
//        JPanel pageHeader = new JPanel();
//        pageHeader.setBackground(Color.white);
//        pageHeader.setLayout(new BoxLayout(pageHeader, BoxLayout.X_AXIS));
//        pageHeader.add(backButton);
//        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
//        backButton.setAlignmentY(0);
//        pageHeader.add(Box.createHorizontalGlue());
//        pageHeader.add(title);
//        title.setAlignmentX(Component.RIGHT_ALIGNMENT);
//        title.setAlignmentY(Component.TOP_ALIGNMENT);
//
//        this.add(pageHeader);
//        this.add(new Box.Filler(new Dimension(100,30), new Dimension(100,30), new Dimension(100,30)));
//        this.add(form); // Registration form
//    }
//}
//class ViewDoctorInfoPage extends JPanel {
//    JButton backButton = new RoundedButton(" Return ");
//    ViewMode form = new ViewMode();
//    JLabel title = new JLabel("#MedicalID");
//
//    ViewDoctorInfoPage(){
//        title.setFont(title.getFont().deriveFont(18.0F));
//
//        this.setBackground(Color.white);
//        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 20));
//        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//
//        JPanel pageHeader = new JPanel();
//        pageHeader.setBackground(Color.white);
//        pageHeader.setLayout(new BoxLayout(pageHeader, BoxLayout.X_AXIS));
//        pageHeader.add(backButton);
//        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
//        pageHeader.add(Box.createHorizontalGlue());
//        pageHeader.add(title);
//        title.setAlignmentX(Component.RIGHT_ALIGNMENT);
//
//        this.add(pageHeader);
//        this.add(new Box.Filler(new Dimension(100,15), new Dimension(100,15), new Dimension(100,15)));
//        this.add(form); // Registration form
//    }
//
//    static class ViewMode extends JPanel {
//        JTextField name;
//        JTextField phone;
//        JTextField gender;
//        JTextField DOB;
//        JTextArea address;
//        JTextField bloodGroup;
//        ViewMode(){
//            JPanel form = Form();
//            setLayout(new BorderLayout());
//            setBorder(BorderFactory.createLineBorder(Color.BLACK,1,true));
//            setSize(700,400);
//            add(form);
//            setVisible(true);
//        }
//
//        JPanel Form() {
//            JLabel title = new JLabel("Personal Information");
//            title.setFont(new Font("Arial", Font.BOLD,18));
//            title.setForeground(Color.gray);
//            title.setBounds(50, 10, 200, 50);
//
//            // Patient's name
//            JLabel nameLabel = new JLabel("Name");
//            nameLabel.setBounds(300-250,50+ 20,95,20);
//            name = new ViewModeTextField();
//            name.setBounds(385-250,50+ 20,200,20);
//
//            //  Patient's phone number
//            JLabel phoneLabel = new JLabel("Phone");
//            phoneLabel.setBounds(300-250,80+ 20,95,20);
//            phone = new ViewModeTextField();
//            phone.setBounds(385-250,80+ 20,200,20);
//
//            // Patient's gender
//            JLabel genderLabel = new JLabel("Gender");
//            genderLabel.setBounds(300-250,110+ 20,95,20);
//            gender = new ViewModeTextField();
//            gender.setBounds(385-250, 130, 50,20);
//
//            // Date of birth (DOB)
//            JLabel DOBLabel = new JLabel("Date of birth");
//            DOBLabel.setBounds(300-250,140+ 20,100,20);
//            DOB = new ViewModeTextField();
//            DOB.setBounds(385-250, 140+ 20, 70, 20);
//
//            // Address
//            JLabel addressLabel = new JLabel("Address");
//            addressLabel.setBounds(300-250,170+ 20,100,20);
//            address = new JTextArea();
//            address.setEditable(false);
//            address.setBounds(385-250, 170+ 20, 200, 80);
//            address.setLineWrap(true);
//
//            // Patient's blood group
//            JLabel bloodGroupLabel = new JLabel("Blood type");
//            bloodGroupLabel.setBounds(300-250,270+ 20,100,20);
//            bloodGroup = new ViewModeTextField();
//            bloodGroup.setBounds(385-250,270+ 20,70,20);
//
//            JPanel form = new JPanel();
//            form.setBackground(Color.white);
//            form.add(title);
//            form.setLayout(null);
//            form.add(nameLabel);
//            form.add(name);
//            form.add(phoneLabel);
//            form.add(phone);
//            form.add(genderLabel);
//            form.add(gender);
//            form.add(DOBLabel);
//            form.add(DOB);
//            form.add(addressLabel);
//            form.add(address);
//            form.add(bloodGroupLabel);
//            form.add(bloodGroup);
//
//            return form;
//        }
//
//        static class ViewModeTextField extends JTextField {
//            ViewModeTextField(){
//                super();
//                setEditable(false);
//                setBorder(BorderFactory.createEmptyBorder());
//                setBackground(Color.white);
//            }
//        }
//    }
//}
//class DoctorForm extends JPanel{
//    JButton createBtn;
//    JTextField IDInput;
//    JTextField nameInput ;
//    JTextField phoneInput ;
//    JRadioButton male;
//    JRadioButton female;
//    JRadioButton otherGender;
//    ButtonGroup gender;
//    JFormattedTextField DOBInput;
//    JTextArea addressInput;
//    JTextField bloodGroupInput;
//    DoctorForm() {
//        JPanel form = Form();
//        setLayout(new BorderLayout());
//        setBorder(BorderFactory.createLineBorder(Color.BLACK,1,true));
//        setSize(700,400);
//        add(form);
//        setVisible(true);
//    }
//
//    public JPanel Form (){
//        // Patient's ID
//        JLabel IDLabel = new JLabel("Medical ID");
//        IDLabel.setBounds(300,20 + 20,95,20);
//        IDInput = new JTextField();
//        IDInput.setBounds(385,20 + 20,100,20);
//
//        // Patient's name
//        JLabel nameLabel = new JLabel("Name");
//        nameLabel.setBounds(300,50+ 20,95,20);
//        nameInput = new JTextField();
//        nameInput.setBounds(385,50+ 20,200,20);
//
//        //  Patient's phone number
//        JLabel phoneLabel = new JLabel("Phone");
//        phoneLabel.setBounds(300,80+ 20,95,20);
//        phoneInput = new JTextField();
//        phoneInput.setBounds(385,80+ 20,200,20);
//
//        // Patient's gender
//        JLabel genderLabel = new JLabel("Gender");
//        genderLabel.setBounds(300,110+ 20,95,20);
//        male = new JRadioButton("Male");
//        male.setBounds(380,110+ 20,60,20);
//        male.setBackground(Color.white);
//        female = new JRadioButton("Female");
//        female.setBounds(440,110+ 20,70,20);
//        female.setBackground(Color.white);
//        otherGender = new JRadioButton("Other");
//        otherGender.setBounds(515,110+ 20,70,20);
//        otherGender.setBackground(Color.white);
//        gender = new ButtonGroup();
//        gender.add(male);
//        gender.add(female);
//        gender.add(otherGender);
//
//        // Date of birth (DOB)
//        JLabel DOBLabel = new JLabel("Date of birth");
//        DOBLabel.setBounds(300,140+ 20,100,20);
//        DOBInput = new JFormattedTextField(createFormatter());
//        DOBInput.setText("01-01-1980");
//        DOBInput.setBounds(385, 140+ 20, 70, 20);
//
//        // Address
//        JLabel addressLabel = new JLabel("Address");
//        addressLabel.setBounds(300,170+ 20,100,20);
//        addressInput = new JTextArea();
//        addressInput.setBounds(385, 170+ 20, 200, 80);
//        addressInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        addressInput.setLineWrap(true);
//
//        // Patient's blood group
//        JLabel bloodGroupLabel = new JLabel("Blood type");
//        bloodGroupLabel.setBounds(300,270+ 20,100,20);
//        bloodGroupInput = new JTextField();
//        bloodGroupInput.setBounds(385,270+ 20,70,20);
//
//        // Create button
//        createBtn = new JButton("CREATE");
//        createBtn.setBackground(new Color(0x3497F9));
//        createBtn.setForeground(Color.white);
//        createBtn.setBounds(400,380-10,100,30);
//
//        JPanel form = new JPanel();
//        form.setBackground(Color.white);
//        form.setLayout(null);
//        form.add(nameLabel);
//        form.add(nameInput);
//        form.add(phoneLabel);
//        form.add(phoneInput);
//        form.add(genderLabel);
//        form.add(male);
//        form.add(female);
//        form.add(otherGender);
//        form.add(DOBLabel);
//        form.add(DOBInput);
//        form.add(addressLabel);
//        form.add(addressInput);
//        form.add(IDLabel);
//        form.add(IDInput);
//        form.add(bloodGroupLabel);
//        form.add(bloodGroupInput);
//        form.add(createBtn);
//
//        return form;
//    }
//
//    protected MaskFormatter createFormatter() {
//        MaskFormatter formatter = null;
//        try {
//            formatter = new MaskFormatter("##-##-####");
//        } catch (java.text.ParseException exc) {
//            System.err.println(STR."formatter is bad: \{exc.getMessage()}");
//            System.exit(-1);
//        }
//        return formatter;
//    }
//
//    public static String reformatDate(String inputDate) {
//        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
//        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date date = inputFormat.parse(inputDate);
//            return outputFormat.format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
