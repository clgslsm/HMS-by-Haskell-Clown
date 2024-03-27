import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame{
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    JPanel mainF = new JPanel();
    CardLayout cl = new CardLayout();
    DefaultTableModel tableModel;
    JTable table;
    Object[][] data = {
        {"1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3"},
        {"1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3uirvrn4gnionpwbruoni2cklm2bigunfomxp,vgbhujnkml,;", "Bobvguhbo3i2noibc4unovbunijjbnkmrvrhjnimkclecewijok3rcijopk32cpjuihjoc32px[plkcno3mxfp]", "Johnsce3nuoimx3ozbghnjemkzl,vbhujklvbhnjkmhj kxrctfvghbjfghjon", "1reciokmlx,;zwhjkl", "Johxrjnekln", "Doeenxjokml;,", "3zsexdfcgvhbjnzesxdfcgvhbasdfgsdxfcgv hdxfcgvxdcfgv"},
        {"1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3",
        "1", "John", "Doe", "1", "John", "Doe", "1"},
        {"2", "Jane", "Smith", "1", "John", "Doe", "2"},
        {"3", "Bob", "Johnson", "1", "John", "Doe", "3"}
    };
    // Tên các cột
    String[] columnNames = {"Tên khoa", "Tên bác sĩ", "Thời gian vào", "Thời gian ra", "Chẩn đoán", "Trạng thái", "Đánh giá dịch vụ"};
        
    JPanel patientPanel = new JPanel();
    JPanel doctorPanel = new JPanel();
    JPanel appointmentPanel = new JPanel();
    JPanel historyPanel = CreateHistoryPanel();
    JButton historyButton = new JButton("Lịch sử khám bệnh");


    MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("ABC HOSPITAL MANAGEMENT SYSTEM");
        setSize(screenSize.width, screenSize.height);
        getContentPane().setBackground(new Color(0xF1F8FF)); // Thiết lập màu nền cho frame là màu xanh

        mainF.setLayout(cl);

        JPanel controlPanel = CreateControlPanel();

        // Patient Panel
        patientPanel.setBackground(new Color(0xF1F8FF));
        patientPanel.add(historyButton);
        patientPanel.setLayout(new BoxLayout(patientPanel,BoxLayout.Y_AXIS));

        historyButton.addActionListener(e -> {
            System.out.println("Lịch sử");
            cl.show(mainF, "4");
        });
        patientPanel.add(new PatientPanel());

        doctorPanel.setBackground(Color.YELLOW);
        appointmentPanel.setBackground(Color.GREEN);
        historyPanel.setBackground(Color.PINK);

        mainF.add(patientPanel, "1");
        mainF.add(doctorPanel, "2");
        mainF.add(appointmentPanel, "3");
        mainF.add(historyPanel, "4");

        cl.show(mainF, "1");

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.WEST);

        add(mainF);

        this.setVisible(true);
    }

    private JPanel CreateHistoryPanel() {
        JPanel hPanel = new JPanel();
        tableModel = new DefaultTableModel(data, columnNames);
        //hPanel.setBackground(new Color(0xE7F3FE));

        JLabel headerLabel = new JLabel(" Lịch sử khám bệnh");
        headerLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        headerLabel.setForeground(new Color(0x374858));
        headerLabel.setBounds(200, 40, 300, 50);

        JButton returnButton = new JButton();
        returnButton.setText("Quay về");
        returnButton.setForeground(Color.WHITE);
        returnButton.setBackground(new Color(0x3497F9));
        returnButton.setFont(new Font("Courier New", Font.PLAIN, 18));
        returnButton.setFocusable(false);
        returnButton.setBorderPainted(false);
        returnButton.setBounds(50, 40, 130, 50);

        returnButton.addActionListener(e -> {
            System.out.println("back");
            cl.show(mainF, "1");
        });

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0xE7F3FE));

        headerPanel.setLayout(null);
        headerPanel.add(returnButton);
        headerPanel.add(headerLabel);

        JLabel nameLabel = new JLabel("Tên khoa");
        nameLabel.setBounds(50, 10, 100, 30);
        nameLabel.setFont(new Font("Courier New", Font.PLAIN, 18));
        JTextField nameInput = new JTextField();
        nameInput.setBounds(180, 10, 400, 30);

        JButton submitButton = new JButton();
        submitButton.setText("Thêm lịch hẹn mới");
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(new Color(0x3497F9));
        submitButton.setFont(new Font("Courier New", Font.PLAIN, 18));
        submitButton.setFocusable(false);
        submitButton.setBorderPainted(false);
        submitButton.setBounds(600, 0, 250, 50);

        submitButton.addActionListener(e -> {
            System.out.println("them lich hen moi");
            String userInput = nameInput.getText();
            JOptionPane.showMessageDialog(mainF, "Dữ liệu bạn đã nhập là: " + userInput);
            Object[] newRow = {"2", "Jane", "Smith", "2", "Jane", "Smith", "2"};
            tableModel.addRow(newRow);
            table.setModel(tableModel);
        });

        JPanel form = new JPanel();
        form.setBackground(new Color(0xE7F3FE));
        form.setSize(100, 100);
        form.setLayout(null);
        form.add(nameLabel);
        form.add(nameInput);
        form.add(submitButton);
        form.setBorder(null);;

        JPanel tablePanel = new JPanel(new GridLayout(1,1));
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(0xE7F3FE), 20));
        tablePanel.setBackground(Color.WHITE);
        
        // Tạo JTable với dữ liệu và tên cột
        table = new JTable(tableModel);
        
        //table.setAutoscrolls(true);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        table.setShowGrid(false);
        table.setBackground(Color.white);
        table.setRowHeight(100);
        tablePanel.add(new JScrollPane(table));

        // Thêm các panel vào frame
        BoxLayout box = new BoxLayout(hPanel, BoxLayout.Y_AXIS);
        hPanel.setLayout(box);
        
        hPanel.add(headerPanel);
        hPanel.add(form);
        hPanel.add(tablePanel);
        
        return hPanel;
    }


    private JPanel CreateControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(200, this.getHeight())); // Đặt kích thước cho panel
        controlPanel.setBackground(Color.WHITE); // Đặt màu nền cho panel

        // Thêm nội dung vào control panel
        JLabel label = new JLabel("ABC HOSPITAL");
        label.setIcon(new ImageIcon("src/img/logo.jpg"));
        label.setFont(new Font("Ubuntu", Font.BOLD, 20));
        label.setVerticalTextPosition(JLabel.CENTER);
        label.setForeground(new Color(0x3497F9));
        label.setBorder(new EmptyBorder(50, 0, 100, 0));

        // Thêm các điều hướng
        CustomButton option1Button = new CustomButton("Bệnh nhân");
        CustomButton option2Button = new CustomButton("Bác sĩ");
        CustomButton option3Button = new CustomButton("Lịch hẹn");
        option1Button.setSelected(true);
        option2Button.setSelected(false);
        option3Button.setSelected(false);

        // Thêm action listener cho mỗi nút
        option1Button.addActionListener(e -> {
            System.out.println("Lua chon 1");
            option1Button.setSelected(true);
            option2Button.setSelected(false);
            option3Button.setSelected(false);
            cl.show(mainF, "1");
        });

        option2Button.addActionListener(e -> {
            System.out.println("Lua chon 2");
            option1Button.setSelected(false);
            option2Button.setSelected(true);
            option3Button.setSelected(false);
            cl.show(mainF, "2");
        });

        option3Button.addActionListener(e -> {
            System.out.println("Lua chon 3");
            option1Button.setSelected(false);
            option2Button.setSelected(false);
            option3Button.setSelected(true);
            cl.show(mainF, "3");
        });

        controlPanel.add(label);

        JPanel cPanel = new JPanel();
        cPanel.setPreferredSize(new Dimension(200, this.getHeight()));
        cPanel.setBackground(Color.WHITE);
        cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));

        cPanel.add(option1Button);
        option1Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        option1Button.setMaximumSize(new Dimension(Integer.MAX_VALUE, option1Button.getPreferredSize().height));
        cPanel.add(Box.createVerticalStrut(50));
        cPanel.add(option2Button);
        option2Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        option2Button.setMaximumSize(new Dimension(Integer.MAX_VALUE, option2Button.getPreferredSize().height));
        cPanel.add(Box.createVerticalStrut(50));
        cPanel.add(option3Button);
        option3Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        option3Button.setMaximumSize(new Dimension(Integer.MAX_VALUE, option3Button.getPreferredSize().height));
        cPanel.add(Box.createVerticalStrut(50));

        controlPanel.add(cPanel);
        return controlPanel;
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}

