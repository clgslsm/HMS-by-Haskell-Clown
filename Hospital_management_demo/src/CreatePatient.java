import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class PatientInfo {
    String ID;
    String name;
    String gender;
    int age;
    String phone;
    String address;
    String bloodGroup;
    String dateOfBirth;
    int height;
    int weight;

    PatientInfo(String ID, String name, String gender, String dateOfBirth, String phone, String address, String bloodGroup, int height, int weight){
        this.ID = ID;
        this.name = name;
        this.gender = gender;
        this.age = 0;
        this.phone = phone;
        this.address = address;
        this.bloodGroup = bloodGroup;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.weight = weight;
    }

    public void printInfo() {
        System.out.printf("Patient no.%s {Name: %s - Phone: %s - Blood type: %s}\n", this.ID, this.name,this.phone,this.bloodGroup );
    }
}
class PatientPanel extends JPanel {
    ArrayList<PatientInfo> data = new ArrayList<>();
    DefaultPage defaultPage;
    PatientPanel() {
        CardLayout currentPage = new CardLayout();
        this.setLayout(currentPage);
        this.setBackground(Color.white);

        defaultPage = new DefaultPage();

        // When we click "Add patient" => change to Patient Registration Page
        defaultPage.createPatientBtn.addActionListener(e -> {
            // Create Patient Registration Page
            AddNewPatientPage addPatientPage = new AddNewPatientPage();
            this.add(addPatientPage, "add-patient-page");

            // Get back to default page
            addPatientPage.backButton.addActionListener(mouseClicked->{
                currentPage.removeLayoutComponent(addPatientPage);
                currentPage.show(this,"default-page");
            });

            // Fill in the form and store the information of the new patient
            addPatientPage.form.createBtn.addActionListener(mouseClicked->{
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
                int height = addPatientPage.form.heightInput.getText().isEmpty() ? 0 : Integer.parseInt(addPatientPage.form.heightInput.getText());
                int weight = addPatientPage.form.heightInput.getText().isEmpty() ? 0 : Integer.parseInt(addPatientPage.form.heightInput.getText());

                PatientInfo newPatient = new PatientInfo(ID, name, gender, dateOfBirth, phone, address, bloodGroup, height, weight);
                data.add(newPatient);
                defaultPage.addPatientToTable(newPatient);
                System.out.println(data);
                newPatient.printInfo();

                currentPage.removeLayoutComponent(addPatientPage);
                currentPage.show(this,"default-page");
            });

            currentPage.show(this, "add-patient-page");
        });

        // Always show default page
        this.add(defaultPage, "default-page");
        currentPage.show(this, "default-page");
    }
}
class DefaultPage extends JLabel {
    CreatePatientButton createPatientBtn = new CreatePatientButton();
    CustomTableModel model;
    JTable patientList;
    DefaultPage() {
        this.setMaximumSize(new Dimension(1300,600));
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 75));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Header container
        JPanel header = new JPanel();
        JLabel title = new JLabel("Patient Info");
        title.setFont(title.getFont().deriveFont(20F));
        header.setBackground(Color.white);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.add(createPatientBtn);
        header.add(Box.createHorizontalGlue());
        header.add(title);

        //Table
        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        body.setBackground(Color.white);

        model = new CustomTableModel();
        patientList = new JTable(model);
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
    void addPatientToTable (PatientInfo patient){
        JButton viewButton = View();
        Object[] rowData = {patient.ID, patient.name, Integer.toString(patient.age), patient.gender, patient.bloodGroup, patient.phone, viewButton};
        model.addRow(rowData);
    }

    public JButton View() {
        JButton viewButton = new JButton("View");
        viewButton.setBackground(Color.green);
        viewButton.setForeground(Color.white);
        viewButton.setPreferredSize(new Dimension(70,30));
        viewButton.setFont(new Font("Courier",Font.PLAIN,12));
        return viewButton;
    }
}
class AddNewPatientPage extends JPanel {
    JButton backButton = BackwardButton();
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

