package com.javafirebasetest.entity;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static com.javafirebasetest.entity.HashPassword.getSHA;
import static com.javafirebasetest.entity.HashPassword.toHexString;

public class User {
    private String ID;
    private String username;
    private String password;
    private Mode userMode;
<<<<<<< HEAD
    private String staffId;
    protected static User instanceUser;
=======
    private String Staff_ID;
    //protected static User instanceUser;
>>>>>>> ad772331ba671cff71e9ca796461cba8af6f37ae
    public enum Mode {
        ADMIN("Admin") , DOCTOR("Doctor"), RECEPTIONIST("Receptionist"),
        TECHNICIAN("Technician"), PHARMACIST("Pharmacist");
        private final String value;
        Mode(String value) {this.value = value;}
        public String getValue() {return value;}
        public static User.Mode fromValue(String value) {
            for (User.Mode g : User.Mode.values())
                if (g.value.equalsIgnoreCase(value)) return g;
            throw new IllegalArgumentException("Invalid user mode: " + value);
        }
    }
<<<<<<< HEAD
    private User() {super();}
    private User(String username, String password, Mode userMode) {
=======
    public User() {super();}
    public User(String userName, String password, Mode userMode, String ID) {
        super();
        this.userName = userName;
        this.password = password;
        this.userMode = userMode;
        this.Staff_ID = ID;
    }
    public User(String userName, String password, Mode userMode) {
>>>>>>> ad772331ba671cff71e9ca796461cba8af6f37ae
        super();
        this.username = username;
        this.password = password;
        this.userMode = userMode;
    }

    private User(String ID, Map<String, Object> user) {
        super();
        this.ID = ID;
        this.username = (String) user.get("username");
        this.password = (String) user.get("password");
        this.userMode = Mode.fromValue((String) user.get("userMode"));
        this.staffId = (String) user.get("staffId");
    }
    public static User getInstanceUser() {
        if (instanceUser == null) instanceUser = new User();
        return instanceUser;
    }
    public static User getInstanceUser(String userName, String password, Mode userMode) {
        if (instanceUser == null) instanceUser = new User(userName, password, userMode);
        return instanceUser;
    }

    public void setUserMode(Mode userMode) {this.userMode = userMode;}
<<<<<<< HEAD
    public void setStaffId(String staffId) {this.staffId = staffId;}
    public Mode getUserMode() {return userMode;}
    public String getStaffId() {return staffId;}
=======
    public void setID(String ID) {this.Staff_ID = ID;}

    public String getUserName() {return userName;}
    public String getPassword() {return password;}
    public Mode getUserMode() {return userMode;}
    public String getID() {return Staff_ID;}
>>>>>>> ad772331ba671cff71e9ca796461cba8af6f37ae
    public String getHashPassword() throws NoSuchAlgorithmException {return toHexString(getSHA(password));}

    public String toString() {
        return "User [userName=" + username + ", password=" + password + ", userMode=" + userMode.getValue() + ", staffID=" + getStaffId() +  "]";
    }
}

