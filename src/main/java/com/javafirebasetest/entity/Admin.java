package com.javafirebasetest.entity;

public class Admin extends Staff {
    private Admin() {super();}
    private Admin(String staffId, String name, User.Mode userMode) {
            super(staffId, name, userMode);
        }
}
