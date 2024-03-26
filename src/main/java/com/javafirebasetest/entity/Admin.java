package com.javafirebasetest.entity;

public class Admin extends User {
    protected static Admin instanceAdmin;
    private Admin() {super();}
    private Admin(String username, String password) {
        super(username, password, Mode.ADMIN);
    }
    public static Admin getInstanceAdmin() {
        if (instanceAdmin == null) instanceAdmin = new Admin();
        return instanceAdmin;
    }
    public static Admin getInstanceAdmin(String username, String password) {
        if (instanceAdmin == null) instanceAdmin = new Admin(username, password);
        return instanceAdmin;
    }
    @Override
    public String toString() {
        return "admin [userName=" + getUserName() + ", password=" + getPassword() + "]";
    }
}
