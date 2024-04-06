package com.javafirebasetest.entity;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.javafirebasetest.entity.HashPassword.getSHA;
import static com.javafirebasetest.entity.HashPassword.toHexString;

public class User {
    private String userId;
    private String username;
    private String password;
    private Mode userMode;
    private String staffId;

    public enum Mode {
        ADMIN("Admin"), DOCTOR("Doctor"), RECEPTIONIST("Receptionist"),
        TECHNICIAN("Technician"), PHARMACIST("Pharmacist");
        private final String value;

        Mode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Mode fromValue(String value) {
            for (Mode g : Mode.values())
                if (g.value.equalsIgnoreCase(value)) return g;
            throw new IllegalArgumentException("Invalid user mode: " + value);
        }
    }

    public User() {
        super();
    }

    //WITH HASH

    public User(String userId, String username, String password, Mode userMode, String staffId) {
        super();
        this.userId = userId;
        this.username = username;
        this.password = hashPassword(password);
        this.userMode = userMode;
        this.staffId = staffId;
    }

    //WITHOUT HASH

    public User(String userId, Map<String, Object> user) {
        super();
        this.userId = userId;
        this.username = (String) user.get("username");
        this.password = (String) user.get("password");
        this.userMode = Mode.fromValue((String) user.get("userMode"));
        this.staffId = (String) user.get("staffId");
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Mode getUserMode() {
        return userMode;
    }

    public String getStaffId() {
        return staffId;
    }

    public static String hashPassword(String password){
        try {
            return toHexString(getSHA(password));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("getSHA function not found: " + e);
            return null;
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("userMode", userMode.getValue());
        map.put("staffId", staffId);
        return map;
    }

    public String toString() {
        return "User [userName=" + username + ", password=" + password + ", userMode=" + userMode.getValue() + ", staffId=" + getStaffId() + "]";
    }
}

