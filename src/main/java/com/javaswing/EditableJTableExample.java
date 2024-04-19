import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditableJTableExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Editable JTable Example");

        // Sample data for the table
        Object[][] data = {
                {"1", "John", "Doe"},
                {"2", "Jane", "Smith"},
                {"3", "Bob", "Johnson"}
        };

        // Column names
        String[] columnNames = {"ID", "First Name", "Last Name"};

        // Create a default table model
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells editable
                return true;
            }
        };

        // Create a JTable with the default table model
        JTable table = new JTable(model);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the frame
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add a cell editor listener to the table
        table.getDefaultEditor(Object.class).addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                saveData(table);
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                // Do nothing when editing is canceled
            }
        });

        // Add a window listener to handle saving data when the window closes
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveData(table);
            }
        });

        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    // Method to save data from the JTable
    private static void saveData(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();
        int colCount = model.getColumnCount();
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                Object value = model.getValueAt(row, col);
                // Here you can write the code to save the data to your database or file
                System.out.print(value + "\t");
            }
            System.out.println();
        }
        // Here you can add code to save data to your database or file
    }
}
