package com.javafirebasetest.entity;
public class Receptionist extends Staff {
    private Receptionist() {super();}
    private Receptionist(String id, String name) {super(id, name, User.Mode.RECEPTIONIST);}
    @Override
    public String toString() {
        return "receptionist [ID=" + getStaffId() + ", name=" + getName() + "]";
    }
}
