package com.javaswing;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.javafirebasetest.dao.StaffDAO;
import com.javafirebasetest.entity.Staff;
import com.javafirebasetest.entity.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.concurrent.ExecutionException;

public class MainPage extends JFrame {
    MainPage(User user) throws ExecutionException, InterruptedException {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new FlatSVGIcon("logo.svg").getImage());
        getRootPane().putClientProperty("JRootPane.titleBarBackground",Constants.DARK_MODE_2);
        getRootPane().putClientProperty("JRootPane.titleBarForeground",Color.white);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setTitle("ABC Hospital @%s".formatted(user.getUsername()));
        this.getContentPane().setBackground(Constants.LIGHT_BLUE);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.add(new MainPageUIContainer(user,this));
        this.pack();
    }
}
class MainPageUIContainer extends JPanel {
    User user;
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    // Get the screen size
    Dimension screenSize = toolkit.getScreenSize();
    // Get the screen insets (including taskbar and other system decorations)
    Insets screenInsets = toolkit.getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());

    // Calculate the usable screen bounds (excluding taskbar)
    int usableScreenWidth = screenSize.width - screenInsets.left - screenInsets.right;
    int usableScreenHeight = screenSize.height - screenInsets.top - screenInsets.bottom;
    CardLayout containerLayout = new CardLayout();
    JPanel navContainer;
    JPanel mainPageContainer;
    MainPageUIContainer(User u, MainPage mainPage) throws ExecutionException, InterruptedException {
        this.user=u;
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(usableScreenWidth, usableScreenHeight));


        navContainer = NavigationContainer(mainPage);
        mainPageContainer  = MainPageContainer();
        this.add(navContainer, BorderLayout.WEST);
        this.add(mainPageContainer);
    }
    private JPanel StaffAccountContainer(){
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        Staff st = StaffDAO.getStaffById(user.getStaffId());

        JLabel userLabel = new JLabel(st.getName());
        userLabel.setFont(new Font(FlatInterFont.FAMILY,Font.BOLD,20));
        userLabel.setForeground(Color.WHITE);
        userLabel.setIcon(new FlatSVGIcon("user.svg",25,25));

        JLabel staffIdLabel = new JLabel(STR."Staff ID: \{st.getStaffId()}");
        staffIdLabel.setFont(new Font(FlatInterFont.FAMILY,Font.PLAIN,13));
        staffIdLabel.setForeground(Color.WHITE);

        JLabel userModeLabel = new JLabel(STR."\{user.getUserMode()}");
        userModeLabel.setFont(new Font(FlatInterFont.FAMILY,Font.BOLD,13));
        userModeLabel.setForeground(Color.yellow);

        panel.add(userLabel);
        panel.add(userModeLabel);
        panel.add(staffIdLabel);
        return panel;
    }
    private JPanel NavigationContainer(MainPage mainPage) {
        JPanel navigationContainer = new JPanel();
        navigationContainer.setPreferredSize(new Dimension(usableScreenWidth * 4275 / 27320, usableScreenHeight));
        navigationContainer.setLayout(new BoxLayout(navigationContainer, BoxLayout.Y_AXIS));
        navigationContainer.setBackground(Constants.DARK_MODE_1);
        // Thêm nội dung vào control panel
        JLabel label = new JLabel("ABC HOSPITAL");
        label.setIcon(new FlatSVGIcon("logo.svg",50,50));
        label.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 25));
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setForeground(Constants.BLUE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(new EmptyBorder(40, 20, 40, 20));

        // Thêm các điều hướng
//        NavButton defaultSection = new NavButton("Staff information");
        NavButton patientSection = new NavButton("Patients");
        NavButton staffSection = new NavButton("Staffs");
        NavButton medicineSection = new NavButton("Medicine");
        NavButton machineSection = new NavButton("Available Machine");
        NavButton unusableMachineSection = new NavButton("Unusable Machine");
        NavButton exportMedicineSection = new NavButton("Export Medicine");
        NavButton medrecSection = new NavButton("Medical Records");

//        defaultSection.setSelected(true);
        patientSection.setSelected(false);
        staffSection.setSelected(false);
        medicineSection.setSelected(false);
        machineSection.setSelected(false);
        exportMedicineSection.setSelected(false);

//        defaultSection.addActionListener(_->{
//            defaultSection.setSelected(true);
//            patientSection.setSelected(false);
//            staffSection.setSelected(false);
//            medicineSection.setSelected(false);
//            machineSection.setSelected(false);
//            exportMedicineSection.setSelected(false);
//            medrecSection.setSelected(false);
//            unusableMachineSection.setSelected(false);
//            containerLayout.show(mainPageContainer,"staff-default-panel");
//        });

        patientSection.addActionListener(e -> {
//            defaultSection.setSelected(false);
            patientSection.setSelected(true);
            staffSection.setSelected(false);
            medicineSection.setSelected(false);
            machineSection.setSelected(false);
            exportMedicineSection.setSelected(false);
            medrecSection.setSelected(false);
            unusableMachineSection.setSelected(false);
            containerLayout.show(mainPageContainer,"patient-panel");
        });

        staffSection.addActionListener(e -> {
//            defaultSection.setSelected(false);
            patientSection.setSelected(false);
            staffSection.setSelected(true);
            medicineSection.setSelected(false);
            machineSection.setSelected(false);
            exportMedicineSection.setSelected(false);
            medrecSection.setSelected(false);
            unusableMachineSection.setSelected(false);
            containerLayout.show(mainPageContainer, "staff-panel");
        });

        medicineSection.addActionListener(e -> {
//            defaultSection.setSelected(false);
            patientSection.setSelected(false);
            staffSection.setSelected(false);
            medicineSection.setSelected(true);
            machineSection.setSelected(false);
            exportMedicineSection.setSelected(false);
            medrecSection.setSelected(false);
            unusableMachineSection.setSelected(false);
            containerLayout.show(mainPageContainer,"medicine-panel");
        });

        machineSection.addActionListener(e -> {
//            defaultSection.setSelected(false);
            patientSection.setSelected(false);
            staffSection.setSelected(false);
            medicineSection.setSelected(false);
            machineSection.setSelected(true);
            exportMedicineSection.setSelected(false);
            medrecSection.setSelected(false);
            unusableMachineSection.setSelected(false);
            containerLayout.show(mainPageContainer,"machine-panel");
        });

        exportMedicineSection.addActionListener(_->{
//            defaultSection.setSelected(false);
            patientSection.setSelected(false);
            staffSection.setSelected(false);
            medicineSection.setSelected(false);
            machineSection.setSelected(false);
            exportMedicineSection.setSelected(true);
            medrecSection.setSelected(false);
            unusableMachineSection.setSelected(false);
            containerLayout.show(mainPageContainer,"export-medicine-panel");
        });

        medrecSection.addActionListener(_->{
//            defaultSection.setSelected(false);
            patientSection.setSelected(false);
            staffSection.setSelected(false);
            medicineSection.setSelected(false);
            machineSection.setSelected(false);
            exportMedicineSection.setSelected(false);
            medrecSection.setSelected(true);
            unusableMachineSection.setSelected(false);
            containerLayout.show(mainPageContainer,"medrec-panel");
        });

        unusableMachineSection.addActionListener(_->{
//            defaultSection.setSelected(false);
            patientSection.setSelected(false);
            staffSection.setSelected(false);
            medicineSection.setSelected(false);
            machineSection.setSelected(false);
            exportMedicineSection.setSelected(false);
            medrecSection.setSelected(false);
            unusableMachineSection.setSelected(true);
            containerLayout.show(mainPageContainer,"unusable-machine-panel");
        });

        navigationContainer.add(label);

        JPanel cPanel = new JPanel();
        cPanel.setSize(new Dimension(this.getWidth(), this.getHeight() * 257/384));
        cPanel.setBackground(Constants.DARK_MODE_1);
        cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));

