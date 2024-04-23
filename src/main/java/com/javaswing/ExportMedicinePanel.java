package com.javaswing;
import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.javafirebasetest.dao.MedicineDAO;
import com.javafirebasetest.entity.Medicine;
import com.javafirebasetest.entity.User;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import static com.javaswing.MedicineDefaultPage.refreshMedicineTable;

class ExportMedicinePanel extends JPanel {
    private final User user;
    private JTextField MedicineNameInput;
    private JTextField MedicineIDInput;
    private JTextField MedicineExpiryDateInput;
    private JTextField UnitNumberInput;
    private JLabel StockLabel;
    private long stock = 0;
    private JTextField SearchInput;
    private JTable MedicineSearchTable;
    private JTable MedicineCartTable;
    private MedicineSearchTableModel model;
    private MedicineCartTableModel cartModel;
    private Medicine localMedicine;
    ExportMedicinePanel(User user) {
        this.user = user;
        this.setMaximumSize(new Dimension(1000, 700));
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
        container.add(Box.createVerticalStrut(50));

        body.add(Box.createHorizontalStrut(50));
        body.add(searchContainer());
        body.add(Box.createHorizontalGlue());
        body.add(container);
        body.add(Box.createHorizontalStrut(30));
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
        MedicineSearchTable.setMaximumSize(new Dimension(200,600));
        hideColumn(MedicineSearchTable);
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
                stock = localMedicine.getAmount();
                StockLabel.setText(STR."Stock: \{stock}");
            }
        });
        JScrollPane scrollPane = new JScrollPane(MedicineSearchTable);
        scrollPane.getViewport().setBackground(Color.WHITE);

        List<Medicine> medicines = MedicineDAO.getAllMedicine();
        for(Medicine medicine : medicines){
            AddMedicineToTable(medicine);
        }

        SearchInput = new RoundedTextField(40,20);
        SearchInput.setMinimumSize(new Dimension(200,35));
        SearchInput.setPreferredSize(new Dimension(200,40));
        SearchInput.setMaximumSize(new Dimension(200,40));
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
        container.add(Box.createVerticalStrut(30));

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
        UnitNumberInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                long noUnit = (UnitNumberInput.getText().isEmpty())? 0 : Long.parseLong(UnitNumberInput.getText());
                long remain = stock - noUnit;
                StockLabel.setText(STR."Stock: \{remain}");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                long noUnit = (UnitNumberInput.getText().isEmpty())? 0 : Long.parseLong(UnitNumberInput.getText());
                long remain = stock - noUnit;
                StockLabel.setText(STR."Stock: \{remain}");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                long noUnit = (UnitNumberInput.getText().isEmpty())? 0 : Long.parseLong(UnitNumberInput.getText());
                long remain = stock - noUnit;
                StockLabel.setText(STR."Stock: \{remain}");
            }
        });
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
        JButton addToCartButton = new RoundedButton(" Add To Cart ");
        addToCartButton.setMaximumSize(new Dimension(125,50));
        addToCartButton.addActionListener(_->{
            long noUnit = (UnitNumberInput.getText().isEmpty()) ? 0 : Long.parseLong(UnitNumberInput.getText());
            if (noUnit > 0){
                AddMedicineToCart();
                resetForm();
                JOptionPane.showOptionDialog(this,"Successfully added to cart",null,JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,null,null);
            }
            else
                JOptionPane.showOptionDialog(this,"No of Units must be greater than 0",null,JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE,null,null,null);
        });
        AddToCartBox.add(addToCartButton);

        JPanel StockBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        StockBox.setMaximumSize(new Dimension(600,50));
        StockBox.setOpaque(false);
        StockLabel = new JLabel(STR."Stock: \{stock}");
        StockLabel.setFont(new Font("Courier",Font.ITALIC,15));
        StockLabel.setForeground(Color.gray);
        StockBox.add(StockLabel);

        form.add(container1);
        form.add(Box.createVerticalStrut(15));
        form.add(container2);
        form.add(Box.createVerticalStrut(15));
        form.add(StockBox);
        form.add(Box.createVerticalStrut(5));
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
        MedicineCartTable.getColumn("").setPreferredWidth(10);
        MedicineCartTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = MedicineCartTable.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / MedicineCartTable.getRowHeight();
                Object deleteBtn = cartModel.getValueAt(row,column);
                if (deleteBtn instanceof JButton){
                    cartModel.deleteRow(row);
                }
            }
        });
        // Center-align the content in each column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < MedicineCartTable.getColumnCount() - 1; i++) {
            MedicineCartTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(MedicineCartTable);

        JPanel purchaseButtonBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        purchaseButtonBox.setOpaque(false);
        JButton purchaseButton = new RoundedButton(" Purchase & Print ");
        purchaseButton.addActionListener(_->{
            int rowCount = MedicineCartTable.getRowCount();
            exportBill();
            for (int row = 0; row < rowCount; row++) {
                String medicineid = cartModel.getValueAt(row,1).toString();
                Medicine medicine = MedicineDAO.getMedicineById(medicineid);
                MedicineDAO.updateMedicine(medicineid,
                        "amount",medicine.getAmount()-Long.parseLong(cartModel.getValueAt(row,2).toString()));
            }
            resetExport();
            refreshSearch();
            refreshMedicineTable();
        });
        purchaseButtonBox.add(purchaseButton);

        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(30));
        panel.add(purchaseButtonBox);

        return panel;
    }
    void AddMedicineToTable(Medicine medicine){
        String format = STR."\{medicine.getMedicineName()}-\{medicine.getAmount()}-\{medicine.getformattedExpiryDate()}";
        Object[] data = new Object[]{format,medicine.getMedicineId()};
        model.addRow(data);
    }
    void AddMedicineToCart(){
        PatientDefaultPage.DeleteButtonRenderer deleteButtonRenderer = new PatientDefaultPage.DeleteButtonRenderer();
        Object[] data = new Object[]{localMedicine.getMedicineName(),
                localMedicine.getMedicineId(),
                Long.parseLong(UnitNumberInput.getText()),
                deleteButtonRenderer
        };
        cartModel.addRow(data);
    }
    private void hideColumn(JTable table){
        table.getColumnModel().getColumn(1).setMaxWidth(0);
        table.getColumnModel().getColumn(1).setMinWidth(0);
        table.getColumnModel().getColumn(1).setPreferredWidth(0);
        table.getColumnModel().getColumn(1).setResizable(false);
    }
    private void resetForm(){
        localMedicine = null;
        MedicineNameInput.setText("");
        MedicineIDInput.setText("");
        MedicineExpiryDateInput.setText("");
        UnitNumberInput.setText("");
        stock = 0;
        StockLabel.setText(STR."Stock: \{stock}");
    }
    private void refreshSearch(){
        SearchInput.setText("");
        model.clearData();
        List<Medicine> medicines = MedicineDAO.getAllMedicine();
        for(Medicine medicine : medicines){
            AddMedicineToTable(medicine);
        }
        model.fireTableDataChanged();
        System.out.println("Refresh Search");
    }
    private void resetExport(){
        resetForm();
        cartModel.clearData();
    }
    private void exportBill(){
        // Specify the path of the folder you want to create
        String folderPath = "C:/bill";

        // Create a File object with the folder path
        File folder = new File(folderPath);

        // Check if the folder already exists
        if (!folder.exists()) {
            // If the folder does not exist, create it
            boolean created = folder.mkdir();

            // Check if the folder creation was successful
            if (created) {
                System.out.println("Folder created successfully.");
            } else {
                System.out.println("Failed to create folder.");
            }
        } else {
            System.out.println("Folder already exists.");
        }

        String path= STR."C://bill//Bill_\{generateBillID()}.pdf";
        Rectangle one = new Rectangle(180,400);
        Document doc = new Document(one);
        doc.setMargins(10, 10, 30, 30);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.open();

            // Add title to the document
            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
            Paragraph title = new Paragraph("ABC HOSPITAL", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(title);

            // Add a horizontal line
            // Add dashed line
            // Add dashes as separator
            com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 7);
            String dashes = " - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n";
            Paragraph lineSeparator = new Paragraph(dashes,font);
            lineSeparator.setAlignment(Element.ALIGN_CENTER);
            doc.add(lineSeparator);

            // Add Bill ID and date
            String billID = generateBillID();
            String currentDate = getCurrentDate();
            Paragraph billInfo = new Paragraph("Bill ID: " + billID + "\nDate: " + currentDate + "\nStaff: " + user.getUsername(),font);
            billInfo.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(billInfo);
            doc.add(lineSeparator);

            // Add table
            PdfPTable tb1 = new PdfPTable(2);
            tb1.setWidthPercentage(80);
            tb1.getDefaultCell().setBackgroundColor(null);
            tb1.getDefaultCell().setPadding(5);
            tb1.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tb1.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

            // Add cells to table
            PdfPCell cell1 = new PdfPCell(new Phrase("Medicine", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7)));
            PdfPCell cell2 = new PdfPCell(new Phrase("No of Units", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7)));

            // Set alignment for cells
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

            tb1.addCell(cell1);
            tb1.addCell(cell2);
            int row = cartModel.getRowCount();
            for(int i = 0; i < row; i++){
                PdfPCell cell3 = new PdfPCell(new Phrase(cartModel.getValueAt(i,0).toString(), font));
                PdfPCell cell4 = new PdfPCell(new Phrase(cartModel.getValueAt(i,2).toString(), font));
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                tb1.addCell(cell3);
                tb1.addCell(cell4);
            }
            doc.add(new Paragraph("\n"));
            doc.add(tb1);
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph(""));

            // Add content to the document
            doc.add(lineSeparator);

            // Close the document
            doc.close();
            System.out.println(STR."PDF created successfully at: \{path}");
        }
        catch (Exception _){}
    }
    static public String generateBillID(){
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS");
        return formatter.format(zonedDateTime);
    }
    private static String getCurrentDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format the current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return currentDateTime.format(formatter);
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
    private final Class[] columnTypes = {String.class,String.class,long.class,JButton.class};
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
    // Method to clear all data from the table
    public void clearData() {
        int rowCount = getRowCount();
        data = new Object[0][0];
        if (rowCount > 0) fireTableRowsDeleted(0, rowCount - 1); // Notify the table that rows have been deleted
    }
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
}

