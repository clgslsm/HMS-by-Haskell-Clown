package com.javafirebasetest.entity;
public class Pharmacist extends Staff {
    private Pharmacist() {super();}
    private Pharmacist(String id, String name) {super(id, name, User.Mode.PHARMACIST);}
    @Override
    public String toString() {
        return "pharmacist [ID=" + getStaffId() + ", name=" + getName() + userMode.getValue() +  "]";
    }
}
