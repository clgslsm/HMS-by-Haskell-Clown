import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SmallTableExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Small Table Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tạo DefaultTableModel với dữ liệu và tiêu đề cột mẫu
        DefaultTableModel model = new DefaultTableModel(new Object[][]{{"1", "One"}, {"2", "Two"}, {"3", "Three"}}, new Object[]{"ID", "Name"});

        // Tạo JTable với DefaultTableModel
        JTable table = new JTable(model);

        // Thiết lập kích thước cho table
        table.setPreferredScrollableViewportSize(new Dimension(400, 400));

        // Tạo JScrollPane và thêm table vào đó
        JScrollPane scrollPane = new JScrollPane(table);

        // Đặt vị trí của JScrollPane ở góc phải của frame
        frame.add(scrollPane, BorderLayout.EAST);

        // Thiết lập kích thước cho frame và hiển thị frame
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
