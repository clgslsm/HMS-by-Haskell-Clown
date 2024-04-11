package com.javaswing;
import com.google.firebase.database.collection.LLRBNode;
import com.javafirebasetest.dao.MedicineDAO;
import com.javafirebasetest.entity.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class MedicinePanel extends JPanel {
    ArrayList<Medicine> data = new ArrayList<>();
    MedicineDefaultPage defaultPage;
    ViewMedicineInfoPage viewMedicineInfoPage;
    MedicinePanel() {
        CardLayout currentPage = new CardLayout();
        this.setLayout(currentPage);
        this.setBackground(Color.white);

        defaultPage = new MedicineDefaultPage();

        // When we click "Add Medicine" => change to Medicine Registration Page
        defaultPage.addMedicineBtn.addActionListener(_ -> {
            // Create Medicine Registration Page
            AddNewMedicinePage addMedicinePage = new AddNewMedicinePage();
            this.add(addMedicinePage, "add-medicine-page");

            // Get back to default page
            addMedicinePage.backButton.addActionListener(_ ->{
                currentPage.removeLayoutComponent(addMedicinePage);
                currentPage.show(this,"default-page");
            });

            // Fill in the form and store the information of the new patient
            addMedicinePage.form.createBtn.addActionListener(_ ->{
                String ID = addMedicinePage.form.IDInput.getText();
                String name = addMedicinePage.form.nameInput.getText();
                String gender;
                if (addMedicinePage.form.male.isSelected())
                    gender = "Male";
                else if (addMedicinePage.form.female.isSelected())
                    gender = "Female";
                else gender = "Other";
                String phone = addMedicinePage.form.phoneInput.getText();
                String address = addMedicinePage.form.addressInput.getText();
                String bloodGroup = addMedicinePage.form.bloodGroupInput.getText();
                String dateOfBirth = addMedicinePage.form.DOBInput.getText();
                System.out.println(MedicineForm.reformatDate(dateOfBirth));

                // Creating the map
                Map<String, Object> medicineInfo = new HashMap<>();
                medicineInfo.put("name", name);
                medicineInfo.put("gender", gender);
                medicineInfo.put("phoneNumber", phone);
                medicineInfo.put("address", address);
                medicineInfo.put("bloodGroup", bloodGroup);
                medicineInfo.put("birthDate", MedicineForm.reformatDate(dateOfBirth));
                //Medicine newMedicine = new Medicine(ID, medicineInfo);
//                data.add(newMedicine);
//                try {
//                    MedicineDAO.addMedicine(newMedicine);
//                } catch (ExecutionException | InterruptedException ex) {
//                    throw new RuntimeException(ex);
//                }
//                defaultPage.addMedicineToTable(newMedicine);
//                System.out.println(data);

                currentPage.removeLayoutComponent(addMedicinePage);
                currentPage.show(this,"medicine-default-page");
            });

            currentPage.show(this, "add-medicine-page");
        });

        // See full information and medical records of a specific patient
        MedicinePanel medicinePanel = this;
        defaultPage.medicineList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = defaultPage.medicineList.getColumnModel().getColumnIndexAtX(evt.getX());
                int row = evt.getY() / defaultPage.medicineList.getRowHeight();

                if (row < defaultPage.medicineList.getRowCount() && row >= 0 && column < defaultPage.medicineList.getColumnCount() && column >= 0) {
                    Object value = defaultPage.medicineList.getValueAt(row, column);
                    if (value instanceof JButton) {
                        // Instead of simulating button click, print to terminal
                        System.out.println(STR."Button clicked for row: \{row}");
                        try {
                            viewMedicineInfoPage = defaultPage.viewPage(row);
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
//                        parentPanel.add(viewMedicinInfoPage, "view-page");
//                        currentPage.show(parentPanel, "view-page");
//
//                        viewMedicinInfoPage.backButton.addActionListener(_ ->{
//                            currentPage.removeLayoutComponent(viewMedicinInfoPage);
//                            currentPage.show(parentPanel,"default-page");
//                        });
                    }
                }
            }
        });

        // Always show default page
        this.add(defaultPage, "default-page");
        currentPage.show(this, "default-page");
    }
}
class MedicineDefaultPage extends JLabel {
    JButton addMedicineBtn = AddMedicineButton();
    CustomTableModel model;
    JTable medicineList;
    MedicineDefaultPage() {
        this.setMaximumSize(new Dimension(1300,600));
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 25));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Header container
        JPanel header = new JPanel();
        JLabel title = new JLabel("List of Medicines");
        title.setFont(title.getFont().deriveFont(25F));
        title.setForeground(new Color(0x3497F9));
        header.setBackground(Color.white);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));


        header.add(title);
        header.add(Box.createHorizontalGlue());
        header.add(addMedicineBtn);

        //Table
        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        body.setBackground(Color.white);

        model = new CustomTableModel();
        List<Medicine> allMedicins = MedicineDAO.getAllMedicine();
        for (Medicine p : allMedicins) {
            addMedicineToTable(p);
        }
        medicineList = new JTable(model); // UI for patient list

        medicineList.getTableHeader().setPreferredSize(new Dimension(medicineList.getTableHeader().getWidth(), 36));
        medicineList.getTableHeader().setFont(new Font("Courier", Font.BOLD, 13));
        medicineList.getTableHeader().setOpaque(false);
        medicineList.getTableHeader().setBackground(new Color(32, 136, 203));
        medicineList.getTableHeader().setForeground(new Color(255,255,255));
        medicineList.setFocusable(false);
        medicineList.setIntercellSpacing(new java.awt.Dimension(0, 0));
        medicineList.setSelectionBackground(new java.awt.Color(232, 57, 95));
        medicineList.setShowVerticalLines(false);
        medicineList.getTableHeader().setReorderingAllowed(false);
        medicineList.setFont(new Font("Courier",Font.PLAIN,13));
        medicineList.getColumn("Action").setCellRenderer(new ButtonRenderer());
        medicineList.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
        medicineList.setRowHeight(32);
        // Center-align the content in each column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < medicineList.getColumnCount() - 1; i++) {
            medicineList.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(medicineList);
        body.add(scrollPane);

        this.add(header);
        JPanel space = new JPanel();
        space.setBackground(Color.white);
        space.setSize(new Dimension(40, 40));
        this.add(space);
        this.add(body);
    }
    void addMedicineToTable (Medicine medicine){
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        Object[] rowData = new Object[]{ medicine.getMedicineName(), medicine.getMedicineId(), medicine.getformattedImportDate(), medicine.getformattedExpiryDate(), medicine.getAmount(), medicine.getUnit(), buttonRenderer};
        model.addRow(rowData);
    }
    public ViewMedicineInfoPage viewPage(int row) throws ExecutionException, InterruptedException {
        ViewMedicineInfoPage viewPage = new ViewMedicineInfoPage();
        // call patient ID
//        Medicin medicin = MedicinDAO.getMedicinByID(MedicinList.getValueAt(row,0).toString());
//        viewPage.title.setText(STR."#\{medicin.getMedicinId()}");
//        viewPage.form.name.setText(medicin.getName());
//        viewPage.form.phone.setText(medicin.getPhoneNumber());
//        viewPage.form.bloodGroup.setText(medicin.getBloodGroup().getValue());
//        viewPage.form.address.setText(medicin.getAddress());
//        viewPage.form.DOB.setText(medicin.getformattedDate());
//        viewPage.form.gender.setText(Medicin.getGender().getValue());
        return viewPage;
    }

    static class CustomTableModel extends AbstractTableModel {
        // Data for each column
        private Object[][] data = {};

        // Column names
        private final String[] columnNames = {"Name","Medicine ID","Import Date","Expiry Date", "Stock in Qty","Group Name", "Action"};

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
            setBackground(Color.WHITE);
            setText("View Full Detail >>");
            setMaximumSize(new Dimension(70,18));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder());
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
            button.setText("View Full Detail >>");
            button.setSize(25,25);
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

    public JButton AddMedicineButton(){
        JButton addMedicinButton = new RoundedButton("  + Add medicine  ");
        addMedicinButton.setFont(new Font("Courier",Font.PLAIN,13));
        addMedicinButton.setFocusable(false);
        addMedicinButton.setForeground(Color.WHITE);
        addMedicinButton.setBackground(new Color(0x3497F9));
        addMedicinButton.setBounds(100, 100, 125, 60);
        addMedicinButton.setBorder(new EmptyBorder(10,10,10,10));
        return addMedicinButton;
    }
}
class AddNewMedicinePage extends JPanel {
    BackButton backButton = new BackButton();
    MedicineForm form = new MedicineForm();
    AddNewMedicinePage() {
        JLabel title = new JLabel("Medicin Registration Form");
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
class ViewMedicineInfoPage extends JPanel {
    BackButton backButton = new BackButton();
    ViewMode form = new ViewMode();
    JLabel title = new JLabel("#MedicalID");

    ViewMedicineInfoPage(){
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
class MedicineForm extends JPanel{
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
    MedicineForm() {
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
