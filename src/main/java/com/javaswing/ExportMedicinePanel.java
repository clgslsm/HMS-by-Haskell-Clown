package com.javaswing;
import com.javafirebasetest.dao.MedicineDAO;
import com.javafirebasetest.entity.Medicine;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

class ExportMedicinePanel extends JPanel {
    private JTextField MedicineNameInput;
    private JTextField MedicineIDInput;
    private JTextField MedicineExpiryDateInput;
    private JTextField UnitNumberInput;
    private JButton AddToCartButton;
    private JButton PurchaseButton;
    private JTextField SearchInput;
    private JTable MedicineSearchTable;
    private JTable MedicineCartTable;
    private MedicineSearchTableModel model;
    private MedicineCartTableModel cartModel;
    private Medicine localMedicine;
    private List<Medicine> cart;
    ExportMedicinePanel() {
        this.setMaximumSize(new Dimension(1300, 700));
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 35));
        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(headerContainer());
        this.add(Box.createVerticalStrut(30));
        this.add(bodyContainer());
    }
    JPanel headerContainer(){
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setMaximumSize(new Dimension(1300,70));

        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new GridLayout(2,1));
        titleContainer.setBackground(Color.white);
        JLabel title = new JLabel("Export Medicine");
        title.setFont(title.getFont().deriveFont(28F));
        title.setForeground(new Color(0x3497F9));
        JLabel subTitle = new JLabel("Use patient's medical prescription to export medicine");
        subTitle.setFont(new Font("Poppins",Font.PLAIN,15));
        titleContainer.add(title);
        titleContainer.add(subTitle);

        header.add(titleContainer);
        return header;
    }
    JPanel bodyContainer(){
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body,BoxLayout.X_AXIS));
        body.setMaximumSize(new Dimension(1300,800));

        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        container.add(formContainer());
        container.add(Box.createVerticalStrut(50));
        container.add(cartContainer());

        body.add(Box.createHorizontalStrut(50));
        body.add(searchContainer());
        body.add(Box.createHorizontalGlue());
        body.add(container);
        body.add(Box.createHorizontalStrut(50));
        return body;
    }
    JPanel searchContainer(){
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        container.setMaximumSize(new Dimension(150,800));
        container.setBackground(Color.BLACK);

        model = new MedicineSearchTableModel();
        MedicineSearchTable = new JTable(model);
        TableRowSorter sorter = new TableRowSorter(model);
        MedicineSearchTable.setRowSorter(sorter);
        MedicineSearchTable.setRowHeight(35);
        MedicineSearchTable.setShowHorizontalLines(false);
        MedicineSearchTable.setTableHeader(null);
        MedicineSearchTable.setOpaque(false);
        MedicineSearchTable.setSelectionBackground(new Color(0xF1F8FF));
        hideColumn(MedicineSearchTable,1);
        MedicineSearchTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedViewRowIndex = MedicineSearchTable.getSelectedRow(); // Get the view index of the selected row
                int selectedModelRowIndex = MedicineSearchTable.convertRowIndexToModel(selectedViewRowIndex); // Convert view index to model index
                TableModel model = MedicineSearchTable.getModel();
                String medicineID = model.getValueAt(selectedModelRowIndex,1).toString();
                localMedicine = MedicineDAO.getMedicineById(medicineID);
                MedicineNameInput.setText(localMedicine.getMedicineName());
                MedicineIDInput.setText(medicineID);
                MedicineExpiryDateInput.setText(AddNewMedicinePage.convertLocalDateToString(localMedicine.getExpiryDate()));
            }
        });
        JScrollPane scrollPane = new JScrollPane(MedicineSearchTable);
        scrollPane.getViewport().setBackground(Color.WHITE);

        List<Medicine> medicines = MedicineDAO.getAllMedicine();
        for(Medicine medicine : medicines){
            AddMedicineToTable(medicine);
        }

        SearchInput = new RoundedTextField(40,20);
        SearchInput.setMinimumSize(new Dimension(300,35));
        SearchInput.setPreferredSize(new Dimension(300,40));
        SearchInput.setMaximumSize(new Dimension(300,40));
        SearchInput.setFont(new Font("Courier",Font.PLAIN,15));
        SearchInput.setText("Search by medicine name");
        SearchInput.setForeground(Color.gray);
        SearchInput.addMouseListener(new CustomMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SearchInput.getText().equals("Search by medicine name") || SearchInput.getText().equals("No medicine found")) {
                    SearchInput.setText("");
                    SearchInput.setForeground(Color.BLACK);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                SearchInput.setFocusable(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                SearchInput.setFocusable(false);
                if (SearchInput.getText().isEmpty()) {
                    SearchInput.setForeground(Color.GRAY);
                    SearchInput.setText("Search by medicine name");
                }
            }
        });
        SearchInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                model.fireTableDataChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                model.fireTableDataChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                model.fireTableDataChanged();
            }
        });

        sorter.setRowFilter(new RowFilter() {
            @Override
            public boolean include(Entry entry) {
                String name = entry.getValue(0).toString();
                String searchText = SearchInput.getText();
                if (searchText.trim().isEmpty())
                    return false;
                return name.startsWith(searchText);
            }
        });
        container.add(SearchInput);
        container.add(Box.createVerticalStrut(20));
        container.add(scrollPane);
        container.add(Box.createVerticalStrut(100));

        return container;
    }
    JPanel formContainer(){
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form,BoxLayout.Y_AXIS));
        form.setOpaque(false);
        form.setMaximumSize(new Dimension(900,600));
        form.setBackground(Color.green);

        // Medicine Name
        JPanel MedicineNameBox = new JPanel();
        MedicineNameBox.setOpaque(false);
        MedicineNameBox.setMaximumSize(new Dimension(600,130));
        MedicineNameBox.setLayout(new GridLayout(2,1,0,5));
        JLabel MedicineNameLabel = new JLabel("Medicine Name");
        MedicineNameLabel.setFont(new Font("Poppins",Font.PLAIN,15));
        MedicineNameInput = new RoundedTextField(40,20);
        MedicineNameInput.setPreferredSize(new Dimension(300,50));
        MedicineNameInput.setFont(new Font("Courier",Font.PLAIN,15));
        MedicineNameInput.setEditable(false);
        MedicineNameBox.add(MedicineNameLabel);
        MedicineNameBox.add(MedicineNameInput);

        // Medicine ID
        JPanel MedicineIDBox = new JPanel();
        MedicineIDBox.setOpaque(false);
        MedicineIDBox.setLayout(new GridLayout(2,1,0,5));
        JLabel MedicineIDLabel = new JLabel("Medicine ID");
        MedicineIDLabel.setFont(new Font("Poppins",Font.PLAIN,15));
        MedicineIDInput = new RoundedTextField(40,20);
        MedicineIDInput.setText("");
        MedicineIDInput.setPreferredSize(new Dimension(300,50));
        MedicineIDInput.setFont(new Font("Courier",Font.PLAIN,15));
        MedicineIDInput.setEditable(false);
        MedicineIDBox.add(MedicineIDLabel);
        MedicineIDBox.add(MedicineIDInput);

        // Contain 1
        JPanel container1 = new JPanel();
        container1.setMaximumSize(new Dimension(600,80));
        container1.setOpaque(false);
        container1.setLayout(new GridLayout(1,2,50,0));
        container1.add(MedicineNameBox);
        container1.add(MedicineIDBox);

        // Medicine ExpiryDate
        JPanel MedicineExpiryDateBox = new JPanel();
        MedicineExpiryDateBox.setOpaque(false);
        MedicineExpiryDateBox.setLayout(new GridLayout(2,1,0,5));
        JLabel MedicineExpiryDateLabel = new JLabel("Expiry Date");
        MedicineExpiryDateLabel.setFont(new Font("Poppins",Font.PLAIN,15));
        MedicineExpiryDateInput = new RoundedTextField(40,20);
        MedicineExpiryDateInput.setToolTipText("Enter a date in the format DD-MM-YYYY");
        MedicineExpiryDateInput.setPreferredSize(new Dimension(300,50));
        MedicineExpiryDateInput.setFont(new Font("Courier",Font.PLAIN,15));
        MedicineExpiryDateInput.setEditable(false);
        MedicineExpiryDateBox.add(MedicineExpiryDateLabel);
        MedicineExpiryDateBox.add(MedicineExpiryDateInput);

        // Number of Units
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
        JPanel UnitNumberBox = new JPanel();
        UnitNumberBox.setOpaque(false);
        UnitNumberBox.setLayout(new GridLayout(2,1,0,5));
        JLabel UnitNumberLabel = new JLabel("No of Units");
        UnitNumberLabel.setFont(new Font("Poppins",Font.PLAIN,15));
        UnitNumberInput = new RoundedTextField(40,20);
        UnitNumberInput.setText("");
        UnitNumberInput.setDocument(doc);
        UnitNumberInput.setPreferredSize(new Dimension(100,50));
        UnitNumberInput.setFont(new Font("Courier",Font.PLAIN,15));
        UnitNumberBox.add(UnitNumberLabel);
        UnitNumberBox.add(UnitNumberInput);

        // Contain 2
        JPanel container2 = new JPanel();
        container2.setMaximumSize(new Dimension(600,80));
        container2.setOpaque(false);
        container2.setLayout(new GridLayout(1,2,50,0));
        container2.add(MedicineExpiryDateBox);
        container2.add(UnitNumberBox);

        JPanel AddToCartBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        AddToCartBox.setMaximumSize(new Dimension(600,50));
        AddToCartBox.setOpaque(false);
        AddToCartButton = new RoundedButton(" Add To Cart ");
        AddToCartButton.setMaximumSize(new Dimension(125,50));
        AddToCartBox.add(AddToCartButton);

        form.add(container1);
        form.add(Box.createVerticalStrut(15));
        form.add(container2);
        form.add(Box.createVerticalStrut(30));
        form.add(AddToCartBox);

        return form;
    }
    JPanel cartContainer(){
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(600,400));

        cartModel = new MedicineCartTableModel();
        MedicineCartTable = new JTable(cartModel);
        MedicineCartTable.getTableHeader().setPreferredSize(new Dimension(MedicineCartTable.getTableHeader().getWidth(), 35));
        MedicineCartTable.getTableHeader().setFont(new Font("Courier", Font.BOLD, 13));
        MedicineCartTable.getTableHeader().setOpaque(false);
        MedicineCartTable.getTableHeader().setBackground(new Color(32, 136, 203));
        MedicineCartTable.getTableHeader().setForeground(new Color(255,255,255));
        MedicineCartTable.setFocusable(false);
        MedicineCartTable.setIntercellSpacing(new java.awt.Dimension(0, 0));
        MedicineCartTable.setSelectionBackground(new Color(0x126DA6));
        MedicineCartTable.setSelectionForeground(Color.white);
        MedicineCartTable.setShowVerticalLines(false);
        MedicineCartTable.getTableHeader().setReorderingAllowed(false);
        MedicineCartTable.setFont(new Font("Courier",Font.PLAIN,13));
        MedicineCartTable.setRowHeight(36);
        MedicineCartTable.getColumn("").setCellRenderer(new PatientDefaultPage.DeleteButtonRenderer());
        MedicineCartTable.getColumn("").setCellEditor(new PatientDefaultPage.DeleteButtonEditor(new JCheckBox()));
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(MedicineCartTable);

        JPanel purchaseButtonBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        purchaseButtonBox.setOpaque(false);
        PurchaseButton = new RoundedButton(" Purchase & Print ");
        purchaseButtonBox.add(PurchaseButton);

        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(30));
        panel.add(purchaseButtonBox);

        return panel;
    }
    void AddMedicineToTable(Medicine medicine){
        String format = STR."\{medicine.getMedicineName()}-\{medicine.getAmount()}-\{medicine.getformattedExpiryDate()}";
        Object[] data = new Object[]{format,medicine.getMedicineId()};
        System.out.println(format);
        model.addRow(data);
    }
    private void hideColumn(JTable table, int columnIndex){
        table.getColumnModel().getColumn(columnIndex).setMaxWidth(0);
        table.getColumnModel().getColumn(columnIndex).setMinWidth(0);
        table.getColumnModel().getColumn(columnIndex).setPreferredWidth(0);
        table.getColumnModel().getColumn(columnIndex).setResizable(false);
    }
}
class MedicineSearchTableModel extends AbstractTableModel {
    // Data for each column
    private Object[][] data = {};
    // Column names
    private final String[] columnNames = {"Medicine","Medicine ID"};
    // Data types for each column
    @SuppressWarnings("rawtypes")
    private final Class[] columnTypes = {String.class,String.class};
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
        System.out.println(Arrays.toString(rowData));
        newData[data.length] = rowData;
        data = newData;
        fireTableRowsInserted(data.length - 1, data.length - 1); // Notify the table that rows have been inserted
    }
    // Method to clear all data from the table
    public void clearData() {
        int rowCount = getRowCount();
        data = new Object[0][0];
        if (rowCount > 0) fireTableRowsDeleted(0, rowCount - 1); // Notify the table that rows have been deleted
    }
}
class MedicineCartTableModel extends AbstractTableModel {
    // Data for each column
    private Object[][] data = {};
    // Column names
    private final String[] columnNames = {"Medicine Name","Medicine ID","No of Units",""};
    // Data types for each column
    @SuppressWarnings("rawtypes")
    private final Class[] columnTypes = {String.class,String.class,int.class,JButton.class};
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
        System.out.println(Arrays.toString(rowData));
        newData[data.length] = rowData;
        data = newData;
        fireTableRowsInserted(data.length - 1, data.length - 1); // Notify the table that rows have been inserted
    }
    // Method to clear all data from the table
    public void clearData() {
        int rowCount = getRowCount();
        data = new Object[0][0];
        if (rowCount > 0) fireTableRowsDeleted(0, rowCount - 1); // Notify the table that rows have been deleted
    }
}
