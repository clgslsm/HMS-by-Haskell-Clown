package com.javaswing;
import com.javafirebasetest.dao.MedicineDAO;
import com.javafirebasetest.entity.*;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

class MedicinePanel extends JPanel {
    ArrayList<Medicine> data = new ArrayList<>();
    MedicineDefaultPage defaultPage;
    ViewMedicineInfoPage viewMedicineInfoPage;
    CardLayout currentPage = new CardLayout();
    MedicinePanel() {
        this.setLayout(currentPage);
        this.setBackground(Color.white);

        defaultPage = new MedicineDefaultPage(this);

        // Always show default page
        this.add(defaultPage, "default-page");
        currentPage.show(this, "default-page");
    }
}
class MedicineDefaultPage extends JLabel {
    JButton addMedicineBtn = AddMedicineButton();
    MedicineSearchEngine searchEngine = new MedicineSearchEngine();
    private JComboBox<String> filter = filterMedicine();
    static CustomTableModel model;
    JTable medicineList;
    static JLabel title = new JLabel("List of Medicines");
    MedicinePanel panel;
    MedicineDefaultPage(MedicinePanel panel) {
        this.panel = panel;
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

        addMedicineBtn.addActionListener(_->{
            // Create Add New Medicine Page
            AddNewMedicinePage addMedicinePage = new AddNewMedicinePage(panel);
            panel.add(addMedicinePage, "add-medicine-page");
            panel.currentPage.show(panel, "add-medicine-page");
        });

        header.add(titleContainer);
        header.add(Box.createHorizontalGlue());
        header.add(addMedicineBtn);

        //Table
        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        body.setBackground(Color.white);

        model = new CustomTableModel();
        medicineList = new JTable(model); // UI for patient list
//        medicineTableRowSorter = new TableRowSorter(model);
        refreshMedicineTable();

//        medicineList.setRowSorter(medicineTableRowSorter);
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

        medicineList.getColumn("").setCellRenderer(new ButtonRenderer());
        medicineList.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox()));
        medicineList.setRowHeight(36);
        // Center-align the content in each column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < medicineList.getColumnCount() - 1; i++) {
            medicineList.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // See full information and medical records of a specific patient
        medicineList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = medicineList.getColumnModel().getColumnIndexAtX(evt.getX());
                int row = evt.getY() / medicineList.getRowHeight();

                if (row < medicineList.getRowCount() && row >= 0 && column < medicineList.getColumnCount() && column >= 0) {
                    Object value = medicineList.getValueAt(row, column);
                    if (value instanceof JButton) {
                        // Instead of simulating button click, print to terminal
                        ViewMedicineInfoPage viewMedicineInfoPage;
                        System.out.println(STR."Button clicked for row: \{row}");
                        try {
                            viewMedicineInfoPage = viewPage(row);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        panel.add(viewMedicineInfoPage, "view-page");
                        panel.currentPage.show(panel, "view-page");
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(medicineList);
        body.add(scrollPane);

        this.add(header);
        JPanel space = new JPanel();

        space.setBackground(Color.white);
        space.setSize(new Dimension(40, 40));
        this.add(space);
        this.add(SearchFilterContainer());
        this.add(Box.createVerticalStrut(20));
        this.add(body);
    }
    JPanel SearchFilterContainer(){
        JPanel pan =  new JPanel();
        pan.setOpaque(false);
        pan.setLayout(new BoxLayout(pan,BoxLayout.X_AXIS));

        searchEngine.setAlignmentX(LEFT_ALIGNMENT);
        searchEngine.searchButton.addActionListener(_-> {
            try {
                showSearchResult(searchEngine.searchInput.getText());
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        filter.addActionListener(_->{
            String choice = filter.getSelectedItem().toString();
            if (choice.equals("All medicine")){
                System.out.println("Option 1");
                refreshMedicineTable();
            }
            else if (choice.equals("Medicine near to expiry")){
                model.clearData();
                System.out.println("Option 2");
                List<Medicine> medicines = MedicineDAO.getAllMedicine();
                for(Medicine medicine : medicines){
                    if (checkNearExpiryMedicine(medicine))
                        addMedicineToTable(medicine);
                }
            }
            else if (choice.equals("Out of stock")){
                model.clearData();
                System.out.println("Option 3");
                List<Medicine> medicines = MedicineDAO.getAllMedicine();
                for(Medicine medicine : medicines){
                    if (checkOutOfStock(medicine))
                        addMedicineToTable(medicine);
                }
            }
        });

        pan.add(searchEngine);
        pan.add(Box.createHorizontalGlue());
        pan.add(filter);
        return pan;
    }
    static void addMedicineToTable(Medicine medicine){
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        Object[] rowData = new Object[]{ medicine.getMedicineName(), medicine.getMedicineId(), medicine.getformattedImportDate(), medicine.getformattedExpiryDate(), medicine.getAmount(), medicine.getUnit(), buttonRenderer};
        model.addRow(rowData);
    }
    public ViewMedicineInfoPage viewPage(int row) throws Exception {
        ViewMedicineInfoPage viewPage = new ViewMedicineInfoPage(panel);
        // call medicine ID
        Medicine medicine = MedicineDAO.getMedicineById(medicineList.getValueAt(row,1).toString());
        viewPage.titleMedicine.setText(medicine.getMedicineName());
        BufferedImage image = ViewMedicineInfoPage.generateEAN13BarcodeImage(medicine.getMedicineId());
        viewPage.MedicineID.setIcon(new ImageIcon(image));
        viewPage.medicineid = medicine.getMedicineId();
        viewPage.MedicineUnit.setText(medicine.getUnit());
        viewPage.medicineSupply.setText(medicine.getAmount().toString());
        viewPage.description.setText(medicine.getDescription());
        viewPage.ExpDate.setText(medicine.getformattedExpiryDate());
        return viewPage;
    }
    static class CustomTableModel extends AbstractTableModel {
        // Data for each column
        private Object[][] data = {};
        // Column names
        private final String[] columnNames = {"Name","Medicine ID","Import Date","Expiry Date", "Stock in Qty","Unit", ""};
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
            setForeground(Color.gray);
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
            button.setForeground(Color.gray);
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
    static void refreshMedicineTable(){
        model.clearData();
        List<Medicine> allMedicins = MedicineDAO.getAllMedicine();
        for (Medicine p : allMedicins) {
            addMedicineToTable(p);
        }
        title.setText("List of Medicines (%d)".formatted(allMedicins.size()));
        System.out.println("Refresh Medicine Table");
    }
    private void showSearchResult(String name) throws ExecutionException, InterruptedException {
        if (!name.trim().isEmpty() && !name.trim().equals("Search by medicine name")){
            try{
                List<Medicine> res = MedicineDAO.getMedicineByName(name);
                model.clearData();
                for (Medicine m : res) {
                    addMedicineToTable(m);
                }
            }
            catch (Exception e) {
                refreshMedicineTable();
                searchEngine.searchInput.setText("No medicine found");
                searchEngine.searchInput.setForeground(Color.red);
            }
        }
        else refreshMedicineTable();
    }
    private JComboBox<String> filterMedicine(){
        String[] choice = {"All medicine", "Medicine near to expiry", "Out of stock"};
        JComboBox<String> filter = new JComboBox<>(choice);
        filter.setMaximumSize(new Dimension(250,50));
        filter.setBackground(Color.WHITE);
        filter.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        filter.setForeground(Color.gray);
        filter.setFont(new Font("Poppins", Font.ITALIC, 14));
        filter.setSelectedItem("All medicine");
        return filter;
    }
    private Boolean checkNearExpiryMedicine(Medicine medicine){
        return (!medicine.getExpiryDate().isAfter(LocalDate.now().plusDays(14)));
    }
    private Boolean checkOutOfStock(Medicine medicine){
        return medicine.getAmount() == 0;
    }
}
class AddNewMedicinePage extends JPanel {
    MedicinePanel panel;
    JButton backButton = new RoundedButton(" Return ");
    JTextField MedicineNameInput;
    JTextField MedicineIDInput;
    JComboBox<String> MedicineUnitInput;
    JTextField QuantityInput;
    JTextArea descriptionInput;
    JTextField MedicineImportInput;
    JTextField MedicineExpiredDayInput;
    JLabel emptyName = CannotBeLeftEmpty();
    JLabel emptyQuantity = CannotBeLeftEmpty();
    JLabel emptyExpiryDate = CannotBeLeftEmpty();
    JButton SaveButton = new RoundedButton(" Save ");
    JButton ResetButton = new RoundedButton(" Reset ");
    AddNewMedicinePage(MedicinePanel panel) {
        this.panel = panel;
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 25));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(10,10,10,10));
        // Get back to default page
        backButton.addActionListener(_ ->{
            this.panel.currentPage.removeLayoutComponent(this);
            this.panel.currentPage.show(this,"default-page");
        });
        container.add(headerContainer("Add New Medicine"));
        container.add(Box.createVerticalStrut(30));
        container.add(formContainer());
        container.add(Box.createVerticalStrut(30));
        container.add(footerContainer());

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
    }

    AddNewMedicinePage() {
    }

    JPanel headerContainer(String medicineTitle){
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(1300,160));

        JLabel titleContainer = new JLabel();
        titleContainer.setLayout(new BoxLayout(titleContainer,BoxLayout.Y_AXIS));
        titleContainer.setBackground(Color.white);

        JPanel titleJoined = new JPanel();
        titleJoined.setLayout(new BoxLayout(titleJoined,BoxLayout.X_AXIS));
        titleJoined.setOpaque(false);

        JLabel title = new JLabel("List of Medicines > ");
        title.setFont(title.getFont().deriveFont(28F));
        title.setForeground(new Color(0x3497F9));

        JLabel titleMedicine = new JLabel(medicineTitle);
        titleMedicine.setFont(title.getFont().deriveFont(28F));
        titleMedicine.setForeground(Color.BLACK);

        titleJoined.add(title);
        titleJoined.add(titleMedicine);
        titleJoined.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subTitle = new JLabel("*All fields are mandatory, except mentioned as (option)");
        subTitle.setOpaque(false);
        subTitle.setFont(new Font("Poppins",Font.PLAIN,15));
        subTitle.setAlignmentX(LEFT_ALIGNMENT);

        titleContainer.add(titleJoined);
        titleContainer.add(subTitle);
        titleContainer.setMaximumSize(new Dimension(1300,130));

        JPanel backButtonContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonContainer.setMaximumSize(new Dimension(1300,50));
        backButtonContainer.setOpaque(false);
        backButton.setMaximumSize(new Dimension(80,35));
        backButton.setFont(new Font("Poppins",Font.PLAIN,15));
        backButtonContainer.add(backButton);

        JPanel container01 = new JPanel();
        container01.setLayout(new BoxLayout(container01,BoxLayout.X_AXIS));
        container01.setOpaque(false);
        container01.add(titleContainer);

        panel.add(backButtonContainer);
        panel.add(Box.createVerticalStrut(10));
        panel.add(container01);

        return panel;
    }
    JPanel formContainer(){
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setMaximumSize(new Dimension(600,400));
        form.setLayout(new BoxLayout(form,BoxLayout.Y_AXIS));

        // Medicine Name
        JPanel MedicineNameBox = new JPanel();
        MedicineNameBox.setOpaque(false);
        MedicineNameBox.setMaximumSize(new Dimension(600,100));
        MedicineNameBox.setLayout(new GridLayout(3,1,0,5));
        JLabel MedicineNameLabel = new JLabel("Medicine Name");
        MedicineNameLabel.setFont(new Font("Poppins",Font.PLAIN,15));
        MedicineNameInput = new RoundedTextField(40,20);
        MedicineNameInput.setPreferredSize(new Dimension(300,50));
        MedicineNameInput.setFont(new Font("Courier",Font.PLAIN,15));
        emptyName.setVisible(false);
        MedicineNameBox.add(MedicineNameLabel);
        MedicineNameBox.add(MedicineNameInput);
        MedicineNameBox.add(emptyName);

        // Medicine ID
        JPanel MedicineIDBox = new JPanel();
        MedicineIDBox.setOpaque(false);
        MedicineIDBox.setLayout(new GridLayout(3,1,0,5));
        JLabel MedicineIDLabel = new JLabel("Medicine ID (optional)");
        MedicineIDLabel.setFont(new Font("Poppins",Font.PLAIN,15));
        MedicineIDInput = new RoundedTextField(40,20);
        MedicineIDInput.setText("");
        MedicineIDInput.setPreferredSize(new Dimension(300,50));
        MedicineIDInput.setFont(new Font("Courier",Font.PLAIN,15));
        MedicineIDBox.add(MedicineIDLabel);
        MedicineIDBox.add(MedicineIDInput);

        // Contain 1
        JPanel container1 = new JPanel();
        container1.setMaximumSize(new Dimension(600,60));
        container1.setOpaque(false);
        container1.setLayout(new GridLayout(1,2,50,0));
        container1.add(MedicineNameBox);
        container1.add(MedicineIDBox);

        // Medicine Unit
        JPanel MedicineUnitBox = new JPanel();
        MedicineUnitBox.setOpaque(false);
        MedicineUnitBox.setLayout(new GridLayout(3,1,0,5));
        JLabel MedicineUnitLabel = new JLabel("Unit");
        MedicineUnitLabel.setFont(new Font("Poppins",Font.PLAIN,15));
        MedicineUnitInput = new JComboBox<>();
        MedicineUnitInput.setPreferredSize(new Dimension(300,40));
        MedicineUnitInput.setFont(new Font("Courier",Font.PLAIN,15));
        MedicineUnitInput.setBackground(Color.white);
        MedicineUnitInput.setBorder(BorderFactory.createEmptyBorder());
        MedicineUnitInput.setOpaque(false);
        MedicineUnitBox.add(MedicineUnitLabel);
        MedicineUnitBox.add(MedicineUnitInput);

        // Quantity in Number
        PlainDocument doc = new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) {
                    return;
                }
                char[] chars = str.toCharArray();
                boolean isNumeric = true;
                for (char c : chars) {
                    if (!Character.isDigit(c)) {
                        isNumeric = false;
                        break;
                    }
                }
                if (isNumeric) {
                    super.insertString(offs, new String(chars), a);
                }
            }
        };
        JPanel QuantityBox = new JPanel();
        QuantityBox.setOpaque(false);
        QuantityBox.setLayout(new GridLayout(3,1,0,5));
        JLabel QuantityLabel = new JLabel("Quantity in Number");
        QuantityLabel.setFont(new Font("Poppins",Font.PLAIN,15));
        QuantityInput = new RoundedTextField(40,20);
        QuantityInput.setDocument(doc);
        QuantityInput.setPreferredSize(new Dimension(100,50));
        QuantityInput.setFont(new Font("Courier",Font.PLAIN,15));
        emptyQuantity.setVisible(false);
        QuantityBox.add(QuantityLabel);
        QuantityBox.add(QuantityInput);
        QuantityBox.add(emptyQuantity);

        // Contain 2
        JPanel container2 = new JPanel();
        container2.setMaximumSize(new Dimension(600,60));
        container2.setOpaque(false);
        container2.setLayout(new GridLayout(1,2,50,0));
        container2.add(MedicineUnitBox);
        container2.add(QuantityBox);

        // Medicine ImportDate
        JPanel MedicineImportBox = new JPanel();
        MedicineImportBox.setOpaque(false);
        MedicineImportBox.setLayout(new GridLayout(3,1,0,5));
        JLabel MedicineImportLabel = new JLabel("Import Date");
        MedicineImportLabel.setFont(new Font("Poppins",Font.PLAIN,15));
        MedicineImportInput = new RoundedTextField(40,20);
        MedicineImportInput.setPreferredSize(new Dimension(300,50));
        MedicineImportInput.setFont(new Font("Courier",Font.PLAIN,15));
        MedicineImportInput.setEnabled(false);
        MedicineImportInput.setText(convertLocalDateToString(LocalDate.now()));
        MedicineImportBox.add(MedicineImportLabel);
        MedicineImportBox.add(MedicineImportInput);

        // Medicine ExpiryDate
        JPanel MedicineExpiredDayBox = new JPanel();
        MedicineExpiredDayBox.setOpaque(false);
        MedicineExpiredDayBox.setLayout(new GridLayout(3,1,0,5));
        JLabel MedicineExpiredDayLabel = new JLabel("Expiry Date");
        MedicineExpiredDayLabel.setFont(new Font("Poppins",Font.PLAIN,15));
        MedicineExpiredDayInput = new RoundedTextField(40,20);
        MedicineExpiredDayInput.setToolTipText("Enter a date in the format DD-MM-YYYY");
        MedicineExpiredDayInput.setPreferredSize(new Dimension(300,50));
        MedicineExpiredDayInput.setFont(new Font("Courier",Font.PLAIN,15));
        emptyExpiryDate.setVisible(false);
        MedicineExpiredDayBox.add(MedicineExpiredDayLabel);
        MedicineExpiredDayBox.add(MedicineExpiredDayInput);
        MedicineExpiredDayBox.add(emptyExpiryDate);

        // Contain 3
        JPanel container3 = new JPanel();
        container3.setMaximumSize(new Dimension(600,60));
        container3.setOpaque(false);
        container3.setLayout(new GridLayout(1,2,50,0));
        container3.add(MedicineImportBox);
        container3.add(MedicineExpiredDayBox);

        // Description
        JPanel DescriptionBox = new JPanel();
        DescriptionBox.setOpaque(false);
        DescriptionBox.setLayout(new BoxLayout(DescriptionBox,BoxLayout.Y_AXIS));
        DescriptionBox.setMaximumSize(new Dimension(700,200));
        JPanel DescriptionLabelBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        DescriptionLabelBox.setOpaque(false);
        JLabel DescriptionLabel = new JLabel("Description (optional)");
        DescriptionLabelBox.add(DescriptionLabel);
        DescriptionLabel.setFont(new Font("Poppins",Font.PLAIN,15));
        descriptionInput = new RoundedTextArea(3,100,20,Color.BLACK);
        descriptionInput.setMinimumSize(new Dimension(700,80));
        descriptionInput.setLineWrap(true);
        descriptionInput.setWrapStyleWord(true);
        descriptionInput.setFont(new Font("Courier",Font.PLAIN,15));
        DescriptionBox.add(DescriptionLabelBox);
        DescriptionBox.add(descriptionInput);


        form.add(container1);
        form.add(Box.createVerticalStrut(10));
        form.add(container2);
        form.add(Box.createVerticalStrut(10));
        form.add(container3);
        form.add(Box.createVerticalStrut(10));
        form.add(DescriptionBox);

        return form;
    }
    JPanel footerContainer(){
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(600,50));

        SaveButton.setMaximumSize(new Dimension(100,50));
        SaveButton.addActionListener(_->{
            if (!checkEmptyTextField())
            {
                Medicine medicine = new Medicine(
                        MedicineIDInput.getText().trim().isEmpty()? null : MedicineIDInput.getText().trim(),
                        MedicineNameInput.getText().trim(),
                        convertStringToLocalDate(MedicineImportInput.getText().trim()),
                        convertStringToLocalDate(MedicineExpiredDayInput.getText().trim()),
                        Long.parseLong(QuantityInput.getText().trim()),
                        "Viên",
                        descriptionInput.getText().trim()
                );
                MedicineDAO.addMedicine(medicine);
                MedicineDefaultPage.refreshMedicineTable();
                this.panel.currentPage.removeLayoutComponent(this);
                this.panel.currentPage.show(this.panel,"default-page");
            }
        });
        ResetButton.setMaximumSize(new Dimension(100,50));
        ResetButton.setBackground(Color.WHITE);
        ResetButton.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(0x3497F9)),
                new EmptyBorder(10,10,10,10)
        ));
        ResetButton.setForeground(new Color(0x3497F9));
        ResetButton.addActionListener(_->resetInfo());

        panel.add(ResetButton);
        panel.add(SaveButton);
        return panel;
    }
    void resetInfo(){
        MedicineNameInput.setText("");
        if (MedicineUnitInput.getItemCount() > 0)
            MedicineUnitInput.setSelectedIndex(0);
        QuantityInput.setText("");
        descriptionInput.setText("");
    }
    JLabel CannotBeLeftEmpty(){
        JLabel label = new JLabel("Cannot be left empty");
        label.setForeground(Color.red);
        return label;
    }
    static String convertLocalDateToString(LocalDate date){
        DateTimeFormatter dateFormatter4 = DateTimeFormatter
                .ofPattern("dd-LL-yyyy");
        return date.format(dateFormatter4);
    }
    static LocalDate convertStringToLocalDate(String date){
        // dd-LL-yyyy pattern
        return LocalDate.parse(date,DateTimeFormatter.ofPattern("dd-LL-yyyy"));
    }
    Boolean checkEmptyTextField() {
        Boolean check = false;
        if (MedicineNameInput.getText().trim().isEmpty()){
            alertEmptyTextField(MedicineNameInput);
            emptyName.setVisible(true);
            check = true;
        }
        else {
            alertUnEmptyTextField(MedicineNameInput);
            emptyName.setVisible(false);
        }

        if (QuantityInput.getText().trim().isEmpty()){
            alertEmptyTextField(QuantityInput);
            emptyQuantity.setVisible(true);
            check = true;
        }
        else {
            alertUnEmptyTextField(QuantityInput);
            emptyQuantity.setVisible(false);
        }

        if (MedicineExpiredDayInput.getText().trim().isEmpty()){
            alertEmptyTextField(MedicineExpiredDayInput);
            emptyExpiryDate.setVisible(true);
            check = true;
        }
        else {
            alertUnEmptyTextField(MedicineExpiredDayInput);
            emptyExpiryDate.setVisible(false);
        }

        return check;
    }
    void alertEmptyTextField(JTextComponent component){
        Color ALERT_COLOR = new Color(0xffdbdd);
        component.setBackground(ALERT_COLOR);
    }
    void alertUnEmptyTextField(JTextComponent component){
        component.setBackground(Color.WHITE);
    }
}
class EditDetailMedicinePage extends AddNewMedicinePage {
    ViewMedicineInfoPage page;
    Medicine medicine;
    EditDetailMedicinePage(MedicinePanel panel, ViewMedicineInfoPage page) {
        this.panel = panel;
        this.page = page;
        medicine = MedicineDAO.getMedicineById(page.medicineid);
        System.out.println("Hello");
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 25));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(10,10,10,10));
        container.add(headerContainer("Edit Details"));
        container.add(Box.createVerticalStrut(30));
        container.add(formContainer());
        container.add(Box.createVerticalStrut(30));
        container.add(footerContainer());

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
    }

    EditDetailMedicinePage(MedicinePanel panel) {
        super(panel);
    }

    @Override
    JPanel headerContainer(String medicineTitle) {
        return super.headerContainer(medicineTitle);
    }
    @Override
    JPanel formContainer(){
        JPanel form = super.formContainer();
        MedicineNameInput.setText(medicine.getMedicineName());
        MedicineIDInput.setText(medicine.getMedicineId());
        QuantityInput.setText(medicine.getAmount().toString());
        MedicineImportInput.setEnabled(true);
        MedicineImportInput.setText(convertLocalDateToString(medicine.getImportDate()));
        MedicineExpiredDayInput.setText(convertLocalDateToString(medicine.getExpiryDate()));
        descriptionInput.setText(medicine.getDescription());
        return form;
    }
    @Override
    void resetInfo(){
        System.out.println(medicine.getMedicineName());
        MedicineNameInput.setText(medicine.getMedicineName());
        MedicineIDInput.setText(medicine.getMedicineId());
        QuantityInput.setText(medicine.getAmount().toString());
        MedicineImportInput.setEnabled(true);
        MedicineImportInput.setText(convertLocalDateToString(medicine.getImportDate()));
        MedicineExpiredDayInput.setText(convertLocalDateToString(medicine.getExpiryDate()));
        descriptionInput.setText(medicine.getDescription());
    }
    JPanel footerContainer(){
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(600,50));

        SaveButton.setMaximumSize(new Dimension(100,50));
        SaveButton.addActionListener(_->{
            if (!checkEmptyTextField())
            {
                Medicine medicine = new Medicine(
                        MedicineIDInput.getText().trim().isEmpty()? null : MedicineIDInput.getText().trim(),
                        MedicineNameInput.getText().trim(),
                        convertStringToLocalDate(MedicineImportInput.getText().trim()),
                        convertStringToLocalDate(MedicineExpiredDayInput.getText().trim()),
                        Long.parseLong(QuantityInput.getText().trim()),
                        "Viên",
                        descriptionInput.getText().trim()
                );
                System.out.println(medicine.getImportDate());
                MedicineDAO.addMedicine(medicine);
                try {
                    page.updateInfo(medicine);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                this.panel.currentPage.removeLayoutComponent(this);
                System.out.println("view");
                this.panel.currentPage.show(this.panel,"view-page");
                MedicineDefaultPage.refreshMedicineTable();
            }
        });
        ResetButton.setMaximumSize(new Dimension(100,50));
        ResetButton.setBackground(Color.WHITE);
        ResetButton.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(0x3497F9)),
                new EmptyBorder(10,10,10,10)
        ));
        ResetButton.setForeground(new Color(0x3497F9));
        ResetButton.addActionListener(_->resetInfo());

        panel.add(ResetButton);
        panel.add(SaveButton);
        return panel;
    }

}
class ViewMedicineInfoPage extends JPanel {
    MedicinePanel panel;
    JButton backButton = new RoundedButton(" Return ");
    JButton editButton = new RoundedButton(" Edit Details ");
    JLabel titleMedicine;
    JLabel MedicineID;
    String medicineid;
    JLabel ExpDate;
    JLabel MedicineUnit;
    JLabel medicineSupply;
    JTextArea description;
    JButton deleteButton = new RoundedButton(" Delete Medicine ");
    Boolean hasEdited = false;
    ViewMedicineInfoPage(MedicinePanel panel){
        this.panel = panel;
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
        deleteButton.addActionListener(_->{
            MedicineDAO.deleteMedicine(medicineid);
            MedicineDefaultPage.refreshMedicineTable();
            panel.currentPage.removeLayoutComponent(this);
            panel.currentPage.show(panel,"default-page");
        });

        container.add(headerContainer());
        container.add(Box.createVerticalStrut(30));
        container.add(bodyContainer());
        container.add(Box.createVerticalStrut(20));
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
        subTitle.setFont(new Font("Poppins",Font.PLAIN,15));
        subTitle.setAlignmentX(LEFT_ALIGNMENT);

        JPanel ExpireDateBox = new JPanel(new GridLayout(2,1));
        ExpireDateBox.setMaximumSize(new Dimension(100,80));
        ExpireDateBox.setOpaque(false);
        ExpireDateBox.setBorder(BorderFactory.createLineBorder(Color.red));
        ExpireDateBox.setAlignmentX(LEFT_ALIGNMENT);
        JLabel ExpireDateLabel = new JLabel("Expiry date");
        ExpireDateLabel.setFont(new Font("Courier",Font.PLAIN,13));
        ExpireDateLabel.setForeground(Color.red);
        ExpireDateLabel.setAlignmentX(CENTER_ALIGNMENT);
        ExpDate = new JLabel("");
        ExpDate.setForeground(Color.red);
        ExpDate.setFont(new Font("Courier",Font.PLAIN,13));
        ExpDate.setAlignmentX(CENTER_ALIGNMENT);
        ExpireDateBox.add(ExpireDateLabel);
        ExpireDateBox.add(ExpDate);

        titleContainer.add(titleJoined);
        titleContainer.add(subTitle);
        titleContainer.add(ExpireDateBox);
        titleContainer.setMaximumSize(new Dimension(1300,350));

        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setMaximumSize(new Dimension(1300,150));

        JPanel backButtonContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonContainer.setMaximumSize(new Dimension(1300,50));
        backButtonContainer.setOpaque(false);
        backButton.setMaximumSize(new Dimension(80,35));
        backButton.setFont(new Font("Courier",Font.PLAIN,15));
        backButton.addActionListener(_ ->{
            if (hasEdited){
                MedicineDefaultPage.refreshMedicineTable();
            }
            panel.currentPage.removeLayoutComponent(this);
            panel.currentPage.show(panel,"default-page");
        });
        backButtonContainer.add(backButton);

        JPanel container01 = new JPanel();
        container01.setLayout(new BoxLayout(container01,BoxLayout.X_AXIS));
        container01.setOpaque(false);

        editButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        editButton.setMaximumSize(new Dimension(125,35));
        editButton.setFont(new Font("Courier",Font.PLAIN,15));
        editButton.addActionListener(_->{
            hasEdited = true;
            EditDetailMedicinePage editPage = editDetail();
            panel.add(editPage,"edit-page");
            panel.currentPage.show(panel,"edit-page");
        });

        container01.add(titleContainer);
        container01.add(editButton);

        header.add(backButtonContainer);
        header.add(Box.createVerticalStrut(5));
        header.add(container01);
        return header;
    }
    JPanel bodyContainer(){
        JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(1300,400));
        panel.setLayout(new GridLayout(2,1,0,15));
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

