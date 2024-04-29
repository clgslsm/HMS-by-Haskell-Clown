package com.javaswing;
import com.javafirebasetest.entity.User;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

import static com.javafirebasetest.dao.UserDAO.getUserByUsernamePassword;

class LoginPage extends JFrame {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Home home;
    private LoginForm form;

    public LoginPage() {
        setTitle("ABC HOSPITAL _ LOGIN");
        setSize(screenSize.width,screenSize.height);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);

        // Create a custom JPanel with the background image
        home = new Home(new ImageIcon("src/main/java/com/javaswing/img/home.jpg").getImage());
        form = new LoginForm();

        JPanel container1 = new JPanel();
        container1.setLayout(null);
        container1.setBackground(new Color(0x3497F9));
        container1.setSize(screenSize.width,screenSize.height);
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container,BoxLayout.X_AXIS));
        container.setMinimumSize(new Dimension(1000,500));
        container.add(home);
        container.add(form);
        container.setBounds((screenSize.width-1000)/2,(screenSize.height-600)/2,1000,500);

        container1.add(container);


        add(container1);
    }
}

class Home extends JPanel {
    private Image backgroundImage;

    public Home(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        this.setMinimumSize(new Dimension(600,500));
        this.setMaximumSize(new Dimension(600,500));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

class LoginForm extends JPanel implements ActionListener {
    JButton loginButton = new RoundedButton("Login");
    JButton resetButton = new RoundedButton("Reset");
    JTextField userNameField = new JTextField("Technician16");
    JPasswordField userPasswordField = new JPasswordField("Technician16");
    JLabel userNameLabel = new JLabel("User name:");
    JLabel userPasswordLabel = new JLabel("Password:");
    JLabel messageLabel = new JLabel();
    LoginForm(){
        setMaximumSize(new Dimension(400,500));
        setMinimumSize(new Dimension(400,500));
        setLayout(null);
        setBounds(0, 0, 400, 400);

        JLabel hospitalName = new JLabel("ABC HOSPITAL");
        hospitalName.setFont(new Font("Tahoma",Font.BOLD,30));
        hospitalName.setForeground(new Color(0x3497F9));
        hospitalName.setBounds(100,25,400,80);

        userNameLabel.setBounds(100, 120, 100, 40);
        userNameLabel.setFont(new Font("Ubuntu", Font.BOLD, 15));
        userPasswordLabel.setBounds(100, 200, 100, 40);
        userPasswordLabel.setFont(new Font("Ubuntu", Font.BOLD, 15));

        messageLabel.setBounds(100, 380, 250, 50);
        messageLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
        messageLabel.setForeground(Color.RED);

        int topPadding = 5;
        int leftPadding = 10;
        int bottomPadding = 5;
        int rightPadding = 10;

        userNameField.setBounds(100, 155, 225, 40);
        userNameField.setBorder(BorderFactory.createCompoundBorder(
                userNameField.getBorder(),
                BorderFactory.createEmptyBorder(topPadding, leftPadding, bottomPadding, rightPadding)
        ));
        userPasswordField.setBounds(100, 235, 225, 40);
        userPasswordField.setBorder(BorderFactory.createCompoundBorder(
                userPasswordField.getBorder(),
                BorderFactory.createEmptyBorder(topPadding, leftPadding, bottomPadding, rightPadding)
        ));

        loginButton.setBounds(100, 350, 100, 40);
        loginButton.setFont(new Font("Ubuntu", Font.PLAIN, 15));
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        resetButton.setBounds(225, 350, 100, 40);
        resetButton.setFont(new Font("Ubuntu", Font.PLAIN, 15));
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);

        add(hospitalName);
        add(userNameLabel);
        add(userPasswordLabel);
        add(userNameField);
        add(userPasswordField);
        add(loginButton);
        add(resetButton);
        add(messageLabel);
        setBackground(Color.WHITE);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            userNameField.setText("");
            userPasswordField.setText("");
            messageLabel.setText("");
        }

        if (e.getSource() == loginButton) {
            String userName = userNameField.getText().trim();
            String password = String.valueOf(userPasswordField.getPassword()).trim();

            if (!userName.isEmpty() && !password.isEmpty()) {
                User user = getUserByUsernamePassword(userName, password);
                if (user != null) {
                    messageLabel.setText("OK");
                    try {
                        MainPage newMainPage = new MainPage(user);
                    } catch (ExecutionException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    this.setVisible(false);
                } else {
                    messageLabel.setText("Username or password incorrect!");
                }
            } else {
                // Handle case when fields are empty
                messageLabel.setText("Please fill in all fields.");
            }
        }
    }
}
