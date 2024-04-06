package com.javafirebasetest.entity;
public abstract class Staff {
    private User.Mode userMode;
    private String ID;
    private String name;
    public Staff() {super();}
    public Staff(User.Mode userMode, String id, String name) {
        super();
        this.userMode = userMode;
        this.ID = id;
        this.name = name;
    }
    public void setUserMode(User.Mode userMode) {this.userMode = userMode;}
    public void setID(String id) {this.ID = id;}
    public void setName(String name) {this.name = name;}
    public User.Mode getUserMode() {return userMode;}

    public String getID() {return this.ID;}
    public String getName() {return this.name;}
    public String toString() {
        return "Staff [userMode=" + userMode.getValue() + ", ID=" + ID + ", name=" + name + "]";
    }
}
