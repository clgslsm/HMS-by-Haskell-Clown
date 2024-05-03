package com.javaswing;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.javafirebasetest.dao.*;
import com.javafirebasetest.entity.*;

import javax.print.Doc;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

class StaffPanel extends JPanel {
    public StaffDefaultPage defaultPage;
    StaffPanel() {
        CardLayout currentPage = new CardLayout();
        this.setLayout(currentPage);
        this.setBackground(Constants.LIGHT_BLUE);

        defaultPage = new StaffDefaultPage();

        String[] job = new  String[User.Mode.values().length];
        String[] department = new String[DeptType.values().length];
        int i = 0, k = 0;
        for (DeptType dt : DeptType.values()) {
            department[i] = dt.getValue();
            i++;
        }
        for (User.Mode m : User.Mode.values()) {
            job[k] = m.getValue();
            k++;
        }
        JComboBox<String> jo = new JComboBox<>(job);
        JComboBox<String> dep = new JComboBox<>(department);
        jo.setBackground(Color.white);
        dep.setBackground(Color.white);
        jo.setBorder(BorderFactory.createEmptyBorder());
        dep.setBorder(BorderFactory.createEmptyBorder());
        jo.setBounds(385 - 250, 100, 70, 20);
        dep.setBounds(385-250,130,70,20);
        JTextField nameField = new JTextField(30);
        JTextField usernameField = new JTextField(30);
        JTextField passField = new JTextField(30);

        dep.setEnabled(false);
        jo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jo.getSelectedItem().equals("Doctor")) {
                    dep.setEnabled(true);
                } else {
                    dep.setEnabled(false);
                }
            }
        });

        // When we click "Add Staff" => change to Staff Registration Page
        defaultPage.addStaffBtn.addActionListener(_ -> {
            dep.setEnabled(true);
            Object[] message = {
                    "Name of Job: ", jo,
                    "Name of Department: ", dep,
                    "Name: ", nameField,
                    "Username: ", usernameField,
                    "Password: ", passField
            };

            int option = JOptionPane.showConfirmDialog(null, message, "", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String j = Objects.requireNonNull(jo.getSelectedItem()).toString();
                String d = Objects.requireNonNull(dep.getSelectedItem()).toString();

                String name = nameField.getText();
                String username = usernameField.getText();
                String pass = passField.getText();

                User test = UserDAO.getUserByUsernamePassword(username, pass);

                // Kiểm tra xem có ô nào bị bỏ trống không
                if (j.isEmpty() || d.isEmpty() || name.isEmpty() || username.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "The information box cannot be left blank!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (test != null) {
                    JOptionPane.showMessageDialog(null, "Username and pass is unavailable", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Job: " + j + "\nDepartment: " + d + "\nName: " + name, "Information", JOptionPane.INFORMATION_MESSAGE);
                    if (j !=  "Doctor") {
                        Staff newStaff = new Staff(null, name, User.Mode.fromValue(j));
                        String returnId = StaffDAO.addStaff(newStaff);
                        UserDAO.addUser(new User(null, username, pass, User.Mode.fromValue(j), returnId));
                    }
                    else {
                        Doctor newDoctor = new Doctor(null, name, DeptType.fromValue(d));
                        String returnId = DoctorDAO.addDoctor(newDoctor);
                        UserDAO.addUser(new User(null, username, pass, User.Mode.fromValue(j), returnId));
                    }
                    defaultPage.updateTableUI();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cancel", "Notification", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        // Always show default page
        this.add(defaultPage, "default-page");
        currentPage.show(this, "default-page");
    }
}
class StaffDefaultPage extends JLabel {
    StaffDefaultPage.SearchEngine searchEngine = new StaffDefaultPage.SearchEngine();
    JButton addStaffBtn = AddStaffButton();
    CustomTableModel model;
    JTable staffList;
    JLabel title = new JLabel("List of Staffs");
    StaffDefaultPage() {
        this.setMaximumSize(new Dimension(1300,1000));
        this.setBackground(Constants.LIGHT_BLUE);
        this.setBorder(BorderFactory.createLineBorder(Constants.LIGHT_BLUE, 40));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Header container
        JPanel header = new JPanel();
        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new GridLayout(2,1));
        titleContainer.setOpaque(false);
        title.setFont(new Font(FlatRobotoFont.FAMILY,Font.BOLD,28));
        title.setForeground(Constants.BLUE);
        JLabel subTitle = new JLabel("Show all staffs of the hospital.");
        subTitle.setFont(Constants.commonUsed);
        titleContainer.add(title);
        titleContainer.add(subTitle);
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));

        header.add(titleContainer);
        header.add(Box.createHorizontalGlue());
        header.add(addStaffBtn);

        JPanel pan =  new JPanel();
        pan.setOpaque(false);
        pan.setLayout(new BoxLayout(pan,BoxLayout.X_AXIS));
        searchEngine.setAlignmentX(LEFT_ALIGNMENT);
        searchEngine.searchButton.addActionListener(_-> {
            try {
                showSearchResult(searchEngine.searchInput.getText());
            } catch (ExecutionException | InterruptedException e) {
                updateTableUI();
                throw new RuntimeException(e);
            }
        });
        pan.add(searchEngine);
        pan.add(Box.createHorizontalGlue());

        //Table
        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        body.setBackground(Color.white);

        model = new CustomTableModel();
        List<Staff> allStaffs = StaffDAO.getAllStaff();
        for (Staff p : allStaffs) {
            addStaffToTable(p);
        }
        staffList = new JTable(model) {
            @Override
            public Class<?> getColumnClass(int column) {
                return Object.class; // All columns are of type Object
            }
        }; // UI for patient list

        staffList.getTableHeader().setPreferredSize(new Dimension(staffList.getTableHeader().getWidth(), 40));
        staffList.getTableHeader().setFont(new Font(FlatInterFont.FAMILY, Font.BOLD, 15));
        staffList.getTableHeader().setOpaque(false);
        staffList.getTableHeader().setBackground(new Color(32, 136, 203));
        staffList.getTableHeader().setForeground(Color.white);

        staffList.setFocusable(false);
        staffList.setIntercellSpacing(new java.awt.Dimension(0, 0));
        staffList.setSelectionBackground(new Color(0x9ACEF5));
        staffList.setShowVerticalLines(false);
        staffList.getTableHeader().setReorderingAllowed(false);
        staffList.setFont(Constants.commonUsed);

        DeleteButtonRenderer deleteButtonRenderer = new DeleteButtonRenderer();
        DeleteButtonEditor deleteButtonEditor = new DeleteButtonEditor(new JCheckBox());
        staffList.getColumn("  ").setCellRenderer(deleteButtonRenderer);
        staffList.getColumn("  ").setCellEditor(deleteButtonEditor);
        staffList.getColumn("  ").setMaxWidth(50);
        staffList.setRowHeight(35);

        staffList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = staffList.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY()/staffList.getRowHeight();
                Object value = staffList.getValueAt(row,column);
                if (column == 4 && value instanceof JButton){
                    System.out.println(STR."Button clicked for row: \{row} \{staffList.getValueAt(row, 0).toString()}");
                    if (staffList.getValueAt(row, 2).equals("Doctor")) {
                        List<MedicalRecord> mr = MedRecDAO.getMedRecByDoctorId(staffList.getValueAt(row, 0).toString());
                        boolean can = true;
                        for (MedicalRecord m : mr) {
                            if (!m.getStatus().getValue().equals("Checked_out")) {
                                can = false;
                                break;
                            }
                        }
                        if (!can) {
                            Object[] message = {
                                    STR."<html><b style='color:#3497F9; font-size:15px;'>Name: \{staffList.getValueAt(row, 1)}<b><html>",
                                    STR."<html><b style='font-size:10px;'>Job: \{staffList.getValueAt(row, 2)}<b><html>",
                                    STR."<html><br><br><p>This doctor has patient, can not delete.</p><html>"
                            };
                            int option = JOptionPane.showConfirmDialog(null, message, "", JOptionPane.DEFAULT_OPTION);
                        }
                        else {
                            StaffDAO.deleteStaffById(staffList.getValueAt(row, 0).toString());
                            Object[] message = {
                                    STR."<html><br><br><p>Delete complete.</p><html>"
                            };
                            int option = JOptionPane.showConfirmDialog(null, message, "", JOptionPane.DEFAULT_OPTION);
                            updateTableUI();
                        }
                    }
                    else if (staffList.getValueAt(row, 2).equals("Admin")) {
                        Object[] message = {
                                STR."<html><br><br><p>You can not delete an Admin</p><html>"
                        };
                        int option = JOptionPane.showConfirmDialog(null, message, "", JOptionPane.DEFAULT_OPTION);
                    }
                    else {
                        StaffDAO.deleteStaffById(staffList.getValueAt(row, 0).toString());
                        Object[] message = {
                                STR."<html><br><br><p>Delete complete.</p><html>"
                        };
                        int option = JOptionPane.showConfirmDialog(null, message, "", JOptionPane.DEFAULT_OPTION);
                        updateTableUI();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(staffList);
        body.add(scrollPane);

        this.add(header);
        this.add(pan);
        JPanel space = new JPanel();
        space.setBackground(Constants.LIGHT_BLUE);
        space.setSize(new Dimension(100, 100));
        this.add(space);
        this.add(body);
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
            field.setText("Search by staff name");
            field.addMouseListener(new CustomMouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (field.getText().equals("Search by staff name") || field.getText().equals("No staff found")) {
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
                        field.setText("Search by staff name");
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
    void addStaffToTable (Staff staff){
        String job = staff.getUserMode().getValue();
        String department = "";
        DeleteButtonRenderer deleteButtonRenderer = new DeleteButtonRenderer();
        if (staff.getUserMode().getValue() == "Doctor") {
            department = (DoctorDAO.getDoctorById(staff.getStaffId())).getDepartment().getValue();
        }

        Object[] rowData = new Object[]{staff.getStaffId(), staff.getName(),job, department, deleteButtonRenderer};
        model.addRow(rowData);
    }
    public void showSearchResult(String name) throws ExecutionException, InterruptedException {
        if (!name.trim().isEmpty() && !name.trim().equals("Search by staff name")){
            try{
                List<Staff> res = StaffDAO.getStaffByName(name);
                model.clearData();
                for (Staff t : res) {
                    addStaffToTable(t);
                }
            }
            catch (Exception e) {
                updateTableUI();
                searchEngine.searchInput.setText("No staff found");
                searchEngine.searchInput.setForeground(Constants.RED);
            }
        }
        else {
            updateTableUI();
        }
    }
    public void updateTableUI() {
        model.clearData();
        List<Staff> allStaffs = StaffDAO.getAllStaff();
        for (Staff p : allStaffs) {
            addStaffToTable(p);
        }
        title.setText(STR."List of Staffs (\{allStaffs.size()})");
    }

    static class CustomTableModel extends AbstractTableModel {
        // Data for each column
        private Object[][] data = {};

        // Column names
        private final String[] columnNames = {"ID","Name", "Job", "Department", "  "};

        // Data types for each column
        @SuppressWarnings("rawtypes")
        private final Class[] columnTypes = {String.class,String.class,String.class,String.class,JButton.class};

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
            return columnIndex == 4;
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

    public JButton AddStaffButton(){
        JButton addStaffButton = new RoundedButton("Add Staff");
        addStaffButton.setFont(Constants.commonUsed);
        addStaffButton.setIcon(new FlatSVGIcon("add-person.svg"));
        addStaffButton.setFocusable(false);
        addStaffButton.setForeground(Color.WHITE);
        addStaffButton.setBackground(Constants.BLUE);
        addStaffButton.setBounds(100, 100, 125, 60);
        addStaffButton.setBorder(new EmptyBorder(10,10,10,10));

        return addStaffButton;
    }

    static class DeleteButtonRenderer extends JButton implements TableCellRenderer {
        public DeleteButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setIcon(new FlatSVGIcon("delete.svg"));
            setBackground(Color.white);
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
            button.setIcon(new FlatSVGIcon("delete.svg"));
            button.setForeground(Color.white);
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