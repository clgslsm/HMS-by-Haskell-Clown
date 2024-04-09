package com.javaswing;

import com.javafirebasetest.entity.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainPage extends JFrame {
    MainPage(String role){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("ABC Hospital @Receptionist");
        this.getContentPane().setBackground(new Color(0xF1F8FF));
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.add(new MainPageUIContainer(role));
        this.pack();
    }
}

class MainPageUIContainer extends JPanel {
    String role;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    CardLayout containerLayout = new CardLayout();
    JPanel navContainer;
    JPanel mainPageContainer;
    MainPageUIContainer(String user){
        role = user;

        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(screenSize.width, screenSize.height));

        navContainer = NavigationContainer();
        mainPageContainer  = MainPageContainer();
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
        label.setIcon(new ImageIcon(new ImageIcon("src/main/java/com/javaswing/img/logo.jpg").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
        label.setFont(new Font("Ubuntu", Font.BOLD, 20));
        label.setVerticalTextPosition(JLabel.CENTER);
        label.setForeground(new Color(0x3497F9));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(new EmptyBorder(40, 0, 40, 0));

        // Thêm các điều hướng
        NavButton patientSection = new NavButton("Patients");
        NavButton doctorSection = new NavButton("Doctors");
        NavButton medicineSection = new NavButton("Medicine");
        NavButton machineSection = new NavButton("Machine");

        patientSection.setSelected(true);
        doctorSection.setSelected(false);
        medicineSection.setSelected(false);
        machineSection.setSelected(false);

        patientSection.addActionListener(e -> {
            patientSection.setSelected(true);
            doctorSection.setSelected(false);
            medicineSection.setSelected(false);
            machineSection.setSelected(false);
            containerLayout.show(mainPageContainer,"patient-panel");
        });

        doctorSection.addActionListener(e -> {
            patientSection.setSelected(false);
            doctorSection.setSelected(true);
            medicineSection.setSelected(false);
            machineSection.setSelected(false);
            containerLayout.show(mainPageContainer, "doctor-panel");
        });

        medicineSection.addActionListener(e -> {
            patientSection.setSelected(false);
            doctorSection.setSelected(false);
            medicineSection.setSelected(true);
            machineSection.setSelected(false);
            containerLayout.show(mainPageContainer,"medicine-panel");
        });

        machineSection.addActionListener(e -> {
            patientSection.setSelected(false);
            doctorSection.setSelected(false);
            medicineSection.setSelected(false);
            machineSection.setSelected(true);
            containerLayout.show(mainPageContainer,"machine-panel");
        });

        navigationContainer.add(label);

        JPanel cPanel = new JPanel();
        cPanel.setSize(new Dimension(this.getWidth(), this.getHeight() * 257/384));
        cPanel.setBackground(Color.WHITE);
        cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));


        // (role != null && role.equals("Doctor")) {
            cPanel.add(patientSection);
            cPanel.add(Box.createVerticalStrut(10));
        //}
        //else {
            cPanel.add(Box.createVerticalStrut(10));

            cPanel.add(doctorSection);
            cPanel.add(Box.createVerticalStrut(10));

            cPanel.add(Box.createVerticalStrut(10));

            cPanel.add(medicineSection);
            cPanel.add(Box.createVerticalStrut(10));

            cPanel.add(Box.createVerticalStrut(10));

            cPanel.add(machineSection);
            cPanel.add(Box.createVerticalStrut(10));
        //}

        navigationContainer.add(cPanel);
        return navigationContainer;
    }
    private JPanel MainPageContainer() {
        JPanel container = new JPanel();
        container.setLayout(containerLayout);
        container.setPreferredSize(new Dimension(screenSize.width * 23059 / 27320, screenSize.height));

        JPanel patientPanel = new PatientPanel();
        JPanel doctorPanel = new DoctorPanel();
        JPanel medicinePanel = new MedicinePanel();
        JPanel machinePanel = new MachinePanel();

        container.add(patientPanel, "patient-panel");
        container.add(doctorPanel, "doctor-panel");
        container.add(medicinePanel, "medicine-panel");
        container.add(machinePanel, "machine-panel");

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
class BackButton extends JButton {
    BackButton(){
        setBackground(Color.white);
        setBorder(BorderFactory.createEmptyBorder());
        setIcon(new ImageIcon(new ImageIcon("src/main/java/com/javaswing/img/back-icon.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        setSize(new Dimension(20,20));
    }
}

class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
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