class CustomButton extends JButton {
    public CustomButton(String text) {
        super(text);
        setPreferredSize(new Dimension(200, 50)); // Thiết lập kích thước tùy chỉnh cho nút
        setForeground(new Color(0x7F8F98)); // Đặt màu chữ
        setBackground(Color.WHITE); // Đặt màu nền
        setFocusPainted(false); // Tắt viền xung quanh khi nút được chọn
        setContentAreaFilled(false); // Loại bỏ nền của nút
        setOpaque(true); // Cho phép vẽ nền của nút
        setFocusable(false);
        setFont(new Font("Noto Sans CJK HK", Font.PLAIN, 18));
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 10, 0, 0, Color.white), // Tạo viền màu xanh chỉ ở phía trái
                BorderFactory.createEmptyBorder(0, 10, 0, 0) // Tạo khoảng cách trống phía bên trái viền
        );
        setBorder(border);
    }

    // Override phương thức setSelected để điều chỉnh màu nền khi nút được chọn
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            setBackground(new Color(0xE7F3FE)); // 0xĐổi màu nền sang xanh khi được chọn
            setForeground(new Color(0x3497F9));
            setBorderPainted(true);
            Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 10, 0, 0, new Color(0x3497F9)), // Tạo viền màu xanh chỉ ở phía trái
                    BorderFactory.createEmptyBorder(0, 10, 0, 0) // Tạo khoảng cách trống phía bên trái viền
            );
            setBorder(border);
        } else {
            setBackground(Color.WHITE); // Đổi màu nền về trắng khi không được chọn
            setForeground(new Color(0x7F8F98));
            setBorderPainted(false);
        }
    }
}


