package com.javafirebasetest.entity;

import java.security.NoSuchAlgorithmException;

import static com.javafirebasetest.entity.HashPassword.getSHA;
import static com.javafirebasetest.entity.HashPassword.toHexString;

public class User {
    private String userName;
    private String password;
    private Mode userMode;
    private String staffID;
    protected static User instanceUser;
    public enum Mode {
        ADMIN("Admin") , DOCTOR("Doctor"), RECEPTIONIST("Receptionist"),
        TECHNICIAN("Technician"), PHARMACIST("Pharmacist");
        private final String value;
        Mode(String value) {this.value = value;}
        public String getValue() {return value;}
        public static User.Mode fromValue(String value) {
            for (User.Mode g : User.Mode.values())
                if (g.value.equalsIgnoreCase(value)) return g;
            throw new IllegalArgumentException("Invalid gender: " + value);
        }
    }
    User() {super();}
    User(String userName, String password, Mode userMode, String staffID) {
        super();
        this.userName = userName;
        this.password = password;
        this.userMode = userMode;
        this.staffID = staffID;
    }
    public static User getInstanceUser() {
        if (instanceUser == null) instanceUser = new User();
        return instanceUser;
    }
    public static User getInstanceUser(String userName, String password, Mode userMode, String staffID) {
        if (instanceUser == null) instanceUser = new User(userName, password, userMode, staffID);
        return instanceUser;
    }
    public void setUserName(String userName) {this.userName = userName;}
    public void setPassword(String password) {this.password = password;}
    public void setUserMode(Mode userMode) {this.userMode = userMode;}
    public void setStaffID(String staffID) {this.staffID = staffID;}
    public String getUserName() {return userName;}
    public String getPassword() {return password;}
    public Mode getUserMode() {return userMode;}
    public String getStaffID() {return staffID;}
    public String getHashPassword() throws NoSuchAlgorithmException {return toHexString(getSHA(password));}

    public String toString() {
        return "User [userName=" + userName + ", password=" + password + ", userMode=" + userMode.getValue() + ", staffID=" + getStaffID() +  "]";
    }
}