        JPanel DescriptionBox = new JPanel();
        DescriptionBox.setLayout(new BoxLayout(DescriptionBox,BoxLayout.Y_AXIS));
        DescriptionBox.setOpaque(false);
        TitledBorder border3 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Description", TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
        border3.setTitleFont(new Font("Poppins", Font.BOLD, 20));
        DescriptionBox.setBorder(border3);
        String text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate";
        description = new JTextArea();
        description.setText(text);
        description.setEditable(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setFont(new Font("Poppins",Font.PLAIN,15));
        description.setBorder(new EmptyBorder(10,30,10,30));
        description.setOpaque(false);
        description.setMaximumSize(new Dimension(1300,300));
        JScrollPane DescriptionScrollPane = new JScrollPane(description);
        DescriptionScrollPane.setOpaque(false);
        DescriptionScrollPane.getViewport().setBackground(Color.WHITE);
        DescriptionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrollbar
        DescriptionScrollPane.setBorder(BorderFactory.createEmptyBorder());
        DescriptionBox.add(DescriptionScrollPane);

        panel.add(MedicineContainer);
        panel.add(DescriptionBox);

        return panel;
    }
    public static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
        Barcode barcode = BarcodeFactory.createCode128A(barcodeText);
        barcode.setFont(new Font("Poppins",Font.BOLD,17));
        return BarcodeImageHandler.getImage(barcode);
    }
    public void updateInfo(Medicine medicine) throws Exception {
        titleMedicine.setText(medicine.getMedicineName());
        BufferedImage image = ViewMedicineInfoPage.generateEAN13BarcodeImage(medicine.getMedicineId());
        MedicineID.setIcon(new ImageIcon(image));
        medicineid = medicine.getMedicineId();
        MedicineUnit.setText(medicine.getUnit());
        medicineSupply.setText(medicine.getAmount().toString());
        description.setText(medicine.getDescription());
        ExpDate.setText(medicine.getformattedExpiryDate());
    }
    EditDetailMedicinePage editDetail(){
        EditDetailMedicinePage page = new EditDetailMedicinePage(panel,this);
        page.resetInfo();
        return page;
    }
}
class MedicineSearchEngine extends JPanel {
    JTextField searchInput = SearchBox();
    JButton searchButton = SearchButton();
    MedicineSearchEngine(){
        setOpaque(false);
        setMaximumSize(new Dimension(350, 45));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(searchInput);
        add(Box.createHorizontalStrut(10));
        add(searchButton);
    }
    JTextField SearchBox(){
        RoundedTextField field = new RoundedTextField(20, 20);
        field.setMaximumSize(new Dimension(1500, 35));
        field.setBackground(Color.white);
        field.setForeground(Color.GRAY);
        field.setFocusable(false);
        field.revalidate();
        field.setFont(new Font("Courier",Font.PLAIN,16));
        field.setText("Search by medicine name");
        field.addMouseListener(new CustomMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (field.getText().equals("Search by medicine name") || field.getText().equals("No medicine found")) {
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
                    field.setText("Search by medicine name");
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