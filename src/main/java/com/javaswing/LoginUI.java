//package com.javaswing;
//
//import com.javafirebasetest.dao.DoctorDAO;
//import com.javafirebasetest.dao.UserDAO;
//import com.javafirebasetest.entity.Doctor;
//import com.javafirebasetest.entity.User;
//import com.javafirebasetest.AuthManager;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.security.NoSuchAlgorithmException;
//
//public class LoginUI extends JFrame implements ActionListener {
//    // Components
//    private JTextField usernameField;
//    private JPasswordField passwordField;
//    private JButton loginButton;
//
//    // Constructor
//    public LoginUI() {
//        setTitle("Login");
//        setSize(300, 150);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setResizable(false);
//
//        // Create components
//        JLabel usernameLabel = new JLabel("Username:");
//        JLabel passwordLabel = new JLabel("Password:");
//        usernameField = new JTextField(15);
//        passwordField = new JPasswordField(15);
//        loginButton = new JButton("Login");
//
//        // Add action listener to the login button
//        loginButton.addActionListener(this);
//
//        // Layout
//        JPanel panel = new JPanel(new GridLayout(3, 2));
//        panel.add(usernameLabel);
//        panel.add(usernameField);
//        panel.add(passwordLabel);
//        panel.add(passwordField);
//        panel.add(new JLabel()); // Empty label for spacing
//        panel.add(loginButton);
//
//        getContentPane().add(panel);
//    }
//
//    // Action performed when login button is clicked
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        String username = usernameField.getText();
//        String password = new String(passwordField.getPassword());
//
//        try {
//            // Attempt to log in the user
//            AuthManager authManager = new AuthManager(); // Assuming you have a UserDAO instance
//            User loggedInUser = authManager.login(username, password);
//
//            if (loggedInUser != null) {
//                // If login is successful
//                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
//
//                // Check user mode and open corresponding UI
//                switch (loggedInUser.getUserMode()) {
//                    case ADMIN:
//                        SwingUtilities.invokeLater(MainPage::new);
//                        break;
//                    case DOCTOR:
//                        // If the user is a doctor, print doctor information
////                        System.out.println(loggedInUser.getID());
//                        Doctor doctor = DoctorDAO.getDoctorById(loggedInUser.getID());
//                        System.out.println(doctor.toString());
//                        break;
//                    default:
//                        // Handle other user modes if needed
//                        break;
//                }
//            } else {
//                // If login fails
//                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error while authenticating user", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    // Main method to run the program
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            LoginUI loginUI = new LoginUI();
//            loginUI.setVisible(true);
//        });
//    }
//}
//
////package com.javaswing;
////
////import com.javafirebasetest.dao.DoctorDAO;
////import com.javafirebasetest.entity.Doctor;
////
////import javax.swing.*;
////import java.awt.*;
////import java.awt.event.*;
////
////public class LoginUI extends JFrame implements ActionListener {
////    // Components
////    private JTextField usernameField;
////    private JPasswordField passwordField;
////    private JButton loginButton;
////
////    // Constructor
////    public LoginUI() {
////        setTitle("Login");
////        setSize(300, 150);
////        setDefaultCloseOperation(EXIT_ON_CLOSE);
////        setLocationRelativeTo(null);
////        setResizable(false);
////
////        // Create components
////        JLabel usernameLabel = new JLabel("Username:");
////        JLabel passwordLabel = new JLabel("Password:");
////        usernameField = new JTextField(15);
////        passwordField = new JPasswordField(15);
////        loginButton = new JButton("Login");
////
////        // Add action listener to the login button
////        loginButton.addActionListener(this);
////
////        // Layout
////        JPanel panel = new JPanel(new GridLayout(3, 2));
////        panel.add(usernameLabel);
////        panel.add(usernameField);
////        panel.add(passwordLabel);
////        panel.add(passwordField);
////        panel.add(new JLabel()); // Empty label for spacing
////        panel.add(loginButton);
////
////        getContentPane().add(panel);
////    }
////
////    // Action performed when login button is clicked
////    @Override
////    public void actionPerformed(ActionEvent e) {
////        String username = usernameField.getText();
////        String password = new String(passwordField.getPassword());
////
////        try {
////            // Attempt to log in the user
////            AuthManager authManager = new AuthManager(); // Assuming you have a UserDAO instance
////            User loggedInUser = authManager.login(username, password);
////
////            if (loggedInUser != null) {
////                // If login is successful
////                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
////
////                // Check user mode and open corresponding UI
////                switch (loggedInUser.getUserMode()) {
////                    case ADMIN:
////                        SwingUtilities.invokeLater(MainPage::new);
////                        break;
////                    case DOCTOR:
////                        // If the user is a doctor, print doctor information
//////                        System.out.println(loggedInUser.getID());
////                        Doctor doctor = DoctorDAO.getDoctorById(loggedInUser.getStaffID());
////                        System.out.println(doctor.toString());
////                        break;
////                    default:
////                        // Handle other user modes if needed
////                        break;
////                }
////            } else {
////                // If login fails
////                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
////            }
////        } catch (Exception ex) {
////            ex.printStackTrace();
////            JOptionPane.showMessageDialog(this, "Error while authenticating user", "Error", JOptionPane.ERROR_MESSAGE);
////        }
////    }
////
////    // Main method to run the program
////    public static void main(String[] args) {
////        SwingUtilities.invokeLater(() -> {
////            LoginUI loginUI = new LoginUI();
////            loginUI.setVisible(true);
////        });
////    }
////}
