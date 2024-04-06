package com.javafirebasetest.entity;
public class Technician extends Staff {
    public Technician() {super();}
    public Technician(String id, String name) {super(User.Mode.TECHNICIAN, id, name);}
    @Override
    public String toString() {
        return "technician [ID=" + getID() + ", name=" + getName() + "]";
    }
}