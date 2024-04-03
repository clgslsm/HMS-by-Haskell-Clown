import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReceptionistUI extends JFrame {
    ReceptionistUI(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("ABC Hospital @Receptionist");
        this.getContentPane().setBackground(new Color(0xF1F8FF));
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.add(new ReceptionistUIContainer());
        this.pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReceptionistUI::new);
    }
}

class ReceptionistUIContainer extends JPanel {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    CardLayout containerLayout = new CardLayout();
    JPanel navContainer = NavigationContainer();
    JPanel mainPageContainer = MainPageContainer();
    ReceptionistUIContainer(){
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(screenSize.width, screenSize.height));


        this.add(navContainer, BorderLayout.WEST);
        this.add(mainPageContainer);
    }
    private JPanel NavigationContainer() {
        JPanel navigationContainer = new JPanel();
        navigationContainer.setPreferredSize(new Dimension(screenSize.width * 4261 / 27320, screenSize.height));
        navigationContainer.setLayout(new BoxLayout(navigationContainer, BoxLayout.Y_AXIS));
        navigationContainer.setBackground(Color.WHITE);

        // Thêm nội dung vào control panel
        JLabel label = new JLabel("ABC HOSPITAL");
        label.setIcon(new ImageIcon(new ImageIcon("src/img/logo.jpg").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
        label.setFont(new Font("Ubuntu", Font.BOLD, 20));
        label.setVerticalTextPosition(JLabel.CENTER);
        label.setForeground(new Color(0x3497F9));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(new EmptyBorder(40, 0, 40, 0));

        // Thêm các điều hướng
        NavButton patientSection = new NavButton("Patients");
        NavButton doctorSection = new NavButton("Doctors");
        NavButton appointmentSection = new NavButton("Appointments");
        patientSection.setSelected(true);
        doctorSection.setSelected(false);
        appointmentSection.setSelected(false);

        patientSection.addActionListener(e -> {
            System.out.println("Lua chon 1");
            patientSection.setSelected(true);
            doctorSection.setSelected(false);
            appointmentSection.setSelected(false);
            containerLayout.show(mainPageContainer,"patient-panel");
        });

        doctorSection.addActionListener(e -> {
            System.out.println("Lua chon 2");
            patientSection.setSelected(false);
            doctorSection.setSelected(true);
            appointmentSection.setSelected(false);
//            containerLayout.show(this, "2");
        });

        appointmentSection.addActionListener(e -> {
            System.out.println("Lua chon 3");
            patientSection.setSelected(false);
            doctorSection.setSelected(false);
            appointmentSection.setSelected(true);
            containerLayout.show(mainPageContainer,"appointment-panel");
        });

        navigationContainer.add(label);

        JPanel cPanel = new JPanel();
        cPanel.setSize(new Dimension(this.getWidth(), this.getHeight() * 257/384));
        cPanel.setBackground(Color.WHITE);
        cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));

        cPanel.add(patientSection);
        cPanel.add(Box.createVerticalStrut(10));

        cPanel.add(Box.createVerticalStrut(10));

        cPanel.add(appointmentSection);
        cPanel.add(Box.createVerticalStrut(10));

        navigationContainer.add(cPanel);
        return navigationContainer;
    }
    private JPanel MainPageContainer() {
        JPanel container = new JPanel();
        container.setLayout(containerLayout);
        container.setPreferredSize(new Dimension(screenSize.width * 23059 / 27320, screenSize.height));

        JPanel patientPanel = new PatientPanel();
        JPanel appointmentPanel = new PageSection();


        container.add(patientPanel, "patient-panel");
        container.add(appointmentPanel, "appointment-panel");

        containerLayout.show(container, "patient-panel");

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

class PageSection extends JPanel {
    PageSection(){
        setBackground(new Color(0xF1F8FF));
    }
}

