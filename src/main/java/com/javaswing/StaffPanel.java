package com.javaswing;
import com.javafirebasetest.dao.DoctorDAO;
import com.javafirebasetest.dao.PatientDAO;
import com.javafirebasetest.dao.UserDAO;
import com.javafirebasetest.entity.*;

import javax.print.Doc;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.javafirebasetest.dao.StaffDAO;

class StaffPanel extends JPanel {
    public StaffDefaultPage defaultPage;
    StaffPanel() {
        CardLayout currentPage = new CardLayout();
        this.setLayout(currentPage);
        this.setBackground(Color.white);

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
                    "User name: ", usernameField,
                    "Password: ", passField
            };

            int option = JOptionPane.showConfirmDialog(null, message, "", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String j = Objects.requireNonNull(jo.getSelectedItem()).toString();
                String d = Objects.requireNonNull(dep.getSelectedItem()).toString();

                String name = nameField.getText();
                String username = usernameField.getText();
                String pass = passField.getText();

                // Kiểm tra xem có ô nào bị bỏ trống không
                if (j.isEmpty() || d.isEmpty() || name.isEmpty() || username.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "The information box cannot be left blank!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Job: " + j + "\nDepartment: " + d + "\nName: " + name, "Information", JOptionPane.INFORMATION_MESSAGE);
                    if (j !=  "Doctor") {
                        Staff newStaff = new Staff(null, name, User.Mode.fromValue(j));
                        StaffDAO.addStaff(newStaff);
                        UserDAO.addUser(new User(null, username, pass, User.Mode.fromValue(j), newStaff.getStaffId()));
                    }
                    else {
                        Doctor newDoctor = new Doctor(null, name, DeptType.fromValue(d));
                        DoctorDAO.addDoctor(newDoctor);
                        UserDAO.addUser(new User(null, username, pass, User.Mode.fromValue(j), newDoctor.getStaffId()));
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
    StaffDefaultPage() {
        this.setMaximumSize(new Dimension(1300,1000));
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 40));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Header container
        JPanel header = new JPanel();
        JLabel title = new JLabel("Staff Information");
        title.setFont(title.getFont().deriveFont(25F));
        title.setForeground(new Color(0x3497F9));
        header.setBackground(new Color(0xF1F8FF));
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));


        header.add(title);
        header.add(Box.createHorizontalGlue());
        header.add(searchEngine);
        header.add(Box.createHorizontalGlue());
        header.add(addStaffBtn);

        searchEngine.searchButton.addActionListener(_-> {
            try {
                showSearchResult(searchEngine.searchInput.getText());
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

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

        staffList.getTableHeader().setPreferredSize(new Dimension(staffList.getTableHeader().getWidth(), 60));
        staffList.getTableHeader().setFont(new Font("Courier", Font.BOLD, 16));
        staffList.getTableHeader().setOpaque(false);
        staffList.getTableHeader().setBackground(new Color(32, 136, 203));
        staffList.getTableHeader().setForeground(new Color(255,255,255));

        staffList.setFocusable(false);
        staffList.setIntercellSpacing(new java.awt.Dimension(0, 0));
        staffList.setSelectionBackground(new Color(0x9ACEF5));
        staffList.setShowVerticalLines(false);
        staffList.getTableHeader().setReorderingAllowed(false);
        staffList.setFont(new Font("Courier",Font.PLAIN,16));

        ButtonRenderer buttonRenderer = new ButtonRenderer();
        ButtonEditor buttonEditor = new ButtonEditor();
        staffList.getColumn("Action").setCellRenderer(buttonRenderer);
        staffList.getColumn("Action").setCellEditor(buttonEditor);
        staffList.setRowHeight(60);

        staffList.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = staffList.getSelectedRow();
            buttonRenderer.setSelectedRow(selectedRow);
            //buttonEditor.setID(staffList.getValueAt(selectedRow, 0).toString());
            staffList.repaint();
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(staffList);
        body.add(scrollPane);

        this.add(header);
        JPanel space = new JPanel();
        space.setBackground(new Color(0xF1F8FF));
        space.setSize(new Dimension(100, 100));
        this.add(space);
        this.add(body);
    }
    static class SearchEngine extends JPanel {
        JTextField searchInput = SearchBox();
        JButton searchButton = SearchButton();
        SearchEngine(){
            setBackground(new Color(0xF1F8FF));
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
            field.setFont(new Font("Courier",Font.PLAIN,16));
            field.setText("Search by staff ID");
            field.addMouseListener(new CustomMouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (field.getText().equals("Search by staff ID") || field.getText().equals("No staff found")) {
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
                        field.setText("Search by staff ID");
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
    void addStaffToTable (Staff staff){
        String job = staff.getUserMode().getValue();
        String department = "";
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        if (staff.getUserMode().getValue() == "Doctor") {
            department = (DoctorDAO.getDoctorById(staff.getStaffId())).getDepartment().getValue();
        }

        Object[] rowData = new Object[]{staff.getStaffId(), staff.getName(),job, department, buttonRenderer};
        model.addRow(rowData);
    }
    public void showSearchResult(String ID) throws ExecutionException, InterruptedException {
        if (!ID.trim().isEmpty() && !ID.trim().equals("Search by Staff ID")){
            try{
                Staff res = StaffDAO.getStaffById(ID);
                model.clearData();
                addStaffToTable(res);}
            catch (Exception e) {
                updateTableUI();
                searchEngine.searchInput.setText("No staff found");
                searchEngine.searchInput.setForeground(Color.red);
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
    }

    static class CustomTableModel extends AbstractTableModel {
        // Data for each column
        private Object[][] data = {};

        // Column names
        private final String[] columnNames = {"ID","Name", "Job", "Department", "Action"};

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
        JButton addStaffButton = new RoundedButton("  + Add Staff  ");
        addStaffButton.setFont(new Font("Courier",Font.PLAIN,13));
        addStaffButton.setFocusable(false);
        addStaffButton.setForeground(Color.WHITE);
        addStaffButton.setBackground(new Color(0x3497F9));
        addStaffButton.setBounds(100, 100, 125, 60);
        addStaffButton.setBorder(new EmptyBorder(10,10,10,10));

        return addStaffButton;
    }

    static class ButtonRenderer extends DefaultTableCellRenderer {
        private JPanel panel;
        private RoundedButton viewButton;
        private RoundedButton deleteButton;
        private int selectedRow = -1;


        public ButtonRenderer() {
            panel = new JPanel();
            viewButton = new RoundedButton("View");
            viewButton.setBackground(new Color(0xB5ED57));
            deleteButton = new RoundedButton("Delete");
            deleteButton.setBackground(new Color(0xFF9AA2));
            panel.setBackground(new Color(0, 0, 0, 0));

            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(Box.createHorizontalStrut(40));
            panel.add(viewButton);
            panel.add(Box.createHorizontalStrut(20));
            panel.add(deleteButton);
            panel.add(Box.createHorizontalStrut(20));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            if (isSelected && row == selectedRow) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            return panel;
        }

        public void setSelectedRow(int selectedRow) {
            this.selectedRow = selectedRow;
        }

    }

    static class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private RoundedButton viewButton;
        private RoundedButton deleteButton;
        private String id;

        public void setID(String id) {
            this.id = id;
        }

        public ButtonEditor() {
            super(new JCheckBox());

            panel = new JPanel();
            viewButton = new RoundedButton("View");
            viewButton.setBackground(new Color(0xB5ED57));
            deleteButton = new RoundedButton("Delete");
            deleteButton.setBackground(new Color(0xFF9AA2));
            panel.setBackground(new Color(0x9ACEF5));

            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

            //viewButton.addActionListener(e -> fireEditingStopped());
            //deleteButton.addActionListener(e -> fireEditingStopped());
            viewButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
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
                    // Xử lý sự kiện khi nhấn nút "View"
                    System.out.println("View button clicked in row " + id);
                    Staff st = StaffDAO.getStaffById(id);

                    jo.setSelectedItem(st.getUserMode().getValue());
                    jo.setEnabled(false);
                    if (st.getUserMode().getValue() == "Doctor") {
                        dep.setSelectedItem(DoctorDAO.getDoctorById(st.getStaffId()).getDepartment().getValue());
                    }
                    nameField.setText(st.getName());

                    Object[] message = {
                            "Name of Job: ", jo,
                            "Name of Department: ", dep,
                            "Name: ", nameField
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, "", JOptionPane.OK_CANCEL_OPTION);

                    if (option == JOptionPane.OK_OPTION) {
                        String j = Objects.requireNonNull(jo.getSelectedItem()).toString();
                        String d = Objects.requireNonNull(dep.getSelectedItem()).toString();

                        String name = nameField.getText();

                        // Kiểm tra xem có ô nào bị bỏ trống không
                        if (j.isEmpty() || d.isEmpty() || name.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "The information cannot be left blank!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Job: " + j + "\nDepartment: " + d + "\nName: " + name, "Information", JOptionPane.INFORMATION_MESSAGE);
                            if (j !=  "Doctor") {
                                StaffDAO.updateStaff(st.getStaffId(),
                                        "name", name,
                                        "userMode", User.Mode.fromValue(j));
                            }
                            else {
                                DoctorDAO.updateDoctor(st.getStaffId(),
                                        "name", name,
                                        "department", DeptType.fromValue(d));
                                StaffDAO.updateStaff(st.getStaffId(),
                                        "name", name);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Cancel", "Notification", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(Box.createHorizontalStrut(40));
            panel.add(viewButton);
            panel.add(Box.createHorizontalStrut(20));
            panel.add(deleteButton);
            panel.add(Box.createHorizontalStrut(20));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "View/Delete";
        }
    }

}