package com.javafirebasetest.entity;
public class Technician extends Staff {
    public Technician() {super();}
    public Technician(String id, String name) {super(id, name, User.Mode.TECHNICIAN);}
    @Override
    public String toString() {
        return "technician [ID=" + getStaffId() + ", name=" + getName() + "]";
    }
}