//        cPanel.add(defaultSection);
        cPanel.add(Box.createVerticalStrut(10));

        if (user != null && user.getUserMode().getValue().equals("Doctor")) {
            patientSection.setSelected(true);
            cPanel.add(patientSection);
            cPanel.add(Box.createVerticalStrut(10));
        }
        else if (user != null && user.getUserMode().getValue().equals("Admin")) {
            staffSection.setSelected(true);
            cPanel.add(staffSection);
            cPanel.add(Box.createVerticalStrut(10));
        }
        else if (user != null && user.getUserMode().getValue().equals("Receptionist")) {
            patientSection.setSelected(true);
            cPanel.add(patientSection);
            cPanel.add(Box.createVerticalStrut(10));
        }
        else if (user != null && user.getUserMode().getValue().equals("Technician")) {
            medrecSection.setSelected(true);
            cPanel.add(medrecSection);
            cPanel.add(Box.createVerticalStrut(10));
            cPanel.add(machineSection);
            cPanel.add(Box.createVerticalStrut(10));
            cPanel.add(unusableMachineSection);
            cPanel.add(Box.createVerticalStrut(10));
        }
        else if (user != null && user.getUserMode().getValue().equals("Pharmacist")) {
            exportMedicineSection.setSelected(true);
            cPanel.add(medicineSection);
            cPanel.add(Box.createVerticalStrut(10));
            cPanel.add(exportMedicineSection);
            cPanel.add(Box.createVerticalStrut(10));
        }

        JPanel logoutBox = new JPanel();
        logoutBox.setOpaque(false);
        logoutBox.setLayout(new BoxLayout(logoutBox,BoxLayout.X_AXIS));
        logoutBox.setSize(200,70);
        JButton logoutBtn = new JButton("Log Out");
        logoutBtn.setFont(new Font(FlatInterFont.FAMILY,Font.BOLD,15));
        logoutBtn.setOpaque(false);
        logoutBtn.setBackground(Constants.DARK_MODE_1);
        logoutBtn.setAlignmentX(LEFT_ALIGNMENT);
        logoutBtn.setForeground(Color.white);
        logoutBtn.setBackground(Constants.RED);
        logoutBtn.setMinimumSize(new Dimension(125,50));
        logoutBtn.addActionListener(_->{
            mainPage.setVisible(false);
            mainPage.dispose();
            LoginPage login = new LoginPage();
        });
        logoutBox.add(logoutBtn);


        JPanel StaffBox = new JPanel();
        StaffBox.setMaximumSize(new Dimension(this.getWidth(), this.getHeight() * 45/384));
        StaffBox.setOpaque(false);
        JPanel staff = StaffAccountContainer();
        staff.setAlignmentX(LEFT_ALIGNMENT);
        StaffBox.add(staff);

        navigationContainer.add(StaffBox);
        navigationContainer.add(logoutBox);
        navigationContainer.add(Box.createVerticalStrut(20));
        navigationContainer.add(cPanel);
        navigationContainer.add(Box.createVerticalStrut(150));
        return navigationContainer;
    }
    private JPanel MainPageContainer() throws ExecutionException, InterruptedException {
        JPanel container = new JPanel();
        container.setLayout(containerLayout);
        container.setPreferredSize(new Dimension(usableScreenWidth * 23059 / 27320, usableScreenHeight));

        JPanel defaultPanel = new DefaultPanel(user);
//        container.add(defaultPanel, "staff-default-panel");

        if (user != null && user.getUserMode().getValue().equals("Doctor")) {
            JPanel patientPanel = new PatientPanel(user.getStaffId());
            container.add(patientPanel, "patient-panel");
            containerLayout.show(container, "patient-panel");
        }
        else if (user != null && user.getUserMode().getValue().equals("Admin")) {
            JPanel staffPanel = new StaffPanel();
            container.add(staffPanel, "staff-panel");
            containerLayout.show(container, "staff-panel");
        }
        else if (user != null && user.getUserMode().getValue().equals("Receptionist")) {
            JPanel patientPanel = new PatientPanel(user.getStaffId());
            container.add(patientPanel, "patient-panel");
            containerLayout.show(container, "patient-panel");
        }
        else if (user != null && user.getUserMode().getValue().equals("Technician")) {
            JPanel medrecPanel = new MedicalRecordPanel(user);
            JPanel machinePanel = new MachinePanel();
            JPanel unusableMachinePanel = new UnusableMachinePanel();
            container.add(machinePanel, "machine-panel");
            container.add(unusableMachinePanel,"unusable-machine-panel");
            container.add(medrecPanel, "medrec-panel");
            containerLayout.show(container, "medrec-panel");
        }
        else if (user != null && user.getUserMode().getValue().equals("Pharmacist")) {
            JPanel medicinePanel = new MedicinePanel();
            JPanel exportMedicinePanel = new ExportMedicinePanel(user);
            container.add(medicinePanel, "medicine-panel");
            container.add(exportMedicinePanel,"export-medicine-panel");
            containerLayout.show(container, "export-medicine-panel");
        }

        return container;
    }
}
class NavButton extends JButton {
    public NavButton(String text) {
        super(text);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setForeground(Color.WHITE);
        setBackground(Constants.DARK_MODE_2);
        setFocusPainted(false);
        setContentAreaFilled(true);
        setOpaque(true);
        setFocusable(false);
        setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 17));
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
            setBackground(Color.BLACK);
            setForeground(Constants.BLUE);
            setBorderPainted(true);
            Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 5, 0, 0, Constants.BLUE), // Tạo viền màu xanh chỉ ở phía trái
                    BorderFactory.createEmptyBorder(0, 5, 0, 0) // Tạo khoảng cách trống phía bên trái viền
            );
            setBorder(border);
        } else {
            setBackground(Constants.DARK_MODE_1);
            setForeground(Color.white);
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
        setBackground(Constants.BLUE);
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
        this.setFont(Constants.commonUsed);
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