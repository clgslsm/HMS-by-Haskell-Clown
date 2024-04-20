package com.javaswing;

import com.javafirebasetest.dao.StaffDAO;
import com.javafirebasetest.entity.Staff;
import com.javafirebasetest.entity.User;

import javax.swing.*;
import java.awt.*;


public class DefaultPanel extends JPanel {
    Staff st;
    DefaultPanel(User u) {
        st = StaffDAO.getStaffById(u.getStaffId());

        this.setMaximumSize(new Dimension(1300,600));
        this.setBorder(BorderFactory.createLineBorder(new Color(0xF1F8FF), 40));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Header container
        JPanel header = new JPanel();
        JLabel title = new JLabel("Welcome back " + st.getName());
        title.setFont(title.getFont().deriveFont(30F));
        title.setForeground(new Color(0x3497F9));
        title.setBackground(new Color(0xF1F8FF));
        header.setBackground(new Color(0xF1F8FF));
        //header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.add(title);

        this.add(header);
        JPanel space = new JPanel();
        space.setBackground(new Color(0xF1F8FF));
        space.setSize(new Dimension(100, 100));
        this.add(space);
        //this.add(body);
    }
}
