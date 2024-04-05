package com.javafirebasetest.entity;

public class Admin extends User {
    private Admin() {super();}
    private Admin(String username, String password) {super(username, password, User.Mode.ADMIN, getInstanceUser().getStaffID());}
    @Override
    public String toString() {
        return "admin [userName=" + getUserName() + ", password=" + getPassword() + "]";
    }
}
