package com.javaswing;

import com.javafirebasetest.dao.UserDAO;
import com.javafirebasetest.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;

public class LoginUI extends JFrame implements ActionListener {
    // Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    // Constructor
    public LoginUI() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");

        // Add action listener to the login button
        loginButton.addActionListener(this);

        // Layout
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(loginButton);

        getContentPane().add(panel);
    }

    // Action performed when login button is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            User user = UserDAO.getUserByUsername(username);
            if (UserDAO.authenticateUser(user, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while authenticating user", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to validate user credentials
    private boolean isValidUser(User user) {
        // You can implement your logic here to check against a database or any other data source
        // For demonstration purpose, let's assume we have a hardcoded user
        User correctUser = new User("admin", "password", null); // Change this with your actual user data

        try {
            // Check if username matches and hashed passwords match
            return user.getUserName().equals(correctUser.getUserName()) &&
                    user.getHashPassword().equals(correctUser.getHashPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Main method to run the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginUI loginUI = new LoginUI();
            loginUI.setVisible(true);
        });
    }
}