    public JButton BackwardButton (){
        JButton backButton = new JButton("Back");
        backButton.setMaximumSize(new Dimension(80,25));
        return backButton;
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
    JTextField heightInput;
    JTextField weightInput;
    JTextField bloodGroupInput;
    PatientForm() {
        JPanel form = Form();
        setLayout(new BorderLayout());
        setSize(700,400);
        add(form);
        setVisible(true);
    }

    public JPanel Form (){
        // Patient's ID
        JLabel IDLabel = new JLabel("Patient ID");
        IDLabel.setBounds(600,20,100,20);
        IDInput = new JTextField();
        IDInput.setBounds(670,22,100,20);

        // Patient's name
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(100,20,100,20);
        nameInput = new JTextField();
        nameInput.setBounds(185,22,150,20);

        //  Patient's phone number
        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setBounds(100,50,100,20);
        phoneInput = new JTextField();
        phoneInput.setBounds(185,52,150,20);

        // Patient's gender
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setBounds(100,80,100,20);
        male = new JRadioButton("Male");
        male.setBounds(180,80,60,20);
        female = new JRadioButton("Female");
        female.setBounds(240,80,70,20);
        otherGender = new JRadioButton("Other");
        otherGender.setBounds(315,80,70,20);
        gender = new ButtonGroup();
        gender.add(male);
        gender.add(female);
        gender.add(otherGender);

        // Date of birth (DOB)
        JLabel DOBLabel = new JLabel("Date of birth");
        DOBLabel.setBounds(100,110,100,20);
        DOBInput = new JFormattedTextField(createFormatter("##-##-####"));
        DOBInput.setText("01-01-1908");
        DOBInput.setBounds(185, 110, 75, 20);

        // Address
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(100,140,100,20);
        addressInput = new JTextArea();
        addressInput.setBounds(185, 140, 150, 80);
        addressInput.setLineWrap(true);

        // Patient's Height
        JLabel heightLabel = new JLabel("Height (cm)");
        heightLabel.setBounds(100,250,100,20);
        heightInput = new JTextField();
        heightInput.setBounds(185,250,40,20);

        // Patient's weight
        JLabel weightLabel = new JLabel("Weight (kg)");
        weightLabel.setBounds(240,250,100,20);
        weightInput = new JTextField();
        weightInput.setBounds(325,250,40,20);

        // Patient's blood group
        JLabel bloodGroupLabel = new JLabel("Blood type");
        bloodGroupLabel.setBounds(100,280,100,20);
        bloodGroupInput = new JTextField();
        bloodGroupInput.setBounds(185,280,100,20);

        // Create button
        createBtn = new JButton("Create");
        createBtn.setBackground(new Color(0x3497F9));
        createBtn.setForeground(Color.white);
        createBtn.setBounds(430,380,100,30);

        JPanel form = new JPanel();
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
        form.add(heightLabel);
        form.add(heightInput);
        form.add(weightLabel);
        form.add(weightInput);
        form.add(bloodGroupLabel);
        form.add(bloodGroupInput);
        form.add(createBtn);

        return form;
    }

    protected MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
    }
}
class CreatePatientButton extends JButton {
    CreatePatientButton(){
        this.setText("  + Add patient  ");
        this.setForeground(Color.white);
        this.setBackground(new Color(0x3497F9));
        this.setMaximumSize(new Dimension(125,30));
        this.setBorder(BorderFactory.createEmptyBorder());
    }
}
class CustomTableModel extends AbstractTableModel {
    // Data for each column
    private Object[][] data = {};

    // Column names
    private String[] columnNames = {"ID","Name","Age","Gender","Blood Type","Phone Number","User Action"};

    // Data types for each column
    @SuppressWarnings("rawtypes")
    private Class[] columnTypes = {String.class,String.class,String.class,String.class,String.class,String.class,JButton.class};

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
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i];
        }
        newData[data.length] = rowData;
        data = newData;
        fireTableRowsInserted(data.length - 1, data.length - 1); // Notify the table that rows have been inserted
    }
}
class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(Color.white);
            setBackground(Color.green);
        } else {
            setForeground(Color.white);
            setBackground(Color.green);
        }
        setText("View");
        return this;
    }
}
class ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(Color.white);
            button.setBackground(Color.green);
        } else {
            button.setForeground(Color.white);
            button.setBackground(Color.green);
        }
//        label = (value == null) ? "" : value.toString();
        button.setText("View");
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
//            JOptionPane.showMessageDialog(button, label + ": Ouch!");
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}