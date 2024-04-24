package com.javaswing;

import com.javafirebasetest.dao.UserDAO;
import com.javafirebasetest.entity.Staff;
import com.javafirebasetest.entity.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.RoundRectangle2D;

public class MainPage extends JFrame {
    MainPage(User user){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("ABC Hospital @%s".formatted(user.getUsername()));
        this.getContentPane().setBackground(new Color(0xF1F8FF));
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.add(new MainPageUIContainer(user,this));
        this.pack();
    }
}
class MainPageUIContainer extends JPanel {
    User user;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    CardLayout containerLayout = new CardLayout();
    JPanel navContainer;
    JPanel mainPageContainer;
    MainPageUIContainer(User u, MainPage mainPage){
        user = u;

        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(screenSize.width, screenSize.height));

        navContainer = NavigationContainer(mainPage);
        mainPageContainer  = MainPageContainer();
        this.add(navContainer, BorderLayout.WEST);
        this.add(mainPageContainer);
    }
    private JPanel NavigationContainer(MainPage mainPage) {
        JPanel navigationContainer = new JPanel();
        navigationContainer.setPreferredSize(new Dimension(screenSize.width * 4261 / 27320, screenSize.height));
        navigationContainer.setLayout(new BoxLayout(navigationContainer, BoxLayout.Y_AXIS));
        navigationContainer.setBackground(Color.WHITE);
        // Thêm nội dung vào control panel
        JLabel label = new JLabel("ABC HOSPITAL");
        label.setIcon(new ImageIcon(new ImageIcon("src/main/java/com/javaswing/img/logo.jpg").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
        label.setFont(new Font("Ubuntu", Font.BOLD, 25));
        label.setVerticalTextPosition(JLabel.CENTER);
        label.setForeground(new Color(0x3497F9));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(new EmptyBorder(40, 0, 40, 0));

        // Thêm các điều hướng
        NavButton defaultSection = new NavButton("Staff information");
        NavButton patientSection = new NavButton("Patients");
        NavButton staffSection = new NavButton("Staffs");
        NavButton medicineSection = new NavButton("Medicine");
        NavButton machineSection = new NavButton("Machine");
        NavButton exportMedicineSection = new NavButton("Export Medicine");

        defaultSection.setSelected(true);
        patientSection.setSelected(false);
        staffSection.setSelected(false);
        medicineSection.setSelected(false);
        machineSection.setSelected(false);
        exportMedicineSection.setSelected(false);

        defaultSection.addActionListener(_->{
            defaultSection.setSelected(true);
            patientSection.setSelected(false);
            staffSection.setSelected(false);
            medicineSection.setSelected(false);
            machineSection.setSelected(false);
            exportMedicineSection.setSelected(false);
            containerLayout.show(mainPageContainer,"staff-default-panel");
        });

        patientSection.addActionListener(e -> {
            defaultSection.setSelected(false);
            patientSection.setSelected(true);
            staffSection.setSelected(false);
            medicineSection.setSelected(false);
            machineSection.setSelected(false);
            exportMedicineSection.setSelected(false);
            containerLayout.show(mainPageContainer,"patient-panel");
        });

        staffSection.addActionListener(e -> {
            defaultSection.setSelected(false);
            patientSection.setSelected(false);
            staffSection.setSelected(true);
            medicineSection.setSelected(false);
            machineSection.setSelected(false);
            exportMedicineSection.setSelected(false);
            containerLayout.show(mainPageContainer, "staff-panel");
        });

        medicineSection.addActionListener(e -> {
            defaultSection.setSelected(false);
            patientSection.setSelected(false);
            staffSection.setSelected(false);
            medicineSection.setSelected(true);
            machineSection.setSelected(false);
            exportMedicineSection.setSelected(false);
            containerLayout.show(mainPageContainer,"medicine-panel");
        });

        machineSection.addActionListener(e -> {
            defaultSection.setSelected(false);
            patientSection.setSelected(false);
            staffSection.setSelected(false);
            medicineSection.setSelected(false);
            machineSection.setSelected(true);
            exportMedicineSection.setSelected(false);
            containerLayout.show(mainPageContainer,"machine-panel");
        });

        exportMedicineSection.addActionListener(_->{
            defaultSection.setSelected(false);
            patientSection.setSelected(false);
            staffSection.setSelected(false);
            medicineSection.setSelected(false);
            machineSection.setSelected(false);
            exportMedicineSection.setSelected(true);
            containerLayout.show(mainPageContainer,"export-medicine-panel");
        });

        navigationContainer.add(label);

        JPanel cPanel = new JPanel();
        cPanel.setSize(new Dimension(this.getWidth(), this.getHeight() * 257/384));
        cPanel.setBackground(Color.WHITE);
        cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));

        cPanel.add(defaultSection);
        cPanel.add(Box.createVerticalStrut(10));

        if (user != null && user.getUserMode().getValue().equals("Doctor")) {
            cPanel.add(patientSection);
            cPanel.add(Box.createVerticalStrut(10));
        }
        else if (user != null && user.getUserMode().getValue().equals("Admin")) {
            cPanel.add(staffSection);
            cPanel.add(Box.createVerticalStrut(10));
        }
        else if (user != null && user.getUserMode().getValue().equals("Receptionist")) {
            cPanel.add(patientSection);
            cPanel.add(Box.createVerticalStrut(10));
        }
        else if (user != null && user.getUserMode().getValue().equals("Technician")) {
            cPanel.add(patientSection);
            cPanel.add(Box.createVerticalStrut(10));
            cPanel.add(machineSection);
            cPanel.add(Box.createVerticalStrut(10));
        }
        else if (user != null && user.getUserMode().getValue().equals("Pharmacist")) {
            cPanel.add(medicineSection);
            cPanel.add(Box.createVerticalStrut(10));
            cPanel.add(exportMedicineSection);
            cPanel.add(Box.createVerticalStrut(10));
        }

        JPanel logoutBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoutBox.setMaximumSize(new Dimension(200,50));
        logoutBox.setBorder(new EmptyBorder(0,20,0,20));
        logoutBox.setOpaque(false);
        JButton logoutBtn = new RoundedButton(" Logout ");
        logoutBtn.setOpaque(false);
        logoutBtn.setForeground(Color.red);
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setMinimumSize(new Dimension(125,50));
        logoutBtn.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(Color.red),
                new EmptyBorder(10,10,10,10)
        ));
        logoutBtn.addActionListener(_->{
            mainPage.setVisible(false);
            LoginPage login = new LoginPage();
        });
        logoutBox.add(logoutBtn);


        navigationContainer.add(cPanel);
        navigationContainer.add(Box.createVerticalStrut(200));
        navigationContainer.add(logoutBtn);

        return navigationContainer;
    }
    private JPanel MainPageContainer() {
        JPanel container = new JPanel();
        container.setLayout(containerLayout);
        container.setPreferredSize(new Dimension(screenSize.width * 23059 / 27320, screenSize.height));

        JPanel defaultPanel = new DefaultPanel(user);
        JPanel patientPanel = new PatientPanel(user.getStaffId());
        JPanel staffPanel = new StaffPanel();
        JPanel medicinePanel = new MedicinePanel();
        JPanel exportMedicinePanel = new ExportMedicinePanel(user);
        JPanel machinePanel = new MachinePanel();

        container.add(defaultPanel, "staff-default-panel");
        container.add(patientPanel, "patient-panel");
        container.add(staffPanel, "staff-panel");
        container.add(medicinePanel, "medicine-panel");
        container.add(exportMedicinePanel,"export-medicine-panel");
        container.add(machinePanel, "machine-panel");

        containerLayout.show(container, "staff-default-panel");

        return container;
    }
}
class NavButton extends JButton {
    public NavButton(String text) {
        super(text);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setForeground(new Color(0x7F8F98));
        setBackground(Color.WHITE);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);
        setFocusable(false);
        setFont(new Font("Verdana", Font.PLAIN, 16));
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, Color.white), // Tạo viền màu xanh chỉ ở phía trái
                BorderFactory.createEmptyBorder(0, 5, 0, 0) // Tạo khoảng cách trống phía bên trái viền
        );
        setBorder(border);
    }

    // Adjust button's background when selected
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            setBackground(new Color(0xE7F3FE));
            setForeground(new Color(0x3497F9));
            setBorderPainted(true);
            Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(0x3497F9)), // Tạo viền màu xanh chỉ ở phía trái
                    BorderFactory.createEmptyBorder(0, 5, 0, 0) // Tạo khoảng cách trống phía bên trái viền
            );
            setBorder(border);
        } else {
            setBackground(Color.WHITE); // Đổi màu nền về trắng khi không được chọn
            setForeground(new Color(0x7F8F98));
            setBorderPainted(false);
        }
    }
}
class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusable(false);
        setFont(new Font("Courier",Font.PLAIN,16));
        setForeground(Color.WHITE);
        setBackground(new Color(0x3497F9));
        setBounds(100, 100, 125, 60);
        setBorder(new EmptyBorder(10,10,10,10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.lightGray);
        }
        else {
            g.setColor(getBackground());
        }
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.fillRoundRect(0,0,getWidth(), getHeight(), 20, 20);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawRoundRect(0,0,getWidth()-1, getHeight()-1, 20, 20);
    }
}
class RoundedTextField extends JTextField {
    private static final long serialVersionUID = 1L;
    private int radius;

