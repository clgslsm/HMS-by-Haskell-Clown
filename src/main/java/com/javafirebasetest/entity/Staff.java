package com.javafirebasetest.entity;

public class Staff extends User {
    private String ID;
    private String name;
    //protected static Staff instanceStaff;
    public Staff() {super();}
    public Staff(String username, String password, Mode userMode, String id, String name) {
        super(username, password, userMode);
        this.ID = id;
        this.name = name;
    }
//    public static Staff getInstanceStaff() {
//        if (instanceStaff == null) instanceStaff = new Staff();
//        return instanceStaff;
//    }
//    public static Staff getInstanceStaff(String username, String password, Mode userMode, String id, String name) {
//        if (instanceStaff == null) instanceStaff = new Staff(username, password, userMode, id, name);
//        return instanceStaff;
//    }
    public void setID(String id) {this.ID = id;}
    public void setName(String name) {this.name = name;}
    public String getID() {return this.ID;}
    public String getName() {return this.name;}
    public String toString() {
        return "Staff [ID=" + ID + ", name=" + name + "]";
    }
}
