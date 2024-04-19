//package com.javaswing;
//import com.javafirebasetest.dao.DoctorDAO;
//import com.javafirebasetest.dao.MachineDAO;
//import com.javafirebasetest.dao.StaffDAO;
//import com.javafirebasetest.entity.*;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.table.AbstractTableModel;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.TableCellRenderer;
//import javax.swing.text.MaskFormatter;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseEvent;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//class MachinePanel extends JPanel {
//    ArrayList<Machine> data = new ArrayList<>();
//    MachineDefaultPage defaultPage;
//    MachinePanel() {
//        CardLayout currentPage = new CardLayout();
//        this.setLayout(currentPage);
//        this.setBackground(Color.white);
//
//        defaultPage = new MachineDefaultPage();
//
//        // When we click "Add Doctor" => change to Doctor Registration Page
//        defaultPage.addMachineBtn.addActionListener(_ -> {
//            String[] status = new String[Machine.Status.values().length];
//            int i = 0;
//            for (Machine.Status st : Machine.Status.values()) {
//                status[i] = st.getValue();
//                i++;
//            }
//            JComboBox<String> sta = new JComboBox<>(status);
//            sta.setBackground(Color.white);
//            sta.setBorder(BorderFactory.createEmptyBorder());
//            sta.setBounds(385-250,130,70,20);
//            JTextField nameField = new JTextField(30);
//
//            Object[] message = {
//                    "Name of machine:", nameField,
//                    "Status:", sta
//            };
//
//            int option = JOptionPane.showConfirmDialog(null, message, "", JOptionPane.OK_CANCEL_OPTION);
//
//            if (option == JOptionPane.OK_OPTION) {
//                String s = Objects.requireNonNull(sta.getSelectedItem()).toString();
//                String name = nameField.getText();
//
//                // Kiểm tra xem có ô nào bị bỏ trống không
//                if (s.isEmpty() || name.isEmpty()) {
//                    JOptionPane.showMessageDialog(null, "The input box cannot be left blank!", "Error", JOptionPane.ERROR_MESSAGE);
//                } else {
//                    JOptionPane.showMessageDialog(null, "Name: " + name + "\nStatus: " + s , "Information", JOptionPane.INFORMATION_MESSAGE);
//                    //Machine newMachine = new Machine();
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
//
//        // See full information and medical records of a specific patient
//        MachinePanel machinePanel = this;
//
//        // Always show default page
//        this.add(defaultPage, "default-page");
//        currentPage.show(this, "default-page");
//    }
//}
//class MachineDefaultPage extends JLabel {
//    MachineDefaultPage.SearchEngine searchEngine = new MachineDefaultPage.SearchEngine();
//    JButton addMachineBtn = AddMachineButton();
//    CustomTableModel model;
//    JTable machineList;
//    MachineDefaultPage() {
//        this.setMaximumSize(new Dimension(1300,600));
//        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 40));
//        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//
//        // Header container
//        JPanel header = new JPanel();
//        JLabel title = new JLabel("Machine Information");
//        title.setFont(title.getFont().deriveFont(25F));
//        title.setForeground(new Color(0x3497F9));
//        header.setBackground(new Color(0xF1F8FF));
//        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
//
//        header.add(title);
//        header.add(Box.createHorizontalGlue());
//        header.add(searchEngine);
//        header.add(Box.createHorizontalGlue());
//        header.add(addMachineBtn);
//
//        searchEngine.searchButton.addActionListener(_-> {
//            try {
//                showSearchResult(searchEngine.searchInput.getText());
//            } catch (ExecutionException | InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        //Table
//        JPanel body = new JPanel();
//        body.setLayout(new BorderLayout());
//        body.setBackground(Color.white);
//
//        model = new CustomTableModel();
//        List<Machine> allMachines = MachineDAO.getAllMachines();
//        for (Machine p : allMachines) {
//            addMachineToTable(p);
//        }
//        machineList = new JTable(model) {
//            @Override
//            public Class<?> getColumnClass(int column) {
//                return Object.class; // All columns are of type Object
//            }
//        }; // UI for patient list
//
//        machineList.getTableHeader().setPreferredSize(new Dimension(machineList.getTableHeader().getWidth(), 60));
//        machineList.getTableHeader().setFont(new Font("Courier", Font.BOLD, 16));
//        machineList.getTableHeader().setOpaque(false);
//        machineList.getTableHeader().setBackground(new Color(32, 136, 203));
//        machineList.getTableHeader().setForeground(new Color(255,255,255));
//
//        machineList.setFocusable(false);
//        machineList.setIntercellSpacing(new java.awt.Dimension(0, 0));
//        machineList.setSelectionBackground(new Color(0x9ACEF5));
//        machineList.setShowVerticalLines(false);
//        machineList.getTableHeader().setReorderingAllowed(false);
//        machineList.setFont(new Font("Courier",Font.PLAIN,16));
//
//        ButtonRenderer buttonRenderer = new MachineDefaultPage.ButtonRenderer();
//        ButtonEditor buttonEditor = new MachineDefaultPage.ButtonEditor();
//        machineList.getColumn("Action").setCellRenderer(buttonRenderer);
//        machineList.getColumn("Action").setCellEditor(buttonEditor);
//        machineList.setRowHeight(60);
//
//        machineList.getSelectionModel().addListSelectionListener(e -> {
//            int selectedRow = machineList.getSelectedRow();
//            buttonRenderer.setSelectedRow(selectedRow);
//            //buttonEditor.setID(staffList.getValueAt(selectedRow, 0).toString());
//            machineList.repaint();
//        });
//        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setViewportView(machineList);
//        body.add(scrollPane);
//
//        this.add(header);
//        JPanel space = new JPanel();
//        space.setBackground(new Color(0xF1F8FF));
//        space.setSize(new Dimension(100, 100));
//        this.add(space);
//        this.add(body);
//    }
//    static class SearchEngine extends JPanel {
//        JTextField searchInput = SearchBox();
//        JButton searchButton = SearchButton();
//        SearchEngine(){
//            setBackground(new Color(0xF1F8FF));
//            setMaximumSize(new Dimension(1000, 60));
//            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//            add(searchInput);
//            add(Box.createHorizontalStrut(10));
//            add(searchButton);
//        }
//        JTextField SearchBox(){
//            RoundedTextField field = new RoundedTextField(20, 20);
//            field.setPreferredSize(new Dimension(1500, 40));
//            field.setBackground(Color.white);
//            field.setForeground(Color.GRAY);
//            field.setFocusable(false);
//            field.revalidate();
//            field.setFont(new Font("Courier",Font.PLAIN,16));
//            field.setText("Search by machine name");
//            field.addMouseListener(new CustomMouseListener() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    if (field.getText().equals("Search by machine name") || field.getText().equals("No machine found")) {
//                        field.setText("");
//                        field.setForeground(Color.BLACK);
//                    }
//                }
//                @Override
//                public void mouseEntered(MouseEvent e) {
//                    field.setFocusable(true);
//                }
//                @Override
//                public void mouseExited(MouseEvent e) {
//                    field.setFocusable(false);
//                    if (field.getText().isEmpty()) {
//                        field.setForeground(Color.GRAY);
//                        field.setText("Search by machine name");
//                    }
//                }
//            });
//            return field;
//        }
//
//        JButton SearchButton(){
//            JButton button = new RoundedButton("Search");
//            button.setFont(new Font("Courier",Font.PLAIN,13));
//            button.setFocusable(false);
//            button.setForeground(Color.WHITE);
//            button.setBackground(new Color(0x3497F9));
//            button.setBounds(100, 100, 125, 60);
//            button.setBorder(new EmptyBorder(10,10,10,10));
//            return button;
//        }
////    }
////    void addMachineToTable (Machine machine){
////        ButtonRenderer buttonRenderer = new ButtonRenderer();
////        Object[] rowData = new Object[]{machine.getMachineId(), machine.getMachineName(), machine.getPurchaseDate(), machine.getMachineStatus(), machine.getUsageHistory(), buttonRenderer};
////        model.addRow(rowData);
////    }
////    public void showSearchResult(String name) throws ExecutionException, InterruptedException {
////        if (!name.trim().isEmpty() && !name.trim().equals("Search by Machine name")){
////            try{
////                List<Machine> res = MachineDAO.getMachineByName(name);
////                model.clearData();
////                for (Machine p : res) {
////                    addMachineToTable(p);
////                }
////            }
////            catch (Exception e) {
////                updateTableUI();
////                searchEngine.searchInput.setText("No machine found");
////                searchEngine.searchInput.setForeground(Color.red);
////            }
////        }
////        else {
////            updateTableUI();
////        }
////    }
////    public void updateTableUI() {
////        model.clearData();
////        List<Machine> allMachines = MachineDAO.getAllMachines();
////        for (Machine p : allMachines) {
////            addMachineToTable(p);
////        }
//    }
//    static class CustomTableModel extends AbstractTableModel {
//        // Data for each column
//        private Object[][] data = {};
//
//        // Column names
//        private final String[] columnNames = {"ID","Name","Purchase Date","Status","Usage History", "Action"};
//
//        // Data types for each column
//        @SuppressWarnings("rawtypes")
//        private final Class[] columnTypes = {String.class,String.class,String.class,String.class,String.class,JButton.class};
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
//            return columnIndex == 5;
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
//
//        public void clearData() {
//            int rowCount = getRowCount();
//            data = new Object[0][0];
//            if (rowCount > 0) fireTableRowsDeleted(0, rowCount - 1);
//        }
//    }
//    public JButton AddMachineButton(){
//        JButton addMachineButton = new RoundedButton("  + Add Machine  ");
//        addMachineButton.setFont(new Font("Courier",Font.PLAIN,13));
//        addMachineButton.setFocusable(false);
//        addMachineButton.setForeground(Color.WHITE);
//        addMachineButton.setBackground(new Color(0x3497F9));
//        addMachineButton.setBounds(100, 100, 125, 60);
//        addMachineButton.setBorder(new EmptyBorder(10,10,10,10));
//
//        return addMachineButton;
//    }
//
//    static class ButtonRenderer extends DefaultTableCellRenderer {
//        private JPanel panel;
//        private RoundedButton viewButton;
//        private RoundedButton deleteButton;
//        private int selectedRow = -1;
//
//
//        public ButtonRenderer() {
//            panel = new JPanel();
//            viewButton = new RoundedButton("View");
//            viewButton.setBackground(new Color(0xB5ED57));
//            deleteButton = new RoundedButton("Delete");
//            deleteButton.setBackground(new Color(0xFF9AA2));
//            panel.setBackground(new Color(0, 0, 0, 0));
//
//            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
//
//            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
//            panel.add(Box.createHorizontalStrut(40));
//            panel.add(viewButton);
//            panel.add(Box.createHorizontalStrut(20));
//            panel.add(deleteButton);
//            panel.add(Box.createHorizontalStrut(20));
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
//                                                       boolean hasFocus, int row, int column) {
//            if (isSelected && row == selectedRow) {
//                panel.setBackground(table.getSelectionBackground());
//            } else {
//                panel.setBackground(table.getBackground());
//            }
//            return panel;
//        }
//
//        public void setSelectedRow(int selectedRow) {
//            this.selectedRow = selectedRow;
//        }
//
//    }
//
//    static class ButtonEditor extends DefaultCellEditor {
//        private JPanel panel;
//        private RoundedButton viewButton;
//        private RoundedButton deleteButton;
//        private String id;
//
//        public void setID(String id) {
//            this.id = id;
//        }
//
//        public ButtonEditor() {
//            super(new JCheckBox());
//
//            panel = new JPanel();
//            viewButton = new RoundedButton("View");
//            viewButton.setBackground(new Color(0xB5ED57));
//            deleteButton = new RoundedButton("Delete");
//            deleteButton.setBackground(new Color(0xFF9AA2));
//            panel.setBackground(new Color(0x9ACEF5));
//
//            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
//
//            //viewButton.addActionListener(e -> fireEditingStopped());
//            //deleteButton.addActionListener(e -> fireEditingStopped());
//            viewButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    System.out.println("View button clicked in row " + id);
//                }
//            });
//
//            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
//            panel.add(Box.createHorizontalStrut(40));
//            panel.add(viewButton);
//            panel.add(Box.createHorizontalStrut(20));
//            panel.add(deleteButton);
//            panel.add(Box.createHorizontalStrut(20));
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//            return panel;
//        }
//
//        @Override
//        public Object getCellEditorValue() {
//            return "View/Delete";
//        }
//    }
//}
