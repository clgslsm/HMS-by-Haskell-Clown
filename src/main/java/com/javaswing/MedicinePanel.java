package com.javaswing;
import com.javafirebasetest.dao.MedicineDAO;
import com.javafirebasetest.dao.PatientDAO;
import com.javafirebasetest.entity.*;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
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
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        medicinePanel.add(viewMedicineInfoPage, "view-page");
                        currentPage.show(medicinePanel, "view-page");

                        viewMedicineInfoPage.backButton.addActionListener(_ ->{
                            currentPage.removeLayoutComponent(viewMedicineInfoPage);
                            currentPage.show(medicinePanel,"default-page");
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
class MedicineDefaultPage extends JLabel {
    JButton addMedicineBtn = AddMedicineButton();
    CustomTableModel model;
    JTable medicineList;
    JLabel title = new JLabel("List of Medicines");
    MedicineDefaultPage() {
        this.setMaximumSize(new Dimension(1300,600));
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 35));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Header container
        JPanel header = new JPanel();
        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new GridLayout(2,1));
        titleContainer.setBackground(Color.white);
        title.setFont(title.getFont().deriveFont(28F));
        title.setForeground(new Color(0x3497F9));
        JLabel subTitle = new JLabel("List of medicines available for sales");
        subTitle.setFont(new Font("Arial",Font.BOLD,15));
        titleContainer.add(title);
        titleContainer.add(subTitle);
        header.setBackground(Color.white);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));

        header.add(titleContainer);
        header.add(Box.createHorizontalGlue());
        header.add(addMedicineBtn);

        //Table
        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        body.setBackground(Color.white);

        model = new CustomTableModel();
        medicineList = new JTable(model); // UI for patient list
        refreshMedicineTable();

        medicineList.getTableHeader().setPreferredSize(new Dimension(medicineList.getTableHeader().getWidth(), 40));
        medicineList.getTableHeader().setFont(new Font("Courier", Font.BOLD, 13));
        medicineList.getTableHeader().setOpaque(false);
        medicineList.getTableHeader().setBackground(new Color(32, 136, 203));
        medicineList.getTableHeader().setForeground(new Color(255,255,255));

        medicineList.setFocusable(false);
        medicineList.setIntercellSpacing(new java.awt.Dimension(0, 0));
        medicineList.setSelectionBackground(new Color(0x126DA6));
        medicineList.setSelectionForeground(Color.white);
        medicineList.setShowVerticalLines(false);
        medicineList.getTableHeader().setReorderingAllowed(false);
        medicineList.setFont(new Font("Courier",Font.PLAIN,13));

        medicineList.getColumn("Action").setCellRenderer(new ButtonRenderer());
        medicineList.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
        medicineList.setRowHeight(36);
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
    public ViewMedicineInfoPage viewPage(int row) throws Exception {
        ViewMedicineInfoPage viewPage = new ViewMedicineInfoPage();
        // call medicine ID
        Medicine medicine = MedicineDAO.getMedicineById(medicineList.getValueAt(row,1).toString());
        viewPage.titleMedicine.setText(medicine.getMedicineName());
        BufferedImage image = ViewMedicineInfoPage.generateEAN13BarcodeImage(medicine.getMedicineId());
        viewPage.MedicineID.setIcon(new ImageIcon(image));
        viewPage.MedicineUnit.setText(medicine.getUnit());
        viewPage.medicineSupply.setText(medicine.getAmount().toString());
        viewPage.howToUse.setText(medicine.getDescription());
        return viewPage;
    }
    static class CustomTableModel extends AbstractTableModel {
        // Data for each column
        private Object[][] data = {};

        // Column names
        private final String[] columnNames = {"Name","Medicine ID","Import Date","Expiry Date", "Stock in Qty","Group Name", "Action"};

        // Data types for each column
        @SuppressWarnings("rawtypes")
        private final Class[] columnTypes = {String.class,String.class,String.class,String.class,String.class,String.class,String.class, JButton.class};

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
            return columnIndex == 7;
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
        addMedicinButton.setFont(new Font("Courier",Font.PLAIN,15));
        addMedicinButton.setFocusable(false);
        addMedicinButton.setForeground(Color.WHITE);
        addMedicinButton.setBackground(new Color(0x3497F9));
        addMedicinButton.setBounds(100, 100, 145, 50);
        return addMedicinButton;
    }
    public void refreshMedicineTable(){
        model.clearData();
        List<Medicine> allMedicins = MedicineDAO.getAllMedicine();
        for (Medicine p : allMedicins) {
            addMedicineToTable(p);
        }
        title.setText("List of Medicines (%d)".formatted(allMedicins.size()));
        System.out.println("Refresh Medicine Table");
    }
}
class AddNewMedicinePage extends JPanel {
    JButton backButton = new RoundedButton(" Return ");
    MedicineForm form = new MedicineForm();
    AddNewMedicinePage() {
        JLabel title = new JLabel("Add New Medicine");
        title.setFont(title.getFont().deriveFont(28.0F));

        this.setBackground(Color.white);
        this.setMaximumSize(new Dimension(1300,600));
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 35));
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
    JButton backButton = new RoundedButton(" Return ");
    JButton editButton = new RoundedButton(" Edit Details ");
    JLabel titleMedicine;
    JLabel MedicineID;
    JLabel MedicineUnit;
    JLabel medicineSupply;
    JTextArea howToUse;
    JTextArea sideEffects;
    JButton deleteButton = new RoundedButton(" Delete Medicine ");

    ViewMedicineInfoPage(){
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 25));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(10,10,10,10));

        deleteButton.setBackground(Color.WHITE);
        deleteButton.setForeground(Color.RED);
        deleteButton.setBorder(BorderFactory.createLineBorder(Color.red));
        deleteButton.setMaximumSize(new Dimension(160,50));

        container.add(headerContainer());
        container.add(Box.createVerticalStrut(30));
        container.add(bodyContainer());
        container.add(Box.createVerticalStrut(30));
        container.add(deleteButton);

        add(container);

    }
    JPanel headerContainer(){
        JPanel header = new JPanel();
        header.setOpaque(false);

        JLabel titleContainer = new JLabel();
        titleContainer.setLayout(new BoxLayout(titleContainer,BoxLayout.Y_AXIS));
        titleContainer.setBackground(Color.white);

        JPanel titleJoined = new JPanel();
        titleJoined.setLayout(new BoxLayout(titleJoined,BoxLayout.X_AXIS));
        titleJoined.setOpaque(false);

        JLabel title = new JLabel("List of Medicines > ");
        title.setFont(title.getFont().deriveFont(28F));
        title.setForeground(new Color(0x3497F9));

        titleMedicine = new JLabel("");
        titleMedicine.setFont(title.getFont().deriveFont(28F));
        titleMedicine.setForeground(Color.BLACK);

        titleJoined.add(title);
        titleJoined.add(titleMedicine);
        titleJoined.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subTitle = new JLabel("List of medicines available for sales");
        subTitle.setOpaque(false);
        subTitle.setFont(new Font("Arial",Font.BOLD,15));
        subTitle.setAlignmentX(LEFT_ALIGNMENT);

        titleContainer.add(titleJoined);
        titleContainer.add(subTitle);
        titleContainer.setMaximumSize(new Dimension(1300,80));

        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setMaximumSize(new Dimension(1300,130));

        JPanel backButtonContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonContainer.setMaximumSize(new Dimension(1300,50));
        backButtonContainer.setOpaque(false);
        backButton.setMaximumSize(new Dimension(80,35));
        backButton.setFont(new Font("Courier",Font.PLAIN,15));
        backButtonContainer.add(backButton);

        JPanel container01 = new JPanel();
        container01.setLayout(new BoxLayout(container01,BoxLayout.X_AXIS));
        container01.setOpaque(false);

        editButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        editButton.setMaximumSize(new Dimension(125,35));
        editButton.setFont(new Font("Courier",Font.PLAIN,15));

        container01.add(titleContainer);
        container01.add(editButton);

        header.add(backButtonContainer);
        header.add(Box.createVerticalStrut(10));
        header.add(container01);
        return header;
    }
    JPanel bodyContainer(){
        JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(1300,600));
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JPanel MedicineContainer01 = new JPanel();
        MedicineContainer01.setLayout(new BoxLayout(MedicineContainer01,BoxLayout.X_AXIS));
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Medicine", TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
        border.setTitleFont(new Font("Poppins", Font.BOLD, 20));
        MedicineContainer01.setBorder(border);
        MedicineContainer01.setOpaque(false);

        JPanel MedicineIDBox = new JPanel();
        MedicineIDBox.setBorder(new EmptyBorder(10,30,10,10));
        MedicineIDBox.setBackground(Color.WHITE);
        MedicineIDBox.setLayout(new GridLayout(2,1));
        JLabel MedicineIDLabel = new JLabel("Medicine ID");
        MedicineIDLabel.setFont(new Font("Poppins",Font.PLAIN,17));
        MedicineID = new JLabel();
        MedicineID.setFont(new Font("Poppins",Font.BOLD,19));
        MedicineIDBox.add(MedicineID);
        MedicineIDBox.add(MedicineIDLabel);

        JPanel MedicineGroup = new JPanel();
        MedicineGroup.setBackground(Color.WHITE);
        MedicineGroup.setBorder(new EmptyBorder(10,30,10,20));
        MedicineGroup.setLayout(new GridLayout(2,1));
        JLabel MedicineGroupLabel = new JLabel("Medicine Group");
        MedicineGroupLabel.setFont(new Font("Poppins",Font.PLAIN,17));
        MedicineUnit = new JLabel();
        MedicineUnit.setFont(new Font("Poppins",Font.BOLD,19));
        MedicineGroup.add(MedicineUnit);
        MedicineGroup.add(MedicineGroupLabel);

        MedicineContainer01.add(MedicineIDBox);
        MedicineContainer01.add(MedicineGroup);

        JPanel MedicineContainer02 = new JPanel();
        MedicineContainer02.setLayout(new GridLayout(1,3,10,10));
        TitledBorder border2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Inventory in Qty", TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
        border2.setTitleFont(new Font("Poppins", Font.BOLD, 20));
        MedicineContainer02.setBorder(border2);
        MedicineContainer02.setOpaque(false);

        JPanel medicineSupplyBox = new JPanel(new GridLayout(2,1));
        medicineSupplyBox.setBorder(new EmptyBorder(10,30,10,20));
        medicineSupplyBox.setBackground(Color.WHITE);
        JLabel medicineSupplyLabel = new JLabel("Lifetime Supply");
        medicineSupplyLabel.setFont(new Font("Poppins",Font.PLAIN,17));
        medicineSupply = new JLabel();
        medicineSupply.setFont(new Font("Poppins",Font.BOLD,23));
        medicineSupplyBox.add(medicineSupply);
        medicineSupplyBox.add(medicineSupplyLabel);

        JPanel medicineSoldBox = new JPanel(new GridLayout(2,1));
        medicineSoldBox.setBorder(new EmptyBorder(10,30,10,20));
        medicineSoldBox.setBackground(Color.WHITE);
        JLabel medicineSoldLabel = new JLabel("Lifetime Sales");
        medicineSoldLabel.setFont(new Font("Poppins",Font.PLAIN,17));
        JLabel medicineSold = new JLabel("0");
        medicineSold.setFont(new Font("Poppins",Font.BOLD,23));
        medicineSoldBox.add(medicineSold);
        medicineSoldBox.add(medicineSoldLabel);

        JPanel medicineInStockBox = new JPanel(new GridLayout(2,1));
        medicineInStockBox.setBorder(new EmptyBorder(10,30,10,20));
        medicineInStockBox.setBackground(Color.WHITE);
        JLabel medicineInStockLabel = new JLabel("Stock Left");
        medicineInStockLabel.setFont(new Font("Poppins",Font.PLAIN,17));
        JLabel medicineInStock = new JLabel("0");
        medicineInStock.setFont(new Font("Poppins",Font.BOLD,23));
        medicineInStockBox.add(medicineInStock);
        medicineInStockBox.add(medicineInStockLabel);

        MedicineContainer02.add(medicineSupplyBox);
        MedicineContainer02.add(medicineSoldBox);
        MedicineContainer02.add(medicineInStockBox);

        JPanel MedicineContainer = new JPanel();
        MedicineContainer.setLayout(new BoxLayout(MedicineContainer,BoxLayout.X_AXIS));
        MedicineContainer.setOpaque(false);
        MedicineContainer.add(MedicineContainer01);
        MedicineContainer.add(Box.createHorizontalStrut(30));
        MedicineContainer.add(MedicineContainer02);

        JPanel HowToUseBox = new JPanel();
        HowToUseBox.setLayout(new BoxLayout(HowToUseBox,BoxLayout.Y_AXIS));
        HowToUseBox.setOpaque(false);
        TitledBorder border3 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "How to use", TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
        border3.setTitleFont(new Font("Poppins", Font.BOLD, 20));
        HowToUseBox.setBorder(border3);
        String text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate";
        howToUse = new JTextArea();
        howToUse.setText(text);
        howToUse.setEditable(false);
        howToUse.setLineWrap(true);
        howToUse.setWrapStyleWord(true);
        howToUse.setEditable(false);
        howToUse.setFont(new Font("Poppins",Font.PLAIN,15));
        howToUse.setBorder(new EmptyBorder(10,30,10,30));
        howToUse.setOpaque(false);
        JScrollPane HowToUseScrollPane = new JScrollPane(howToUse);
        HowToUseScrollPane.setOpaque(false);
        HowToUseScrollPane.getViewport().setBackground(Color.WHITE);
        HowToUseScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrollbar
        HowToUseScrollPane.setBorder(BorderFactory.createEmptyBorder());
        HowToUseBox.add(HowToUseScrollPane);

        JPanel SideEffectBox = new JPanel();
        SideEffectBox.setLayout(new BoxLayout(SideEffectBox,BoxLayout.Y_AXIS));
        SideEffectBox.setOpaque(false);
        TitledBorder border4 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Side Effects", TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
        border4.setTitleFont(new Font("Poppins", Font.BOLD, 20));
        SideEffectBox.setBorder(border4);
        sideEffects = new JTextArea();
        sideEffects.setText(text);
        sideEffects.setEditable(false);
        sideEffects.setLineWrap(true);
        sideEffects.setWrapStyleWord(true);
        sideEffects.setEditable(false);
        sideEffects.setFont(new Font("Poppins",Font.PLAIN,15));
        sideEffects.setBorder(new EmptyBorder(20,30,20,30));
        sideEffects.setOpaque(false);
        JScrollPane SideEffectScrollPane = new JScrollPane(sideEffects);
        SideEffectScrollPane.setOpaque(false);
        SideEffectScrollPane.getViewport().setBackground(Color.WHITE);
        SideEffectScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrollbar
        SideEffectScrollPane.setBorder(BorderFactory.createEmptyBorder());
        SideEffectBox.add(SideEffectScrollPane);

        panel.add(MedicineContainer);
        panel.add(Box.createVerticalStrut(30));
        panel.add(HowToUseBox);
        panel.add(Box.createVerticalStrut(30));
        panel.add(SideEffectBox);

        return panel;
    }
    public static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
        Barcode barcode = BarcodeFactory.createCode128A(barcodeText);
        barcode.setFont(new Font("Poppins",Font.BOLD,17));
        return BarcodeImageHandler.getImage(barcode);
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
//        setSize(700,400);
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
