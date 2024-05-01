package com.javaswing;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
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
        this.setBorder(BorderFactory.createLineBorder(Constants.LIGHT_BLUE, 40));
        this.setBackground(Constants.LIGHT_BLUE);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Header container
        JPanel header = new JPanel();
        JLabel title = new JLabel(STR."Welcome back \{st.getName()}");
        title.setFont(new Font(FlatRobotoFont.FAMILY,Font.BOLD,30));
        title.setForeground(Constants.BLUE);
        title.setBackground(Constants.LIGHT_BLUE);
        header.setBackground(Constants.LIGHT_BLUE);
        //header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.add(title);

        this.add(header);
        JPanel space = new JPanel();
        space.setBackground(Constants.LIGHT_BLUE);
        space.setSize(new Dimension(100, 100));
        this.add(space);
        //this.add(body);
    }
}
