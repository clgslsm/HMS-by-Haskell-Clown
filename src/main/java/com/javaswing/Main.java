package com.javaswing;
import javax.swing.*;
import com.javafirebasetest.dao.receptionist.PatientDAO;
import com.javafirebasetest.entity.Patient;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}


