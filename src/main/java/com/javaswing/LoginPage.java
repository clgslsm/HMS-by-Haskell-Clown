//package com.javaswing;
//
//import com.javafirebasetest.entity.User;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import static com.javafirebasetest.dao.UserDAO.getUserByUsername;
//import static com.javafirebasetest.dao.UserDAO.getUserByUsernamePassword;
//
//public class LoginPage implements ActionListener {
//
//    JFrame frame = new JFrame();
//    JButton loginButton = new RoundedButton("Login");
//    JButton resetButton = new RoundedButton("Reset");
//    JTextField userNameField = new JTextField();
//    JPasswordField userPasswordField = new JPasswordField();
//    JLabel userNameLabel = new JLabel("User name:");
//    JLabel userPasswordLabel = new JLabel("Password:");
//    JLabel messageLabel = new JLabel();
//    JPanel form = new JPanel();
//    JPanel pic = new JPanel();
//
//    public LoginPage() {
//        // Setup form panel
//        form.setLayout(null);
//        form.setBounds(240, 0, 400, 420);
//
//        userNameLabel.setBounds(25, 100, 100, 40);
//        userNameLabel.setFont(new Font("Ubuntu", Font.BOLD, 15));
//        userPasswordLabel.setBounds(25, 180, 100, 40);
//        userPasswordLabel.setFont(new Font("Ubuntu", Font.BOLD, 15));
//
//        messageLabel.setBounds(110, 215, 250, 50);
//        messageLabel.setFont(new Font(null, Font.ITALIC, 15));
//        messageLabel.setForeground(Color.RED);
//
//        userNameField.setBounds(110, 100, 225, 40);
//        userPasswordField.setBounds(110, 180, 225, 40);
//
//        loginButton.setBounds(110, 260, 100, 40);
//        loginButton.setFont(new Font("Ubuntu", Font.PLAIN, 15));
//        loginButton.setFocusable(false);
//        loginButton.addActionListener(this);
//
//        resetButton.setBounds(235, 260, 100, 40);
//        resetButton.setFont(new Font("Ubuntu", Font.PLAIN, 15));
//        resetButton.setFocusable(false);
//        resetButton.addActionListener(this);
//
//        form.add(userNameLabel);
//        form.add(userPasswordLabel);
//        form.add(userNameField);
//        form.add(userPasswordField);
//        form.add(loginButton);
//        form.add(resetButton);
//        form.add(messageLabel);
//        form.setBackground(new Color(0xF1F8FF));
//
//        // Setup pic panel
//        pic.setBounds(0, 0, 240, 420);
//        JLabel picLabel = new JLabel();
//        ImageIcon imageIcon = new ImageIcon("src/main/java/com/javaswing/img/logo.jpg");
//        Image image = imageIcon.getImage();
//        Image newImage = image.getScaledInstance(100, 220, Image.SCALE_SMOOTH);
//        ImageIcon newImageIcon = new ImageIcon(newImage);
//        picLabel.setIcon(newImageIcon);
//        pic.setBackground(new Color(0x3497F9));
//        //pic.add(picLabel);
//
//        // Setup frame
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setResizable(false);
//        frame.setSize(600, 420);
//        frame.setLayout(null);
//        frame.setTitle("ABC Hospital Login");
//
//        // Add panels to the frame
//        frame.add(form);
//        frame.add(pic);
//
//        frame.setVisible(true);
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == resetButton) {
//            userNameField.setText("");
//            userPasswordField.setText("");
//            messageLabel.setText("");
//        }
//
//        if (e.getSource() == loginButton) {
//            String userName = userNameField.getText().trim();
//            String password = String.valueOf(userPasswordField.getPassword()).trim();
//
//            if (!userName.isEmpty() && !password.isEmpty()) {
//                User user = getUserByUsernamePassword(userName, password);
//                if (user != null) {
//                    messageLabel.setText("OK");
//                    MainPage newMainPage = new MainPage(user.getUserMode().getValue());
//                    frame.setVisible(false);
//                } else {
//                    messageLabel.setText("Username or password incorrect.");
//                }
//            } else {
//                // Handle case when fields are empty
//                messageLabel.setText("Please fill in all fields.");
//            }
//        }
//    }
//}
