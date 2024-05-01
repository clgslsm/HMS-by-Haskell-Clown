package com.javaswing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

class CustomDatePicker extends JPanel {
    static JComboBox<String> dayComboBox;
    static JComboBox<String> monthComboBox;
    static JComboBox<String> yearComboBox;

    static String convertToMonthName(String monthStr) {
        switch (monthStr) {
            case "01":
                return "January";
            case "02":
                return "February";
            case "03":
                return "March";
            case "04":
                return "April";
            case "05":
                return "May";
            case "06":
                return "June";
            case "07":
                return "July";
            case "08":
                return "August";
            case "09":
                return "September";
            case "10":
                return "October";
            case "11":
                return "November";
            case "12":
                return "December";
            case "January":
                return "01";
            case "February":
                return "02";
            case "March":
                return "03";
            case "April":
                return "04";
            case "May":
                return "05";
            case "June":
                return "06";
            case "July":
                return "07";
            case "August":
                return "08";
            case "September":
                return "09";
            case "October":
                return "10";
            case "November":
                return "11";
            case "December":
                return "12";
            default:
                return "Invalid month";
        }
    }
    // Tách chuỗi ngày tháng năm thành ba phần: year, month, day
    static String[] splitDate(String dateStr) {
        String[] parts = dateStr.split("-");
        parts[1] = convertToMonthName(parts[1]);
        return parts;
    }
    // Ghép ba chuỗi thành chuỗi ngày tháng năm
    public static String mergeDate() {
        String day = dayComboBox.getSelectedItem().toString();
        if (Integer.parseInt(day) < 10) {
            day = "0" + day; // Thêm số 0 phía trước nếu ngày nhỏ hơn 10
        }
        String month = convertToMonthName(monthComboBox.getSelectedItem().toString());
        String year = yearComboBox.getSelectedItem().toString();
        return year + "-" + month + "-" + day;
    }

    public CustomDatePicker(String[] l) {
        // Sử dụng GridLayout để chỉ chứa 3 JComboBox
        setLayout(new GridLayout(1, 3));

        // Thêm các combobox cho ngày, tháng và năm
        dayComboBox = new JComboBox<>();
        monthComboBox = new JComboBox<>();
        yearComboBox = new JComboBox<>();

        dayComboBox.setFont(new Font("Courier",Font.PLAIN,16));
//        dayComboBox
        dayComboBox.setBackground(Color.white);
//        dayComboBox.setBorder(BorderFactory.createEmptyBorder());
        dayComboBox.setBounds(200,140,100,20);

        monthComboBox.setFont(new Font("Courier",Font.PLAIN,16));
        monthComboBox.setBackground(Color.white);
//        monthComboBox.setBorder(BorderFactory.createEmptyBorder());
        monthComboBox.setBounds(200,140,100,20);

        yearComboBox.setFont(new Font("Courier",Font.PLAIN,16));
        yearComboBox.setBackground(Color.white);
//        yearComboBox.setBorder(BorderFactory.createEmptyBorder());
        yearComboBox.setBounds(200,140,100,20);

        // Thêm các lựa chọn cho ngày và tháng
        for (int i = 1; i <= 31; i++) {
            String day = (i < 10) ? "0" + i : String.valueOf(i);
            dayComboBox.addItem(day);
        }
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            monthComboBox.addItem(month);
        }

        // Thêm các lựa chọn cho năm
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 100; i <= currentYear; i++) {
            yearComboBox.addItem(String.valueOf(i));
        }

        // Thêm sự kiện để kiểm tra tháng 2 và điều chỉnh ngày tương ứng
        monthComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMonth = (String) monthComboBox.getSelectedItem();
                int selectedDay = Integer.parseInt((String) dayComboBox.getSelectedItem());
                if (selectedMonth.equals("February")) {
                    int selectedYear = Integer.parseInt((String) yearComboBox.getSelectedItem());
                    int maxDay = getMaxDay(selectedYear, selectedMonth);
                    if (selectedDay > maxDay) {
                        dayComboBox.setSelectedItem(String.valueOf(maxDay));
                    }
                    updateDayComboBox(maxDay);
                } else {
                    updateDayComboBox(31);
                }
            }
        });

        dayComboBox.setSelectedItem(l[2]);
        monthComboBox.setSelectedItem(l[1]);
        yearComboBox.setSelectedItem(l[0]);

        add(dayComboBox);
        add(monthComboBox);
        add(yearComboBox);
    }

    // Cập nhật số ngày trong tháng
    private void updateDayComboBox(int maxDay) {
        dayComboBox.removeAllItems();
        for (int i = 1; i <= maxDay; i++) {
            dayComboBox.addItem(String.valueOf(i));
        }
    }

    // Lấy số ngày tối đa trong tháng
    private int getMaxDay(int year, String month) {
        int maxDay;
        switch (month) {
            case "February":
                if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                    maxDay = 29;
                } else {
                    maxDay = 28;
                }
                break;
            case "April":
            case "June":
            case "September":
            case "November":
                maxDay = 30;
                break;
            default:
                maxDay = 31;
        }
        return maxDay;
    }
}
