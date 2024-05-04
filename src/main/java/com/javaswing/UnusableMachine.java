package com.javaswing;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.javafirebasetest.dao.MachineDAO;
import com.javafirebasetest.entity.Machine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;

class UnusableMachinePanel extends JPanel {
    UnusableMachineDefaultPage defaultPage;
    UnusableMachinePanel(){
        CardLayout currentPage = new CardLayout();
        this.setLayout(currentPage);
        this.setBackground(Constants.LIGHT_BLUE);

        defaultPage = new UnusableMachineDefaultPage();

        // Always show default page
        this.add(defaultPage, "default-page");
        currentPage.show(this, "default-page");
    }
}

class UnusableMachineDefaultPage extends JLabel {
    SearchEngine searchEngine = new SearchEngine();
    JButton refreshButton;
    private static CustomTableModel model;
    private JTable machineList;
    static JLabel title = new JLabel("List of Unusable Machine");
    UnusableMachineDefaultPage() {
        this.setMaximumSize(new Dimension(1300,600));
        this.setBackground(Constants.LIGHT_BLUE);
        this.setBorder(BorderFactory.createLineBorder(Constants.LIGHT_BLUE, 40));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(headerContainer());
        JPanel space = new JPanel();
        space.setBackground(Constants.LIGHT_BLUE);
        space.setSize(new Dimension(100, 100));
        this.add(space);
        this.add(bodyContainer());
    }
    JPanel headerContainer(){
        // Header container
        JPanel header = new JPanel();

        title.setFont(new Font(FlatInterFont.FAMILY,Font.BOLD,28));
        title.setForeground(Constants.BLUE);
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));

        header.add(title);
        header.add(Box.createHorizontalGlue());
        header.add(searchEngine);
        header.add(Box.createHorizontalGlue());

        refreshButton = new RoundedButton("Refresh");
        refreshButton.setFont(Constants.commonUsed);
        refreshButton.setIcon(new FlatSVGIcon("img/refresh.svg"));
        refreshButton.addActionListener(_->refreshMachineTable());

        header.add(refreshButton);

        searchEngine.searchButton.addActionListener(_-> {
            try {
                showSearchResult(searchEngine.searchInput.getText());
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return header;
    }
    JPanel bodyContainer(){
        //Table
        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        body.setOpaque(false);

        model = new CustomTableModel();

        machineList = new JTable(model) {
            @Override
            public Class<?> getColumnClass(int column) {
                return Object.class; // All columns are of type Object
            }
        }; // UI for patient list

        machineList.getTableHeader().setPreferredSize(new Dimension(machineList.getTableHeader().getWidth(), 40));
        machineList.getTableHeader().setFont(new Font(FlatInterFont.FAMILY, Font.BOLD, 15));
        machineList.getTableHeader().setOpaque(false);
        machineList.getTableHeader().setBackground(new Color(32, 136, 203));
        machineList.getTableHeader().setForeground(Color.white);

        machineList.setFocusable(false);
        machineList.setIntercellSpacing(new java.awt.Dimension(0, 0));
        machineList.setSelectionBackground(new Color(0x9ACEF5));
        machineList.setShowVerticalLines(false);
        machineList.getTableHeader().setReorderingAllowed(false);
        machineList.setFont(Constants.commonUsed);

        ButtonRenderer buttonRenderer = new ButtonRenderer();
        ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox());
        DeleteButtonRenderer deleteButtonRenderer = new DeleteButtonRenderer();
        DeleteButtonEditor deleteButtonEditor = new DeleteButtonEditor(new JCheckBox());
        machineList.getColumn(" ").setCellRenderer(buttonRenderer);
        machineList.getColumn(" ").setCellEditor(buttonEditor);
        machineList.getColumn("  ").setCellRenderer(deleteButtonRenderer);
        machineList.getColumn("  ").setCellEditor(deleteButtonEditor);
        machineList.getColumn(" ").setPreferredWidth(250);
        machineList.getColumn("  ").setMaxWidth(50);
        machineList.setRowHeight(35);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 2; i < 4; i++) {
            machineList.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        machineList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = machineList.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY()/machineList.getRowHeight();
                Object value = machineList.getValueAt(row,column);
                if (column == 4 && value instanceof JButton){
                    System.out.println(STR."Button clicked for row: \{row}");
                    MachineDAO.maintainMachine(machineList.getValueAt(row,0).toString());
                    refreshMachineTable();
                    MachineDefaultPage.refreshMachineTable();
                    Object[] message = {
                            STR."<html><b style='color:#3497F9; font-size:15px;'>\{machineList.getValueAt(row,1)}<b><html>",
                            STR."<html><b style='font-size:8px;'>ID: \{machineList.getValueAt(row,0)}<b><html>",
                            "<html><br><br><p>Machine is under maintenance</p><html>"
                    };
                    JOptionPane.showConfirmDialog(null, message, "", JOptionPane.DEFAULT_OPTION);
                }
                if (column == 5 && value instanceof JButton){
                    System.out.println(STR."Button clicked for row: \{row}");
                    MachineDAO.deleteMachine(machineList.getValueAt(row,0).toString());
                    refreshMachineTable();
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(machineList);
        refreshMachineTable();
        body.add(scrollPane);
        return body;
    }
    static class SearchEngine extends JPanel {
        JTextField searchInput = SearchBox();
        JButton searchButton = SearchButton();
        SearchEngine(){
            setBackground(Constants.LIGHT_BLUE);
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
            field.setFont(Constants.commonUsed);
            field.setText("Search by machine name");
            field.addMouseListener(new CustomMouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (field.getText().equals("Search by machine name") || field.getText().equals("No machine found")) {
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
                        field.setText("Search by machine name");
                    }
                }
            });
            return field;
        }

        JButton SearchButton(){
            JButton button = new RoundedButton("Search");
            button.setFont(Constants.commonUsed);
            button.setFocusable(false);
            button.setForeground(Color.WHITE);
            button.setBackground(Constants.BLUE);
            button.setBounds(100, 100, 125, 60);
            button.setBorder(new EmptyBorder(10,10,10,10));
            return button;
        }
    }
    private static void addMachineToTable(Machine machine){
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        DeleteButtonRenderer deleteButtonRenderer = new DeleteButtonRenderer();
        Object[] rowData = new Object[]{machine.getMachineId(), machine.getMachineName(), machine.getAvaiUse(), machine.getUseCount(), buttonRenderer, deleteButtonRenderer};
        model.addRow(rowData);
    }
    static void refreshMachineTable(){
        if (model != null) model.clearData();
        java.util.List<Machine> availableMachines = MachineDAO.getUnusableMachine();
        for (Machine p : availableMachines) {
            addMachineToTable(p);
        }
        title.setText(STR."List of Unusable Machine (\{availableMachines.size()})");
        System.out.println("Refresh Machine Table");
    }
    public void showSearchResult(String name) throws ExecutionException, InterruptedException {
        if (!name.trim().isEmpty() && !name.trim().equals("Search by Machine name")){
            try{
                List<Machine> res = MachineDAO.getMachineByName(name);
                model.clearData();
                for (Machine p : res) {
                    if (!p.isUsable())
                        addMachineToTable(p);
                }
            }
            catch (Exception e) {
                refreshMachineTable();
                searchEngine.searchInput.setText("No machine found");
                searchEngine.searchInput.setForeground(Color.red);
            }
        }
        else {
            refreshMachineTable();
        }
    }
    static class CustomTableModel extends AbstractTableModel {
        // Data for each column
        private Object[][] data = {};

        // Column names
        private final String[] columnNames = {"Machine ID","Machine Name","Available Use","Use Count", " ", "  "};

        // Data types for each column
        @SuppressWarnings("rawtypes")
        private final Class[] columnTypes = {String.class,String.class, Long.class, Long.class,JButton.class,JButton.class};

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
            return columnIndex == 5;
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
            if (rowCount > 0) fireTableRowsDeleted(0, rowCount - 1);
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
            setFont(new Font(FlatInterFont.FAMILY,Font.BOLD,15));
            setText("Request Maintenance");
            setForeground(new Color(0xfe8702));
            setMaximumSize(new Dimension(70,18));
            setBorder(BorderFactory.createEmptyBorder());
            setOpaque(false);
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
            button.addActionListener(_ -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            button.setBackground(Color.WHITE);
            button.setText("Request Maintenance");
            button.setFont(new Font(FlatInterFont.FAMILY,Font.BOLD,15));
            button.setForeground(new Color(0xfe8702));
            button.setMaximumSize(new Dimension(70,18));
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setOpaque(false);
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
            setIcon(new FlatSVGIcon("img/delete.svg"));
            setBorder(BorderFactory.createEmptyBorder());
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
            button.setIcon(new FlatSVGIcon("img/delete.svg"));
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setFocusable(false);
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
}
