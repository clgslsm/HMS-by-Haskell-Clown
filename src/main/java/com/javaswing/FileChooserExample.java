package com.javaswing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileChooserExample extends JFrame implements ActionListener {
    JButton chooseButton;
    JLabel filePathLabel;

    public FileChooserExample() {
        setTitle("File Chooser Example");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        chooseButton = new JButton("Choose File");
        chooseButton.addActionListener(this);

        filePathLabel = new JLabel("Selected File Path will appear here");

        mainPanel.add(chooseButton, BorderLayout.NORTH);
        mainPanel.add(filePathLabel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooseButton) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathLabel.setText("Selected File Path: " + selectedFile.getAbsolutePath());
            }
        }
    }

    public static void main(String[] args) {
        new FileChooserExample();
    }
}
