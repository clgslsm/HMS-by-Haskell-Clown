package com.javafirebasetest.entity;

import java.util.HashMap;
import java.util.Map;

public class Staff {
    protected String staffId;
    protected String name;
    protected User.Mode userMode;

    public Staff() {
        super();
    }

    public Staff(String staffId, String name, User.Mode userMode) {
        this.userMode = userMode;
        this.staffId = staffId;
        this.name = name;
    }

    public Staff(String staffId, Map<String, Object> staff) {
        this.staffId = staffId;
        this.name = (String) staff.get("name");
        this.userMode = User.Mode.fromValue((String) staff.get("userMode")); // Assuming birthDate is stored as String in the map
    }

    public void setStaffId(String id) {
        this.staffId = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserMode(User.Mode userMode) {
        this.userMode = userMode;
    }

    public String getStaffId() {
        return this.staffId;
    }

    public String getName() {
        return this.name;
    }

    public User.Mode getUserMode() {
        return userMode;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", getName());
        map.put("userMode", userMode.getValue());
        return map;
    }

    public String toString() {
        return "Staff [userMode=" + userMode + ", ID=" + staffId + ", name=" + name + "]";
    }
}