    public RoundedTextField(int columns, int radius) {
        super(columns);
        this.radius = radius;
        setFont(new Font("Courier",Font.PLAIN,16));
        setOpaque(false);
        setBorder(new LineBorder(Color.BLACK));
        setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5)); // Thiết lập padding
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Vẽ nền
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        // Vẽ viền sau
        ((Graphics2D) g).setStroke(new BasicStroke(0.5f)); // Thiết lập độ dày cho viền
        g.setColor(Color.BLACK);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        // Gọi phương thức paintComponent của lớp cha để vẽ nội dung JTextField
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Không làm gì ở đây để ngăn việc vẽ viền mặc định của JTextField
    }
}
class RoundedTextArea extends JTextArea {
    private int radius;
    private Color borderColor;

    public RoundedTextArea(int rows, int columns, int radius, Color borderColor) {
        super(rows, columns);
        this.setFont(new Font("Courier",Font.PLAIN,16));
        this.radius = radius;
        this.borderColor = borderColor;
        setOpaque(false); // Để hiển thị hình dạng bo góc

        // Loại bỏ viền của JTextArea
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setStroke(new BasicStroke(0.5f));
        // Vẽ hình chữ nhật bo góc
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
        // Vẽ viền bo góc
        g2.setColor(borderColor);
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
        g2.dispose();

        // Gọi phương thức paintComponent của lớp cha để vẽ nội dung JTextArea
        super.paintComponent(g);
    }
}